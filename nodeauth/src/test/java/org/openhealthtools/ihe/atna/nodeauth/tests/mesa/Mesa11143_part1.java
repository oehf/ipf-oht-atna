/**
 * ****************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors:
 * IBM Corporation - initial API and implementation
 * *****************************************************************************
 */

package org.openhealthtools.ihe.atna.nodeauth.tests.mesa;

import org.junit.Ignore;
import org.junit.Test;

import java.net.Socket;

/**
 * Implementation of IHE ITI MESA Test 11143
 * Unsuccessful attempt to connect with an expired certificate
 *
 * Contains two major parts:
 *  - Attempts to connect to a TLS server that is using an expired certificate [THIS FILE]
 *  - Audits a Node Authentication Failure audit event to the MESA Syslog Listener
 *
 * Steps:
 *  - Start MESA TLS and Syslog Servers
 *  - Run org.openhealthtools.ihe.atna.nodeauth.tests.mesa.Mesa11143_part1.test11143_part1() method
 *  - Run org.openhealthtools.ihe.atna.auditor.tests.mesa.Mesa11143_part2.test11143_part2() method 
 *  - Grade and Submit Result
 *
 * For details, see:
 *   http://ihewiki.wustl.edu/wiki/index.php/MESA/Secure_Application#11143:_Client_ATNA_Certificate_Exchange_with_Expired_Certificate
 *
 */
public class Mesa11143_part1 extends NodeAuthMesaTest {

    @Ignore
    public void test11143() throws Exception {
        Socket s = CONTEXT.getSocketHandler().getSocket(
                TestConfiguration.MESA_SERVER_HOST, 4102, true, mesaSecurityDomain);
        s.close();
    }
}
