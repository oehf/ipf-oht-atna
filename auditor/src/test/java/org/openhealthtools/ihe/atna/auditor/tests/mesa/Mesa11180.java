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
import org.openhealthtools.ihe.atna.auditor.XDSConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSSourceAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

import java.util.ArrayList;
import java.util.List;

@Ignore
public class Mesa11180 extends AuditorMesaTest {

    @Test
    public void test11181() {
        PIXSourceAuditor auditor = PIXSourceAuditor.getAuditor();
        auditor.auditActorStartEvent(RFC3881EventOutcomeCodes.SUCCESS,
                "OTHER_IBM_BRIDGE", "mdavis");
    }

    @Test
    public void test11182() {
        PIXSourceAuditor auditor = PIXSourceAuditor.getAuditor();
        auditor.auditActorStopEvent(RFC3881EventOutcomeCodes.SUCCESS,
                "OTHER_IBM_BRIDGE", "mdavis");
    }

    @Test
    public void test11190() {
        PIXSourceAuditor auditor = PIXSourceAuditor.getAuditor();
        auditor.auditNodeAuthenticationFailure(true,
                "mdavis", "OTHER_IBM_BRIDGE",
                "mesa_server", "https://hii012.almaden.ibm.com:4102",
                "Authentication Failed");
    }

    @Test
    public void test11195() {
        PIXSourceAuditor auditor = PIXSourceAuditor.getAuditor();
        auditor.auditCreatePatientRecordEvent(
                RFC3881EventOutcomeCodes.SUCCESS, "mllp://xds-ibm.lgs.com:3600",
                "IBM", "XDSab_REG_IBM",
                "IBM", "OTHER_IBM_BRIDGE",
                "37744223343", "1234^^^&1.2.3.4&ISO");
    }

    @Test
    public void test11196() {
        XDSSourceAuditor sourceAuditor = XDSSourceAuditor.getAuditor();
        sourceAuditor.auditProvideAndRegisterDocumentSetEvent(RFC3881EventOutcomeCodes.SUCCESS, "http://xds-ibm.lgs.com:9080/IBMXDSRepository/provideAndRegister", "1.2.3.4", "1234^^^&1.2.3.4&ISO", "4711");
    }

    @Test
    public void test11197() {
        XDSConsumerAuditor consumerAuditor = XDSConsumerAuditor.getAuditor();
        consumerAuditor.auditRetrieveDocumentEvent(RFC3881EventOutcomeCodes.SUCCESS, "http://xds-ibm.lgs.com:9080/IBMXDSRepository/retrieve?id=1.2.3.4", "1.2.3.4", "1234^^^&1.2.3.4&ISO", "4711");
    }

    @Test
    public void test11199() {
        XDSConsumerAuditor consumerAuditor = XDSConsumerAuditor.getAuditor();
        List<CodedValueType> purposesOfUse = new ArrayList<>();
        CodedValueType codedValue = new CodedValueType();
        codedValue.setCode("purposeOfUse");
        purposesOfUse.add(codedValue);
        consumerAuditor.auditRegistryStoredQueryEvent(RFC3881EventOutcomeCodes.SUCCESS, "http://xds-ibm.lgs.com:9080/IBMXDSRegistry/registry", "urn:uuid:1234", "<request><blah/></request>", "1.1.1.1", "1234^^^&1.2.3.4&ISO",
                "4711", purposesOfUse, null);
    }

}
