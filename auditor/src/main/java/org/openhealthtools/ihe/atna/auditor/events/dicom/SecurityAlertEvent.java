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

import java.util.LinkedList;
import java.util.List;

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes;
import org.openhealthtools.ihe.atna.auditor.events.GenericAuditEventMessageImpl;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * Audit Event representing a DICOM 95 Security Alert event (DCM 110113)
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class SecurityAlertEvent extends GenericAuditEventMessageImpl
{
	/**
	 * Creates a Security Alert event message for a given outcome indicator
	 * and event type (e.g. Node Authentication)
	 * 
	 * @param outcome The event outcome indicator
	 * @param eventType The type of the event
	 */
	public SecurityAlertEvent(RFC3881EventOutcomeCodes outcome, DICOMEventTypeCodes eventType)
	{
		super(  
				outcome, 
				RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE,
				new DICOMEventIdCodes.SecurityAlert(),
				new DICOMEventTypeCodes[] {eventType},
                null
		);
	}
	
	/**
	 * Add an active participant for the user reporting the security alert
	 * 
	 * @param userId The User ID of the user reporting the failure
	 */
	public void addReportingUser(String userId)
	{
		addActiveParticipant(
				userId, 
				null, 
				null, 
				true, 
				null, 
				null);
	}
	
	/**
	 * Add an active participant for any participant involved in the alert
	 * @param userId The User ID of the participant
	 */
	public void addActiveParticipant(String userId)
	{
		addActiveParticipant(
				userId, 
				null, 
				null, 
				false, 
				null, 
				null);
	}
	
	/**
	 * Adds a Participant Object representing the URI resource that was accessed and
	 * generated the Security Alert
	 * 
	 * @param failedUri The URI accessed
	 * @param failureDescription A description of why the alert was created
	 */
	public void addURIParticipantObject(String failedUri, String failureDescription)
	{
		List<TypeValuePairType> failureDescriptionValue = new LinkedList<>();
		if (!EventUtils.isEmptyOrNull(failureDescription)) {
			failureDescriptionValue.add(getTypeValuePair("Alert Description", failureDescription.getBytes()));
		}
		this.addParticipantObjectIdentification(
				new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.PatientNumber(), 
				null,
				null, 
				failureDescriptionValue,
				failedUri, 
				RFC3881ParticipantObjectTypeCodes.SYSTEM, 
				RFC3881ParticipantObjectTypeRoleCodes.MASTER_FILE, 
				null, 
				null);
	}
}
