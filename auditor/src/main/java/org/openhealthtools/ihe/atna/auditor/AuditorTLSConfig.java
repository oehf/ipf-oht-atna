/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openhealthtools.ihe.atna.auditor;

import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomainManager;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;

/**
 * Helper configuration bean that initializes the underlying ATNA facilities when
 * TLS is used as transport protocol.
 *
 * @since 3.2
 */
public class AuditorTLSConfig {

    public static final String DEFAULT_SECURITY_DOMAIN_NAME = "mpi-atna-tls";
    private static final Logger LOG = LoggerFactory.getLogger(AuditorTLSConfig.class);

    private String securityDomainName = DEFAULT_SECURITY_DOMAIN_NAME;
    private AuditorModuleConfig auditorModuleConfig;
    private Properties auditProperties;

    public AuditorTLSConfig(AuditorModuleConfig auditorModuleConfig) {
        this(auditorModuleConfig, System.getProperties());
    }

    public AuditorTLSConfig(AuditorModuleConfig auditorModuleConfig, Properties auditProperties) {
        super();
        this.auditorModuleConfig = auditorModuleConfig;
        this.auditProperties = auditProperties;
    }

    public void setSecurityDomainName(String securityDomainName) {
        this.securityDomainName = securityDomainName;
    }

    public void init() throws Exception {
        String transport = auditorModuleConfig.getAuditRepositoryTransport();
        if ("TLS".equals(transport)) {
            LOG.info("ATNA uses {}, setting up Security Domain", transport);
            SecurityDomain securityDomain = new SecurityDomain(securityDomainName, auditProperties);
            NodeAuthModuleContext nodeContext = NodeAuthModuleContext.getContext();
            SecurityDomainManager securityDomainManager = nodeContext.getSecurityDomainManager();
            securityDomainManager.registerSecurityDomain(securityDomain);
            URI uri = new URI("atna://"
                    + auditorModuleConfig.getAuditRepositoryHost() + ":"
                    + auditorModuleConfig.getAuditRepositoryPort());
            securityDomainManager.registerURItoSecurityDomain(uri, securityDomainName);
            LOG.info("Registered {} for domain ", uri, securityDomainName);
        } else {
            LOG.info("ATNA uses {}, no need to setup Security Domain", transport);
        }
    }
}
