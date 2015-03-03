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
package org.openhealthtools.ihe.atna.auditor.context;

import java.util.Properties;


import org.openhealthtools.ihe.atna.context.AbstractModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextInitializer.class);

	public static void initialize(String fileName)
	{

	}
	
	public static void initialize(Properties properties)
	{
		
	}
	
	public static AbstractModuleContext initialize()
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AuditorModuleContext initialized");
		}
		return new AuditorModuleContext();
	}
	
	public static AbstractModuleContext initialize(AuditorModuleConfig config)
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AuditorModuleContext initialized");
		}
		return new AuditorModuleContext(config);
	}
	
	public static AbstractModuleContext defaultInitialize()
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AuditorModuleContext default initializer starting");
		}
		
		AuditorModuleContext context =  new AuditorModuleContext();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AuditorModuleContext default initializer ending");
		}
		return context;
	}
}
