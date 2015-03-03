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

package org.openhealthtools.ihe.atna.nodeauth;

import java.io.InputStream;
import java.net.Socket;
import java.net.URI;

/**
 * Interface for creating socket handlers in the Node Authentication Module.
 * Specific actors / connectors needing access to an external resource via 
 * a socket may call methods in this interface, with the expectation of receiving
 * an open socket ready for input/output and, if requested, secured using the   
 * mechanisms defined by the socket handler's implementation 
 * (e.g. mutually-authenticated TLSv1).
 * 
 * @author <a href="mailto:glenn@almaden.ibm.com">Glenn Deen</a>
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public interface SocketHandler 
{
	/**
	 * 
	 * Negotiates and creates a socket for a given hostname and port  If requested, the socket is 
	 * secured using the implementation's security mechanisms and the default
	 * SecurityDomain for this host and port
	 * 
	 * @param host The hostname or IP address to create a socket to
	 * @param port The port to create a socket to
	 * @param useSecureSocket If the socket should be secured
	 * @return A socket ready for input/output
	 * @throws Exception
	 */
	public Socket getSocket(String host, int port, boolean useSecureSocket) throws Exception;
	
	/**
	 * Negotiates and creates a socket for a given hostname and port.  If a secure socket
	 * is requested, the supplied "SecurityDomain" configuration is used for keystore,
	 * truststore, and cipher information.
	 * 
	 * @param host The hostname or IP address to create a socket to
	 * @param port The port to create a socket to
	 * @param useSecureSocket If the socket should be secured
	 * @param securityDomain Security Domain config to use in securing the socket
	 * @return A socket ready for input/output
	 * @throws Exception
	 */
	public Socket getSocket(String host, int port, boolean useSecureSocket, SecurityDomain securityDomain) throws Exception;
	
	/**
	 * Negotiates and creates a socket for a given URI.  If requested, the socket is 
	 * secured using the implementation's security mechanisms and the default
	 * SecurityDomain for this URI.
	 * 
	 * @param uri The URI for which to open the socket
	 * @param useSecureSocket	If the socket should be secured
	 * @return A socket ready for input/output
	 * @throws Exception
	 */
	public Socket getSocket(URI uri, boolean useSecureSocket) throws Exception;
	
	/**
	 * Negotiates and creates a socket for a given URI.  If a secure socket
	 * is requested, the supplied "SecurityDomain" configuration is used for keystore,
	 * truststore, and cipher information.
	 * 
	 * @param uri The URI for which to open the socket
	 * @param useSecureSocket	If the socket should be secured
	 * @param securityDomain Security Domain config to use in securing the socket
	 * @return A socket ready for input/output
	 * @throws Exception
	 */
	public Socket getSocket(URI uri, boolean useSecureSocket, SecurityDomain securityDomain) throws Exception;
	
	/**
	 * Negotiates and creates a socket for a given hostname and port.  If a secure socket
	 * is requested, the supplied "SecurityDomain" configuration is used for keystore,
	 * truststore, and cipher information.
	 * 
	 * @param host The hostname or IP address to create a socket to
	 * @param port The port to create a socket to
	 * @param useSecureSocket If the socket should be secured
	 * @param securityDomain Security Domain config to use in securing the socket
	 * @param socketWrapper Socket to wrap connection in, generally for HTTPS tunnels
	 * @return A socket ready for input/output
	 * @throws Exception
	 */
	public Socket getSocket(String host, int port, boolean useSecureSocket, SecurityDomain securityDomain, Socket socketWrapper) throws Exception;
	
	/**
	 * MAD 09/24/2008
	 * Removing HTTP-sensitive input stream handlers for the time being
	 * These using a questionable construction mechanism that may not be safe
	 * for use throughout the module.
	 * 
	 * Use getSocket(...) in conjunction with a proper HTTP handler to get 
	 * an HTTPUrlConnection instance for HTTP POST/GET requests.
	 */
	/**
	 * Negotiates and creates a socket for a given URI and returns the InputStream
	 * associated with the socket.  If  a "secure" URI is given (e.g. HTTPS), 
	 * then a secure socket is created using the implementation's security mechanism.
	 * 
	 * @param uri The URI for which to open the socket
	 * @return An InputStream for reading from the socket
	 * @throws Exception
	 */
	public InputStream getInputStream(URI uri) throws Exception;
	
	/**
	 * Negotiates and creates a socket for a given URI and returns the InputStream
	 * associated with the socket.  If a "secure" URI is given (e.g. HTTPS), 
	 * then a secure socket is given created by the implementation's security mechanism
	 * using the SecurityDomain configuration supplied.
	 * 
	 * @param uri The URI for which to open the socket
	 * @param securityDomain Security Domain config to use in securing the socket
	 * @return An InputStream for reading from the socket
	 * @throws Exception
	 */
	public InputStream getInputStream(URI uri, SecurityDomain securityDomain) throws Exception;
}