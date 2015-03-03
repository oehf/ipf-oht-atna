/*******************************************************************************
 * Copyright (c) 2006,2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.nodeauth.handlers;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import javax.net.SocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhealthtools.ihe.atna.nodeauth.NoSecurityDomainException;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.SocketHandler;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;


/**
 * Abstract handler for all implementations of the secure socket handler
 * 
 * @author <a href="mailto:rgd@us.ibm.com">Glenn Deen</a>
 * @author <a href="mailto:rstevens@us.ibm.com">Rick Stevens</a>
 * @author <a href="mailto:srrenly@us.ibm.com">Sondra Renly</a>
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public abstract class AbstractSecureSocketHandler implements SocketHandler 
{
	/**
	 * Logger instance
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractSecureSocketHandler.class);
	
	/**
	 * Module context to use
	 */
	protected final NodeAuthModuleContext CONTEXT;
	
	public AbstractSecureSocketHandler(NodeAuthModuleContext context)
	{
		CONTEXT = context;
	}
	
	/**
	 * Creates a TLS-enabled secure socket for a given URI and SecurityDomain configuration
	 * 
	 * @param host Host to connect to
	 * @param port Port to connect to
	 * @param securityDomain Configuration options to use when securing the socket
	 * @return A socket secured using TLS for a given URI and SecurityDomain
	 * @throws ExceptionO
	 */
	protected abstract Socket createSecureSocket(String host, int port, SecurityDomain securityDomain, Socket socketWrapper) throws Exception;
	
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.nodeauth.SocketHandler#getSocket(java.net.URI, boolean)
	 */
	public Socket getSocket(URI uri, boolean wantTLS) throws Exception
	{
		if (uri == null) {
			throw new URISyntaxException("", "The URI specified cannot be null");
		}
		
		// Get the registered security domain for the URI
		SecurityDomain securityDomain = null;
		if (wantTLS) {
			securityDomain = CONTEXT.getSecurityDomainManager().getSecurityDomain(uri);
		}
		return getSocket(uri,wantTLS,securityDomain);
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.nodeauth.SocketHandler#getSocket(java.net.URI, boolean, org.openhealthtools.ihe.atna.nodeauth.SecurityDomain)
	 */
	public Socket getSocket(URI uri, boolean wantTLS, SecurityDomain securityDomain) throws Exception 
	{
		if (uri == null) {
			throw new URISyntaxException("", "The URI specified cannot be null");
		}

		String host = uri.getHost();
		int port = uri.getPort();
		
		if (!"".equals(host) && port > 0) {
			return getSocket(host,port,wantTLS,securityDomain);
		} else {
			throw new URISyntaxException(uri.toString(), "Invalid host or port specified.  Host cannot be empty and port must be defined and greater than zero");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.nodeauth.SocketHandler#getSocket(java.lang.String, int, boolean)
	 */
	public Socket getSocket(String host, int port, boolean useSecureSocket) throws Exception 
	{
		// Get the registered security domain for the URI
		SecurityDomain securityDomain = null;
		if (useSecureSocket) {
			securityDomain = CONTEXT.getSecurityDomainManager().getSecurityDomain(host,port);
		}
		return getSocket(host,port,useSecureSocket,securityDomain);
	}
	

	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.nodeauth.SocketHandler#getSocket(java.lang.String, int, boolean, org.openhealthtools.ihe.atna.nodeauth.SecurityDomain)
	 */
	public Socket getSocket(String host, int port, boolean useSecureSocket, SecurityDomain securityDomain) throws Exception {
		return getSocket(host,port,useSecureSocket,securityDomain,null);
	}
	
	public Socket getSocket(String host, int port, boolean useSecureSocket, SecurityDomain securityDomain, Socket socketWrapper) throws Exception {
		if (!CONTEXT.isTLSEnabled() && ! CONTEXT.isNonTLSConnectionsPermitted()) {
			throw new NoSuchAlgorithmException("TLS has been disabled for ATNA connections via "+NodeAuthModuleContext.class.getName()+".setTLSEnabled(false)");
		}

		boolean useTLS = useSecureSocket && CONTEXT.isTLSEnabled() || ! CONTEXT.isNonTLSConnectionsPermitted();

		Socket socket=null;
		if (useTLS) {
			if (null == securityDomain) {
				throw new NoSecurityDomainException("TLS was requested but no Security Domain provided for the host "+ host +" on port " + port);
			}

			socket = createSecureSocket(host,port,securityDomain,socketWrapper);
			if (useTLS) logger.info("Connection succesfully made using TLS to host "+ host +" on port " + port);
		} else {
			socket = createSocket(host,port);
		}
		return socket;
	}
	
	/**
     * Create non-secure socket for a given URI.
     * 
     * @param URI uri to connect to
     * @return socket
	 * @throws SocketException
	 * @throws UnknownHostException
     * @throws Exception
     */
	protected Socket createSocket(String host, int port) throws SocketException, UnknownHostException
    {
        Socket socket = null;
        int retries = 0;

        // Loop to get a connection or until we've exhausted number of retries
        Throwable cause = null;
        while (retries < CONTEXT.getConfig().getSocketRetries()) {
            try {
            	
            	socket = createSocketFromFactory(SocketFactory.getDefault(), host, port);
            	
                break;
            } catch (UnknownHostException e) {
            	logger.error("Unknown host. Unable to establish connection to " + host + " on port "+ port +". Reason: "+e.getLocalizedMessage(),e);
            	throw e;
            } catch (SocketException e) {
                // Some kind of error, retry if we haven't exceeded max retry
                // count
            	logger.error("Error connecting to " + host + " on port "+ port +". Will retry in " + CONTEXT.getConfig().getSocketRetryWait() / 1000 + " seconds. "
            			       + (CONTEXT.getConfig().getSocketRetries() - retries) +" retries left. Cause: "+e.getLocalizedMessage(),e);

            	cause = e;
            	retries++;
            	
                try {
                    Thread.sleep(CONTEXT.getConfig().getSocketRetryWait());
                    continue;
                } catch (InterruptedException ie) {
                	if(logger.isDebugEnabled()){
                		logger.debug("Sleep awoken early");
                	}
                    continue;
                }

            } catch (IOException e) {
                // Some kind of error, retry if we haven't exceeded max retry
                // count
            	logger.error("Error connecting to " + host + " on port "+ port +". Will retry in "
                        + CONTEXT.getConfig().getSocketRetryWait() / 1000 + " seconds. "
     			       + (CONTEXT.getConfig().getSocketRetries() - retries) +" retries left. Cause: "+e.getLocalizedMessage(),e);

                retries++;
                cause = e;
                continue;
            }
        }

        if (retries >= CONTEXT.getConfig().getSocketRetries()) {
			logger.error("Socket Connect Retries Exhausted.", cause);
           	throw new ConnectException("Socket Connect Retries Exhausted. "+ (cause != null ? "Cause was: "+cause.getLocalizedMessage():""));
        }

        return socket;
    }
	
	/**
	 * Creates a new connected socket to a given host and port from a provided Socket Factory.
	 * @param factory Java Socket Factory to use in the connection
	 * @param host Hostname to connect to
	 * @param port Port to connect to
	 * @return Connected socket instance from the given factory
	 * @throws IOException
	 */
	protected Socket createSocketFromFactory(SocketFactory factory, String host, int port) throws IOException
	{
       	if (logger.isDebugEnabled()) {
    		logger.debug("Connecting to " + host +" on port " + port + 
    				" (timeout: " + CONTEXT.getConfig().getConnectTimeout() + " ms) using factory "+ factory.getClass().getName());
    	}
    	SocketAddress address = new InetSocketAddress(host,port);
        Socket socket = factory.createSocket();
    	socket.connect(address, CONTEXT.getConfig().getConnectTimeout());
        // Set amount of time to wait on socket read before timing out
        socket.setSoTimeout(CONTEXT.getConfig().getSocketTimeout());
        socket.setKeepAlive(true);
        
        return socket;
	}
}
