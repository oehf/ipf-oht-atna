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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;

/**
 * Simple Netty client implementation of RFC 5425 TLS syslog transport
 * for sending audit messages to an Audit Record Repository that implements TLS syslog.
 * Multiple messages may be sent over the same socket.
 * <p>
 * Designed to run in a standalone mode from the standard IHE Auditor
 * and is not dependent on any context or configuration.
 *
 * @author Christian Ohr
 */
public class NettyTLSSyslogSenderImpl extends NioTLSSyslogSenderImpl<Channel> {


    private static final Logger LOG = LoggerFactory.getLogger(NettyTLSSyslogSenderImpl.class);

    public NettyTLSSyslogSenderImpl() {
        super();
    }


    @Override
    protected NioTLSSyslogSenderImpl.Destination<Channel> makeDestination(String host, int port, boolean logging) throws Exception {
        return new NettyDestination(host, port, false);
    }

    private static final class NettyDestination implements Destination<Channel> {
        private long sendTimeout = 10000;
        private Bootstrap bootstrap;
        private EventLoopGroup workerGroup;
        private Channel channel;

        public NettyDestination(String host, int port, final boolean withLogging) throws Exception {

            workerGroup = new NioEventLoopGroup(5);

            // Configure the client.
            bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    if (withLogging) channel.pipeline().addLast(new LoggingHandler(getClass()));
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            super.channelRead(ctx, msg);
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            LOG.info("Exception on receiving message for context {}", ctx , cause);
                            if (ctx != null) {
                                ctx.close();
                            }
                        }
                    });
                    final SslHandler sslHandler = new SslHandler(SSLContext.getDefault().createSSLEngine());
                    channel.pipeline().addFirst(sslHandler);
                }
            });
        }

        @Override
        public void shutdown() {
            workerGroup.shutdownGracefully();
        }

        @Override
        public Channel getSession() throws SyslogSenderException {
            if (channel == null || !channel.isActive()) {
                try {
                    ChannelFuture future = bootstrap.connect().sync();
                    channel = future.channel();
                    if (channel == null) {
                        throw new SyslogSenderException("Could not establish connection");
                    }
                } catch (InterruptedException e) {
                    throw new SyslogSenderException("Could not connect");
                }
            }
            return channel;
        }

        @Override
        public void write(byte[] bytes) {
            // The write operation is asynchronous. Use ChannelFuture to wait until the session has been written
            try {
                ChannelFuture future = getSession().write(bytes);
                LOG.trace("Waiting for write to complete for body: {} using session: {}", bytes, getSession());
                if (!future.awaitUninterruptibly(sendTimeout)) {
                    if (!future.isSuccess()) {
                        throw new SyslogSenderException("Could not send audit message");
                    }
                }
            } catch (SyslogSenderException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
