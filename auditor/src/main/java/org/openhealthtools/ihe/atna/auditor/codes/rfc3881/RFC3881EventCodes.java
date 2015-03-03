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
 * RFC 3881 defined codes for Event Identification blocks
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @since OHT IHE Profiles 0.4.0
 */
public interface RFC3881EventCodes
{
	/**
	 * Event Action codes defined by RFC 3881
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 * @since OHT IHE Profiles 0.4.0
	 */
	public enum RFC3881EventActionCodes
	{
		/**
		 * "C", Create
		 */
		CREATE("C"),
		/**
		 * "R", Read
		 */
		READ("R"),
		/**
		 * "U", Update
		 */
		UPDATE("U"),
		/**
		 * "D", Delete
		 */
		DELETE("D"),
		/**
		 * "E", Execute
		 */
		EXECUTE("E");
		
		private String code;
		
		private RFC3881EventActionCodes(String code)
		{
			this.code = code;
		}
		
		public String getCode()
		{
			return this.code;
		}
	}

	
	/**
	 * Event Outcome Indicator codes defined by RFC 3881
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 * @since OHT IHE Profiles 0.4.0
	 */
	public enum RFC3881EventOutcomeCodes
	{
		/**
		 * "0", Success
		 */
		SUCCESS (0),
		/**
		 * "4", Minor Failure
		 */
		MINOR_FAILURE (4),
		/**
		 * "8", Serious Failure
		 */
		SERIOUS_FAILURE (8),
		/**
		 * "12", Major Failure
		 */
		MAJOR_FAILURE (12);
		
		private Integer code;
		
		private RFC3881EventOutcomeCodes(int code)
		{
			this.code = code;
		}
		
		public Integer getCode()
		{
			return this.code;
		}
		
		public static RFC3881EventOutcomeCodes getCodeForInt(int value)
		{
			switch (value) {
				case 4: return MINOR_FAILURE;
				case 8: return SERIOUS_FAILURE;
				case 12: return MAJOR_FAILURE;
				default: return SUCCESS;
			}
		}
	}
}
