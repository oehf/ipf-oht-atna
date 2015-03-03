/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
import org.openhealthtools.ihe.atna.auditor.events.ihe.ImportEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.List;

/**
 * Implementation of an IHE Auditor for the IHE XDM profile, including
 * the Portable Media Creator and 
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class XDMAuditor extends XDSAuditor 
{
	/**
	 * Get an instance of the XDM auditor from the 
	 * global context
	 * 
	 * @return XDM auditor instance
	 */
	public static XDMAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (XDMAuditor)ctx.getAuditor(XDMAuditor.class);
	}
	

	/**
	 * Audits a PHI Import event for the IHE XDM  Portable Media Importer 
	 * actor and ITI-32  Distribute Document Set on Media Transaction.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param submissionSetUniqueId The Unique ID of the Submission Set imported
	 * @param patientId The ID of the Patient to which the Submission Set pertains
	 */
	public void auditPortableMediaImport(
			RFC3881EventOutcomeCodes eventOutcome,
			String submissionSetUniqueId,
			String patientId,
            List<CodedValueType> purposesOfUse)
	{
		if (!isAuditorEnabled()) {
			return;
		}

		ImportEvent importEvent = new ImportEvent(false, eventOutcome, new IHETransactionEventTypeCodes.DistributeDocumentSetOnMedia(), purposesOfUse);
		importEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		importEvent.addDestinationActiveParticipant(getSystemUserId(), getSystemAltUserId(), getSystemUserName(), getSystemNetworkId(), false);
		if (!EventUtils.isEmptyOrNull(patientId)) {
			importEvent.addPatientParticipantObject(patientId);
		}
		importEvent.addSubmissionSetParticipantObject(submissionSetUniqueId);
		audit(importEvent);
	}
	
	/**
	 * Audits a PHI Export event for the IHE XDM Portable Media Creator actor, 
	 * ITI-32 Distribute Document Set on Media transaction.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param submissionSetUniqueId The Unique ID of the Submission Set exported
	 * @param patientId The ID of the Patient to which the Submission Set pertains
	 */
	public void auditPortableMediaCreate(
			RFC3881EventOutcomeCodes eventOutcome,
			String submissionSetUniqueId,
			String patientId,
            List<CodedValueType> purposesOfUse)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		ExportEvent exportEvent = new ExportEvent(true, eventOutcome, new IHETransactionEventTypeCodes.DistributeDocumentSetOnMedia(), purposesOfUse);
		exportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		exportEvent.addSourceActiveParticipant(getSystemUserId(), getSystemAltUserId(), getSystemUserName(), getSystemNetworkId(), true);
		if (!EventUtils.isEmptyOrNull(patientId)) {
			exportEvent.addPatientParticipantObject(patientId);
		}
		exportEvent.addSubmissionSetParticipantObject(submissionSetUniqueId);
		audit(exportEvent);
	}
}
