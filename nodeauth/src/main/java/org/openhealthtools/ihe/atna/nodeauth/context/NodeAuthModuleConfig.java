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
package org.openhealthtools.ihe.atna.nodeauth.context;

import org.openhealthtools.ihe.atna.context.AbstractModuleConfig;

/**
 * Module Context Configuration manager for the Node Authentication Module.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public class NodeAuthModuleConfig extends AbstractModuleConfig
{
    /**
	 * Serial Number
	 */
	private static final long serialVersionUID = 1540226641819947849L;

	/**
     * Config reference key for TLS Enable/Disable
     */
    public static final String NODEAUTH_TLS_ENABLED_KEY = "nodeauth.tls.enabled";
    
    /**
     * Config reference key to enable/disable non-TLS connections
     */
    public static final String NODEAUTH_NON_TLS_CONNECTIONS_PERMITTED_KEY = "nodeauth.nontls.permitted";
    
    /**
     * Config reference key for the number of socket retries before failure
     */
    public static final String NODEAUTH_SOCKET_RETRIES_KEY = "nodeauth.socket.retries";
    
    /**
     * Config reference key for the number of milliseconds between each retry
     */
    public static final String NODEAUTH_SOCKET_RETRY_WAIT_KEY = "nodeauth.socket.retry.wait";
    
    /**
     * Config reference key for the number of milliseconds before the socket times out
     */
    public static final String NODEAUTH_SOCKET_TIMEOUT_KEY = "nodeauth.socket.timeout";
    
    /**
     * Config reference key for the number of milliseconds before the socket times out while connecting
     */
    public static final String NODEAUTH_CONNECT_TIMEOUT_KEY = "nodeauth.connect.timeout";
    
    /**
     * Default constructor for the Node Authentication Module configuration
     */
    public NodeAuthModuleConfig()
    {
        super();
        setTLSEnabled(true);
        setNonTLSConnectionsPermitted(true);
        setSocketRetries(ContextInitializer.DEFAULT_SOCKET_RETRIES);
        setSocketRetryWait(ContextInitializer.DEFAULT_SOCKET_RETRY_WAIT);
        setSocketTimeout(ContextInitializer.DEFAULT_SOCKET_TIMEOUT);
        setConnectTimeout(ContextInitializer.DEFAULT_CONNECT_TIMEOUT);
    }
 
    /**
     * Returns if TLS connections are currently allowed
     * @return If TLS connections are allowed
     */
    public boolean isTLSEnabled()
    {
        return Boolean.valueOf(getOption(NODEAUTH_TLS_ENABLED_KEY));
    }
    
    /**
     * Sets if TLS connections are currently allowed
     * @param enable Allow TLS connections (true) or not (false)
     */
    public void setTLSEnabled(boolean enable)
    {
        setOption(NODEAUTH_TLS_ENABLED_KEY, Boolean.toString(enable));
    }
    
    /**
     * Returns if non-TLS connections can be created.  If this is false, 
     * and a non-secure socket is created, then an exception is thrown
     * @return Whether non-TLS sockets can be created
     */
    public boolean isNonTLSConnectionsPermitted()
    {
        return Boolean.valueOf(getOption(NODEAUTH_NON_TLS_CONNECTIONS_PERMITTED_KEY));
    }
    
    /**
     * Sets if non-TLS connections are permitted
     * @param enable Allow non-TLS connections (true) or not (false)
     */
    public void setNonTLSConnectionsPermitted(boolean enable)
    {
        setOption(NODEAUTH_NON_TLS_CONNECTIONS_PERMITTED_KEY, Boolean.toString(enable));
    }
    
    /**
     * Gets the number of times to try opening a socket before giving up
     * @return	The number of connect tries
     */
    public int getSocketRetries()
    {
    	return Integer.valueOf(getOption(NODEAUTH_SOCKET_RETRIES_KEY));
    }
    
    /**
     * Sets the number of times to try opening a socket before giving up
     * @param count	The number of tries
     */
    public void setSocketRetries(int count)
    {
    	setOption(NODEAUTH_SOCKET_RETRIES_KEY, Integer.toString(count));
    }
    
    /**
     * Gets the number of milliseconds to wait between each connect attempt
     * if the previous failed
     * @return The wait time in milliseconds between attempts
     */
    public int getSocketRetryWait()
    {
    	return Integer.valueOf(getOption(NODEAUTH_SOCKET_RETRY_WAIT_KEY));
    }
    
    /**
     * Sets the number of milliseconds to wait between each connect attempt
     * if the previous failed
     * @param ms The number of milliseconds to wait
     */
    public void setSocketRetryWait(int ms)
    {
    	setOption(NODEAUTH_SOCKET_RETRY_WAIT_KEY, Integer.toString(ms));
    }
    
    /**
     * Gets the number of milliseconds to wait before a socket times out
     * @return The socket timeout in milliseconds
     */
    public int getSocketTimeout()
    {
    	return Integer.valueOf(getOption(NODEAUTH_SOCKET_TIMEOUT_KEY));
    }
    
    /**
     * Sets the number of milliseconds to wait before a socket times out
     * @param ms The socket timeout in milliseconds
     */
    public void setSocketTimeout(int ms)
    {
    	setOption(NODEAUTH_SOCKET_TIMEOUT_KEY, Integer.toString(ms));
    }
    
    /**
     * Gets the number of milliseconds to wait before a socket times out
     * @return The socket timeout in milliseconds
     */
    public int getConnectTimeout()
    {
    	return Integer.valueOf(getOption(NODEAUTH_CONNECT_TIMEOUT_KEY));
    }
    
    /**
     * Sets the number of milliseconds to wait before a socket times out while connecting
     * @param ms The socket connect timeout in milliseconds
     */
    public void setConnectTimeout(int ms)
    {
    	setOption(NODEAUTH_CONNECT_TIMEOUT_KEY, Integer.toString(ms));
    }
}

