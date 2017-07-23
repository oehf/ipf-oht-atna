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
package org.openhealthtools.ihe.atna.auditor.events.dicom;

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMActiveParticipantRoleIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.events.GenericAuditEventMessageImpl;

import java.util.Collections;


/**
 * Audit Event representing a DICOM 95 Application Activity event (DCM 110100)
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class ApplicationActivityEvent extends GenericAuditEventMessageImpl
{
	/**
	 * Creates an application activity event for a given outcome and 
	 * DICOM Event Type (e.g. Application Start or Application Stop)
	 * @param outcome Event outcome indicator
	 * @param type The DICOM 95 Event Type
	 */
	public ApplicationActivityEvent(RFC3881EventOutcomeCodes outcome, DICOMEventTypeCodes type)
	{
		super(	outcome, 
				RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE,
				new DICOMEventIdCodes.ApplicationActivity(),
				new DICOMEventTypeCodes[] {type},
                null
		);
	}
	
	/**
	 * Add an Application Active Participant to this message
	 * 
	 * @param userId The Active Participant's User ID
	 * @param altUserId The Active Participant's Alternate UserID
	 * @param userName The Active Participant's UserName
	 * @param networkId The Active Participant's Network Access Point ID
	 */
	public void addApplicationParticipant(String userId, String altUserId, String userName, String networkId)
	{
		addActiveParticipant(
				userId, 
				altUserId, 
				userName, 
				false,
				Collections.singletonList(new DICOMActiveParticipantRoleIdCodes.Application()),
				networkId);
	}
	
	/**
	 * Add an Application Starter Active Participant to this message
	 * 
	 * @param userId The Active Participant's User ID
	 * @param altUserId The Active Participant's Alternate UserID
	 * @param userName The Active Participant's UserName
	 * @param networkId The Active Participant's Network Access Point ID
	 */
	public void addApplicationStarterParticipant(String userId, String altUserId, String userName, String networkId)
	{
		addActiveParticipant(
				userId, 
				altUserId, 
				userName, 
				true,
				Collections.singletonList(new DICOMActiveParticipantRoleIdCodes.ApplicationLauncher()),
				networkId);
	}	

	/**
	 * Audit Event representing a DICOM 95 Application Activity event (DCM 110100)
	 * with event type of "Application Start" (DCM 110120)
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 */
	public static class ApplicationStartEvent extends ApplicationActivityEvent
	{
		/**
		 * @param outcome Event outcome indicator
		 */
		public ApplicationStartEvent(RFC3881EventOutcomeCodes outcome)
		{
			super(outcome,new DICOMEventTypeCodes.ApplicationStart());
		}
	}
	
	/**
	 * Audit Event representing a DICOM 95 Application Activity event (DCM 110100)
	 * with event type of "Application Start" (DCM 110121)
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 */
	public static class ApplicationStopEvent extends ApplicationActivityEvent
	{
		/**
		 * @param outcome Event outcome indicator
		 */
		public ApplicationStopEvent(RFC3881EventOutcomeCodes outcome)
		{
			super(outcome,new DICOMEventTypeCodes.ApplicationStop());
		}
	}

	
}
