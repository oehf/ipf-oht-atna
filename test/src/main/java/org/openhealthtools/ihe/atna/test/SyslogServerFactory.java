package org.openhealthtools.ihe.atna.test;

import io.vertx.core.Verticle;
import io.vertx.ext.unit.Async;

import java.util.concurrent.CountDownLatch;

/**
 *
 */
public final class SyslogServerFactory {

    public static Verticle createUDPServer(String host, int port, Async async){
        return new UDPSyslogServer(host, port, async);
    }

    public static Verticle createTCPServer(int port, Async async){
        return new TCPSyslogServer(port, async);
    }

    public static Verticle createTCPServerOneWayTLS(int port, String keystorePath, String keystorePassword, Async async){
        return new TCPSyslogServer(port, "NONE", null, null, keystorePath, keystorePassword, async);
    }

    public static Verticle createTCPServerTwoWayTLS(int port,
                                                    String truststorePath, String truststorePassword,
                                                    String keystorePath, String keystorePassword, Async async){
        return new TCPSyslogServer(port, "REQUIRED", truststorePath, truststorePassword,
                                   keystorePath, keystorePassword, async);
    }

    public static void createJMSConsumer(String brokerUrl, String queueName, CountDownLatch latch, boolean daemon){
        Runnable runnable = new JmsAtnaMessageConsumer(latch, brokerUrl, queueName);
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

}
