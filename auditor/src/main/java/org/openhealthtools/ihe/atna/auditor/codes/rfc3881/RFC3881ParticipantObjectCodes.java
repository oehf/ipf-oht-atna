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

import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

/**
 * RFC 3881 defined codes for Participant Object blocks
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @since OHT IHE Profiles 0.4.0
 */
public abstract class RFC3881ParticipantObjectCodes 
{
	/**
	 * Participant Object Type codes defined by RFC 3881
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 * @since OHT IHE Profiles 0.4.0
	 */		
	public enum RFC3881ParticipantObjectTypeCodes 
	{
		/**
		 * 1, Person
		 */
		PERSON(1),
		/**
		 * 2, System
		 */
		SYSTEM(2),
		/**
		 * 3, Organization
		 */
		ORGANIZATION(3),
		/**
		 * 4, Other
		 */
		OTHER(4);
		
		private Short code;
		
		private RFC3881ParticipantObjectTypeCodes(int code)
		{
			this.code = (short)code;
		}
		
		public Short getCode()
		{
			return this.code;
		}
	}
	
	
	/**
	 * Participant Object Type Role codes defined by RFC 3881
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 * @since OHT IHE Profiles 0.4.0
	 */	
	public enum RFC3881ParticipantObjectTypeRoleCodes
	{
		/**
		 * 1, PATIENT
		 */
		PATIENT(1),
		/**
		 * 2, LOCATION
		 */
		LOCATION(2),
		/**
		 * 3, REPORT
		 */
		REPORT(3),
		/**
		 * 4, RESOURCE
		 */
		RESOURCE(4),
		/**
		 * 5, MASTER FILE
		 */
		MASTER_FILE(5),
		/**
		 * 6, USER
		 */
		USER(6),
		/**
		 * 7, LIST
		 */
		LIST(7),
		/**
		 * 8, DOCTOR
		 */
		DOCTOR(8),
		/**
		 * 9, SUBSCRIBER
		 */
		SUBSCRIBER(9),
		/**
		 * 10, GUARANTOR
		 */
		GUARANTOR(10),
		/**
		 * 11, SECURITY USER ENTITY
		 */
		SECURITY_USER_ENTITY(11),
		/**
		 * 12, SECURITY USER GROUP
		 */
		SECURITY_USER_GROUP(12),
		/**
		 * 13, SECURITY RESOURCE
		 */
		SECURITY_RESOURCE(13),
		/**
		 * 14, SECURITY GRANULARITY DEFINITION
		 */
		SECURITY_GRANULARITY_DEFINITION(14),
		/**
		 * 15, PROVIDER
		 */
		PROVIDER(15),
		/**
		 * 16, DATA DESTINATION
		 */
		DATA_DESTINATION(16),
		/**
		 * 17, DATA REPOSITORY
		 */
		DATA_REPOSITORY(17),
		/**
		 * 18, SCHEDULE
		 */
		SCHEDULE(18),
		/**
		 * 19, CUSTOMER
		 */
		CUSTOMER(19),
		/**
		 * 20, JOB
		 */
		JOB(20),
		/**
		 * 21, JOB STREAM
		 */
		JOB_STREAM(21),
		/**
		 * 22, TABLE
		 */
		TABLE(22),
		/**
		 * 23, ROUTING CRITERIA
		 */
		ROUTING_CRITERIA(23),
		/**
		 * 24, QUERY
		 */
		QUERY(24);
		
		private Short code;
		
		private RFC3881ParticipantObjectTypeRoleCodes(int code)
		{
			this.code = (short)code;
		}
		
		public Short getCode()
		{
			return this.code;
		}
	}
	
