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
	
    String KEY_STORE = "/security/client.keystore";
    String KEY_STORE_PASS ="initinit";

    String TRUST_STORE="/security/server.keystore";
    String TRUST_STORE_PASS="initinit";
}
