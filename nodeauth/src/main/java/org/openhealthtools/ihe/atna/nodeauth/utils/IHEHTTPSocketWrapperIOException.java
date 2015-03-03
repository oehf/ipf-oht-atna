/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.openhealthtools.ihe.atna.nodeauth.utils;

import java.io.IOException;


/**
 * Wrapped IO Exception for socket creation failure 
 * capable of returning a nested exception cause.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class IHEHTTPSocketWrapperIOException extends IOException 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nested cause
	 */
	private Throwable cause;
	
	/**
	 *
	 * @param message
	 */
	public IHEHTTPSocketWrapperIOException(String message)
	{
		this(message,null);
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public IHEHTTPSocketWrapperIOException(String message, Throwable cause)
	{
		super(message);
		this.cause = cause;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getCause()
	 */
	public Throwable getCause()
	{
		return cause;
	}
}
