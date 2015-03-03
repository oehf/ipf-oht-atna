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

import java.net.URI;

/**
 * Exception for handling instances where no security domain is specified when
 * attempting to lookup a keystore/truststore for a transaction.
 * 
 * @author Glenn Deen  <a href="mailto:glenn@almaden.ibm.com">glenn@almaden.ibm.com</a>
 *
 * @since OHF 0.1.0
 */
public class NoSecurityDomainException extends SecurityDomainException
{
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * URI that failed Security Domain lookup
	 */
	protected final URI uri;

	public NoSecurityDomainException(String message)
	{
		this(null,message,null);
	}
	
	/**
	 * @param uri URI that failed the Security Domain lookup
	 * @param message Message relating to the error
	 */
	public NoSecurityDomainException(URI uri, String message)
	{
		this(uri,message,null);
	}

	/**
	 * @param uri URI that failed the Security Domain lookup
	 * @param message Message relating to the error 
	 * @param cause Root cause of the exception
	 */
	public NoSecurityDomainException(URI uri, String message, Throwable cause)
	{
		super("", message, cause);
		this.uri = uri;

	}
	
	/**
	 * Gets the URI that this exception is related to
	 * @return The URI that this exception is related to
	 */
	public URI getURI()
	{
		return  uri;
	}
}
