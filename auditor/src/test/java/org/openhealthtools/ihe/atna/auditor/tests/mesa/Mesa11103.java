/*******************************************************************************
 * Copyright (c) 2008,2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.tests.mesa;

import org.junit.Ignore;
import org.junit.Test;
import org.openhealthtools.ihe.atna.auditor.PIXSourceAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Implementation of Secure Node / Secure Actor MESA Test 11103
 * Audit an actor-specific audit event
 * 
 * See:
 *   http://ihewiki.wustl.edu/wiki/index.php/MESA/Secure_Application#11103:_ATNA_Audit:_Actor_Specific_Audit_Message
 */
@Ignore
public class Mesa11103 extends AuditorMesaTest 
{

	@Test
	public void test11103()
	{
		PIXSourceAuditor auditor = PIXSourceAuditor.getAuditor();
		auditor.auditCreatePatientRecordEvent(
				RFC3881EventOutcomeCodes.SUCCESS, "mllp://xds-ibm.lgs.com:3600", 
				"IBM", "XDSab_REG_IBM", 
				TestConfiguration.MESA_ENTERPRISE_SYSTEM_ID, TestConfiguration.MESA_SYSTEM_ID, 
				"37744223343", "1234^^^&1.2.3.4&ISO");
	}
}
