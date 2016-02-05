/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * IBM Corporation - initial API and implementation
 * Washington University in St. Louis - refactor for RFC 5426
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.sender;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Simple MINA client implementation of RFC 5425 TLS syslog transport
 * for sending audit messages to an Audit Record Repository that implements TLS syslog.
 * Multiple messages may be sent over the same socket.
 * <p>
 * Designed to run in a standalone mode from the standard IHE Auditor
 * and is not dependent on any context or configuration.
 *
 * @author Christian Ohr
 */
public class MinaTLSSyslogSenderImpl extends NioTLSSyslogSenderImpl<IoSession> {


    private static final Logger LOG = LoggerFactory.getLogger(MinaTLSSyslogSenderImpl.class);

    public MinaTLSSyslogSenderImpl() {
        super();
    }

    @Override
    protected NioTLSSyslogSenderImpl.Destination<IoSession> makeDestination(String host, int port, boolean logging) throws Exception {
        return new MinaDestination(host, port, false);
    }


    private static final class MinaDestination implements Destination<IoSession> {

        private SocketAddress socketAddress;
        private IoConnector connector;
        private IoSession session;
        private IoSessionConfig connectorConfig;
        private ExecutorService executor;
        private long sendTimeout = 10000;

        public MinaDestination(String host, int port, boolean withLogging) throws Exception {
            socketAddress = new InetSocketAddress(host, port);
            connector = new NioSocketConnector(Runtime.getRuntime().availableProcessors() + 1);
            connector.setConnectTimeoutMillis(10000L);
            connectorConfig = connector.getSessionConfig();
            executor = new OrderedThreadPoolExecutor(5);

            connector.getFilterChain().addLast("threadPool", new ExecutorFilter(executor));
            if (withLogging) {
                connector.getFilterChain().addLast("logger", new LoggingFilter());
            }

            SslFilter filter = new SslFilter(SSLContext.getDefault(), true);
            filter.setUseClientMode(true);
            connector.getFilterChain().addFirst("sslFilter", filter);
        }

        @Override
        public void shutdown() {
            if (session.isConnected()) session.close(true);
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    int remaining = executor.shutdownNow().size();
                    LOG.warn("{} messages could not be sent due to immediate shutdown", remaining);
                }
            } catch (InterruptedException e) {
                // ok
            }
        }

        @Override
        public IoSession getSession() throws SyslogSenderException {
            if (session == null || !session.isConnected()) {
                synchronized (this) {
                    LOG.debug("Lazily open connection to address: {} using connector: {}", socketAddress, connector);
                    // connect and wait until the connection is established
                    if (connectorConfig != null) {
                        connector.getSessionConfig().setAll(connectorConfig);
                    }
                    connector.setHandler(new ResponseHandler());
                    ConnectFuture future = connector.connect(socketAddress);
                    future.awaitUninterruptibly();
                    try {
                        session = future.getSession();
                        if (session == null) {
                            throw new SyslogSenderException("Could not establish connection to " + socketAddress);
                        }
                    } catch (RuntimeException e) {
                        throw new SyslogSenderException("Could not establish connection to " + socketAddress, e);
                    }
                }
            }

            return session;
        }

        @Override
        public void write(byte[] bytes) {
            // the write operation is asynchronous. Use WriteFuture to wait until the session has been written
            try {
                WriteFuture future = getSession().write(bytes);
                LOG.trace("Waiting for write to complete for body: {} using session: {}", bytes, getSession());
                if (!future.awaitUninterruptibly(sendTimeout)) {
                    if (future.getException() != null) {
                        throw new SyslogSenderException("Could not send audit message", future.getException());
                    } else {
                        throw new SyslogSenderException("Could not send audit message");
                    }
                }
            } catch (SyslogSenderException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Handles response from session writes
         */
        private final class ResponseHandler extends IoHandlerAdapter {

            @Override
            public void messageReceived(IoSession ioSession, Object message) throws Exception {
                LOG.debug("Message received: {}", message);
            }

            @Override
            public void exceptionCaught(IoSession ioSession, Throwable cause) {
                LOG.info("Exception on receiving message from address {} using connector {} ", socketAddress, connector, cause);
                if (ioSession != null) {
                    ioSession.close(true);
                }
            }

        }

    }


}
