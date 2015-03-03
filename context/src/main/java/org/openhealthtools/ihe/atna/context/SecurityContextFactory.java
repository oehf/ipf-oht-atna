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

import org.openhealthtools.ihe.utils.thread.ConfigurableInheritableThreadLocal;


/**
 * Factory to create, initialize, and hold in a Thread Local contex the singleton 
 * instance of the Security Context for use throughout the IHE Security-enabled
 * modules.  The instance of the security context should be fetched statically
 * using the following:
 * 
 * SecurityContext context = SecurityContextFactory.getInstance().getSecurityContext()
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public class SecurityContextFactory
{	
	/**
	 * Thread Local instance of the Security Context module
	 */
	private static final ConfigurableInheritableThreadLocal<SecurityContext> SECURITY_CONTEXT_THREAD_LOCAL_INSTANCE;
	static {
		// Initialize the singleton instance
		SECURITY_CONTEXT_THREAD_LOCAL_INSTANCE = new ConfigurableInheritableThreadLocal<SecurityContext>()
		{
			protected synchronized SecurityContext initialValue()
			{
				return new SecurityContext();
			}
		};
	}

	
	/**
	 * Private constructor for the context factory
	 */
	private SecurityContextFactory()
	{
	}
	
	/**
	 * Get the singeton instance of the security context
	 * @return The security context instance
	 */
	public static synchronized SecurityContext getSecurityContext()
	{
		return SECURITY_CONTEXT_THREAD_LOCAL_INSTANCE.get();
	}
		
}
