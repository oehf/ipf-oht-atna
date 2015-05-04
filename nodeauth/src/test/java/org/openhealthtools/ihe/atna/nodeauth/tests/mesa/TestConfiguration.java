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

package org.openhealthtools.ihe.atna.nodeauth.tests.mesa;

/**
 * Test Configuration for IHE Secure Node / Secure Actor MESA Tests
 * for TLS testing
 *
 */
public interface TestConfiguration 
{
	/**
	 * Hostname of the machine running your MESA software
	 */
	String MESA_SERVER_HOST = "localhost";
	
	/**
	 * Local path to the Java Key Store containing the MESA client key
	 */
	String MESA_KEYSTORE_FILE = "/mesa_certs/test_sys_1.2009.jks";
	
	/**
	 * Java Key Store Password for the MESA client key
	 */
	String MESA_KEYSTORE_PASSWORD = "mesa";
	
	/**
	 * Local path to the Java Key Store containing the trusted MESA server certificates
	 */
	String MESA_TRUSTSTORE_FILE = "/mesa_certs/mesatrusts.2009.jks";
	
	/**
	 * Java Key Store Password for the MESA trusted server certificates
	 */
	String MESA_TRUSTSTORE_PASSWORD = "mesa";
}
