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
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.events.ihe.QueryEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.List;

/**
 * Abstract implementation of an IHE Cross-Enterprise Document Sharing auditor.  
 * Constructs audit messages that comply with ATNA Audit requirements for 
 * various XDS and XCA actors. See actor implementations for more details.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public abstract class XDSAuditor extends IHEAuditor
{
	/**
	 * Audits a QUERY event for IHE XDS transactions, notably 
	 * XDS Registry Query and XDS Registry Stored Query transactions.
	 * 
	 * @param systemIsSource Whether the system sending the message is the source active participant
	 * @param transaction IHE Transaction sending the message
	 * @param eventOutcome The event outcome indicator
	 * @param auditSourceId The Audit Source Identification
	 * @param auditSourceEnterpriseSiteId The Audit Source Enterprise Site Identification
	 * @param sourceUserId The Source Active Participant User ID (varies by transaction)
	 * @param sourceAltUserId The Source Active Participant Alternate User ID 
	 * @param sourceUserName The Source Active Participant UserName
	 * @param sourceNetworkId The Source Active Participant Network ID
     * @param humanRequestor The Human Requestor Active Participant User ID
     * @param humanRequestorName The Human Requestor Active Participant name
	 * @param registryEndpointUri The endpoint of the registry actor in this transaction (sets destination active participant user id and network id)
	 * @param registryAltUserId The registry alternate user id (for registry actors)
	 * @param storedQueryUUID The UUID for the stored query (if transaction is Registry Stored Query)
	 * @param adhocQueryRequestPayload The payload of the adhoc query request element
	 * @param homeCommunityId The home community id of the transaction (if present)
	 * @param patientId The patient ID queried (if query pertained to a patient id)
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	protected void auditQueryEvent(
			boolean systemIsSource, // System Type
			IHETransactionEventTypeCodes transaction, RFC3881EventOutcomeCodes eventOutcome, // Event
			String auditSourceId, String auditSourceEnterpriseSiteId, // Audit Source
			String sourceUserId, String sourceAltUserId, String sourceUserName, String sourceNetworkId, // Source Participant
            String humanRequestor, // Human Participant
            String humanRequestorName, // Human Participant name
            boolean humanAfterDestination,
			String registryEndpointUri, String registryAltUserId, // Destination Participant
			String storedQueryUUID, String adhocQueryRequestPayload, String homeCommunityId,  // Payload Object Participant
			String patientId,			// Patient Object Participant
			List<CodedValueType> purposesOfUse, List<CodedValueType> userRoles)
	{
		QueryEvent queryEvent = new QueryEvent(systemIsSource, eventOutcome, transaction, purposesOfUse);
		queryEvent.setAuditSourceId(auditSourceId, auditSourceEnterpriseSiteId);
		queryEvent.addSourceActiveParticipant(sourceUserId, sourceAltUserId, sourceUserName, sourceNetworkId, true);

        if (humanAfterDestination) {
            queryEvent.addDestinationActiveParticipant(registryEndpointUri, registryAltUserId, null, EventUtils.getAddressForUrl(registryEndpointUri, false), false);
        }

        if(!EventUtils.isEmptyOrNull(humanRequestorName)) {
            queryEvent.addHumanRequestorActiveParticipant(humanRequestorName, null, humanRequestorName, userRoles);
        }

        if (! humanAfterDestination) {
            queryEvent.addDestinationActiveParticipant(registryEndpointUri, registryAltUserId, null, EventUtils.getAddressForUrl(registryEndpointUri, false), false);
        }

		if (!EventUtils.isEmptyOrNull(patientId)) {
			queryEvent.addPatientParticipantObject(patientId);
		}
		
		byte[] queryRequestPayloadBytes = null;
		if (!EventUtils.isEmptyOrNull(adhocQueryRequestPayload)) {
			queryRequestPayloadBytes = adhocQueryRequestPayload.getBytes();
		} 
		
		queryEvent.addQueryParticipantObject(storedQueryUUID, homeCommunityId, queryRequestPayloadBytes, null, transaction);
		audit(queryEvent);
	}
}
