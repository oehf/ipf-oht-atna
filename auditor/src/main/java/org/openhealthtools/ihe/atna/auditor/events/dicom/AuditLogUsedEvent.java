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

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes;
import org.openhealthtools.ihe.atna.auditor.events.GenericAuditEventMessageImpl;

/**
 * Audit Event representing a DICOM 95 Audit Log Used event (DCM 110101)
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class AuditLogUsedEvent extends GenericAuditEventMessageImpl
{
	/**
	 * Creates an audit log used event
	 * 
	 * @param outcome The event outcome indicator
	 */
	public AuditLogUsedEvent(RFC3881EventOutcomeCodes outcome)
	{
		super(
				outcome, 
				RFC3881EventCodes.RFC3881EventActionCodes.READ,
				new DICOMEventIdCodes.AuditLogUsed(),
				null, null
		);
	}
	
	/**
	 * Adds the Active Participant of the User or System that accessed the log
	 * 
	 * @param userId The Active Participant's User ID
	 * @param altUserId The Active Participant's Alternate UserID
	 * @param userName The Active Participant's UserName
	 * @param networkId The Active Participant's Network Access Point ID
	 */
	public void addAccessingParticipant(String userId, String altUserId, String userName, String networkId)
	{
		addActiveParticipant(
				userId, 
				altUserId, 
				userName, 
				true, 
				null, 
				networkId);
	}
	
	/**
	 * Adds the Participant Object block representing the audit log accessed
	 * 
	 * @param auditLogUri The URI of the audit log that was accessed
	 */
	public void addAuditLogIdentity(String auditLogUri)
	{
		addParticipantObjectIdentification(
				new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.URI(), 
				"Security Audit Log",
				null, 
				null, 
				auditLogUri, 
				RFC3881ParticipantObjectTypeCodes.SYSTEM, 
				RFC3881ParticipantObjectTypeRoleCodes.SECURITY_RESOURCE, 
				null, 
				null);
	}	
}
