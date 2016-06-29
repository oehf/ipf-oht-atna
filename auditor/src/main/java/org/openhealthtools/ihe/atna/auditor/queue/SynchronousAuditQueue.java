/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.queue;

import java.net.InetAddress;

import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;

/**
 * Implementation of an audit queue that delivers messages to the audit
 * sender and blocks while the sender sends and receives.  This implementation
 * is fully synchronous.
 *
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class SynchronousAuditQueue extends AbstractAuditMessageQueue {

    public SynchronousAuditQueue() {
        super();
    }

    public SynchronousAuditQueue(AuditorModuleContext context) {
        super(context);
    }

    @Override
    protected void doSend(AuditMessageSender sender, AuditEventMessage[] auditEventMessages, InetAddress destination, int port) throws Exception {
        sender.sendAuditEvent(auditEventMessages, destination, port);
    }

    @Override
    protected void doSend(AuditMessageSender sender, AuditEventMessage... auditEventMessages) throws Exception {
        sender.sendAuditEvent(auditEventMessages);
    }

}
   