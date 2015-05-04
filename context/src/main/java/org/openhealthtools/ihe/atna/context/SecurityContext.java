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
package org.openhealthtools.ihe.atna.context;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Universal, centrally-managed, thread-safe context handler for intra- and inter-plugin
 * configuration between the IHE profile actors, actor transports, and end users. 
 * The Security Context manages the configuration elements for features that require end-user
 * configuration but are not subject to the IHE actors being accessed (such as XDS and PIX).   
 * This includes the configuration and sending of ATNA audit messages, the configuration of TLS Node
 * Authentication mechanism, the handling of XUA assertions, etc.
 * 
 * The Security Context acts as a registry for each individual module context 
 * used by the implementation.  Each module's context shall being a singleton
 * instance and stored within this context, and accessed only via the global security
 * context.  The individual module context, when accessed by a factory, will 
 * access the registry managed by this context.
 * 
 * To get the security context, use the contex factory:
 * 
 * SecurityContext context = SecurityContextFactory.getSecurityContext();
 * 
 * To get an individual module context, use see {@link #getModuleContext(String)}
 * 
 * AbstractModuleContext moduleContext = context.getContext(CONTEXT_NAME);
 * 
 * The Security Context is lazy-loaded, as there are scenarios where it will not
 * be used.  However, it should be initialized before use.  Check the
 * initialization status via {@link #isInitialized()} and {@link #initialize()}.
 * 
 * The Security Context is not intended to be extended.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public final class SecurityContext implements Serializable
{
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -9442092576326191L;
	
	/**
	 * Logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContext.class);
	
	/**
	 * Registry of contexts stored in the security context
	 */
	private final Map<String,AbstractModuleContext> contexts = Collections.synchronizedMap(new HashMap<String,AbstractModuleContext>());

	/**
	 * Whether the security context is initialized
	 */
	private boolean isInitialized = false;
	
	/**
	 * Default constructor
	 */
	protected SecurityContext()
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating SecurityContext");
		}
		//initialize();
	}
	
	/**
	 * Get a module context instance registered for a specific name
	 * @param name Name of the module context to fetch
	 * @return The module context instance (or null if it's not registered)
	 */
	public AbstractModuleContext getModuleContext(String name)
	{
		return contexts.get(name);
	}
	
	/**
	 * Get a list of all contexts registered in this security context.  
	 * Note that the registry instance is not directly accessible
	 * @return The list of all registered contexts
	 */
	public Map<String,AbstractModuleContext> getAllRegisteredContexts()
	{
		return Collections.unmodifiableMap(contexts);
	}
	
	/**
	 * Register and place a new module context in the security context's
	 * registry
	 * 
	 * @param moduleName Name of the module context to register (generally the result of AbstractModuleContext#getContextId())
	 * @param context The module context instance to register
	 */
	public void registerModuleContext(String moduleName, AbstractModuleContext context)
	{
		contexts.put(moduleName, context);
	}
	
	/**
	 * Whether the Security Context has been initialized
	 * @return
	 */
	public boolean isInitialized()
	{
		return isInitialized;
	}
	
	/**
	 * Initialize the security context using the rules
	 * and any default modules that should beinitialized 
	 * with it
	 */
	public synchronized void initialize()
	{
		if (isInitialized()) {
			return;
		}
		SecurityContextInitializer.initialize(this);
		isInitialized = true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.hashCode() + "; cCount=" + contexts.size();
	}
}
