/**
 * ****************************************************************************
 * Copyright (c) 2008,2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors:
 * IBM Corporation - initial API and implementation
 * *****************************************************************************
 */

package org.openhealthtools.ihe.atna.auditor.tests.mesa;

import org.junit.Ignore;
import org.junit.Test;
import org.openhealthtools.ihe.atna.auditor.PIXSourceAuditor;

/**
 * Implementation of IHE ITI MESA Test 11143 Part 2
 * Unsuccessful attempt to connect with an expired certificate
 *
 * Contains two major parts:
 *  - Attempts to connect to a TLS server that is using an expired certificate 
 *  - Audits a Node Authentication Failure audit event to the MESA Syslog Listener [THIS FILE]
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
@Ignore
public class Mesa11143_part2 extends AuditorMesaTest {
    @Test
    public void test11143_part2() throws Exception {
        PIXSourceAuditor.getAuditor().auditNodeAuthenticationFailure(true,
                "mdavis", "OTHER_IBM_BRIDGE",
                "mesa_server", "https://YOUR_HOST_HERE:4102",
                "Node authentication failed - expired certificate");
    }
}
