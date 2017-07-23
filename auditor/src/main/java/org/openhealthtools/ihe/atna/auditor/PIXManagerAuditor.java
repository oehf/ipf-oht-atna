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
 * Implementation of a PIX Auditor to send audit messages for
 * transactions in the Patient Identifier Cross-referencing profile's
 * Patient Identifer Cross-reference Manager actor and the Patient 
 * Demographic Query profile's Patient Demographics Supplier actor.
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-8  Patient Identity Feed (create, update, delete)
 *  - ITI-9  PIX Query
 *  - ITI-10 PIX Update Notification
 *  - ITI-21 Patient Demographics Query
 *  - ITI-22 Patient Demographics and Visit Query 
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class PIXManagerAuditor extends PIXAuditor 
{
	/**
	 * Get an instance of the PIX Manager Auditor from the 
	 * global context
	 * 
	 * @return PIX Manager Auditor instance
	 */
	public static PIXManagerAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (PIXManagerAuditor)ctx.getAuditor(PIXManagerAuditor.class);
	}
	
	/**
	 * Audits an ITI-8 Patient Identity Feed CREATE PATIENT RECORD 
	 * event for Patient Identifier Cross-reference Manager actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param sourceIpAddress The IP address of the patient identity source that initiated the transaction
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param patientId The patient ID that was affected by this event
	 */
	public void auditCreatePatientRecordEvent(RFC3881EventOutcomeCodes eventOutcome,
			String sourceIpAddress, String sendingFacility, String sendingApp, 
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String hl7MessageControlId, 
			String patientId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(false, 
				new IHETransactionEventTypeCodes.PatientIdentityFeed(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.CREATE, 
				sendingFacility, sendingApp, null, sourceIpAddress, 
				receivingFacility, receivingApp, getSystemAltUserId(), EventUtils.getAddressForUrl(pixManagerUri, false), 
				null, 
				hl7MessageControlId, 
				new String[] {patientId}, null, null);
	}

	/**
	 * Audits an ITI-8 Patient Identity Feed DELETE PATIENT RECORD 
	 * event for Patient Identifier Cross-reference Manager actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param sourceIpAddress The IP address of the patient identity source that initiated the transaction
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param patientId The patient ID that was affected by this event
	 */
	public void auditDeletePatientRecordEvent(RFC3881EventOutcomeCodes eventOutcome,
			String sourceIpAddress, String sendingFacility,
			String sendingApp, String pixManagerUri,
			String receivingFacility, String receivingApp, 
			String hl7MessageControlId,
			String patientId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(false,
				new IHETransactionEventTypeCodes.PatientIdentityFeed(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.DELETE, 
				sendingFacility, sendingApp, null, sourceIpAddress, 
				receivingFacility, receivingApp, getSystemAltUserId(), EventUtils.getAddressForUrl(pixManagerUri, false), 
				null, 
				hl7MessageControlId, 
				new String[] {patientId}, null, null);
	}
	
	/**
	 * Audits an ITI-8 Patient Identity Feed UPDATE PATIENT RECORD 
	 * event for Patient Identifier Cross-reference Manager actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param sourceIpAddress The IP address of the patient identity source that initiated the transaction
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param patientId The patient ID that was affected by this event
	 */
	public void auditUpdatePatientRecordEvent(RFC3881EventOutcomeCodes eventOutcome,
			String sourceIpAddress, String sendingFacility, String sendingApp, 
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String hl7MessageControlId, 
			String patientId)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(false,
				new IHETransactionEventTypeCodes.PatientIdentityFeed(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.UPDATE, 
				sendingFacility, sendingApp, null, sourceIpAddress, 
				receivingFacility, receivingApp, getSystemAltUserId(), EventUtils.getAddressForUrl(pixManagerUri, false), 
				null, 
				hl7MessageControlId, 
				new String[] {patientId}, null, null);
	}

	/**
	 * Audits an ITI-21 Patient Demographics Query event for
	 * Patient Demographics Supplier actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param consumerIpAddress IP Address of the Patient Demographic Consumer
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param pixManagerUri The URI of the supplier being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param hl7QueryParameters The HL7 query parameters from the QPD segment
	 * @param patientIds List of patient IDs that were seen in this transaction
	 */
	public void auditPDQQueryEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String consumerIpAddress, String sendingFacility, String sendingApp,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String hl7MessageControlId, String hl7QueryParameters, 
			String[] patientIds)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditQueryEvent(false, 
				new IHETransactionEventTypeCodes.PatientDemographicsQuery(), eventOutcome, 
				sendingFacility, sendingApp, null, consumerIpAddress, 
				receivingFacility, receivingApp, getSystemAltUserId(), EventUtils.getAddressForUrl(pixManagerUri, false), 
				null, 
				hl7MessageControlId, hl7QueryParameters, 
				patientIds, null, null);
	}
	
	/**
	 * Audits an ITI-22 Patient Demographics and Visit Query event for
	 * Patient Demographics Supplier actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param consumerIpAddress IP Address of the Patient Demographic Consumer
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param pixManagerUri The URI of the supplier being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param hl7QueryParameters The HL7 query parameters from the QPD segment
	 * @param patientIds List of patient IDs that were seen in this transaction
	 */
	public void auditPDVQQueryEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String consumerIpAddress, String sendingFacility, String sendingApp,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String hl7MessageControlId, String hl7QueryParameters, 
			String[] patientIds)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditQueryEvent(false, 
				new IHETransactionEventTypeCodes.PatientDemographicsAndVisitQuery(), eventOutcome, 
				sendingFacility, sendingApp, null, consumerIpAddress, 
				receivingFacility, receivingApp, getSystemAltUserId(), EventUtils.getAddressForUrl(pixManagerUri, false), 
				null, 
				hl7MessageControlId, hl7QueryParameters, 
				patientIds, null, null);
	}

	/**
	 * Audits an ITI-9 PIX Query event for
	 * Patient Identifier Cross-reference (PIX) Manager actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param consumerIpAddress IP Address of the PIX Consumer
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param hl7QueryParameters The HL7 query parameters from the QPD segment
	 * @param patientIds List of patient IDs that were seen in this transaction
	 */
	public void auditPIXQueryEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String consumerIpAddress, String sendingFacility, String sendingApp,
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String hl7MessageControlId, String hl7QueryParameters, 
			String[] patientIds)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditQueryEvent(false,
				new IHETransactionEventTypeCodes.PIXQuery(), eventOutcome, 
				sendingFacility, sendingApp, null, consumerIpAddress, 
				receivingFacility, receivingApp, getSystemAltUserId(), EventUtils.getAddressForUrl(pixManagerUri, false), 
				null, 
				hl7MessageControlId, hl7QueryParameters, 
				patientIds, null, null);
	}

	/**
	 * Audits an ITI-10 PIX Update Notification event for
	 * Patient Identifier Cross-reference (PIX) Manager actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixMgrIpAddress The IP Address of this PIX Manager
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param consumerEndpointUri The URI of the PIX Consumer endpoint the notification was sent to
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param patientIds List of patient IDs that were seen in this transaction
	 */
	public void auditUpdateNotificationEvent(RFC3881EventOutcomeCodes eventOutcome,
			String pixMgrIpAddress, String sendingFacility, String sendingApp, 
			String consumerEndpointUri, String receivingFacility, String receivingApp, 
			String hl7MessageControlId,
			String[] patientIds)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(
				true, 
				new IHETransactionEventTypeCodes.PIXUpdateNotification(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.READ, 
				sendingFacility, sendingApp, getSystemAltUserId(), pixMgrIpAddress, 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(consumerEndpointUri, false), 
				null, 
				hl7MessageControlId, 
				patientIds, null, null);
	}
}
