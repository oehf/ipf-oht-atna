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

/**
 * Generic module context, for testing purposes only. This class 
 * should NOT be used in any implementation and cannot be 
 * extended.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public final class GenericModuleContext extends AbstractModuleContext 
{
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 8605143033670326892L;
	
	/**
	 * Context ID for the Generic Module Context
	 */
	public static final String CONTEXT_ID = "org.openhealthtools.ihe.atna.context.generic";
	
	/**
	 * 
	 */
	public GenericModuleContext()
	{
		this(null);
	}
	
	/**
	 * @param configToSet
	 */
	public GenericModuleContext(AbstractModuleConfig configToSet) {
		super(new GenericModuleConfig());
	}

	public String getContextId() {
		return CONTEXT_ID;
	}
}
