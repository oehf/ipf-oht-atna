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
package org.openhealthtools.ihe.atna.auditor;

import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.context.ContextInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class to instantiate and configure actor-specific
 * auditors for use in IHE actors
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public final class AuditorFactory 
{
	/**
	 * Logger Instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditorFactory.class);
	
	/**
	 * Get an auditor instance for the specified auditor class, 
	 * auditor configuration, and auditor context.
	 * 
	 * @param clazz	Class to instantiate
	 * @param configToUse	Auditor configuration to use
	 * @param contextToUse	Auditor context to use
	 * @return Instance of an IHE Auditor
	 */
	public static IHEAuditor getAuditor(Class<? extends IHEAuditor> clazz, AuditorModuleConfig configToUse, AuditorModuleContext contextToUse) 
	{	
		IHEAuditor auditor = AuditorFactory.getAuditorForClass(clazz);
		
		if (auditor != null) {
			auditor.setConfig(configToUse);
			auditor.setContext(contextToUse);
		}
		
		return auditor;
	}
	
	/**
	 * Get an auditor instance for the specified auditor class, module configuration.  Auditor 
	 * will use a standalone context if a non-global context is requested. 
	 * 
	 * @param clazz	Class to instantiate
	 * @param config Auditor configuration to use
	 * @param useGlobalContext Whether to use the global (true) or standalone (false) context
	 * @return Instance of an IHE Auditor
	 */
	public static IHEAuditor getAuditor(Class<? extends IHEAuditor> clazz, AuditorModuleConfig config, boolean useGlobalContext)
	{
		AuditorModuleContext context;
		if (!useGlobalContext) {
			context = (AuditorModuleContext)ContextInitializer.initialize(config);
		} else {
			context = AuditorModuleContext.getContext();
		}
		
		return getAuditor(clazz, config, context);
	}
	
	/**
	 * Get an auditor instance for the specified auditor class.  Auditor 
	 * will use a standalone configuration or context if a non-global 
	 * configuration / context is requested.  If a standalone configuration
	 * is requested, the existing global configuration is cloned and used as the
	 * base configuration.
	 * 
	 * @param clazz	Class to instantiate
	 * @param useGlobalConfig Whether to use the global (true) or standalone (false) config
	 * @param useGlobalContext Whether to use the global (true) or standalone (false) context
	 * @return Instance of an IHE Auditor
	 */
	public static IHEAuditor getAuditor(Class<? extends IHEAuditor> clazz, boolean useGlobalConfig, boolean useGlobalContext)
	{
		AuditorModuleConfig config = AuditorModuleContext.getContext().getConfig();
		if (!useGlobalConfig) {
			config = config.clone();
		}

		return getAuditor(clazz, config, useGlobalContext);
	}
	
	/**
	 * Get an auditor instance for the specified auditor class name, 
	 * auditor configuration, and auditor context.
	 * 
	 * @param className Class name of the auditor class to instantiate
	 * @param config Auditor configuration to use
	 * @param context Auditor context to use
	 * @return Instance of an IHE Auditor
	 */
	public static IHEAuditor getAuditor(String className, AuditorModuleConfig config, AuditorModuleContext context)
	{
		Class<? extends IHEAuditor> clazz = AuditorFactory.getAuditorClassForClassName(className);
		return getAuditor(clazz, config, context);
	}
	
	/**
	 * Get an auditor instance for the specified auditor class name, module configuration.   
	 * Auditor will use a standalone context if a non-global context is requested
	 * 
	 * @param className Class name of the auditor class to instantiate
	 * @param config Auditor configuration to use
	 * @param useGlobalContext Whether to use the global (true) or standalone (false) context
	 * @return Instance of an IHE Auditor
	 */
	public static IHEAuditor getAuditor(String className, AuditorModuleConfig config, boolean useGlobalContext)
	{
		Class<? extends IHEAuditor> clazz = AuditorFactory.getAuditorClassForClassName(className);
		return getAuditor(clazz, config, useGlobalContext);
	}
	
	/**
	 * Get an auditor instance for the specified auditor class name.  Auditor 
	 * will use a standalone configuration or context if a non-global 
	 * configuration / context is requested.  If a standalone configuration
	 * is requested, the existing global configuration is cloned and used as the
	 * base configuration.
	 * 
	 * @param className Class name of the auditor class to instantiate
	 * @param useGlobalConfig Whether to use the global (true) or standalone (false) config
	 * @param useGlobalContext Whether to use the global (true) or standalone (false) context
	 * @return Instance of an IHE Auditor
	 */
	public static IHEAuditor getAuditor(String className, boolean useGlobalConfig, boolean useGlobalContext)
	{
		Class<? extends IHEAuditor> clazz = AuditorFactory.getAuditorClassForClassName(className);
		return getAuditor(clazz,useGlobalConfig, useGlobalContext);
	}
	
	/**
	 * Get a Class instance for the fully-qualified class name specified.  The specified
	 * class should have IHEAuditor as a superclass and implement the necessary 
	 * methods to be an IHE Auditor.
	 * 
	 * @param className	Fully-qualified class name to get a class instance of
	 * @return The requested class instance
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends IHEAuditor> getAuditorClassForClassName(String className)
	{
		try {
			Class<?> clazz = Class.forName(className);
			return (Class<? extends IHEAuditor>)clazz;
		} catch (ClassCastException e) {
			LOGGER.error("The requested class "+ className +" is not a valid auditor", e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Could not find the requested auditor class "+ className, e);
		} catch (Exception e) {
			LOGGER.error("Error creating the auditor for "+ className, e);
		}
		return null;
	}
	
	/**
	 * Create a new instance of the specified IHE Auditor non-abstract subclass that 
	 * implements the auditor API.
	 * 
	 * @param clazz	Class instance to instantiate a new instance for
	 * @return New IHE Auditor instance
	 */
	private static IHEAuditor getAuditorForClass(Class<? extends IHEAuditor> clazz)
	{
		if (null == clazz) {
			LOGGER.error("Error - Cannot specify a null auditor class");
		} else {
			try {
				return clazz.newInstance();
			} catch (ClassCastException e) {
				LOGGER.error("The requested class "+ clazz.getName() +" is not a valid auditor", e);
			} catch (Exception e) {
				LOGGER.error("Error creating the auditor for class "+ clazz.getName(), e);
			}
		}
		return null;
	}
	
	

}
