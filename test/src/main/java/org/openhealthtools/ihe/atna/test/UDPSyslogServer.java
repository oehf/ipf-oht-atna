package org.openhealthtools.ihe.atna.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResultHandler;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.ext.unit.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class UDPSyslogServer extends AbstractVerticle {

    private final int udpPort;

    private final String host;

    private final Async async;

    private final DatagramSocketOptions dsOptions = new DatagramSocketOptions()
            .setIpV6(false)
            .setReuseAddress(true);

    private static final Logger log = LoggerFactory.getLogger(UDPSyslogServer.class);

    public UDPSyslogServer(String host, int udpPort, Async async) {
        this.udpPort = udpPort;
        this.host = host;
        this.async = async;
    }

    @Override
    public void start() {
        final DatagramSocket socket = vertx.createDatagramSocket(dsOptions);
        socket.listen(udpPort, host, (AsyncResultHandler<DatagramSocket>) datagramSocketAsyncResult -> {
            if (datagramSocketAsyncResult.succeeded()){
                log.info("Listening on UDP port " + udpPort);
                async.countDown();
                socket.handler(packet -> {
                    String decoded = packet.data().getString(0, packet.data().length());
                    log.debug("=============== Received content on UDP " + udpPort +
                             " ================= \n" + decoded);
                    async.countDown();
                });
            } else {
                log.warn("Listen failed on port " + udpPort, datagramSocketAsyncResult.cause());
            }
        });
    }
}