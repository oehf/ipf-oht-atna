/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.queue;

import java.net.InetAddress;

import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of an audit queue that delivers messages to the audit
 * sender and blocks while the sender sends and receives.  This implementation
 * is fully synchronous.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class SynchronousAuditQueue implements AuditMessageQueue
{
	private static final Logger LOGGER =
		LoggerFactory.getLogger(SynchronousAuditQueue.class);
	/**
	 * The context to use in this audit queue
	 */
	private final AuditorModuleContext context;
	
	/**
	 * Create an audit queue with the global auditor module context
	 */
	public SynchronousAuditQueue()
	{
		this(AuditorModuleContext.getContext());
	}
	
	/**
	 * Create an audit queue with a given auditor module context
	 * @param contextToUse The auditor module context to use
	 */
	public SynchronousAuditQueue(AuditorModuleContext contextToUse)
	{
		this.context = contextToUse;
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue#sendAuditEvent(org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage, java.net.InetAddress, int)
	 */
	public void sendAuditEvent(AuditEventMessage msg, InetAddress destination, int port)
	{
		try {
			AuditMessageSender sender = context.getSender();
			sender.sendAuditEvent(new AuditEventMessage[] {msg}, destination, port);
		} catch (Exception e) {
			LOGGER.error("Error sending", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue#sendAuditEvent(org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage)
	 */
	public void sendAuditEvent(AuditEventMessage msg) 
	{
		try {
			AuditMessageSender sender = context.getSender();
			sender.sendAuditEvent(new AuditEventMessage[] {msg});
		} catch (Exception e) {
			LOGGER.error("Error sending", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue#flush()
	 */
	public void flush()
	{
		// do nothing, this is synchronous
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue#shutdown()
	 */
	public void shutdown() 
	{
		// do nothing, no external agent is present
	}
}
   