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

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhealthtools.ihe.atna.context.AbstractModuleContext;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomainException;

public class ContextInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextInitializer.class);
	
	public static int DEFAULT_CONNECT_TIMEOUT = 5000;
	
	public static int DEFAULT_SOCKET_TIMEOUT = 60000;
	
	public static int DEFAULT_SOCKET_RETRIES = 3;
	
	public static int DEFAULT_SOCKET_RETRY_WAIT = 2000;
	
	public static void initialize(String fileName)
	{
		
	}
	
	public static void initialize(Properties properties)
	{
		
	}
	
	public static AbstractModuleContext initialize()
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("NodeAuthModule initialized");
		}
		return new NodeAuthModuleContext();
	}
	
	public static AbstractModuleContext defaultInitialize()
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("NodeAuthModule default initializer starting");
		}
		NodeAuthModuleContext context =  new NodeAuthModuleContext();
		
		// Register default security domain (checks javax.net.ssl.keystore)
		
		//ContextInitializer.registerDefaultSecurityDomain(context);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("NodeAuthModule default initializer ending");
		}
		return context;
	}
	

}
