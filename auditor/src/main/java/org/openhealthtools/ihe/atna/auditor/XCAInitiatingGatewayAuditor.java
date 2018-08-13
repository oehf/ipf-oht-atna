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
import org.openhealthtools.ihe.atna.auditor.events.ihe.ImportEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.List;

/**
 * Implementation of a XDS Auditor to send audit messages for
 * transactions under the consumer-side actors in
 * the Cross-Community Access(XCA) profile, notably
 * the XCA Initiating Gateway.
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-38 Cross Gateway Query
 *  - ITI-39 Cross Gateway Retrieve
 *  - ITI-18 Registry Stored Query (as a Document Registry)
 *  - ITI-43 Retrieve Document Set (as a Document Repository)
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class XCAInitiatingGatewayAuditor extends XDSAuditor
{
	/**
	 * Get an instance of the XCA Initiating Gateway from the 
	 * global context
	 * 
	 * @return XCA Initiating Gateway Auditor instance
	 */
	public static XCAInitiatingGatewayAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (XCAInitiatingGatewayAuditor)ctx.getAuditor(XCAInitiatingGatewayAuditor.class);
	}

	/**
	 * Audits an ITI-38 Cross Gateway Query event for XCA Initiating Gateways
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param respondingGatewayEndpointUri The endpoint of the responding gateway in this transaction
	 * @param storedQueryUUID The UUID of the stored query
	 * @param adhocQueryRequestPayload The payload of the adhoc query request element
	 * @param homeCommunityId The home community id of the transaction (if present)
	 * @param patientId The patient ID queried (if query pertained to a patient id)
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditCrossGatewayQueryEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String respondingGatewayEndpointUri, 
			String initiatingGatewayUserId, String initiatingGatewayUserName, 
			String storedQueryUUID, String adhocQueryRequestPayload, String homeCommunityId,
			String patientId, List<CodedValueType> purposesOfUse, List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}

		auditQueryEvent(true, 
				new IHETransactionEventTypeCodes.CrossGatewayQuery(), eventOutcome, 
				getAuditSourceId(), getAuditEnterpriseSiteId(),
				initiatingGatewayUserId, getSystemAltUserId(), initiatingGatewayUserName, getSystemNetworkId(),
				initiatingGatewayUserName, initiatingGatewayUserName, false,
				respondingGatewayEndpointUri, null,
				storedQueryUUID, adhocQueryRequestPayload, homeCommunityId, 
				patientId, purposesOfUse, userRoles);
	}

	/**
	 * Audits an ITI-39 Cross Gateway Retrieve event for XCA Initiating Gateway actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param respondingGatewayEndpointUri The Web service endpoint URI for the document repository
	 * @param documentUniqueIds The list of Document Entry UniqueId(s) for the document(s) retrieved
	 * @param repositoryUniqueIds The list of XDS.b Repository Unique Ids involved in this transaction (aligned with Document Unique Ids array)
	 * @param homeCommunityIds The list of home community ids used in the transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditCrossGatewayRetrieveEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String respondingGatewayEndpointUri,
			String initiatingGatewayUserId, String initiatingGatewayUserName,
			String[] documentUniqueIds, String[] repositoryUniqueIds, String[] homeCommunityIds,
            List<CodedValueType> purposesOfUse, List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		ImportEvent importEvent = new ImportEvent(false, eventOutcome, new IHETransactionEventTypeCodes.CrossGatewayRetrieve(), purposesOfUse);
		importEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		importEvent.addSourceActiveParticipant(respondingGatewayEndpointUri, null, null, EventUtils.getAddressForUrl(respondingGatewayEndpointUri, false), false);
		importEvent.addDestinationActiveParticipant(initiatingGatewayUserId, getSystemAltUserId(), initiatingGatewayUserName, getSystemNetworkId(), true);

		if(!EventUtils.isEmptyOrNull(initiatingGatewayUserName)) {
            importEvent.addHumanRequestorActiveParticipant(initiatingGatewayUserName, null, initiatingGatewayUserName, userRoles);
        }

		if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
			for (int i=0; i<documentUniqueIds.length; i++) {
				importEvent.addDocumentParticipantObject(documentUniqueIds[i], repositoryUniqueIds[i], homeCommunityIds[i]);
			}
		}
		audit(importEvent);
	}
	
	/**
	 * Audits an ITI-43 Retrieve Document Set-b event for XCA Initiating Gateway actors. Audits as a XDS Document Registry.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param consumerUserId The Active Participant UserID for the document consumer (if using WS-Addressing)
	 * @param consumerUserName The Active Participant UserName for the document consumer (if using WS-Security / XUA)
	 * @param consumerIpAddress The IP address of the document consumer that initiated the transaction
	 * @param repositoryEndpointUri The Web service endpoint URI for this document repository
	 * @param documentUniqueIds The list of Document Entry UniqueId(s) for the document(s) retrieved
	 * @param repositoryUniqueIds The list of XDS.b Repository Unique Ids involved in this transaction (aligned with Document Unique Ids array)
	 * @param homeCommunityIds The list of XCA Home Community Ids involved in this transaction (aligned with Document Unique Ids array)
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditRetrieveDocumentSetEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String consumerUserId, String consumerUserName, String consumerIpAddress,
			String repositoryEndpointUri,
			String[] documentUniqueIds, String[] repositoryUniqueIds, String[] homeCommunityIds,
			List<CodedValueType> purposesOfUse, List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		XDSRepositoryAuditor.getAuditor().auditRetrieveDocumentSetEvent(eventOutcome, consumerUserId,
                consumerUserName, consumerIpAddress, repositoryEndpointUri,
                documentUniqueIds, repositoryUniqueIds, homeCommunityIds, purposesOfUse, userRoles);
	}

	/**
	 * Audits an ITI-18 Registry Stored Query event for XCA Initiating Gateway actors.  Audits as a XDS Document Registry.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param consumerUserId The Active Participant UserID for the consumer (if using WS-Addressing)
	 * @param consumerUserName The Active Participant UserName for the consumer (if using WS-Security / XUA)
	 * @param consumerIpAddress The IP Address of the consumer that initiated the transaction
	 * @param registryEndpointUri The URI of this registry's endpoint that received the transaction
	 * @param storedQueryUUID The UUID of the stored query
	 * @param adhocQueryRequestPayload The payload of the adhoc query request element
	 * @param homeCommunityId The home community id of the transaction (if present)
	 * @param patientId The patient ID queried (if query pertained to a patient id)
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditRegistryStoredQueryEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String consumerUserId, String consumerUserName, String consumerIpAddress,
			String registryEndpointUri, 
			String storedQueryUUID, String adhocQueryRequestPayload, String homeCommunityId,
			String patientId, List<CodedValueType> purposesOfUse, List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		XDSRegistryAuditor.getAuditor().auditRegistryStoredQueryEvent(eventOutcome, consumerUserId, consumerUserName,
				consumerIpAddress, registryEndpointUri, storedQueryUUID, adhocQueryRequestPayload, homeCommunityId,
				patientId, purposesOfUse, userRoles);
	}
	
}
