/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.sender;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;
import org.openhealthtools.ihe.atna.auditor.utils.TimestampUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple client implementation of RFC 3164 / BSD syslog 
 * for sending audit messages to an Audit Record Repository
 * that implements BSD syslog.  Facility formats message payloads
 * and includes syslog metadata (such as the message priority).
 * Multiple messages may be sent over the same socket so long as they
 * do not surpass the maximum allowed length of a UDP packet.
 * 
 * Designed to run in a standalone mode from the standard IHE Auditor
 * and is not dependent on any context or configuration.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class BSDSyslogSenderImpl implements AuditMessageSender
{

	/**
	 * Logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BSDSyslogSenderImpl.class);
	
	/**
	 * Default port for this transport
	 */
	public static final int TRANSPORT_DEFAULT_PORT = 514;
	
	/**
	 * Default syslog priority for this transport
	 */
	public static final int TRANSPORT_DEFAULT_PRIORITY = 13;
	
	/**
	 * Maximum UDP packet size for this transport
	 */
	private static final int MAX_DATAGRAM_PACKET_SIZE = 65507;

	/**
	 * Local system hostname for this sender
	 */
	private static String systemHostName;
	
	/**
	 * Default constructor
	 */
	public BSDSyslogSenderImpl()
	{

	}
	
	/**
	 * Send a byte buffer to a designated destination address and port using the 
	 * datagram socket specified.
	 * 
	 * @param msg Message buffer to send
	 * @param socket UDP socket to use
	 * @param destination Destination address to send to
	 * @param port Destination port to send to
	 * @throws Exception
	 */
	private void send(byte[] msg, DatagramSocket socket, InetAddress destination, int port) throws Exception
	{
		if (EventUtils.isEmptyOrNull(msg)) {
			return;
		}
		
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("Auditing to " + destination.getHostAddress() + ":" + port);
        }
        
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug(new String(msg));
        }

        DatagramPacket packet = new DatagramPacket(msg,getBufferLength(msg),destination,port);
        socket.send(packet);
	}
	
	/**
	 * Send an audit message to a designated destination address and port using the 
	 * datagram socket specified.
	 * 
	 * @param msg Message to send
	 * @param socket UDP socket to use
	 * @param destination Destination address to send to
	 * @param port Destination port to send to
	 * @throws Exception
	 */
	private void send(AuditEventMessage msg, DatagramSocket socket, InetAddress destination, int port) throws Exception
	{
		if (EventUtils.isEmptyOrNull(msg)) {
			return;
		}
		
		// Check to validate port
		int portToUse = getTransportPort(port);
		// Serialize and format event message for syslog
		byte[] msgBytes = getTransportPayload(msg);
		// Send packet
		send(msgBytes,socket,destination,portToUse);
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender#sendAuditEvent(org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage[])
	 */
	public void sendAuditEvent(AuditEventMessage[] msgs) throws Exception 
	{
        if (!EventUtils.isEmptyOrNull(msgs)) {
        	DatagramSocket socket = new DatagramSocket();
    		for (int i=0; i<msgs.length; i++) {
    			if (!EventUtils.isEmptyOrNull(msgs[i])) {
    				send(msgs[i], socket, msgs[i].getDestinationAddress(), msgs[i].getDestinationPort());
    			}
    		}
           socket.close();
        }
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender#sendAuditEvent(org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage[], java.net.InetAddress, int)
	 */
	public void sendAuditEvent(AuditEventMessage[] msgs, InetAddress destination, int port) throws Exception 
	{
        if (!EventUtils.isEmptyOrNull(msgs)) {
    		// Check to validate port
    		int portToUse = getTransportPort(port);
        	
        	DatagramSocket socket = new DatagramSocket();

    		for (int i=0; i<msgs.length; i++) {
    			byte[] msgBytes = getTransportPayload(msgs[i]);
    			send(msgBytes, socket, destination, portToUse);
    		}
           socket.close();
        }
	}

	/**
	 * Gets the port designated for this transport
	 * 
	 * @param port Port to check
	 * @return Port to use
	 */
	private int getTransportPort(int port)
	{
		if (port == -1) {
			return TRANSPORT_DEFAULT_PORT;
		}
		return port;
	}
	
	/**
	 * Get the buffer size.  Validates that the buffer does not
	 * exceed the length specified.
	 * 
	 * @param buf The buffer to check
	 * @return The size of the buffer
	 */
	private int getBufferLength(byte[] buf) {
		if (buf.length > MAX_DATAGRAM_PACKET_SIZE) {
			return MAX_DATAGRAM_PACKET_SIZE;
		}
		return buf.length;
	}
	
	/**
	 * Serialize, format, and prepare the message payload body 
	 * for sending by this transport.  This includes adding
	 * the syslog message header.
	 * 
	 * @param msg Message to prepare
	 * @return Buffer to send
	 */
	protected byte[] getTransportPayload(AuditEventMessage msg)
	{
		if (msg == null) {
			return null;
		}
		
		byte[] msgBytes = msg.getSerializedMessage(false);
		if (EventUtils.isEmptyOrNull(msgBytes)) {
			return null;
		}
		
		// Format message with transport-specific headers
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(TRANSPORT_DEFAULT_PRIORITY);
		sb.append(">");
		
		sb.append(TimestampUtils.getBSDSyslogDate(msg.getDateTime()));
		sb.append(" ");
		sb.append(getSystemHostName());
		sb.append(" ");
		sb.append("<?xml version=\"1.0\" encoding=\"ASCII\"?>");
		sb.append(new String(msgBytes));

		return sb.toString().trim().getBytes();
	}
	
	/**
	 * Gets the hostname of the system managing this transport
	 * @return The system's hostname
	 */
	private String getSystemHostName()
	{
		if (EventUtils.isEmptyOrNull(systemHostName)) {
			try {
				systemHostName = InetAddress.getLocalHost().getHostName();
			} catch (Throwable t) {
				systemHostName = "localhost";
			}
		}
		return systemHostName;
	}
}
