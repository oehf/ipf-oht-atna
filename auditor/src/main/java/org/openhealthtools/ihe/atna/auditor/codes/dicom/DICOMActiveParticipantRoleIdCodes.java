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
 * DICOM PS 3-2011 code definitions for Active Participant Roles
 * 
 * @author <a href="mailto:glenn@almaden.ibm.com">Glenn Deen</a>
 * @since Eclipse OHF IHE 0.1.0
 */
public abstract class DICOMActiveParticipantRoleIdCodes extends CodedValueType
{
	protected DICOMActiveParticipantRoleIdCodes(String value, String meaning)
	{
		setCodeSystemName("DCM");
		setCode(value);
		setOriginalText(meaning);
	}
	/**
	 * "DCM","110150", "Application"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  Application extends DICOMActiveParticipantRoleIdCodes
	{
		/**
		 * "DCM","110150", "Application"
		 */
		public Application()
		{
			super("110150", "Application");
		}
	}
	/**
	 * "DCM","110151", "Application Launcher"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  ApplicationLauncher extends DICOMActiveParticipantRoleIdCodes
	{
		/**
		 * "DCM","110151", "Application Launcher"
		 */
		public ApplicationLauncher()
		{
			super("110151", "Application Launcher");
		}
	}
	
	/**
	 * "DCM","110152", "Destination Role ID"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  Destination extends DICOMActiveParticipantRoleIdCodes
	{
		/**
		 * "DCM","110152", "Destination Role ID"
		 */
		public Destination()
		{
			super("110152", "Destination Role ID");
		}
	}
	/**
	 * "DCM","110153", "Source Role ID"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  Source extends DICOMActiveParticipantRoleIdCodes
	{
		/**
		 * "DCM","110153", "Source Role ID"
		 */
		public Source()
		{
			super("110153", "Source Role ID");
		}
	}
	/**
	 *
	 * "DCM","110154", "Destination Media"
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  DestinationMedia extends DICOMActiveParticipantRoleIdCodes
	{
		/**
		 * "DCM","110154", "Destination Media"
		 */
		public DestinationMedia()
		{
			super("110154", "Destination Media");
		}
	}
	/**
	 * "DCM","110155", "Source Media"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  SourceMedia extends DICOMActiveParticipantRoleIdCodes
	{
		/**
		 * "DCM",
		 */
		public SourceMedia()
		{
			super("110155", "Source Media");
		}
	}

}