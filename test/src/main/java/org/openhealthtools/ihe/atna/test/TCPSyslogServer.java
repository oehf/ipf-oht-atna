package org.openhealthtools.ihe.atna.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.net.*;
import io.vertx.ext.unit.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TCPSyslogServer extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(TCPSyslogServer.class);

    private final int port;

    private final NetServerOptions nsOptions;

    private final Async async;

    public TCPSyslogServer(int port, Async async){
        this.port = port;
        this.async = async;
        nsOptions = new NetServerOptions()
                .setReuseAddress(true)
                .setHost("localhost")
                .setSsl(false);
    }

    public TCPSyslogServer(int port, String clientAuth,
                           String trustStorePath, String trustStorePassword,
                           String keyStorePath, String keyStorePassword,
                           Async async){
        this.port = port;
        this.async = async;
        nsOptions = new NetServerOptions()
                .setReuseAddress(true)
                .setHost("localhost")
                .setClientAuth(ClientAuth.valueOf(clientAuth))
                .setTrustStoreOptions(trustStorePath != null? new JksOptions().
                        setPath(trustStorePath).
                        setPassword(trustStorePassword): null)
                .setKeyStoreOptions(keyStorePath != null? new JksOptions().
                        setPath(keyStorePath).
                        setPassword(keyStorePassword): null)
                .setSsl(true);
    }

    @Override
    public void start() throws Exception {
        NetServer netServer = vertx.createNetServer(nsOptions);
        netServer.connectHandler(netSocket -> netSocket.handler(buffer -> {
            log.debug("================= Received content on " + port + ":" + async.count() +
                    " =================== \n" + buffer.toString());
            async.countDown();
        })).listen(port);
    }
}
