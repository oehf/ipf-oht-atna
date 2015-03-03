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
package org.openhealthtools.ihe.atna.auditor.codes.rfc3881;

/**
 * RFC 3881 defined codes for ActiveParticipant blocks
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @since OHT IHE Profiles 0.4.0
 */
public interface RFC3881ActiveParticipantCodes
{
	/**
	 * RFC 3881 ActiveParticipant Network Access Point Type Codes
	 * 
	 * @since OHT IHE Profiles 0.4.0
	 *
	 */
	public enum RFC3881NetworkAccessPointTypeCodes
	{
		/**
		 * "1", "Machine Name"
		 */
		MACHINE_NAME(1),
		/**
		 * "2", "IP Address"
		 */
		IP_ADDRESS(2),
		/**
		 * "2", "Telephone Number"
		 */
		TELEPHONE_NUMBER(3);
		
		private Short code;
		
		private RFC3881NetworkAccessPointTypeCodes(int code)
		{
			this.code = (short)code;
		}
		
		public Short getCode()
		{
			return this.code;
		}
	}
}
