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
import org.openhealthtools.ihe.atna.auditor.PIXConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * Implementation of IHE ITI MESA Test 11101
 * Audit Actor Start Event using BSD Syslog
 *
 * See:  
 *   http://ihewiki.wustl.edu/wiki/index.php/MESA/Secure_Application#11101:_ATNA_Audit:_Actor_Start_BSD
 *
 */
@Ignore
public class Mesa11101 extends AuditorMesaTest {

    @Test
    public void test11101() {
        PIXConsumerAuditor auditor = PIXConsumerAuditor.getAuditor();
        auditor.auditActorStartEvent(RFC3881EventOutcomeCodes.SUCCESS,
                TestConfiguration.MESA_SYSTEM_ID, TestConfiguration.MESA_USER_IDENTITY);
    }
}
