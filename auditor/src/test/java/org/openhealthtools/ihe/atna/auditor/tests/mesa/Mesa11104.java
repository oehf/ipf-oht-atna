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
 * Implementation of Secure Node / Secure Application MESA Test 11104
 * Audit a User Authentication Event
 * 
 * See:
 *  http://ihewiki.wustl.edu/wiki/index.php/MESA/Secure_Application#11104:_ATNA_Audit:_User_Authentication
 *
 */
@Ignore
public class Mesa11104 extends AuditorMesaTest 
{

	@Test
	public void test11104()
	{
		PIXSourceAuditor auditor = PIXSourceAuditor.getAuditor();
		auditor.auditUserAuthenticationLoginEvent(RFC3881EventOutcomeCodes.SUCCESS, true, "KERBEROS_SERVER", "192.168.1.10", "192.168.1.101");
	}
}
