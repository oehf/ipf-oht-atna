/*
 * Copyright 2017 the original author or authors.
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
package org.openhealthtools.ihe.atna.auditor.queue;

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.net.InetAddress;

/**
 * @author Dmytro Rud
 */
public class JmsAuditMessageQueue implements AuditMessageQueue {
    private static transient final Logger LOG = LoggerFactory.getLogger(JmsAuditMessageQueue.class);

    private final Connection connection;
    private final Destination destination;
    private final boolean needCloseSession;

    /**
     * @param connectionFactory JMS connection factory
     * @param destination       JMS destination of ATNA messages
     * @param needCloseSession  whether the JMS session shall be closed after each message (should be <code>false</code> for pooled sessions)
     * @param userName          user name for JMS authentication
     * @param password          user password for JMS authentication
     * @throws JMSException
     */
    public JmsAuditMessageQueue(ConnectionFactory connectionFactory, Destination destination, boolean needCloseSession, String userName, String password) throws JMSException {
        this.connection = connectionFactory.createConnection(userName, password);
        this.destination = destination;
        this.needCloseSession = needCloseSession;
    }

    /**
     * @param connectionFactory JMS connection factory
     * @param destination       JMS destination of ATNA messages
     * @param needCloseSession  whether the JMS session shall be closed after each message (should be <code>false</code> for pooled sessions)
     * @throws JMSException
     */
    public JmsAuditMessageQueue(ConnectionFactory connectionFactory, Destination destination, boolean needCloseSession) throws JMSException {
        this(connectionFactory, destination, needCloseSession, null, null);
    }

    @Override
    public void sendAuditEvent(AuditEventMessage atnaMessage) {
        Session session = null;
        try {
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            BytesMessage message = session.createBytesMessage();
            message.writeBytes(atnaMessage.getSerializedMessage(false));
            session.createProducer(destination).send(message);
        } catch (JMSException e) {
            LOG.error("Could not send ATNA message", e);
        } finally {
            if (needCloseSession && (session != null)) {
                try {
                    session.close();
                } catch (JMSException e1) {
                    LOG.error("Could not close session", e1);
                }
            }
        }
    }

    @Override
    public void sendAuditEvent(AuditEventMessage msg, InetAddress destination, int port) {
        sendAuditEvent(msg);
    }

    @Override
    public void flush() {
        // nop
    }

    @Override
    public void shutdown() {
        try {
            connection.stop();
        } catch (JMSException e) {
            LOG.error("Could not shutdown gracefully", e);
        }
    }

}
