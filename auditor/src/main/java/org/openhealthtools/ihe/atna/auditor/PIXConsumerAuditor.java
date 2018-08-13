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
 * transactions under the consumer-side actors in
 * the Patient Identifier Cross-reference (PIX) profile.
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-9  PIX Query
 *  - ITI-10 PIX Update Notification
 *  - ITI-45  PIXV3 Query
 *  - ITI-46 PIXV3 Update Notification
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class PIXConsumerAuditor extends PIXAuditor 
{
	/**
	 * Get an instance of the PIX Consumer Auditor from the 
	 * global context
	 * 
	 * @return PIX Consumer Auditor instance
	 */
	public static PIXConsumerAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (PIXConsumerAuditor)ctx.getAuditor(PIXConsumerAuditor.class);
	}
	
	/**
	 * Audits an ITI-9 PIX Query event for
	 * Patient Identifier Cross-reference (PIX) Consumer actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixManagerUri The URI of the PIX Manager being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param hl7QueryParameters The HL7 query parameters from the QPD segment
	 * @param patientIds List of patient IDs that were seen in this transaction
	 */
	public void auditPIXQueryEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String sendingFacility, String sendingApp,
			String hl7MessageControlId, String hl7QueryParameters, 
			String[] patientIds)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditQueryEvent(true,
				new IHETransactionEventTypeCodes.PIXQuery(), eventOutcome, 
				sendingFacility, sendingApp, getSystemAltUserId(), getSystemNetworkId(), 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(pixManagerUri, false), 
				getHumanRequestor(), 
				hl7MessageControlId, hl7QueryParameters, 
				patientIds, null, null);
	}
	
	/**
	 * Audits an ITI-45 PIX Query event for
	 * Patient Identifier Cross-reference (PIX) Consumer actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixManagerUri The URI of the PIX Manager being accessed
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageId The HL7 message.id
	 * @param hl7QueryParameters The HL7 query parameters
	 * @param patientIds List of patient IDs that were seen in this transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditPIXQueryV3Event(RFC3881EventOutcomeCodes eventOutcome, 
			String pixManagerUri, String receivingFacility, String receivingApp, 
			String sendingFacility, String sendingApp,
			String hl7MessageId, String hl7QueryParameters, 
			String[] patientIds,
			List<CodedValueType> purposesOfUse,
			List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditQueryEvent(true,
				new IHETransactionEventTypeCodes.PIXQueryV3(), eventOutcome, 
				sendingFacility, sendingApp, getSystemAltUserId(), getSystemNetworkId(), 
				receivingFacility, receivingApp, null, EventUtils.getAddressForUrl(pixManagerUri, false), 
				getHumanRequestor(), 
				hl7MessageId, hl7QueryParameters, 
				patientIds, purposesOfUse, userRoles);
	}
	
	/**
	 * Audits an ITI-10 PIX Update Notification event for
	 * Patient Identifier Cross-reference (PIX) Consumer actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixMgrIpAddress The IP Address of the PIX Manager that sent the event
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageControlId The HL7 message control id from the MSH segment
	 * @param patientIds List of patient IDs that were seen in this transaction
	 */
	public void auditUpdateNotificationEvent(RFC3881EventOutcomeCodes eventOutcome,
			String pixMgrIpAddress, String sendingFacility, String sendingApp, 
			String receivingFacility, String receivingApp,
			String hl7MessageControlId, 
			String[] patientIds)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(false, 
				new IHETransactionEventTypeCodes.PIXUpdateNotification(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.UPDATE, 
				sendingFacility, sendingApp, null, pixMgrIpAddress, 
				receivingFacility, receivingApp, getSystemAltUserId(), getSystemNetworkId(), 
				null, 
				hl7MessageControlId, 
				patientIds, null, null);
	}
	
	/**
	 * Audits an ITI-46 PIX Update Notification event for
	 * Patient Identifier Cross-reference (PIX) Consumer actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param pixMgrIpAddress The IP Address of the PIX Manager that sent the event
	 * @param receivingFacility The HL7 receiving facility
	 * @param receivingApp The HL7 receiving application
	 * @param sendingFacility The HL7 sending facility
	 * @param sendingApp The HL7 sending application
	 * @param hl7MessageId The HL7 message.id
	 * @param patientIds List of patient IDs that were seen in this transaction
	 * @param purposesOfUse purpose of use codes (may be taken from XUA token)
	 * @param userRoles roles of the human user (may be taken from XUA token)
	 */
	public void auditUpdateNotificationV3Event(RFC3881EventOutcomeCodes eventOutcome,
			String pixMgrIpAddress, String sendingFacility, String sendingApp, 
			String receivingFacility, String receivingApp,
			String hl7MessageId, 
			String[] patientIds,
            List<CodedValueType> purposesOfUse,
		    List<CodedValueType> userRoles)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		auditPatientRecordEvent(false, 
				new IHETransactionEventTypeCodes.PIXUpdateNotificationV3(), eventOutcome, RFC3881EventCodes.RFC3881EventActionCodes.UPDATE, 
				sendingFacility, sendingApp, null, pixMgrIpAddress, 
				receivingFacility, receivingApp, getSystemAltUserId(), getSystemNetworkId(), 
				null, 
				hl7MessageId, 
				patientIds, purposesOfUse, userRoles);
	}
}
