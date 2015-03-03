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

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;

/**
 * Interface for audit queue implementations.  Primary purpose 
 * of an auditor queue is to determine the timing and priority in which
 * audit messages are delivered to the transport sender for sending.  
 * Different queues follow different rules and strategies for blocking, 
 * priority, timing, etc.  An audit queue must implement each method 
 * even if the method does nothing (for example, it does not make sense 
 * for a fully synchronous audit queue to take any action when "flush" 
 * is called).
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public interface AuditMessageQueue 
{
	/**
	 * Queues an audit message for sending.  The destination address and port 
	 * held in the message determines the point of delivery.
	 * 
	 * @param msg The message to send
	 */
	public void sendAuditEvent(AuditEventMessage msg);
	
	/**
	 * Queues an audit message for sending to a designated destination and port
	 * 
	 * @param msg The message to send
	 * @param destination Address of the destination for delivery
	 * @param port Port for delivery
	 */
	public void sendAuditEvent(AuditEventMessage msg, InetAddress destination, int port);
	
	/**
	 * Forces all unsent messages in the queue to be sent
	 */
	public void flush();
	
	/**
	 * Flushes the queue and shutdown any associated runtime daemons that 
	 * may be handling queue inflow/outflow
	 */
	public void shutdown();
}