	/**
	 * Participant Object Data Life Cycle codes defined by RFC 3881
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 * @since OHT IHE Profiles 0.4.0
	 */
	public enum RFC3881ParticipantObjectDataLifeCycleCodes
	{
		/**
		 * 1, ORIGINATION
		 */
		ORIGINATION(1),
		/**
		 * 2, IMPORT
		 */
		IMPORT(2),
		/**
		 * 3, AMENDMENT
		 */
		AMENDMENT(3),
		/**
		 * 4, VERIFICATION
		 */
		VERIFICATION(4),
		/**
		 * 5, TRANSLATION
		 */
		TRANSLATION(5),
		/**
		 * 6, ACCESS
		 */
		ACCESS(6),
		/**
		 * 7, DEIDENTIFICATION
		 */
		DEIDENTIFICATION(7),
		/**
		 * 8, AGGREGATION
		 */
		AGGREGATION(8),
		/**
		 * 9, REPORT
		 */
		REPORT(9),
		/**
		 * 10, EXPORT
		 */
		EXPORT(10),
		/**
		 * 11, DISCLOSURE
		 */
		DISCLOSURE(11),
		/**
		 * 12, RECEIPT OF DISCLOSURE
		 */
		RECEIPT_OF_DISCLOSURE(12),
		/**
		 * 13, ARCHIVING
		 */
		ARCHIVING(13),
		/**
		 * 14, LOGICAL DELETION
		 */
		LOGICAL_DELETION(14),
		/**
		 * 15, PERMANENT ERASURE
		 */
		PERMANENT_ERASURE(15);
		
		private Short code;
		
		private RFC3881ParticipantObjectDataLifeCycleCodes(int code)
		{
			this.code = (short)code;
		}
		
		public Short getCode()
		{
			return this.code;
		}
	}
	
	/**
	 * Participant Object ID Type codes defined by RFC 3881
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 * @since OHT IHE Profiles 0.4.0
	 */
	public static class RFC3881ParticipantObjectIDTypeCodes extends CodedValueType
	{
		protected RFC3881ParticipantObjectIDTypeCodes(String value, String originalText)
		{
			setCodeSystemName("RFC-3881");
			setOriginalText(originalText);
			setCode(value);
		}
		
		/**
		 * "1", "Medical Record Number"
		 */
		public static class MedicalRecordNumber extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "1", "Medical Record Number"
			 */
			public MedicalRecordNumber()
			{
				super("1", "Medical Record Number");
			}
		}
		
		/**
		 * "2", "Patient Number"
		 */		
		public static class PatientNumber extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "2", "Patient Number"
			 */
			public PatientNumber()
			{
				super("2", "Patient Number");
			}
		}
		
		/**
		 * "3", "Encounter Number"
		 */		
		public static class EncounterNumber extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "3", "Encounter Number"
			 */
			public EncounterNumber()
			{
				super("3", "Encounter Number");
			}
		}
		
		/**
		 * "4", "Enrollee Number"
		 */
		public static class EnrolleeNumber extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "4", "Enrollee Number"
			 */
			public EnrolleeNumber()
			{
				super("4", "Enrollee Number");
			}
		}
		
		/**
		 * "5", "Social Security Number"
		 */		
		public static class SocialSecurityNumber extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "5", "Social Security Number"
			 */
			public SocialSecurityNumber()
			{
				super("5", "Social Security Number");
			}
		}
		
		/**
		 * "6", "Account Number"
		 */		
		public static class AccountNumber extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "6", "Account Number"
			 */
			public AccountNumber()
			{
				super("6", "Account Number");
			}
		}
		
		/**
		 * "7", "Guarantor Number"
		 */		
		public static class GuarantorNumber extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "7", "Guarantor Number"
			 */
			public GuarantorNumber()
			{
				super("7", "Guarantor Number");
			}
		}
		
		/**
		 * "8", "Report Name"
		 */
		public static class ReportName extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "8", "Report Name"
			 */
			public ReportName()
			{
				super("8", "Report Name");
			}
		}
		
		/**
		 * "9", "Report Number"
		 */
		public static class ReportNumber extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "9", "Report Number"
			 */
			public ReportNumber()
			{
				super("9", "Report Number");
			}
		}
		
		/**
		 * "10", "Search Criteria"
		 */
		public static class SearchCriteria extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "10", "SearchCriteria"
			 */
			public SearchCriteria()
			{
				super("10", "Search Criteria");
			}
		}
		
		/**
		 * "11", "User Identifier"
		 */
		public static class UserIdentifier extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "11", "User Identifier"
			 */
			public UserIdentifier()
			{
				super("11", "User Identifier");
			}
		}
		
		/**
		 * "12", "URI"
		 */
		public static class URI extends RFC3881ParticipantObjectIDTypeCodes
		{
			/**
			 * "12", "URI"
			 */
			public URI()
			{
				super("12", "URI");
			}
		}		

	}
}
