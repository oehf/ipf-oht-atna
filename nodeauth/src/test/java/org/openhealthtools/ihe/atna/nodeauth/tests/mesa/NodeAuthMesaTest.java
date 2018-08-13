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

import org.junit.Before;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;

import java.io.File;
import java.util.Properties;

public abstract class NodeAuthMesaTest {
    /**
     * Node Authentication Module Context to use
     */
    protected static final NodeAuthModuleContext CONTEXT = NodeAuthModuleContext.getContext();

    /**
     * Security Domain to use
     */
    protected SecurityDomain mesaSecurityDomain;

    @Before
    public void setUp() throws Exception {

        // Set the security domain information
        Properties props = new Properties();

        File keystoreFile = new File(getClass().getResource(TestConfiguration.MESA_KEYSTORE_FILE).toURI());
        File truststoreFile = new File(getClass().getResource(TestConfiguration.MESA_TRUSTSTORE_FILE).toURI());

        props.setProperty(SecurityDomain.JAVAX_NET_SSL_KEYSTORE, keystoreFile.getAbsolutePath());
        props.setProperty(SecurityDomain.JAVAX_NET_SSL_KEYSTORE_PASSWORD, TestConfiguration.MESA_KEYSTORE_PASSWORD);
        props.setProperty(SecurityDomain.JAVAX_NET_SSL_TRUSTSTORE, truststoreFile.getAbsolutePath());
        props.setProperty(SecurityDomain.JAVAX_NET_SSL_TRUSTSTORE_PASSWORD, TestConfiguration.MESA_TRUSTSTORE_PASSWORD);
        props.setProperty(SecurityDomain.JAVAX_NET_DEBUG, "all");
        mesaSecurityDomain = new SecurityDomain("mesa", props);
    }

}
