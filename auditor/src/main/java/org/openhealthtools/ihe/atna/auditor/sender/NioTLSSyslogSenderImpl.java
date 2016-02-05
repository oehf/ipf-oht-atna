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

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public abstract class NioTLSSyslogSenderImpl<S> extends RFC5424SyslogSenderImpl implements AuditMessageSender {


    public static final int TRANSPORT_DEFAULT_PORT = 6514;

    private Map<String, Destination<S>> destinations = new ConcurrentHashMap<>();

    public NioTLSSyslogSenderImpl() {
        super();
    }

    /**
     * Send an audit message to a designated destination address and port using the
     * TLS socket specified.
     *
     * @param msg         Message to send
     * @param destination TLS socket to use
     * @throws Exception
     */
    private void send(AuditEventMessage msg, Destination<S> destination) throws Exception {
        if (EventUtils.isEmptyOrNull(msg)) {
            return;
        }
        // Serialize and format event message for syslog
        byte[] msgBytes = getTransportPayload(msg);
        if (EventUtils.isEmptyOrNull(msgBytes)) {
            return;
        }
        destination.write(msgBytes);
    }

    @Override
    public void sendAuditEvent(AuditEventMessage[] msgs) throws Exception {
        if (!EventUtils.isEmptyOrNull(msgs)) {
            for (int i = 0; i < msgs.length; i++) {
                if (!EventUtils.isEmptyOrNull(msgs[i])) {
                    send(msgs[i], getDestination(msgs[i].getDestinationAddress(), msgs[i].getDestinationPort()));
                }
            }
        }
    }

    @Override
    public void sendAuditEvent(AuditEventMessage[] msgs, InetAddress destination, int port) throws Exception {
        if (!EventUtils.isEmptyOrNull(msgs)) {
            for (int i = 0; i < msgs.length; i++) {
                send(msgs[i], getDestination(destination, port));
            }
        }
    }

    private Destination<S> getDestination(InetAddress address, int port) throws Exception {
        Destination<S> destination = destinations.get(address.getHostName() + port);
        if (destination == null) {
            synchronized (this) {
                destination = makeDestination(address.getHostName(), port, false);
                Destination<S> existing = destinations.put(address.getHostName() + port, destination);
                // shutdown replaced connection
                if (existing != null) existing.shutdown();
            }
        }
        return destination;
    }

    protected abstract Destination<S> makeDestination(String host, int port, boolean logging) throws Exception;

    protected void finalize() {
        for (Destination<S> destination : destinations.values()) {
            destination.shutdown();
        }
    }

    public interface Destination<S> {

        void write(byte[] bytes);
        void shutdown();
        S getSession() throws SyslogSenderException;

        final class SyslogSenderException extends Exception {
            public SyslogSenderException(String message) {
                super(message);
            }
            public SyslogSenderException(String message, Throwable cause) {
                super(message, cause);
            }
        }

    }

}
