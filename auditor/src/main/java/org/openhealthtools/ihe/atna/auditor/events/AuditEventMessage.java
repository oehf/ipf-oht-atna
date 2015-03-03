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
package org.openhealthtools.ihe.atna.auditor.events;

import java.net.InetAddress;
import java.net.URI;
import java.util.Date;

import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditMessage;

/**
 * Interface representing an auditable event message
 * for auditing an audit record repository.  Various
 * forms of the audit message instance exist, notably for
 * events defined in DICOM Supplement 95 and IHE ATNA.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public interface AuditEventMessage 
{
	/**
	 * Gets the model instance of the RFC 3881 audit message payload
	 * @return Message payload instance
	 */
	public AuditMessage getAuditMessage();
	
	/**
	 * Gets the date and time that this message was generated
	 * 
	 * @return Message generation date and time
	 */
	public Date getDateTime();
	
	/**
	 * Get a byte-serialized representation of this messsage, 
	 * for sending over the wire.
	 * @param indent Whether to use line spacing or identation (if supported)
	 * @return The serialized byte representation of this message
	 */
	public byte[] getSerializedMessage(boolean useSpacing);
	
	/**
	 * Set the destination URI object for this audit message.  
	 * Throws an exception the provided URI is not a well-formed
	 * URI (example: syslog://example.com:512)or the hostname cannot 
	 * be resolved to an IP address.
	 * 
	 * @param uri The destination
	 * @throws Exception
	 */
	public void setDestinationUri(URI uri) throws Exception;
	
	/**
	 * Gets the destination address for this message
	 * @return The destination address
	 */
	public InetAddress getDestinationAddress();
	
	/**
	 * Sets the destination address for this message
	 * @param destination The destination address
	 */
	public void setDestinationAddress(InetAddress destination);
	
	/**
	 * Gets the destination port for this message
	 * @return The destination port
	 */
	public int getDestinationPort();
	
	/**
	 * Sets the destination port for this message
	 * @param port The destination port
	 */
	public void setDestinationPort(int port);

}


