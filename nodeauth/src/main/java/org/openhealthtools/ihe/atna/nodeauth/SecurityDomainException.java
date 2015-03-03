/*******************************************************************************
 * Copyright (c) 2006,2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.nodeauth;

/**
 * Exception created relating to the creation or lookup of a Security Domain
 * 
 * @author Glenn Deen  <a href="mailto:glenn@almaden.ibm.com">glenn@almaden.ibm.com</a>
 *
 * @since OHF 0.1.0
 */
public class SecurityDomainException extends Exception
{
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -3582793025114250485L;
	
	/**
	 * Security domain that failed
	 */
	private final String domain;

	/**
	 * @param domain Security Domain for which the exception was created
	 */
	public SecurityDomainException(String domain)
	{
		this(domain,null,null);
	}

	/**
	 * @param domain Security Domain for which the exception was created
	 * @param message Information related to the creation of the exception
	 */
	public SecurityDomainException(String domain, String message)
	{
		this(domain,message,null);

	}

	/**
	 * @param domain Security Domain for which the exception was created
	 * @param message Information related to the creation of the exception
	 * @param cause Root cause of the exception
	 */
	public SecurityDomainException(String domain, String message, Throwable cause)
	{
		super(message, cause);
		this.domain = domain;
	}
	
	/**
	 * Gets the Security Domain for which the exception was created
	 * @return The Security Domain for which the exception was created
	 */
	public String getDomain()
	{
		return this.domain;
	}

}
