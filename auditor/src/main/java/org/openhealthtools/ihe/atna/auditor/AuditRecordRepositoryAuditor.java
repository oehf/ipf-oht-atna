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

import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.dicom.AuditLogUsedEvent;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * Implementation of an IHE/DICOM Auditor for the IHE
 * Audit Record Repository (ARR) Actor.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class AuditRecordRepositoryAuditor extends IHEAuditor 
{
	/**
	 * Get an instance of the Audit Record Repository auditor from the 
	 * global context
	 * 
	 * @return Audit Record Repository auditor instance
	 */
	public static AuditRecordRepositoryAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (AuditRecordRepositoryAuditor)ctx.getAuditor(AuditRecordRepositoryAuditor.class);
	}
	
	/**
	 * Audits a DICOM "Audit Log Used" event, for a given User and Process.  
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param accessingUser Identity of the user accessing the audit log
	 * @param accessingProcess Identity of the process accessing the audit log
	 * @param auditLogUri The URI of the audit log being accessed
	 */
	public void auditAuditLogUsed(RFC3881EventOutcomeCodes eventOutcome,
			String accessingUser, String accessingProcess,
			String auditLogUri)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		AuditLogUsedEvent auditLogUsedEvent = new AuditLogUsedEvent(eventOutcome);
		auditLogUsedEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		if (!EventUtils.isEmptyOrNull(accessingUser)) {
			auditLogUsedEvent.addAccessingParticipant(accessingUser, null, null, getSystemNetworkId());
		}
		if (!EventUtils.isEmptyOrNull(accessingProcess)) {
			auditLogUsedEvent.addAccessingParticipant(accessingProcess, null, null, getSystemNetworkId());
		}
		auditLogUsedEvent.addAuditLogIdentity(auditLogUri);
		audit(auditLogUsedEvent);
	}
}
