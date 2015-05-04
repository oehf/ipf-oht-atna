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

package org.openhealthtools.ihe.atna.auditor.tests.mesa;

import java.net.URI;

/**
 * Test Configuration for IHE Secure Node / Secure Application Auditor MESA Tests
 *
 */
public interface TestConfiguration 
{
	/**
	 * URI of the machine running your MESA software
	 */
	// public MESA TLS instance:
	URI MESA_URI = URI.create("tls://ihe-kudu.wustl.edu:4003");
	// local MESA TLS instance:
	//public static final URI MESA_URI = URI.create("tls://localhost:4003");
	// examples for udp, bsd:
	//public static final URI MESA_URI = URI.create("udp://ihe-kudu.wustl.edu:4001");
	//public static final URI MESA_URI = URI.create("bsd://localhost:514"); 
	
	/**
	 * Name of your Connectathon system - e.g. OTHER_IBM_BRIDGE
	 */
	String MESA_SYSTEM_ID = "YOUR_CONNECTATHON_SYSTEM_NAME";
	
	/**
	 * Enterprise Name of your Connectathon system - e.g. IBM_CORPORATION
	 */
	String MESA_ENTERPRISE_SYSTEM_ID = "YOUR_CONNECTATHON_ENTERPRISE_SYSTEM_NAME";
	
	/**
	 * Your personal user identity - such as a username
	 */
	String MESA_USER_IDENTITY = "myUsername";
	
	// MESA testing allows you to use this generic private key, but to look smart
	// you should probably replace it with your own:
    String KEY_STORE = "./resources/security/test_sys_1.jks";
    String KEY_STORE_PASS ="connectathon";
    // this one supplied in the project
    String TRUST_STORE="./resources/security/2011_CA_Cert.jks";
    String TRUST_STORE_PASS="connectathon";
}
