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
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/**
 * Wrapper to pass an ATNA-generated secure socket to the Jakarta
 * Commons httpclient manager to for transport over SSL.
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class IHEHTTPSecureSocketFactoryWrapper 
extends IHEHTTPSocketFactoryWrapper implements SecureProtocolSocketFactory
{
	/**
	 * 
	 */
	public IHEHTTPSecureSocketFactoryWrapper() {
		super(true);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory#createSocket(java.net.Socket, java.lang.String, int, boolean)
	 */
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
			throws IOException, UnknownHostException {
		return executeCreateSocket(host, port, socket);
	}
	

}
