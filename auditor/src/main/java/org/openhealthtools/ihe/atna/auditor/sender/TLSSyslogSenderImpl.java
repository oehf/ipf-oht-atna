/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Washington University in St. Louis - refactor for RFC 5426
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.sender;

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple client implementation of RFC 5425 TLS syslog transport
 * for sending audit messages to an Audit Record Repository
 * that implements TLS syslog.  
 * Multiple messages may be sent over the same socket.
 * 
 * Designed to run in a standalone mode from the standard IHE Auditor
 * and is not dependent on any context or configuration.
 * 
 * @author <a href="mailto:tarboxl@mir.wustl.edu">Lawrence Tarbox</a>
 * Derived from code written by Matthew Davis of IBM.
 *
 */
public class TLSSyslogSenderImpl extends RFC5424SyslogSenderImpl implements AuditMessageSender
{
	private static Map<String, Socket> socketMap = new HashMap<>();

	/**
	 * Logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TLSSyslogSenderImpl.class);
	
	/**
	 * Default port for this transport
	 */
	public static final int TRANSPORT_DEFAULT_PORT = 6514;
	
	/**
	 * Default constructor
	 */
	public TLSSyslogSenderImpl()
	{
	}
	
	/**
	 * Send an audit message to a designated destination address and port using the 
	 * TLS socket specified.
	 * 
	 * @param msg Message to send
	 * @param socket TLS socket to use
	 * @throws Exception
	 */
	private void send(AuditEventMessage msg, Socket socket) throws Exception
	{
		if (EventUtils.isEmptyOrNull(msg)) {
			return;
		}
		
		// Serialize and format event message for syslog
		byte[] msgBytes = getTransportPayload(msg);
		if (EventUtils.isEmptyOrNull(msgBytes)) {
			return;
		}

		//TODO: prepend the SYSLOG-FRAME, which is just the length of msgBytes with no leading zeros followed by a space
		String syslogFrame = String.valueOf(msgBytes.length) + " ";
		
		//TODO: send the frame + message over the socket
       if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("Auditing to " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        	LOGGER.debug(new String(msgBytes));
       }

       // multiple threads can get a pointer to the socket but only one should write at a time
       synchronized (socket){
    	   try{
		       OutputStream out = socket.getOutputStream();
		       out.write(syslogFrame.getBytes());
		       out.write(msgBytes);
		       out.flush();
    	   } catch(SocketException e) {
    		   try {
	    		   LOGGER.info("Failed to connect with existing TLS socket.  Will create a new connection and retry.");
	    		   String key = socket.getInetAddress().getHostName() + ":" + socket.getPort();
	    		   synchronized (socketMap) {
	    			   socketMap.remove(key);
		    		   Socket newSocket = this.getTLSSocket(socket.getInetAddress(), socket.getPort());
				       OutputStream out = newSocket.getOutputStream();
				       out.write(syslogFrame.getBytes());
				       out.write(msgBytes);
				       out.flush();
				       socketMap.put(key, newSocket);
	    		   }
    		   } catch (Exception exception) {
	    		   LOGGER.error("Still exception trying to audit to TLS socket, throwing away socket.  Cannot connect to server, this log message will be lost.", e);
	    		   synchronized (socketMap) {
	    			   // since we don't know what the key was for this socket, just dump the whole map cause something is probably gone wrong anyway
	    			   for(Socket closeMe : socketMap.values()){
	    				   try{
	    					   closeMe.close();
	    				   }catch (Exception ex){
	    					   // nothing we can do about this, ignore it
	    				   }
	    			   }
	    			   socketMap.clear();
	    		   }    			   
	    		   // rethrow the exception so caller knows what happened
	    		   throw e;
    		   }
    		   
    	   }
       }
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender#sendAuditEvent(org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage[])
	 */
	public void sendAuditEvent(AuditEventMessage[] msgs) throws Exception 
	{
        if (!EventUtils.isEmptyOrNull(msgs)) {

    		for (int i=0; i<msgs.length; i++) {
    			if (!EventUtils.isEmptyOrNull(msgs[i])) {
    	    		Socket s = getTLSSocket(msgs[i].getDestinationAddress(), msgs[i].getDestinationPort());
    				send(msgs[i], s);
    			}
    		}
    		
    		//TODO: tear down the TLS transport socket, if needed
        }
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender#sendAuditEvent(org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage[], java.net.InetAddress, int)
	 */
	public void sendAuditEvent(AuditEventMessage[] msgs, InetAddress destination, int port) throws Exception 
	{
        if (!EventUtils.isEmptyOrNull(msgs)) {
    		Socket s = getTLSSocket(destination, port);

    		for (int i=0; i<msgs.length; i++) {
				send(msgs[i], s);
    		}
    		
    		//TODO: tear down the TLS transport socket, if needed
        }
	}

	/**
	 * Gets the socket tied to the address and port for this transport 
	 * 
	 * @param port Port to check
	 * @return Port to use
	 */
	private Socket getTLSSocket(InetAddress destination, int port) throws Exception
	{
		String key = destination.getHostAddress() + ":" + port;
		synchronized (socketMap){
			Socket socket = socketMap.get(key);
			if (socket == null){
				// create a new one
				NodeAuthModuleContext nodeAuthContext = NodeAuthModuleContext.getContext();
	        	socket = nodeAuthContext.getSocketHandler().getSocket(destination.getHostName(), port, true);
	        	// remember it for next time
	        	// TODO: had trouble with this with Steve Moore's online ATNA server so not caching the sockets
	        	// need to worry about synchronization if we try to put this optimization back
	        	// there appears to be one AuditorModuleContext per host/port so may can pool them based on that object?
				socketMap.put(key, socket);
			}
			// whatever happened, now return the socket
			return socket;		
		}
	}
	
	// called when the object is destroyed
	// since this can be used in a webapp it may be used multiple times
	// in a JVM so this will save resources
	protected void finalize(){
		
	}
}
