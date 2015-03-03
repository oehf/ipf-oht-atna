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
package org.openhealthtools.ihe.atna.auditor.sender;

import java.net.InetAddress;

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;

/**
 * Interface for audit message sender implementations.  Designed to
 * handle, in a transport-independent way, the sending of audit
 * message payloads to an audit record repository using the 
 * rules and policies defined by the transport-specific implementation
 * used.  The sender is generally attached to an audit queue or can be 
 * invoked directly if creation of audit messages is separate from the 
 * sending of audit messages
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public interface AuditMessageSender 
{
	/**
	 * Sends an audit message.  The destination address and port 
	 * in the message determine the point of delivery.  If a failure
	 * occurs an exception is thrown.
	 * 
	 * @param msg The message to send
	 * @throws Exception
	 */
	public void sendAuditEvent(AuditEventMessage[] msg) throws Exception;
	
	/**
	 * Sends an audit message to the specified destination address and port
	 * If a failure occurs an exception is thrown.
	 * 
	 * @param msg The message to send
	 * @param destination Destination address
	 * @param port Destination port
	 * @throws Exception
	 */
	public void sendAuditEvent(AuditEventMessage[] msg, InetAddress destination, int port) throws Exception;
}
