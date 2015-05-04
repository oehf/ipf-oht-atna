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
 * Implementation of IHE ITI MESA Test 11142 Part 2
 * Unsuccessful attempt to connect with an unregistered certificate
 *
 * Contains two major parts:
 *  - Attempts to connect to a TLS server that does not recognize your certificate 
 *  - Audits a Node Authentication Failure audit event to the MESA Syslog Listener [THIS FILE]
 *
 * Steps:
 *  - Start MESA TLS and Syslog Servers
 *  - Run org.openhealthtools.ihe.atna.nodeauth.tests.mesa.Mesa11142_part1.test11142_part1() method
 *  - Run org.openhealthtools.ihe.atna.auditor.tests.mesa.Mesa11142_part2.test11142_part2() method 
 *  - Grade and Submit Result
 *
 * For details, see:
 *   http://ihewiki.wustl.edu/wiki/index.php/MESA/Secure_Application#11142:_Client_ATNA_Certificate_Exchange_with_Unregistered_Certificate
 *
 */
@Ignore
public class Mesa11142_part2 extends AuditorMesaTest {
    @Test
    public void test11142_part2() throws Exception {
        PIXSourceAuditor.getAuditor().auditNodeAuthenticationFailure(true,
                "mdavis", "OTHER_IBM_BRIDGE",
                "mesa_server", "https://YOUR_HOST_HERE:4101",
                "Node authentication failed - unrecognized certificate");
    }

}
