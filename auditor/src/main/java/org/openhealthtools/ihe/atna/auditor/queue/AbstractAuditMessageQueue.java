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

package org.openhealthtools.ihe.atna.auditor.queue;

import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * Abstract base class for message queues
 */
abstract class AbstractAuditMessageQueue implements AuditMessageQueue {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAuditMessageQueue.class);

    /**
     * The context to use in this audit queue
     */
    private final AuditorModuleContext context;

    /**
     * Create an audit queue with the global auditor module context
     */
    public AbstractAuditMessageQueue() {
        this(AuditorModuleContext.getContext());
    }

    public AbstractAuditMessageQueue(final AuditorModuleContext context) {
        if (context == null) throw new IllegalArgumentException("AuditorModuleContext must not be null");
        this.context = context;
    }

    @Override
    public void sendAuditEvent(AuditEventMessage msg, InetAddress destination, int port) {
        try {
            AuditMessageSender sender = context.getSender();
            doSend(context.getSender(), new AuditEventMessage[]{msg}, destination, port);
        } catch (Exception e) {
            LOG.warn("Error sending audit message", e);
        }
    }


    @Override
    public void sendAuditEvent(AuditEventMessage msg) {
        try {
            doSend(context.getSender(), new AuditEventMessage[]{msg});
        } catch (Exception e) {
            LOG.warn("Error sending audit message", e);
        }
    }

    @Override
    public void flush() {
        // do nothing
    }

    @Override
    public void shutdown() {
        // do nothing
    }

    protected abstract void doSend(AuditMessageSender sender, AuditEventMessage[] auditEventMessages, InetAddress destination, int port) throws Exception;

    protected abstract void doSend(AuditMessageSender sender, AuditEventMessage[] auditEventMessages) throws Exception;

}
