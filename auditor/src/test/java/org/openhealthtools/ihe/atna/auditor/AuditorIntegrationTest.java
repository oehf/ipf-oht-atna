package org.openhealthtools.ihe.atna.auditor;


import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

import org.openhealthtools.ihe.atna.context.SecurityContextFactory;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleConfig;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.URI;
import java.util.Properties;

import static org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
import static org.openhealthtools.ihe.atna.auditor.tests.mesa.TestConfiguration.*;
import static org.openhealthtools.ihe.atna.nodeauth.SecurityDomain.*;
import static org.openhealthtools.ihe.atna.test.SyslogServerFactory.*;

/**
 *
 */
@RunWith(VertxUnitRunner.class)
public class AuditorIntegrationTest {

    private Logger LOG = LoggerFactory.getLogger(AuditorIntegrationTest.class);

    private Vertx vertx;
    private int port;
    private final String host = "localhost";
    private final long waitTime = 30000L;

    private Properties p;

    private AuditorModuleContext CONTEXT;
    private AuditorModuleConfig CONFIG;
    private IHEAuditor auditor;

    @Before
    public void setup(TestContext context){
        p = System.getProperties();
        port = freePort();
        CONTEXT = AuditorModuleContext.getContext();
        CONFIG = CONTEXT.getConfig();
        CONFIG.setAuditRepositoryPort(port);
        CONFIG.setAuditRepositoryHost(host);
        CONFIG.setAuditRepositoryTransport("TLS");
        auditor = PIXConsumerAuditor.getAuditor();
        auditor.setContext(CONTEXT);
        auditor.setConfig(CONFIG);
        vertx = new VertxFactoryImpl().vertx();
    }

    @After
    public void tearDown(TestContext context) {
        System.setProperties(p);
        SecurityContextFactory.cleanupSecurityContext();
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testUDP(TestContext context) throws Exception {
        CONFIG.setAuditRepositoryTransport("UDP");
        Async async = context.async(2);
        vertx.deployVerticle(createUDPServer(host, port, async));
        while (async.count() > 1){ Thread.sleep(1); }
        auditor.auditActorStartEvent(SUCCESS,
                    MESA_SYSTEM_ID, MESA_USER_IDENTITY);
        async.awaitSuccess(waitTime);
    }

    @Test
    public void testTCPNoTLS(TestContext context) throws Exception {
        Properties properties = initSecurityDomainProperties();
        initSecurityDomain(properties, false);
        Async async = context.async();
        vertx.deployVerticle(createTCPServer(port, async), context.asyncAssertSuccess());
        auditor.auditActorStartEvent(SUCCESS, MESA_SYSTEM_ID, MESA_USER_IDENTITY);
        async.awaitSuccess(waitTime);
    }

    @Test
    public void testTCPOneWayTLS(TestContext context) throws Exception {
        Properties properties = initSecurityDomainProperties();
        initSecurityDomain(properties, true);
        Async async = context.async();
        vertx.deployVerticle(createTCPServerOneWayTLS(port, properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE),
                             properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD), async),
                             context.asyncAssertSuccess());
        auditor.auditActorStartEvent(SUCCESS, MESA_SYSTEM_ID, MESA_USER_IDENTITY);
        async.awaitSuccess(waitTime);
    }

    @Test
    public void testTCPTwoWayTLS(TestContext context) throws Exception {
        Properties properties = initSecurityDomainProperties();
        initSecurityDomain(properties, true);
        Async async = context.async();
        vertx.deployVerticle(createTCPServerTwoWayTLS(port,
                properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE),
                properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD),
                properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE),
                properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD),
                async),
                context.asyncAssertSuccess());
        auditor.auditActorStartEvent(SUCCESS, MESA_SYSTEM_ID, MESA_USER_IDENTITY);
        async.awaitSuccess(waitTime);
    }

    @Test
    public void testTCPTwoWayTLSWrongClientCert(TestContext context) throws Exception {
        Properties properties = initSecurityDomainProperties();
        properties.setProperty(JAVAX_NET_SSL_KEYSTORE,
                   this.getClass().getResource("/security/wrong_client.keystore").getPath());
        initSecurityDomain(properties, true);
        CONFIG.setOption(NodeAuthModuleConfig.NODEAUTH_SOCKET_RETRIES_KEY, "0");
        CONFIG.setOption(NodeAuthModuleConfig.NODEAUTH_SOCKET_RETRY_WAIT_KEY, "0");

        Async async = context.async();
        vertx.deployVerticle(createTCPServerTwoWayTLS(port,
                properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE),
                properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD),
                properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE),
                properties.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD),
                async),
                context.asyncAssertSuccess());
        auditor.auditActorStartEvent(SUCCESS, MESA_SYSTEM_ID, MESA_USER_IDENTITY);
        try {
            async.awaitSuccess(waitTime);
            Assert.fail();
        } catch (Exception e){
            LOG.info("Exception thrown :" + e.getMessage());
        }
        if (async.isSucceeded()){ Assert.fail(); }
        async.complete();
    }

    private Properties initSecurityDomainProperties() throws Exception {
        Properties props = new Properties();
        props.put(JAVAX_NET_SSL_KEYSTORE_PASSWORD, KEY_STORE_PASS);
        props.put(JAVAX_NET_SSL_KEYSTORE, this.getClass().getResource(KEY_STORE).getPath());
        props.put(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD, TRUST_STORE_PASS);
        props.put(JAVAX_NET_SSL_TRUSTSTORE, this.getClass().getResource(TRUST_STORE).getPath());
        props.put(JDK_TLS_CLIENT_PROTOCOLS, "TLSv1.2");
        return props;
    }

    private void initSecurityDomain(Properties properties, boolean enableTls) throws Exception {
        SecurityDomain securityDomain = new SecurityDomain("test-atna-tls", properties);
        NodeAuthModuleContext nodeAuthModuleContext = NodeAuthModuleContext.getContext();
        nodeAuthModuleContext.getSecurityDomainManager().registerSecurityDomain(securityDomain);
        nodeAuthModuleContext.getSecurityDomainManager()
                .registerURItoSecurityDomain(new URI("atna://" + host + ":" + port), "test-atna-tls");

        nodeAuthModuleContext.setTLSEnabled(enableTls);
        nodeAuthModuleContext.setNonTLSConnectionsPermitted(!enableTls);
    }


    private int freePort(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);
            return serverSocket.getLocalPort();
        } catch (Exception e){
            LOG.error(e.getMessage());
            return -1;
        } finally {
            IOUtils.closeQuietly(serverSocket);
        }
    }
}
