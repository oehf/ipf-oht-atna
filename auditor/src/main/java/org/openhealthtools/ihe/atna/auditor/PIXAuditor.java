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
package org.openhealthtools.ihe.atna.auditor;

import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventActionCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.events.ihe.PatientRecordEvent;
import org.openhealthtools.ihe.atna.auditor.events.ihe.QueryEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.List;

/**
 * Abstract implementation of an IHE PIX/PDQ auditor.  Constructs audit messages that
 * comply with ATNA Audit requirements for PIX Manager, Patient Identity Source, 
 * PIX Query, and PDQ Consumer profiles.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public abstract class PIXAuditor extends IHEAuditor
{
	/**
	 * Audit a QUERY event for PIX and PDQ transactions.  Useful for PIX Query, PDQ Query,
	 * and PDVQ Query in the PIX Manager as well as the PIX and PDQ Consumer
	 * 
	 * @param systemIsSource Whether the system sending the message is the source active participant
	 * @param transaction IHE Transaction sending the message
	 * @param eventOutcome The event outcome indicator
	 * @param sourceFacility Source (sending/receiving) facility, from the MSH segment
	 * @param sourceApp Source (sending/receiving) application, from the MSH segment
	 * @param sourceAltUserId Source Active Participant Alternate User ID
	 * @param sourceNetworkId Source Active Participant Network ID
	 * @param destinationFacility Destination (sending/receiving) facility, from the MSH segment
	 * @param destinationApp Destination (sending/receiving) application, from the MSH segment
	 * @param destinationAltUserId Destination Active Participant Alternate User ID
	 * @param destinationNetworkId Destination Active Participant Network ID
	 * @param humanRequestor Identity of the human that initiated the transaction (if known)
	 * @param hl7MessageId The HL7 Message ID (v2 from the MSH segment field 10, v3 from message.Id)
	 * @param hl7QueryParameters The HL7 Query Parameters from the QPD segment
	 * @param patientIds List of patient IDs that were seen in this transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	protected void auditQueryEvent(boolean systemIsSource,
			IHETransactionEventTypeCodes transaction, RFC3881EventOutcomeCodes eventOutcome, 
			String sourceFacility,	String sourceApp, String sourceAltUserId, String sourceNetworkId,
			String destinationFacility,	String destinationApp, String destinationAltUserId, String destinationNetworkId,
			String humanRequestor,
			String hl7MessageId, String hl7QueryParameters, 
			String[] patientIds,
            List<CodedValueType> purposesOfUse,
		    List<CodedValueType> userRoles)
	{
		// Create query event
		QueryEvent queryEvent = new QueryEvent(systemIsSource, eventOutcome, transaction, purposesOfUse);
		queryEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		// Set the source active participant
		queryEvent.addSourceActiveParticipant(EventUtils.concatHL7FacilityApplication(sourceFacility,sourceApp), sourceAltUserId, null, sourceNetworkId, true);
		// Set the human requestor active participant
		if (!EventUtils.isEmptyOrNull(humanRequestor)) {
			queryEvent.addHumanRequestorActiveParticipant(humanRequestor, null, null, userRoles);
		}
		// Set the destination active participant
		queryEvent.addDestinationActiveParticipant(EventUtils.concatHL7FacilityApplication(destinationFacility,destinationApp), destinationAltUserId, null, destinationNetworkId, false);
		// Add a patient participant object for each patient id
		if (!EventUtils.isEmptyOrNull(patientIds)) {
			for (int i=0; i<patientIds.length; i++) {
				queryEvent.addPatientParticipantObject(patientIds[i]);
			}
		}
		
		byte[] queryParamsBytes = null, msgControlBytes = null;
		if (hl7QueryParameters != null) {
			queryParamsBytes = hl7QueryParameters.getBytes();
		}
		if (hl7MessageId != null) {
			msgControlBytes = hl7MessageId.getBytes();
		}
		
		// Add the Query participant object
		queryEvent.addQueryParticipantObject(null, null, queryParamsBytes, msgControlBytes, transaction);
	
		audit(queryEvent);
	}
	
	/**
	 * Audit a PATIENT RECORD event for Patient Identity Source transactions.  Variable on the
	 * event action (Create, Update, Delete) audit patient identity feeds (create, update,
     * merge [update/delete]) for both PIX Source and PIX Managers actors. 
     *  
     * @param systemIsSource Whether the system sending the message is the source active participant
	 * @param transaction IHE Transaction sending the message
	 * @param eventOutcome The event outcome indicator
	 * @param eventActionCode Code for the event action ("C" = create, "U" = update, "D" = delete")
	 * @param sourceFacility Source (sending/receiving) facility, from the MSH segment
	 * @param sourceApp Source (sending/receiving) application, from the MSH segment
	 * @param sourceAltUserId Source Active Participant Alternate User ID
	 * @param sourceNetworkId Source Active Participant Network ID
	 * @param destinationFacility Destination (sending/receiving) facility, from the MSH segment
	 * @param destinationApp Destination (sending/receiving) application, from the MSH segment
	 * @param destinationAltUserId Destination Active Participant Alternate User ID
	 * @param destinationNetworkId Destination Active Participant Network ID
	 * @param humanRequestor Identity of the human that initiated the transaction (if known)
	 * @param hl7MessageId The HL7 Message ID (v2 from the MSH segment field 10, v3 from message.Id)
	 * @param patientIds List of patient IDs that were seen in this transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	protected void auditPatientRecordEvent(boolean systemIsSource,
			IHETransactionEventTypeCodes transaction, RFC3881EventOutcomeCodes eventOutcome, RFC3881EventActionCodes eventActionCode,
			String sourceFacility,	String sourceApp, String sourceAltUserId, String sourceNetworkId,
			String destinationFacility,	String destinationApp, String destinationAltUserId, String destinationNetworkId,
			String humanRequestor,
			String hl7MessageId, 
			String patientIds[],
 		    List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		// Create Patient Record event
		PatientRecordEvent patientEvent = new PatientRecordEvent(systemIsSource, eventOutcome, eventActionCode, transaction, purposesOfUse);
		patientEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		// Set the source active participant
		patientEvent.addSourceActiveParticipant(EventUtils.concatHL7FacilityApplication(sourceFacility,sourceApp), sourceAltUserId, null, sourceNetworkId, true);
		// Set the human requestor active participant
		if (!EventUtils.isEmptyOrNull(humanRequestor)) {
			patientEvent.addHumanRequestorActiveParticipant(humanRequestor, null, null, userRoles);
		}
		// Set the destination active participant
		patientEvent.addDestinationActiveParticipant(EventUtils.concatHL7FacilityApplication(destinationFacility,destinationApp), destinationAltUserId, null, destinationNetworkId, false);
		// Add a patient participant object for each patient id
		if (!EventUtils.isEmptyOrNull(patientIds)) {
			for (int i=0; i<patientIds.length; i++) {
				patientEvent.addPatientParticipantObject(patientIds[i], hl7MessageId.getBytes(), transaction);
			}
		}

		audit(patientEvent);
	}
}
