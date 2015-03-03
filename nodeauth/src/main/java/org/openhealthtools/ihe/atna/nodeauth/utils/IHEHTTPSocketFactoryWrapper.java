/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.openhealthtools.ihe.atna.nodeauth.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;

/**
 * Wrapper to pass an ATNA-generated secure socket to the Jakarta
 * Commons httpclient manager to for transport over SSL.
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class IHEHTTPSocketFactoryWrapper implements ProtocolSocketFactory 
{
	private boolean isSecureWrapper = false;
	
	public IHEHTTPSocketFactoryWrapper()
	{
		this(false);
	}
	
	public IHEHTTPSocketFactoryWrapper(boolean useSecure) 
	{
		isSecureWrapper = useSecure;
	}

	/**
	 * Create a secure socket from a given security domain
	 * @param host Host to create socket to
	 * @param port Port to create socket to
	 * @param s Optional pre-created socket to tunnel through
	 * @return New socket
	 * @throws IOException
	 */
	protected Socket executeCreateSocket(String host, int port, Socket s) throws IOException
	{
		try {
			NodeAuthModuleContext ctx = NodeAuthModuleContext.getContext();
			SecurityDomain securityDomain = null;
			if (isSecureWrapper) {
				if (!ctx.isTLSEnabled() && !ctx.isNonTLSConnectionsPermitted()) {
					throw new NoSuchAlgorithmException( "TLS has been disabled for ATNA connections via "
									+ NodeAuthModuleContext.class.getName()
									+ ".setSetTLSEnabled(false)");
				}
				
				securityDomain = ctx.getSecurityDomainManager().getSecurityDomain(host, port);
			}
			
			return ctx.getSocketHandler().getSocket(host, port, isSecureWrapper, securityDomain, s);
		} catch (Exception e) {
			throw new IHEHTTPSocketWrapperIOException("Error opening socket for send to host "+ host+" on port " + port, e);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int)
	 */
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return executeCreateSocket(host, port, null);
	}
	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
	 */
	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException, UnknownHostException {
		return executeCreateSocket(host, port, null);
	}
	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int, org.apache.commons.httpclient.params.HttpConnectionParams)
	 */
	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
		return executeCreateSocket(host, port, null);
	}
}
