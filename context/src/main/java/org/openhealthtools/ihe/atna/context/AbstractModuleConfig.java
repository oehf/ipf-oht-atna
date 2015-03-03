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
import java.util.Properties;

/**
 * Abstract implementation of a module configuration.  All modules that wish
 * to use the module context and configuration paradigm should extend this class.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 */
public abstract class AbstractModuleConfig implements Serializable, Cloneable 
{
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -4898391890555433842L;
	
	/**
	 * Property elements acting as configuration for this config
	 */
	private final Properties config;

	/**
	 * Default constructor
	 */
	public AbstractModuleConfig()
	{
		this(new Properties());
	}
	
	/**
	 * Constructor accepting a set of pre-defined property values
	 * to act as the backed configuration.
	 * 
	 * @param p Properties to use for configuration
	 */
	public AbstractModuleConfig(Properties p)
	{
		config = p;
	}
	
	/**
	 * Get a value from the properties in this
	 * module config.
	 * 
	 * @param key The key name of the property to fetch
	 * @return The property value
	 */
	public synchronized String getOption(String key)
	{
		return config.getProperty(key);
	}
	
	/**
	 * Set a value to the backed properties in this
	 * module config.
	 * 
	 * @param key The key of the property to set
	 * @param value The value of the property to set
	 */
	public synchronized void setOption(String key, String value)
	{
		if (key == null || value == null) {
			return;
		}
		config.put(key, value);
	}
	
	/**
	 * Get the internal properties that back this configuration file
	 * @return Internal properties that back this configuration file
	 */
	protected Properties getProperties()
	{
		return config;
	}

}
