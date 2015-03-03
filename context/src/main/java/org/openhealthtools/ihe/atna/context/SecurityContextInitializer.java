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

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static Security Context initializer for active loading and configuration
 * of the Security Context as well as for calling associated module contexts
 * that should be initialized by default upon loading of the security context.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public final class SecurityContextInitializer
{

    /**
     * Logger Instance
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextInitializer.class);
	
	/**
	 * Modules to actively initialize the contexts for when the security context is initialized
	 */
	private static final String[] DEFAULT_MODULES = new String[] {
		"org.openhealthtools.ihe.atna.nodeauth",
		"org.openhealthtools.ihe.atna.auditor"
		//,"org.openhealthtools.ihe.xua"
	};
	
	/**
	 * Name of the class inside each module that holds the module's initializer
	 */
	private static final String MODULE_INITIALIZER_CLASS = ".context.ContextInitializer";
	
	/**
	 * Name of the method inside the module's initializer that is invoked
	 */
	private static final String MODULE_INITIALIZER_METHOD = "defaultInitialize";
	
	/**
	 * 
	 */
	private SecurityContextInitializer()
	{
	}
	
	/**
	 * Perform initialization routines on a given security context
	 * @param context The Security Context to perform initialzation routines
	 */
	public static void initialize(SecurityContext context)
	{
		if (context.isInitialized()) {
			return;
		}
		initializeDefaultModules(context);
	}
	
	/**
	 * Initialize and register the contexts for modules that, by default,
	 * are initialized with the security context
	 * 
	 * @param context The Security Context to perform initialzation routines
	 */
	private static void initializeDefaultModules(SecurityContext context)
	{
		if (context.isInitialized()) {
			return;
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SecurityContext default module initializer starting");
		}
		
		String moduleName;
		
		// Loop through the module names
		for (int i=0; i<DEFAULT_MODULES.length; i++) {
			moduleName = DEFAULT_MODULES[i];
			try {
				LOGGER.debug(moduleName + MODULE_INITIALIZER_CLASS);
				
				// Get a class instance for the module initializer
				Class<?> clazz = Class.forName(moduleName + MODULE_INITIALIZER_CLASS);
				
				// Get the method instance for the module initializer's initialization method
				Method method = clazz.getMethod(MODULE_INITIALIZER_METHOD, (Class[])null);
				
				// Invoke the initialization method
				Object invokeResult = method.invoke(clazz.newInstance(), (Object[])null);
				
				// Validate that the result of the initialization is a valid module context
				if (invokeResult instanceof AbstractModuleContext) {
					
					// Register the module context
					context.registerModuleContext(moduleName, (AbstractModuleContext)invokeResult);
				} else {
					throw new IllegalArgumentException("Initializer method did not return correct type");
				}

				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("SecurityContext module "+ moduleName + " initialized");
				}
			}  catch (ClassNotFoundException cnfe) {
				LOGGER.warn("SecurityContext module "+ moduleName + " not found, skipping.");
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Stack trace: ", cnfe);
				}
			} catch (NoSuchMethodException nsme) {
				LOGGER.warn("SecurityContext module "+ moduleName + " does not support default initialization, skipping.", nsme);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Stack trace: ", nsme);
				}
			} catch (Throwable t) {
				LOGGER.error("Error initializing SecurityContext module "+ moduleName, t);
			} finally {

			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SecurityContext default module initializer ending");
		}		
	}
}
