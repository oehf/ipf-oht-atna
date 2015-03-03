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

/**
 * Abstract implementation of the Module Context class, for handling
 * inter- and intra-module configuration at a global level.   A module
 * context, upon creation, is registered in the global 'Security Context' 
 * and should be access through either the SecurityContext's registration
 * method OR through the static factory method in each context that allows
 * singleton access to the instance via a simplified API.
 * 
 * Module contexts are intended to be singletons and should be handled this way.
 * A user or implementor should not instantiate one directly (use the initializer
 * classes instead).
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public abstract class AbstractModuleContext implements Serializable, Cloneable
{

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -6882642527901228642L;

	/**
	 * Underlying implementation of the module configuration to be used
	 * by this class.  In most cases this will be an implementation of a
	 * specific module's configuration file, with specialized wrappers for
	 * individual configuration elements
	 */
	protected final AbstractModuleConfig config;
	
	/**
	 * The global security context that this module context is registered in
	 */
	protected final SecurityContext securityContext;

	/**
	 * Constructor that accepts a module configuration instance to use.  If the
	 * module configuration instance is null, then a generic config will be used.
	 * 
	 * @param configToSet The module configuration to use
	 */
	public AbstractModuleContext(AbstractModuleConfig configToSet)
	{
		securityContext = SecurityContextFactory.getSecurityContext();
		if (null == configToSet) {
			config = new GenericModuleConfig();
		} else {
			config = configToSet;
		}
	}
	
	/**
	 * Get the module context's underlying configuration instance
	 * @return The module context's configuration instance
	 */
	public AbstractModuleConfig getConfig()
	{
		return config;
	}
	
	/**
	 * Get the global security context that this module context is registered in.  Same as 
	 * calling <link>SecurityContextFactory.getSecurityContext()</link>
	 * 
	 * @see org.openhealthtools.ihe.atna.context.SecurityContextFactory#getSecurityContext()
	 * @return The global security context
	 */
	public SecurityContext getSecurityContext()
	{
		return securityContext;
	}
	
	/**
	 * Get the context ID of the module context
	 * @return	The context's identifier
	 */
	public abstract String getContextId();
}
