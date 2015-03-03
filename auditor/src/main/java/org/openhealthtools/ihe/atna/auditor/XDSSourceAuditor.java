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
 * transactions under the client-side actors in
 * the Cross-Enterprise Document Sharing (XDS) profile, notably
 * the XDS Document Source.
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-15 Provide And Register Document Set
 *  - ITI-41 Provide And Register Document Set-b
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class XDSSourceAuditor extends XDSAuditor
{
	/**
	 * Get an instance of the XDS Document Source Auditor from the 
	 * global context
	 * 
	 * @return XDS Document Source Auditor instance
	 */
	public static XDSSourceAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (XDSSourceAuditor)ctx.getAuditor(XDSSourceAuditor.class);
	}
	
	/**
	 * Audits an ITI-15 Provide And Register Document Set event for XDS.a Document Source actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param repositoryEndpointUri The endpoint of the repository in this transaction
	 * @param submissionSetUniqueId The UniqueID of the Submission Set provided
	 * @param patientId The Patient Id that this submission pertains to
	 */
	public void auditProvideAndRegisterDocumentSetEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String repositoryEndpointUri,
            String userName,
			String submissionSetUniqueId, 
			String patientId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditProvideAndRegisterEvent( new IHETransactionEventTypeCodes.ProvideAndRegisterDocumentSet(),
                eventOutcome, repositoryEndpointUri,
                userName,
                submissionSetUniqueId, patientId, null);
	}
	
	/**
	 * Audits an ITI-41 Provide And Register Document Set-b event for XDS.b Document Source actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param repositoryEndpointUri The endpoint of the repository in this transaction
	 * @param submissionSetUniqueId The UniqueID of the Submission Set provided
	 * @param patientId The Patient Id that this submission pertains to
	 */
	public void auditProvideAndRegisterDocumentSetBEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String repositoryEndpointUri,
            String userName,
			String submissionSetUniqueId, 
			String patientId,
            List<CodedValueType> purposesOfUse)
	{	
		if (!isAuditorEnabled()) {
			return;
		}
		auditProvideAndRegisterEvent( new IHETransactionEventTypeCodes.ProvideAndRegisterDocumentSetB(),
                eventOutcome, repositoryEndpointUri,
                userName,
                submissionSetUniqueId, patientId, purposesOfUse);
	}
	
	/**
	 * Generically sends audit messages for XDS Document Source Provide And Register Document Set events
	 * 
	 * @param transaction The specific IHE Transaction (ITI-15 or ITI-41)
	 * @param eventOutcome The event outcome indicator
	 * @param repositoryEndpointUri The endpoint of the repository in this transaction
	 * @param submissionSetUniqueId The UniqueID of the Submission Set provided
	 * @param patientId The Patient Id that this submission pertains to
	 */
	protected void auditProvideAndRegisterEvent(
			IHETransactionEventTypeCodes transaction, RFC3881EventOutcomeCodes eventOutcome, 
			String repositoryEndpointUri,
            String userName,
			String submissionSetUniqueId, 
			String patientId,
            List<CodedValueType> purposesOfUse)
	{
		ExportEvent exportEvent = new ExportEvent(true, eventOutcome, transaction, purposesOfUse);
		exportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		/*
		 * FIXME:  Overriding endpoint URI with "anonymous", for now
		 */
		String replyToUri = "http://www.w3.org/2005/08/addressing/anonymous";
		//String replyToUri = getSystemUserId();
		
		
		exportEvent.addSourceActiveParticipant(replyToUri, getSystemAltUserId(), getSystemUserName(), getSystemNetworkId(), true);
		if (!EventUtils.isEmptyOrNull(userName)) {
			exportEvent.addHumanRequestorActiveParticipant(userName, null, userName, null);
		}
		exportEvent.addDestinationActiveParticipant(repositoryEndpointUri, null, null, EventUtils.getAddressForUrl(repositoryEndpointUri, false), false);
		if (!EventUtils.isEmptyOrNull(patientId)) {
			exportEvent.addPatientParticipantObject(patientId);
		}
		exportEvent.addSubmissionSetParticipantObject(submissionSetUniqueId);		
		audit(exportEvent);
	}

}
