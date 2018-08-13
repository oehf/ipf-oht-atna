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
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * Implementation of a PAM Auditor to send audit messages for
 * transactions in the Patient Identifier Cross-referencing profile 
 * Patient Identity Source actor
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-30 Patient Identity Management (create, update, delete)
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @author <a href="mailto:srrenly@us.ibm.com">Sondra Renly</a>
 *
 */
public class PAMSourceAuditor extends PIXAuditor 
{
	/**
	 * Get an instance of the PAM Source Auditor from the 
	 * global context
	 * 
	 * @return PAM Source Auditor instance
	 */
	public static PAMSourceAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (PAMSourceAuditor)ctx.getAuditor(PAMSourceAuditor.class);
	}
	
	/**
	 * Audits an ITI-30 Patient Identity Management CREATE NEW PATIENT RECORD 
	 * event for Patient Demographic Supplier actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixManagerUri The URI of the PIX Manager being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param patientId The patient ID that was affected by this event
	 */
	public void auditCreatePatientRecordEvent(RFC3881EventOutcomeCodes eventOutcome,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String sendingFacility, String sendingApp, 
			String hl7MessageControlId, 
			String patientId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(true, 
				new IHETransactionEventTypeCodes.PatientIdentityFeed(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.CREATE, 
				sendingFacility, sendingApp, getSystemAltUserId(), getSystemNetworkId(), 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(pixManagerUri, false), 
				getHumanRequestor(), 
				hl7MessageControlId, 
				new String[] {patientId}, null, null);
	}

	/**
	 * Audits an ITI-8 Patient Identity Feed DELETE PATIENT RECORD 
	 * event for Patient Identity Source actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixManagerUri The URI of the PIX Manager being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param patientId The patient ID that was affected by this event
	 */
	public void auditDeletePatientRecordEvent(RFC3881EventOutcomeCodes eventOutcome,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String sendingFacility, String sendingApp, 
			String hl7MessageControlId, 
			String patientId)
	{	
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(true, 
				new IHETransactionEventTypeCodes.PatientIdentityFeed(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.DELETE, 
				sendingFacility, sendingApp, getSystemAltUserId(), getSystemNetworkId(), 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(pixManagerUri, false), 
				getHumanRequestor(), 
				hl7MessageControlId, 
				new String[] {patientId}, null, null);
	}
	
	/**
	 * Audits an ITI-8 Patient Identity Feed UPDATE PATIENT RECORD 
	 * event for Patient Identity Source actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixManagerUri The URI of the PIX Manager being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param patientId The patient ID that was affected by this event
	 */
	public void auditUpdatePatientRecordEvent(RFC3881EventOutcomeCodes eventOutcome,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String sendingFacility, String sendingApp, 
			String hl7MessageControlId, 
			String patientId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(true, 
				new IHETransactionEventTypeCodes.PatientIdentityFeed(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.UPDATE, 
				sendingFacility, sendingApp, getSystemAltUserId(), getSystemNetworkId(), 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(pixManagerUri, false), 
				getHumanRequestor(), 
				hl7MessageControlId, 
				new String[] {patientId}, null, null);
	}
}
