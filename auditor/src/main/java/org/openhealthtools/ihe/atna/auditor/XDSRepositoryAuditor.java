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

import java.util.Arrays;
import java.util.List;

import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.ExportEvent;
import org.openhealthtools.ihe.atna.auditor.events.ihe.ImportEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * Implementation of a XDS Auditor to send audit messages for
 * transactions under the server-side actors in
 * the Cross-Enterprise Document Sharing (XDS) profile, notably
 * the XDS Document Repository.
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-14 Register Document Set
 *  - ITI-15 Provide and Register Document Set
 *  - ITI-17 Retrieve Document
 *  - ITI-41 Provide and Register Document Set-b
 *  - ITI-42 Register Document Set-b
 *  - ITI-43 Retrieve Document Set
 *  
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class XDSRepositoryAuditor extends XDSAuditor
{
	/**
	 * Get an instance of the XDS Document Repository Auditor from the 
	 * global context
	 * 
	 * @return XDS Document Repository Auditor instance
	 */
	public static XDSRepositoryAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (XDSRepositoryAuditor)ctx.getAuditor(XDSRepositoryAuditor.class);
	}

	/**
	 * Audits an ITI-15 Provide And Register Document Set event for XDS.a Document Repository actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param sourceUserId The Active Participant UserID for the document source (if using WS-Addressing)
	 * @param sourceIpAddress The IP Address of the document source that initiated the transaction
	 * @param repositoryEndpointUri The URI of this repository's endpoint that received the transaction
	 * @param submissionSetUniqueId  The UniqueID of the Submission Set provided
	 * @param patientId The Patient Id that this submission pertains to
	 */
	public void auditProvideAndRegisterDocumentSetEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String sourceUserId, String sourceIpAddress,
            String userName,
			String repositoryEndpointUri,
			String submissionSetUniqueId,
			String patientId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditProvideAndRegisterEvent(new IHETransactionEventTypeCodes.ProvideAndRegisterDocumentSet(),
                eventOutcome, sourceUserId, sourceIpAddress,
                userName,
                repositoryEndpointUri, submissionSetUniqueId, patientId, null, null);
	}
	
	/**
	 * Audits an ITI-41 Provide And Register Document Set-b event for XDS.b Document Repository actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param sourceUserId The Active Participant UserID for the document source (if using WS-Addressing)
	 * @param sourceIpAddress The IP Address of the document source that initiated the transaction
	 * @param repositoryEndpointUri The URI of this repository's endpoint that received the transaction
	 * @param submissionSetUniqueId  The UniqueID of the Submission Set provided
	 * @param patientId The Patient Id that this submission pertains to
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditProvideAndRegisterDocumentSetBEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String sourceUserId, String sourceIpAddress,
            String userName,
			String repositoryEndpointUri,
			String submissionSetUniqueId,
			String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditProvideAndRegisterEvent( new IHETransactionEventTypeCodes.ProvideAndRegisterDocumentSetB(),
                eventOutcome, sourceUserId, sourceIpAddress,
                userName,
                repositoryEndpointUri, submissionSetUniqueId, patientId, purposesOfUse, userRoles);
	}

    @Deprecated
    public void auditProvideAndRegisterDocumentSetBEvent(
            RFC3881EventOutcomeCodes eventOutcome,
            String sourceUserId, String sourceIpAddress,
            String userName,
            String repositoryEndpointUri,
            String submissionSetUniqueId,
            String patientId)
    {
        auditProvideAndRegisterDocumentSetBEvent(eventOutcome, sourceUserId, sourceIpAddress,
                userName, repositoryEndpointUri, submissionSetUniqueId, patientId, null, null);
    }

	/**
	 * Audits an ITI-14 Register Document Set event for XDS.a Document Repository actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param registryEndpointUri The endpoint of the registry in this transaction
	 * @param submissionSetUniqueId The UniqueID of the Submission Set registered
	 * @param patientId The Patient Id that this submission pertains to
	 */
	public void auditRegisterDocumentSetEvent(
			RFC3881EventOutcomeCodes eventOutcome, 
			String repositoryUserId,
            String userName,
			String registryEndpointUri,
			String submissionSetUniqueId, String patientId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditRegisterEvent(new IHETransactionEventTypeCodes.RegisterDocumentSet(), eventOutcome,
                repositoryUserId, userName,
                registryEndpointUri, submissionSetUniqueId, patientId, null, null);
	}

	/**
	 * Audits an ITI-42 Register Document Set-b event for XDS.b Document Repository actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param registryEndpointUri The endpoint of the registry in this transaction
	 * @param submissionSetUniqueId The UniqueID of the Submission Set registered
	 * @param patientId The Patient Id that this submission pertains to
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditRegisterDocumentSetBEvent(
			RFC3881EventOutcomeCodes eventOutcome, 
			String repositoryUserId,
            String userName,
			String registryEndpointUri,
			String submissionSetUniqueId, String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditRegisterEvent(new IHETransactionEventTypeCodes.RegisterDocumentSetB(), eventOutcome, repositoryUserId,
                userName,
                registryEndpointUri, submissionSetUniqueId, patientId, purposesOfUse, userRoles);
	}

    @Deprecated
    public void auditRegisterDocumentSetBEvent(
            RFC3881EventOutcomeCodes eventOutcome,
            String repositoryUserId,
            String userName,
            String registryEndpointUri,
            String submissionSetUniqueId, String patientId)
    {
        auditRegisterDocumentSetBEvent(eventOutcome, repositoryUserId, userName, registryEndpointUri,
                submissionSetUniqueId, patientId, null, null);
    }

	/**
	 * Audits an ITI-17 Retrieve Document event for XDS.a Document Repository actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param consumerIpAddress The IP address of the document consumer that initiated the transaction
	 * @param repositoryRetrieveUri The URI that was used to retrieve the document
	 * @param documentUniqueId The Document Entry Unique ID of the document being retrieved (if known)
	 */
	public void auditRetrieveDocumentEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String consumerIpAddress,
            String userName,
			String repositoryRetrieveUri, String documentUniqueId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		ExportEvent exportEvent = new ExportEvent(true, eventOutcome, new IHETransactionEventTypeCodes.RetrieveDocument(), null);
		exportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		exportEvent.addSourceActiveParticipant(repositoryRetrieveUri, getSystemAltUserId(), null, EventUtils.getAddressForUrl(repositoryRetrieveUri, false), false);
        if (!EventUtils.isEmptyOrNull(userName)) {
            exportEvent.addHumanRequestorActiveParticipant(userName, null, userName, (List<CodedValueType>) null);
        }
		exportEvent.addDestinationActiveParticipant(consumerIpAddress, null, null, consumerIpAddress, true);
		//exportEvent.addPatientParticipantObject(patientId);
		exportEvent.addDocumentUriParticipantObject(repositoryRetrieveUri, documentUniqueId);
		audit(exportEvent);
	}

	/**
	 * Audits an ITI-43 Retrieve Document Set-b event for XDS.b Document Repository actors.
	 * Sends audit messages for situations when exactly one repository and zero or one community are specified in the transaction.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param consumerUserId The Active Participant UserID for the document consumer (if using WS-Addressing)
	 * @param consumerUserName The Active Participant UserName for the document consumer (if using WS-Security / XUA)
	 * @param consumerIpAddress The IP address of the document consumer that initiated the transaction
	 * @param repositoryEndpointUri The Web service endpoint URI for this document repository
	 * @param documentUniqueIds The list of Document Entry UniqueId(s) for the document(s) retrieved
	 * @param repositoryUniqueId The XDS.b Repository Unique Id value for this repository
	 * @param homeCommunityId The XCA Home Community Id used in the transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditRetrieveDocumentSetEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String consumerUserId, String consumerUserName, String consumerIpAddress,
			String repositoryEndpointUri,
			String[] documentUniqueIds, String repositoryUniqueId,  String homeCommunityId,
            List<CodedValueType> purposesOfUse, List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		
		String[] repositoryUniqueIds = null;
		if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
			repositoryUniqueIds = new String[documentUniqueIds.length];
			Arrays.fill(repositoryUniqueIds, repositoryUniqueId);
		}
		
		auditRetrieveDocumentSetEvent(eventOutcome, consumerUserId, consumerUserName, consumerIpAddress,
                repositoryEndpointUri, documentUniqueIds, repositoryUniqueIds, homeCommunityId, purposesOfUse, userRoles);
	}

    @Deprecated
    public void auditRetrieveDocumentSetEvent(
            RFC3881EventOutcomeCodes eventOutcome,
            String consumerUserId, String consumerUserName, String consumerIpAddress,
            String repositoryEndpointUri,
            String[] documentUniqueIds, String repositoryUniqueId,  String homeCommunityId)
    {
        auditRetrieveDocumentSetEvent(eventOutcome, consumerUserId, consumerUserName, consumerIpAddress,
                repositoryEndpointUri, documentUniqueIds, repositoryUniqueId, homeCommunityId, null, null);
    }

    /**
	 * Audits an ITI-43 Retrieve Document Set-b event for XDS.b Document Repository actors.
	 * Sends audit messages for situations when more than one repository and zero or one community are specified in the transaction.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param consumerUserId The Active Participant UserID for the document consumer (if using WS-Addressing)
	 * @param consumerUserName The Active Participant UserName for the document consumer (if using WS-Security / XUA)
	 * @param consumerIpAddress The IP address of the document consumer that initiated the transaction
	 * @param repositoryEndpointUri The Web service endpoint URI for this document repository
	 * @param documentUniqueIds The list of Document Entry UniqueId(s) for the document(s) retrieved
	 * @param repositoryUniqueIds The list of XDS.b Repository Unique Ids involved in this transaction (aligned with Document Unique Ids array)
	 * @param homeCommunityId The XCA Home Community Id used in the transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditRetrieveDocumentSetEvent(
			RFC3881EventOutcomeCodes eventOutcome,
			String consumerUserId, String consumerUserName, String consumerIpAddress,
			String repositoryEndpointUri,
			String[] documentUniqueIds, String[] repositoryUniqueIds, String homeCommunityId,
			List<CodedValueType> purposesOfUse, List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		
		String[] homeCommunityIds = null;
		if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
			homeCommunityIds = new String[documentUniqueIds.length];
			Arrays.fill(homeCommunityIds, homeCommunityId);
		}
		
		auditRetrieveDocumentSetEvent(eventOutcome, consumerUserId, consumerUserName, consumerIpAddress,
                repositoryEndpointUri, documentUniqueIds, repositoryUniqueIds, homeCommunityIds, purposesOfUse, userRoles);
	}

    @Deprecated
    public void auditRetrieveDocumentSetEvent(
            RFC3881EventOutcomeCodes eventOutcome,
            String consumerUserId, String consumerUserName, String consumerIpAddress,
            String repositoryEndpointUri,
            String[] documentUniqueIds, String[] repositoryUniqueIds, String homeCommunityId)
    {
        auditRetrieveDocumentSetEvent(eventOutcome, consumerUserId, consumerUserName, consumerIpAddress,
                repositoryEndpointUri, documentUniqueIds, repositoryUniqueIds, homeCommunityId, null, null);
    }

	/**
	 * Audits an ITI-43 Retrieve Document Set-b event for XDS.b Document Repository actors.
	 * Sends audit messages for situations when more than one repository and more than one community are specified in the transaction.
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
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		ExportEvent exportEvent = new ExportEvent(true, eventOutcome, new IHETransactionEventTypeCodes.RetrieveDocumentSet(), purposesOfUse);
		exportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		exportEvent.addSourceActiveParticipant(repositoryEndpointUri, getSystemAltUserId(), null, EventUtils.getAddressForUrl(repositoryEndpointUri, false), false);
		exportEvent.addDestinationActiveParticipant(consumerUserId, null, consumerUserName, consumerIpAddress, true);
        if (! EventUtils.isEmptyOrNull(consumerUserName)) {
            exportEvent.addHumanRequestorActiveParticipant(consumerUserName, null, consumerUserName, userRoles);
        }

		//exportEvent.addPatientParticipantObject(patientId);
		if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
			for (int i=0; i<documentUniqueIds.length; i++) {
				exportEvent.addDocumentParticipantObject(documentUniqueIds[i], repositoryUniqueIds[i], homeCommunityIds[i]);
			}
		}
		audit(exportEvent);
	}

    @Deprecated
    public void auditRetrieveDocumentSetEvent(
            RFC3881EventOutcomeCodes eventOutcome,
            String consumerUserId, String consumerUserName, String consumerIpAddress,
            String repositoryEndpointUri,
            String[] documentUniqueIds, String[] repositoryUniqueIds, String[] homeCommunityIds)
    {
        auditRetrieveDocumentSetEvent(eventOutcome, consumerUserId, consumerUserName, consumerIpAddress,
                repositoryEndpointUri, documentUniqueIds, repositoryUniqueIds, homeCommunityIds, null, null);
    }

    /**
	 * Generically sends audit messages for XDS Document Repository Provide And Register Document Set events
	 * 
	 * @param transaction The specific IHE Transaction (ITI-15 or ITI-41)
	 * @param eventOutcome The event outcome indicator
	 * @param sourceUserId The Active Participant UserID for the document consumer (if using WS-Addressing)
	 * @param sourceIpAddress The IP address of the document source that initiated the transaction
	 * @param repositoryEndpointUri The Web service endpoint URI for this document repository
	 * @param submissionSetUniqueId The UniqueID of the Submission Set registered
	 * @param patientId The Patient Id that this submission pertains to
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	protected void auditProvideAndRegisterEvent (
			IHETransactionEventTypeCodes transaction, 
			RFC3881EventOutcomeCodes eventOutcome,
			String sourceUserId, String sourceIpAddress,
            String userName,
			String repositoryEndpointUri,
			String submissionSetUniqueId,
			String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		ImportEvent importEvent = new ImportEvent(false, eventOutcome, transaction, purposesOfUse);
		importEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		importEvent.addSourceActiveParticipant(sourceUserId, null, null, sourceIpAddress, true);
        if (!EventUtils.isEmptyOrNull(userName)) {
            importEvent.addHumanRequestorActiveParticipant(userName, null, userName, userRoles);
        }
		importEvent.addDestinationActiveParticipant(repositoryEndpointUri, getSystemAltUserId(), null, EventUtils.getAddressForUrl(repositoryEndpointUri, false), false);
		if (!EventUtils.isEmptyOrNull(patientId)) {
			importEvent.addPatientParticipantObject(patientId);
		}
		importEvent.addSubmissionSetParticipantObject(submissionSetUniqueId);		
		
		audit(importEvent);
	}

    @Deprecated
    protected void auditProvideAndRegisterEvent (
            IHETransactionEventTypeCodes transaction,
            RFC3881EventOutcomeCodes eventOutcome,
            String sourceUserId, String sourceIpAddress,
            String userName,
            String repositoryEndpointUri,
            String submissionSetUniqueId,
            String patientId)
    {
        auditProvideAndRegisterEvent(transaction, eventOutcome, sourceUserId, sourceIpAddress, userName,
                repositoryEndpointUri, submissionSetUniqueId, patientId, null, null);
    }

	/**
	 * Generically sends audit messages for XDS Document Repository Register Document Set events
	 * 
	 * @param transaction The specific IHE Transaction (ITI-14 or ITI-42)
	 * @param eventOutcome The event outcome indicator
	 * @param repositoryUserId The Active Participant UserID for the document repository (if using WS-Addressing)
	 * @param registryEndpointUri  The Web service endpoint URI for the document registry
	 * @param submissionSetUniqueId The UniqueID of the Submission Set registered
	 * @param patientId The Patient Id that this submission pertains to
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	protected void auditRegisterEvent(
			IHETransactionEventTypeCodes transaction,
			RFC3881EventOutcomeCodes eventOutcome, 
			String repositoryUserId,
            String userName,
			String registryEndpointUri,
			String submissionSetUniqueId, String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		ExportEvent exportEvent = new ExportEvent(true, eventOutcome, transaction, purposesOfUse);
		exportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		exportEvent.addSourceActiveParticipant(repositoryUserId, getSystemAltUserId(), null, getSystemNetworkId(), true);
        if (!EventUtils.isEmptyOrNull(userName)) {
            exportEvent.addHumanRequestorActiveParticipant(userName, null, userName, userRoles);
        }
		exportEvent.addDestinationActiveParticipant(registryEndpointUri, null, null, EventUtils.getAddressForUrl(registryEndpointUri, false), false);
		if (!EventUtils.isEmptyOrNull(patientId)) {
			exportEvent.addPatientParticipantObject(patientId);
		}
		exportEvent.addSubmissionSetParticipantObject(submissionSetUniqueId);
		audit(exportEvent);
	}

    @Deprecated
    protected void auditRegisterEvent (
            IHETransactionEventTypeCodes transaction,
            RFC3881EventOutcomeCodes eventOutcome,
            String repositoryUserId,
            String userName,
            String registryEndpointUri,
            String submissionSetUniqueId, String patientId)
    {
        auditRegisterEvent(transaction, eventOutcome, repositoryUserId, userName, registryEndpointUri,
                submissionSetUniqueId, patientId, null, null);
    }

}
