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
 * Audit Event Type Code from DICOM PS 3-2011
 *  
 * @author Glenn Deen  <a href="mailto:glenn@almaden.ibm.com">glenn@almaden.ibm.com</a>
 *
 * @since Eclipse OHF IHE 0.1.0
 */
public abstract class DICOMEventTypeCodes extends CodedValueType
{

	protected DICOMEventTypeCodes(String value, String meaning)
	{
		setCodeSystemName("DCM");
		setCode(value);
		setOriginalText(meaning);
	}


	public static class ApplicationStart extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110120","Application Start"
		 */
		public ApplicationStart()
		{
			super("110120","Application Start");
		}
	}

	public static class ApplicationStop extends DICOMEventTypeCodes 
	{
		/**
		 * "DCM","110121","Application Stop"
		 */
		public ApplicationStop()
		{
			super("110121","Application Stop");
		}
	}

	public static class Login extends DICOMEventTypeCodes 
	{
		/**
		 * "DCM","110122","Login"
		 */
		public Login()
		{
			super("110122","Login");
		}
	}

	public static class Logout extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110123", "Logout"
		 */
		public Logout()
		{
			super("110123", "Logout");
		}
	}

	public static class Attach extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110124", "Attach"
		 */
		public Attach()
		{
			super("110124", "Attach");
		}
	}

	public static class Detach extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110125", "Detach"
		 */
		public Detach()
		{
			super("110125", "Detach");
		}
	}

	public static class NodeAuthentication extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110126", "Node Authentication"
		 */
		public NodeAuthentication()
		{
			super("110126", "Node Authentication");
		}
	}

	public static class EmergencyOverrideStarted extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110127", "Emergency Override Started"
		 */
		public EmergencyOverrideStarted()
		{
			super("110127", "Emergency Override Started");
		}
	}

	public static class NetworkConfiguration extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110128", "Network Configuration"
		 */
		public NetworkConfiguration()
		{
			super("110128", "Network Configuration");
		}
	}

	public static class SecurityConfiguration extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110129", "Security Configuration"
		 */
		public SecurityConfiguration()
		{
			super("110129", "Security Configuration");
		}
	}

	public static class HardwareConfiguration extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110130", "Hardware Configuration"
		 */
		public HardwareConfiguration()
		{
			super("110130", "Hardware Configuration");
		}
	}

	public static class SoftwareConfiguration extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110131", "Software Configuration"
		 */
		public SoftwareConfiguration()
		{
			super("110131", "Software Configuration");
		}
	}

	public static class UseOfRestrictedFunction extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110132", "Use of Restricted Function"
		 */
		public UseOfRestrictedFunction()
		{
			super("110132", "Use of Restricted Function");
		}
	}

	public static class AuditRecordingStopped extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110133", "Audit Recording Stopped"
		 */
		public AuditRecordingStopped()
		{
			super("110133", "Audit Recording Stopped");
		}
	}

	public static class AuditRecordingStarted extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110134", "Audit Recording Started"
		 */
		public AuditRecordingStarted()
		{
			super("110134", "Audit Recording Started");
		}
	}

	public static class ObjectSecurityAttributesChanged extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110135", "Object Security Attributes Changed"
		 */
		public ObjectSecurityAttributesChanged()
		{
			super("110135", "Object Security Attributes Changed");
		}
	}

	public static class SecurityRolesChanged extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110136", "Security Roles Changed"
		 */
		public SecurityRolesChanged()
		{
			super("110136", "Security Roles Changed");
		}
	}

	public static class UserSecurityAttributesChanged extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110137", "User Security Attributes Changed"
		 */
		public UserSecurityAttributesChanged()
		{
			super("110137", "User Security Attributes Changed");
		}
	}

	public static class EmergencyOverrideStopped extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110138", "Emergency Override Stopped"
		 */
		public EmergencyOverrideStopped()
		{
			super("110138", "Emergency Override Stopped");
		}
	}

	public static class RemoteServiceOperationStarted extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110139", "Remote Service Operation Started"
		 */
		public RemoteServiceOperationStarted()
		{
			super("110139", "Remote Service Operation Started");
		}
	}

	public static class RemoteServiceOperationStopped extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110140", "Remote Service Operation Stopped"
		 */
		public RemoteServiceOperationStopped()
		{
			super("110140", "Remote Service Operation Stopped");
		}
	}

	public static class LocalServiceOperationStarted extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110141", "Local Service Operation Started"
		 */
		public LocalServiceOperationStarted()
		{
			super("110141", "Local Service Operation Started");
		}
	}

	public static class LocalServiceOperationStopped extends DICOMEventTypeCodes
	{
		/**
		 * "DCM","110142", "Local Service Operation Stopped"
		 */
		public LocalServiceOperationStopped()
		{
			super("110142", "Local Service Operation Stopped");
		}
	}

}
