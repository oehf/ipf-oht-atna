/*******************************************************************************
 * Copyright (c) 2006,2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.openhealthtools.ihe.atna.auditor.codes.dicom;

import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

/**
 * Audit Event ID codes from DICOM PS 3-2011
 * 
 * @author <a href="mailto:glenn@almaden.ibm.com">Glenn Deen</a>
 * @since OHF IHE 0.1.0
 */
public abstract class DICOMEventIdCodes extends CodedValueType
{
	
	protected DICOMEventIdCodes(String value, String meaning)
	{
		setCodeSystemName("DCM");
		setCode(value);
		setOriginalText(meaning);
	}
	
	/**
	 *  "DCM","110100","Application Activity"
	 * 
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static final class ApplicationActivity extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110100","Application Activity"
		 */
		public ApplicationActivity()
		{
			super("110100","Application Activity");
		}	
	}

	
	/**
	 * "DCM","110101","Audit Log Used"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  AuditLogUsed extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110101","Audit Log Used"
		 */
		public AuditLogUsed()
		{
			super("110101","Audit Log Used");
		}
	}
	/**
	 * "DCM","110102","Begin Transferring DICOM Instances"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  BeginTransferringDICOMInstances extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110102","Begin Transferring DICOM Instances"
		 */
		public BeginTransferringDICOMInstances()
		{
			super("110102","Begin Transferring DICOM Instances");
		}
	}
	/**
	 * "DCM","110103","DICOM Instances Accessed"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  DICOMInstancesAccessed extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110103","DICOM Instances Accessed"
		 */
		public DICOMInstancesAccessed()
		{
			super("110103","DICOM Instances Accessed");
		}
	}
	/**
	 * "DCM","110104","DICOM Instances Transferred"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  DICOMInstancesTransferred extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110104","DICOM Instances Transferred"
		 */
		public DICOMInstancesTransferred()
		{
			super("110104","DICOM Instances Transferred");
		}
	}
	/**
	 * "DCM","110105","DICOM Study Deleted"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  DICOMStudyDeleted extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110105","DICOM Study Deleted"
		 */
		public DICOMStudyDeleted()
		{
			super("110105","DICOM Study Deleted");
		}
	}
	/**
	 * "DCM","110106","Export"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  Export extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110106","Export"
		 */
		public Export()
		{
			super("110106","Export");
		}
	}
	/**
	 * "DCM","110107","Import"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  Import extends DICOMEventIdCodes
	{	
		/**
		 * "DCM","110107","Import"
		 */
		public Import()
		{ 
			super("110107","Import");
		}
	}
	
	/**
	 * "DCM","110108","Network Activity"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  NetworkEntry extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110108","Network Entry"
		 */
		public NetworkEntry()
		{
			super("110108","Network Entry");
		}
	}
	/**
	 * "DCM","110109","Order Record"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  OrderRecord extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110109","Order Record"
		 */
		public OrderRecord()
		{
			super("110109","Order Record");
		}
	}
	/**
	 * "DCM","110110","Patient Record"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  PatientRecord extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110110","Patient Record"
		 */
		public PatientRecord()
		{
			super("110110","Patient Record");
		}
	}
	/**
	 * "DCM","110111","Procedure Record"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  ProcedureRecord extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110111","Procedure Record"
		 */
		public ProcedureRecord()
		{
			super("110111","Procedure Record");
		}
	}
	/**
	 *
	 * "DCM","110112","Query"
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  Query extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110112","Query"
		 */
		public Query()
		{
			super("110112","Query");
		}
	}
	/**
	 * "DCM","110113","Security Alert"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  SecurityAlert extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110113","Security Alert"
		 */
		public SecurityAlert()
		{
			super("110113","Security Alert");
		}
	}
	/**
	 * "DCM","110114", "User Authentication"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  UserAuthentication extends DICOMEventIdCodes
	{
		/**
		 * "DCM","110114", "User Authentication"
		 */
		public UserAuthentication()
		{
			super("110114", "User Authentication");
		}
	}
	
}


