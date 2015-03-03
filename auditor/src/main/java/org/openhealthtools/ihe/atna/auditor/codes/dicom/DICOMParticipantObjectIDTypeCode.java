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
 * Audit Participant Object ID Type Code from DICOM PS 3-2011
 *  
 * @author Glenn Deen  <a href="mailto:glenn@almaden.ibm.com">glenn@almaden.ibm.com</a>
 *
 * @since Eclipse OHF IHE 0.1.0
 */
public abstract class DICOMParticipantObjectIDTypeCode extends CodedValueType
{
	protected DICOMParticipantObjectIDTypeCode(String value, String meaning)
	{
		setCodeSystemName("DCM");
		setCode(value);
		setOriginalText(meaning);
	}
	/**
	 *  "DCM","110180", "Study Instance UID"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  StudyInstanceUID extends DICOMParticipantObjectIDTypeCode
	{
		/**
		 * "DCM","110180", "Study Instance UID"
		 */
		public StudyInstanceUID()
		{
			super("110180", "Study Instance UID");
		}
	}
	/**
	 * "DCM","110181", "SOP Class UID"
	 *  
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  SOPClassUID extends DICOMParticipantObjectIDTypeCode
	{
		/**
		 * "DCM","110181", "SOP Class UID"
		 */
		public SOPClassUID()
		{
			super("110181", "SOP Class UID");
		}
	}
	/**
	 * "DCM","110182", "Node ID"
	 *
	 * @since Eclipse OHF IHE 0.1.0
	 */
	public static class  NodeID extends DICOMParticipantObjectIDTypeCode
	{
		/**
		 * "DCM","110182", "Node ID"
		 */
		public NodeID()
		{
			super("110182", "Node ID");
		}
	}
}
