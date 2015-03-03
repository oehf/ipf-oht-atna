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


/**
 * Security Alert Type Code from DICOM PS 3-2011
 * 
 * @author <a href="mailto:glenn@almaden.ibm.com">Glenn Deen</a>
 * @since Eclipse OHF IHE 0.1.0
 */                                    
public abstract class DICOMSecurityAlertTypeCodes extends DICOMEventTypeCodes
{
	protected DICOMSecurityAlertTypeCodes(String value, String meaning)
	{
		super(value,meaning);
	}
	
	/**
	 * "DCM","110126", "Node Authentication"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class NodeAuthentication extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110126", "Node Authentication"
		 */
		public NodeAuthentication()
		{
			super("110126", "Node Authentication");
		}
	}
	
	/**
	 * "DCM","110127", "Emergency Override Started"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  EmergencyOverrideStarted extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110127", "Emergency Override Started"
		 */
		public EmergencyOverrideStarted()
		{
			super("110127", "Emergency Override Started");
		}
	}
	
	/**
	 * "DCM","110128", "Network Configuration"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  NetworkConfiguration extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110128", "Network Configuration"
		 */
		public NetworkConfiguration()
		{
			super("110128", "Network Configuration");
		}
	}
	/**
	 * "DCM","110129", "Security Configuration"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  SecurityConfiguration extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110129", "Security Configuration"
		 */
		public SecurityConfiguration()
		{
			super("110129", "Security Configuration");
		}
	}
	/**
	 * "DCM","110130", "Hardware Configuration"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  HardwareConfiguration extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110130", "Hardware Configuration"
		 */
		public HardwareConfiguration()
		{
			super("110130", "Hardware Configuration");
		}
	}
	/**
	 * "DCM","110131", "Software Configuration"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  SoftwareConfiguration extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110131", "Software Configuration"
		 */
		public SoftwareConfiguration()
		{
			super("110131", "Software Configuration");
		}
	}
	/**
	 * "DCM","110132", "Use of Restricted Function"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  UseOfRestrictedFunction extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110132", "Use of Restricted Function"
		 */
		public UseOfRestrictedFunction()
		{
			super("110132", "Use of Restricted Function");
		}
	}
	/**
	 * "DCM","110133", "Audit Recording Stopped"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  AuditRecordingStopped extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110133", "Audit Recording Stopped"
		 */
		public AuditRecordingStopped()
		{
			super("110133", "Audit Recording Stopped");
		}
	}
	/**
	 * "DCM","110134", "Audit Recording Started"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  AuditRecordingStarted extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110134", "Audit Recording Started"
		 */
		public AuditRecordingStarted()
		{
			super("110134", "Audit Recording Started");
		}
	}
	/**
	 * "DCM","110135", "Object Security Attributed Changed"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  ObjectSecurityAttributedChanged extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110135", "Object Security Attributed Changed"
		 */
		public ObjectSecurityAttributedChanged()
		{
			super("110135", "Object Security Attributed Changed");
		}
	}
	/**
	 * "DCM","110136", "Security Roles Changes"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  SecurityRolesChanges extends DICOMSecurityAlertTypeCodes
	{
		/**
		 *  "DCM","110136", "Security Roles Changes"
		 */
		public SecurityRolesChanges()
		{
			super("110136", "Security Roles Changes");
		}
	}
	/**
	 * "DCM","110137", "User Security Attributes Changed"
	 * 
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  UserSecurityAttributesChanged extends DICOMSecurityAlertTypeCodes
	{
		/**
		 * "DCM","110137", "User Security Attributes Changed"
		 */
		public UserSecurityAttributesChanged()
		{
			super("110137", "User Security Attributes Changed");
		}
	}
}