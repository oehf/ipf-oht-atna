/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Washington University in St. Louis (Lawrence Tarbox) - refactor for RFC 5424
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.sender;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;
import org.openhealthtools.ihe.atna.auditor.utils.TimestampUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple base client implementation of RFC 5424 syslog 
 * for sending audit messages to an Audit Record Repository
 * that implements RFC 5424 syslog.  Facility formats message payloads
 * and includes syslog metadata (such as the message priority).
 * This base class does not sent the message, but merely formats the
 * common payload.  A class that implements AuditMessageSender must extend
 * this class and add the tranport mapping.
 * 
 * Designed to run in a standalone mode from the standard IHE Auditor
 * and is not dependent on any context or configuration.
 * 
 * @author <a href="mailto:tarboxl@mir.wustl.edu">Lawrence Tarbox</a>
 * Derived from code written by Matthew Davis of IBM.
 *
 */
public class RFC5424SyslogSenderImpl
{

	/**
	 * Logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RFC5424SyslogSenderImpl.class);
	
	/**
	 * Default syslog priority for this transport
	 */
	public static final int TRANSPORT_DEFAULT_PRIORITY = 85;
	
	/**
	 * Local system hostname for this sender
	 */
	private static String systemHostName;
	
	/**
	 * Default syslog APP-NAME for this transport
	 */
	public static final String TRANSPORT_DEFAULT_APP = "OHT";

	/**
	 * Default syslog PROCID for this transport
	 */
	public static String auditPROCID;

	/**
	 * Default syslog MSGID for this transport
	 */
	public static final String TRANSPORT_DEFAULT_MSGID = "IHE+RFC-3881";

	/**
	 * Default constructor
	 */
	public RFC5424SyslogSenderImpl()
	{

	}
	
	/**
	 * Serialize, format, and prepare the message payload body 
	 * for sending by this transport.  This includes adding
	 * the syslog message header, which is transport independent.
	 * 
	 * @param msg Message to prepare
	 * @return Buffer to send
	 */
	protected byte[] getTransportPayload(AuditEventMessage msg) throws UnsupportedEncodingException
	{
		if (msg == null) {
			return null;
		}
		
		byte[] msgBytes = msg.getSerializedMessage(false);
		if (EventUtils.isEmptyOrNull(msgBytes)) {
			return null;
		}
		
		// Format message with transport-specific headers
		StringBuilder sb = new StringBuilder();
		
		// The breakdown of the ABNF definition of the message are included as comments
		//PRI
		sb.append("<");
		sb.append(TRANSPORT_DEFAULT_PRIORITY);
		sb.append(">");
		//VERSION SP ; the two together since this is a defined value for this header
		sb.append("1 ");
		//TIMESTAMP SP
		sb.append(TimestampUtils.getRFC3881Timestamp(msg.getDateTime()));
		sb.append(" ");
		//HOSTNAME SP
		sb.append(getSystemHostName());
		sb.append(" ");
		//APP-NAME SP; SHOULD identify the device or application that originated the message
		sb.append(TRANSPORT_DEFAULT_APP); 
		sb.append(" ");
		//PROCID SP; a change in this value indicates a discontinuity in the syslog stream
		//       ; often the process name or process ID associated with a syslog system is used
		sb.append(getPROCID());
		sb.append(" ");
		//MSGID SP; SHOULD identify the type of message
		sb.append(TRANSPORT_DEFAULT_MSGID); 
		//sb.append(" "); // added to next string
		//STRUCTURED-DATA
		sb.append(" - "); // this is the nil value, since IHE ATNA does not use it
		//SP MSG
		//sb.append(" "); // added to previous string

		//sb.append("<?xml version=\"1.0\" encoding=\"ASCII\"?>"); // If just ASCII alternative
		/* UTF 8 alternate from Dmytro Rud */
		// BOM for UTF-8
		//sb.append("\u00EF\u00BB\u00BF");
		sb.append("\uFEFF"); // recommended by Jeremy Huiskamp, over the wire result is EF BB EF as I think it should be
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		/* end UTF 8 alternative*/

		// remainder of XML payload
		sb.append(new String(msgBytes, "UTF-8"));

		return sb.toString().trim().getBytes("UTF-8");
	}
	
	/**
	 * Gets the hostname of the system managing this transport
	 * @return The system's hostname
	 */
	private String getSystemHostName()
	{
		if (EventUtils.isEmptyOrNull(systemHostName)) {
			try {
				systemHostName = InetAddress.getLocalHost().getHostName();
			} catch (Throwable t) {
				systemHostName = "localhost";
			}
		}
		return systemHostName;
	}

	/**
	 * Gets a string that with high probability is different each time this
	 * application is run.  Syslog receivers look for a change in this value 
	 * to detect a discontinuity in the syslog stream (e.g., a stream from a 
	 * new running instance of an application).  Most syslog implementations
	 * would use the process name or process ID associated with this running
	 * instance of a syslog system, but Java provides no access to such OS
	 * dependent identifiers.  So we will simply derive a 3 digit number from
	 * the current system time. 
	 * @return A 3 digit String representative of this process, though not unique
	 */
	private String getPROCID()
	{
		if (EventUtils.isEmptyOrNull(auditPROCID)) {
			try {
				Long tempProcID = System.currentTimeMillis() % 1000;
				auditPROCID = tempProcID.toString();
			} catch (Throwable t) {
				auditPROCID = "-";
			}
		}
		return auditPROCID;
	}
}
