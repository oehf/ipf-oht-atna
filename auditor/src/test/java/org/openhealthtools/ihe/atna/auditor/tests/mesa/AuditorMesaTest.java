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

import org.junit.Assert;
import org.junit.Before;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

import java.io.File;

/**
 * Common MESA Test Configuration for Secure Node / Secure Application Auditing
 *
 */
public abstract class AuditorMesaTest extends Assert {
    protected static final AuditorModuleContext CONTEXT = AuditorModuleContext.getContext();
    protected static final AuditorModuleConfig CONFIG = CONTEXT.getConfig();

    @Before
    public void setUp() throws Exception {

        File keystoreFile = new File(getClass().getResource(TestConfiguration.KEY_STORE).toURI());
        File truststoreFile = new File(getClass().getResource(TestConfiguration.TRUST_STORE).toURI());

        // Set Auditor Configuration Parameters
        // NOTE TO USERS: you should be able to do all your config edits in TestConfiguration.java
        // please let us know if you cannot pass your tests without doing that
        CONFIG.setAuditSourceId(TestConfiguration.MESA_SYSTEM_ID);
        CONFIG.setAuditEnterpriseSiteId(TestConfiguration.MESA_ENTERPRISE_SYSTEM_ID);
        CONFIG.setAuditorEnabled(true);
        CONFIG.setAuditRepositoryUri(TestConfiguration.MESA_URI);
        CONFIG.setSystemUserId(TestConfiguration.MESA_SYSTEM_ID);

        System.setProperty("javax.net.ssl.keyStore", keystoreFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.keyStorePassword", TestConfiguration.KEY_STORE_PASS);
        System.setProperty("javax.net.ssl.trustStore", truststoreFile.getCanonicalPath());
        System.setProperty("javax.net.ssl.trustStorePassword", TestConfiguration.TRUST_STORE_PASS);
        System.setProperty("javax.net.debug", "all");

        // 2011 connectathon cipher suite
        System.setProperty("https.ciphersuites", "TLS_RSA_WITH_AES_128_CBC_SHA");
    }


}
