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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Audit queue that uses an injectable {@link ExecutorService} to
 * asynchonously send away ATNA audit events. When this queue is
 * {@link #shutdown() shut down}, the executor service is also, waiting for at most
 * {@link #shutdownTimeoutSeconds} until all pending events are sent.
 * <p>
 * Note that the {@link ExecutorService} must be explicitly set, otherwise the
 * implementation sends away the event synchonously
 * </p>
 *
 * @since 3.1
 */
public class AsynchronousAuditQueue extends AbstractAuditMessageQueue {

    private static final Logger LOG = LoggerFactory.getLogger(AsynchronousAuditQueue.class);

    private ExecutorService executorService;
    private int shutdownTimeoutSeconds = 30;

    public AsynchronousAuditQueue() {
        super();
    }

    public AsynchronousAuditQueue(AuditorModuleContext context) {
        super(context);
    }

    /**
     * Sets the executor service. If this is null (or not used), audit events are sent synchronously
     *
     * @param executorService executor service
     */
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Sets the timeout before the executor service closes. Defaults to 10
     *
     * @param shutdownTimeoutSeconds timeout before the executor service closes
     */
    public void setShutdownTimeoutSeconds(int shutdownTimeoutSeconds) {
        this.shutdownTimeoutSeconds = shutdownTimeoutSeconds;
    }

    @Override
    protected void doSend(AuditMessageSender sender, AuditEventMessage[] auditEventMessages, InetAddress destination, int port) throws Exception {
        doSendAuditEvent(runnable(sender, auditEventMessages, destination, port));
    }

    @Override
    protected void doSend(AuditMessageSender sender, AuditEventMessage[] auditEventMessages) throws Exception {
        doSendAuditEvent(runnable(sender, auditEventMessages, null, null));
    }

    private void doSendAuditEvent(Runnable runnable) {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.execute(runnable);
        } else {
            runnable.run();
        }
    }

    private Runnable runnable(final AuditMessageSender sender, final AuditEventMessage[] auditEventMessages, final InetAddress destination, final Integer port) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    if (destination == null || port == null)
                        sender.sendAuditEvent(auditEventMessages);
                    else
                        sender.sendAuditEvent(auditEventMessages, destination, port);
                } catch (Exception e) {
                    LOG.warn(String.format("Failed to send ATNA event to destination [%s:%d]", destination, port), e);
                }
            }
        };
    }

    @Override
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(shutdownTimeoutSeconds, TimeUnit.SECONDS)) {
                    LOG.warn("Timeout occurred when flushing ATNA events, some events might have been lost");
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                LOG.warn("Thread interrupt when flushing ATNA events, some events might have been lost", e);
            }
        }
    }

}
