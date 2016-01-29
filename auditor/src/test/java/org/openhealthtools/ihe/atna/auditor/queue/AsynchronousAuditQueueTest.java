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

import org.junit.Test;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

/**
 *
 */
public class AsynchronousAuditQueueTest {

    @Test
    public void sendMessageWithoutExecutor() throws Exception {
        final AuditMessageSender messageSender = mock(AuditMessageSender.class);
        final AuditorModuleContext context = mock(AuditorModuleContext.class);
        final AsynchronousAuditQueue queue = new AsynchronousAuditQueue(context);

        final AuditEventMessage message = someAuditEventMessage();
        when(context.getSender()).thenReturn(messageSender);

        queue.sendAuditEvent(message);

        verify(messageSender).sendAuditEvent(new AuditEventMessage[] { message });
        verifyNoMoreInteractions(messageSender);
    }

    @Test
    public void sendMessageWithExecutor() throws Exception {
        final AuditMessageSender messageSender = mock(AuditMessageSender.class);
        final AuditorModuleContext context = mock(AuditorModuleContext.class);
        final AsynchronousAuditQueue queue = new AsynchronousAuditQueue(context);
        try {
            queue.setExecutorService(Executors.newSingleThreadExecutor());

            final AuditEventMessage message = someAuditEventMessage();
            when(context.getSender()).thenReturn(messageSender);

            queue.sendAuditEvent(message);

            Thread.sleep(500);
            verify(messageSender).sendAuditEvent(new AuditEventMessage[]{message});
            verifyNoMoreInteractions(messageSender);
        } finally {
            queue.shutdown();
        }
    }

    @Test
    public void sendMessageWithoutExecutorToDestination() throws Exception {
        final AuditMessageSender messageSender = mock(AuditMessageSender.class);
        final AuditorModuleContext context = mock(AuditorModuleContext.class);
        final AsynchronousAuditQueue queue = new AsynchronousAuditQueue(context);

        final AuditEventMessage message = someAuditEventMessage();
        final InetAddress destination = someInetAddress();
        final int port = 1234;
        when(context.getSender()).thenReturn(messageSender);

        queue.sendAuditEvent(message, destination, port);


        verify(messageSender).sendAuditEvent(new AuditEventMessage[] { message }, destination, port);
        verifyNoMoreInteractions(messageSender);
    }

    private AuditEventMessage someAuditEventMessage() {
        return mock(AuditEventMessage.class);
    }

    private InetAddress someInetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


}
