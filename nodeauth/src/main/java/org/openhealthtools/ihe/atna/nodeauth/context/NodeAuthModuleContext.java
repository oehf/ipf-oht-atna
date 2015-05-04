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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhealthtools.ihe.atna.context.AbstractModuleContext;
import org.openhealthtools.ihe.atna.context.SecurityContext;
import org.openhealthtools.ihe.atna.context.SecurityContextFactory;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomainManager;
import org.openhealthtools.ihe.atna.nodeauth.SocketHandler;
import org.openhealthtools.ihe.atna.nodeauth.handlers.TLSEnabledSocketHandler;

/**
 * Module context that manages fixed resources and configuration options
 * for the IHE Node Authentication (NA of ATNA) module.  The primary 
 * purpose of this module is to enable the creation of sockets secured
 * using Transport Layer Security (TLS) version 1, also known as 
 * the IHE ITI-19 Node Authentication transaction.
 * 
 * These secured sockets may be non-encrypted (for trusted networks) or 
 * encrypted (for non-trusted networks) using a given set of ciphers.  
 * The most important part is the mutual authentication during socket
 * negotiations, which ensure that a client and its peer are aware 
 * and trust each other using a private key and a set trusted certificates.
 * 
 * The Node Authentication Context manages the following resources in a 
 * global scope:
 * <ul>
 *  <li>The Socket Handler (default is a TLS-enabled Socket Handler)</li>
 *  <li>The Security Domain manager and a mapping of URIs to security domain
 *    (default is to use Java System Property-assigned stores for all URIs)</li>
 * </ul>
 * 
 * Additionally, the context manages the following configuration settings,
 * also in a global scope:
 * <ul>
 *  <li>Whether TLS security is enabled</li>
 *  <li>Whether non-TLS connections are allowed</li>
 *  <li>The number of times to retry a socket connection</li>
 *  <li>The wait time between socket retries</li>
 *  <li>The socket timeout wait time</li>
 * </ul>
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public class NodeAuthModuleContext extends AbstractModuleContext 
{
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 1416678175002183697L;

	/**
	 * Logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(NodeAuthModuleContext.class);
	
	/**
	 * Context ID for the Node Authentication Module Context
	 */
	private static final String CONTEXT_ID = "org.openhealthtools.ihe.atna.nodeauth";
	
	/**
	 * Socket handler to use for socket creation
	 */
    private SocketHandler socketHandler;
    
    /**
     * Security Domain manager to manage the creation and retrieval of 
     * Security Domain configurations (for keystores and truststores)
     */
    private SecurityDomainManager domainManager = new SecurityDomainManager();


	/**
	 * Returns the current singleton instance of the Node Authentication Module Context from the
	 * ThreadLocal cache.  If the ThreadLocal cache has not been initialized or does not contain
	 * this context, then create and initialize module context, register in the ThreadLocal
	 * and return the new instance.
	 * 
	 * @return Context singleton
	 */    
	public static NodeAuthModuleContext getContext()
	{
		SecurityContext securityContext = SecurityContextFactory.getSecurityContext();
		if (!securityContext.isInitialized()) {
			securityContext.initialize();
		}
		
		AbstractModuleContext moduleContext = securityContext.getModuleContext(CONTEXT_ID);
		
		if (!(moduleContext instanceof NodeAuthModuleContext)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Initializing the NodeAuthModuleContext");
			}
			moduleContext = ContextInitializer.defaultInitialize();
			securityContext.registerModuleContext(CONTEXT_ID, moduleContext);
		}
		
		return (NodeAuthModuleContext)moduleContext;
	}
	
	/**
	 * Default constructor using a blank (default-initialized) configuration
	 */
	protected NodeAuthModuleContext() 
	{
		super(new NodeAuthModuleConfig());
		socketHandler = new TLSEnabledSocketHandler(this);
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.context.AbstractModuleContext#getConfig()
	 */
	public NodeAuthModuleConfig getConfig()
	{
		return (NodeAuthModuleConfig)config;
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.context.AbstractModuleContext#getContextId()
	 */
	public String getContextId() 
	{
		return CONTEXT_ID;
	}
	
	/**
	 * Gets the socket creation handler set in this context for creating
	 * secure and non-secured sockets
	 * @return The context's socket handler
	 */
	public final SocketHandler getSocketHandler()
	{
		return socketHandler;
	}
	
	/**
	 * Gets the security domain manager that stores and associates domain 
	 * configurations to fixed URLs.
	 * 
	 * @return The context's security domain manager
	 */
	public final SecurityDomainManager getSecurityDomainManager()
	{
		return domainManager;
	}
	    
    /**
     * Sets if TLS connections are currently allowed
     * @param enableTLS Allow TLS connections (true) or not (false)
     */
	public void setTLSEnabled(boolean enableTLS) 
	{
		getConfig().setTLSEnabled(enableTLS);
	}
	
    /**
     * Returns if TLS connections are currently allowed
     * @return If TLS connections are allowed
     */
	public boolean isTLSEnabled()
	{
		return getConfig().isTLSEnabled();
	}
	
    /**
     * Sets if non-TLS connections are permitted
     * @param enableNonTLS Allow non-TLS connections (true) or not (false)
     */	
	public void setNonTLSConnectionsPermitted(boolean enableNonTLS) 
	{
		getConfig().setNonTLSConnectionsPermitted(enableNonTLS);
	}
	
	/**
     * Returns if non-TLS connections can be created.  If this is false, 
     * and a non-secure socket is created, then an exception is thrown
     * @return Whether non-TLS sockets can be created
     */
	public boolean isNonTLSConnectionsPermitted()
	{
		return getConfig().isNonTLSConnectionsPermitted();
	}
}
