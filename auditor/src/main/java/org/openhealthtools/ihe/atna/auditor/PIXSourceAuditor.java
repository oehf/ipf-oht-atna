/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sage - addition on V3 code
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor;

import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.List;

/**
 * Implementation of a PIX Auditor to send audit messages for
 * transactions in the Patient Identifier Cross-referencing profile 
 * Patient Identity Source actor
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-8 Patient Identity Feed (create, update, delete)
 *  - ITI-44 Patient Identity Feed HL7 V3 (create, update, delete)
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class PIXSourceAuditor extends PIXAuditor 
{
	/**
	 * Get an instance of the PIX Source Auditor from the 
	 * global context
	 * 
	 * @return PIX Source Auditor instance
	 */
	public static PIXSourceAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (PIXSourceAuditor)ctx.getAuditor(PIXSourceAuditor.class);
	}
	
	/**
	 * Audits an ITI-8 Patient Identity Feed CREATE PATIENT RECORD 
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
	 * Audits an ITI-44 Patient Identity Feed CREATE PATIENT RECORD 
	 * event for Patient Identity Source actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixManagerUri The URI of the PIX Manager being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageId The HL7 message.id
	 * @param patientId The patient ID that was affected by this event
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditCreatePatientRecordV3Event(RFC3881EventOutcomeCodes eventOutcome,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String sendingFacility, String sendingApp, 
			String hl7MessageId, 
			String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(true, 
				new IHETransactionEventTypeCodes.PatientIdentityFeedV3(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.CREATE, 
				sendingFacility, sendingApp, getSystemAltUserId(), getSystemNetworkId(), 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(pixManagerUri, false), 
				getHumanRequestor(), 
				hl7MessageId,
				new String[] {patientId}, purposesOfUse, userRoles);
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
	 * Audits an ITI-44 Patient Identity Feed DELETE PATIENT RECORD 
	 * event for Patient Identity Source actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixManagerUri The URI of the PIX Manager being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageId The HL7 message.id
	 * @param patientId The patient ID that was affected by this event
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditDeletePatientRecordV3Event(RFC3881EventOutcomeCodes eventOutcome,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String sendingFacility, String sendingApp, 
			String hl7MessageId, 
			String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{	
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(true, 
				new IHETransactionEventTypeCodes.PatientIdentityFeedV3(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.DELETE, 
				sendingFacility, sendingApp, getSystemAltUserId(), getSystemNetworkId(), 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(pixManagerUri, false), 
				getHumanRequestor(), 
				hl7MessageId, 
				new String[] {patientId}, purposesOfUse, userRoles);
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
	
	/**
	 * Audits an ITI-44 Patient Identity Feed UPDATE PATIENT RECORD 
	 * event for Patient Identity Source actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixManagerUri The URI of the PIX Manager being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageId The HL7 message.id
	 * @param patientId The patient ID that was affected by this event
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditUpdatePatientRecordV3Event(RFC3881EventOutcomeCodes eventOutcome,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String sendingFacility, String sendingApp, 
			String hl7MessageId, 
			String patientId,
            List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(true, 
				new IHETransactionEventTypeCodes.PatientIdentityFeedV3(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.UPDATE, 
				sendingFacility, sendingApp, getSystemAltUserId(), getSystemNetworkId(), 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(pixManagerUri, false), 
				getHumanRequestor(), 
				hl7MessageId, 
				new String[] {patientId}, purposesOfUse, userRoles);
	}
}
