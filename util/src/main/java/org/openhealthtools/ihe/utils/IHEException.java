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

package org.openhealthtools.ihe.utils;


public class IHEException extends Exception {

	private static final long serialVersionUID = 3333261489669700628L;

	public IHEException() {
		this("");
	}
	
	public IHEException(String arg0) {
		super(arg0);
	}

	public IHEException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IHEException(Throwable arg0) {
		super(arg0);
	}

	public IHEException(String message, Exception e) {
		super(message, e);
	}

}
