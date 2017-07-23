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
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.ExportEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.List;

/**
 * Implementation of a XDS Auditor to send audit messages for
 * transactions under the server-side actors in
 * the Cross-Community Access (XCA) profile, notably
 * the XCA Responding Gateway.
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-38 Cross Gateway Query
 *  - ITI-39 Cross Gateway Retrieve
 *  - ITI-18 Registry Stored Query (as a Document Consumer)
 *  - ITI-43 Retrieve Document Set (as a Document Consumer)
 *  
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class XCARespondingGatewayAuditor extends XDSAuditor
{
	/**
	 * Get an instance of the XCA Responding Gateway Auditor from the 
	 * global context
	 * 
	 * @return XCA Responding Gateway Auditor instance
	 */
	public static XCARespondingGatewayAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (XCARespondingGatewayAuditor)ctx.getAuditor(XCARespondingGatewayAuditor.class);
	}


	/**
	 * Audits an ITI-38 Cross Gateway Query event for XCA Responding Gateway actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param initiatingGatewayUserId The Active Participant UserID for the consumer (if using WS-Addressing)
	 * @param initiatingGatewayUserName The Active Participant UserName for the consumer (if using WS-Security / XUA)
	 * @param initiatingGatewayIpAddress The IP Address of the consumer that initiated the transaction
	 * @param respondingGatewayEndpointUri The URI of this registry's endpoint that received the transaction
	 * @param storedQueryUUID The UUID of the stored query
	 * @param adhocQueryRequestPayload The payload of the adhoc query request element
	 * @param homeCommunityId The home community id of the transaction (if present)
	 * @param patientId The patient ID queried (if query pertained to a patient id)
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditCrossGatewayQueryEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String initiatingGatewayUserId, String initiatingGatewayUserName, String initiatingGatewayIpAddress,
			String respondingGatewayEndpointUri, 
			String storedQueryUUID, String adhocQueryRequestPayload, String homeCommunityId,
			String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditQueryEvent(false, 
				new IHETransactionEventTypeCodes.CrossGatewayQuery(), eventOutcome, 
				getAuditSourceId(), getAuditEnterpriseSiteId(), 
				initiatingGatewayUserId, null, initiatingGatewayUserName, initiatingGatewayIpAddress, 
				initiatingGatewayUserName, initiatingGatewayUserName, false,
				respondingGatewayEndpointUri, getSystemAltUserId(), 
				storedQueryUUID, adhocQueryRequestPayload, homeCommunityId, 
				patientId, purposesOfUse, userRoles);
	}
	
	/**
	 * Audits an ITI-39 Cross Gateway Retrieve event for XCA Responding Gateway actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param initiatingGatewayUserId The Active Participant UserID for the document consumer (if using WS-Addressing)
	 * @param initiatingGatewayUserName The Active Participant UserName for the document consumer (if using WS-Security / XUA)
	 * @param initiatingGatewayIpAddress The IP address of the document consumer that initiated the transaction
	 * @param respondingGatewayEndpointUri The Web service endpoint URI for this document repository
	 * @param documentUniqueIds The list of Document Entry UniqueId(s) for the document(s) retrieved
	 * @param repositoryUniqueIds The list of XDS.b Repository Unique Ids involved in this transaction (aligned with Document Unique Ids array)
	 * @param homeCommunityIds The list of home community ids used in the transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditCrossGatewayRetrieveEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String initiatingGatewayUserId, String initiatingGatewayIpAddress,
			String respondingGatewayEndpointUri,
            String initiatingGatewayUserName,
			String[] documentUniqueIds, String[] repositoryUniqueIds, String homeCommunityIds[],
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		ExportEvent exportEvent = new ExportEvent(true, eventOutcome, new IHETransactionEventTypeCodes.CrossGatewayRetrieve(), purposesOfUse);
		exportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		exportEvent.addSourceActiveParticipant(respondingGatewayEndpointUri, getSystemAltUserId(), null, EventUtils.getAddressForUrl(respondingGatewayEndpointUri, false), false);

		if(!EventUtils.isEmptyOrNull(initiatingGatewayUserName)) {
            exportEvent.addHumanRequestorActiveParticipant(initiatingGatewayUserName, null, initiatingGatewayUserName, userRoles);
        }

		exportEvent.addDestinationActiveParticipant(initiatingGatewayUserId, null, null, initiatingGatewayIpAddress, true);
		//exportEvent.addPatientParticipantObject(patientId);
		if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
			for (int i=0; i<documentUniqueIds.length; i++) {
				exportEvent.addDocumentParticipantObject(documentUniqueIds[i], repositoryUniqueIds[i], homeCommunityIds[i]);
			}
		}
		audit(exportEvent);
	}

	/**
	 * Audits an ITI-43 Retrieve Document Set event for XCA Responding Gateway actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param repositoryEndpointUri The Web service endpoint URI for the document repository
	 * @param repositoryUniqueIds The XDS.b RepositoryUniqueId value for the repository
	 * @param documentUniqueIds The list of Document Entry UniqueId(s) for the document(s) retrieved
	 * @param homeCommunityIds The list of home community ids used in the transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditRetrieveDocumentSetEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String repositoryEndpointUri,
            String userName,
			String[] documentUniqueIds, String[] repositoryUniqueIds, String homeCommunityIds[],
            List<CodedValueType> purposesOfUse, List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		XDSConsumerAuditor.getAuditor().auditRetrieveDocumentSetEvent(eventOutcome, repositoryEndpointUri,
                userName,
                documentUniqueIds,  repositoryUniqueIds, homeCommunityIds, null, purposesOfUse, userRoles);
	}
	
	/**
	 * Audits an ITI-18 Registry Stored Query event for XCA Responding Gateway actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param registryEndpointUri The endpoint of the registry in this transaction
	 * @param storedQueryUUID The UUID of the stored query
	 * @param adhocQueryRequestPayload The payload of the adhoc query request element
	 * @param homeCommunityId The home community id of the transaction (if present)
	 * @param patientId The patient ID queried (if query pertained to a patient id)
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditRegistryStoredQueryEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String registryEndpointUri,
            String consumerUserName,
			String storedQueryUUID, String adhocQueryRequestPayload, String homeCommunityId,
			String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		XDSConsumerAuditor.getAuditor().auditRegistryStoredQueryEvent(eventOutcome, registryEndpointUri,
                consumerUserName, storedQueryUUID, adhocQueryRequestPayload,
                homeCommunityId, patientId, purposesOfUse, userRoles);
	}

}
