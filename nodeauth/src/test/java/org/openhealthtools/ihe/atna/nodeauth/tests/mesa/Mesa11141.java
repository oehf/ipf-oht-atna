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

import java.net.Socket;

/**
 * Implementation of IHE ITI MESA Test 11141
 * Successful Secure Socket Connection
 * <p/>
 * Steps:
 * - Start MESA TLS Server
 * - Run test11141() method
 * - Submit Result
 * <p/>
 * For details, see:
 * http://ihewiki.wustl.edu/wiki/index.php/MESA/Secure_Application#11141:_Client_ATNA_Certificate_Exchange_with_Valid_Certificate
 */
public class Mesa11141 extends NodeAuthMesaTest {

    @Ignore
    public void test11141() throws Exception {
        Socket s = CONTEXT.getSocketHandler().getSocket(
                TestConfiguration.MESA_SERVER_HOST, 4100, true, mesaSecurityDomain);
        s.close();
    }

}
