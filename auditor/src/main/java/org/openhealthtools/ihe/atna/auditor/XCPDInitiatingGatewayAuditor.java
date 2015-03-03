/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lawson - XCPD IG implementation based on original IBM implementation of
 *     other auditors
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.QueryEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of XCPD IG Auditor
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-55 Cross Gateway Patient Discovery
 *  
 *  ITI-56 Patient Location Query auditing is not implemented yet
 * 
 * 
 *
 */
public class XCPDInitiatingGatewayAuditor extends IHEAuditor
{
	/**
	 * Logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(XCPDInitiatingGatewayAuditor.class);
	
	/**
	 * Get an instance of the XCPD Initiating Gateway Auditor from the 
	 * global context
	 * 
	 * @return XCPD Initiating Gateway Auditor instance
	 */
	public static XCPDInitiatingGatewayAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (XCPDInitiatingGatewayAuditor)ctx.getAuditor(XCPDInitiatingGatewayAuditor.class);
	}
	
	
	/**
	 * Audits an ITI-55 Cross Gateway Patient Discovery event for
	 * XCPD Initiating Gateway actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param sourceUserId The user id of the source system calling the XCPD IG
	 * @param humanRequestorUserId Human requestor if known, null is ok here
	 * @param humanRequestorRoleIdCode CVT type to define the roll that gives this user access
	 * @param XCPDRGUri The URI of the XCPD Responding Gateway being accessed
	 * @param homeCommunityId the home community id of the source system (the caller, not the callee)
	 * @param queryByParameter a String containing the XML of the queryByParameter portion of the call
	 */
	public void auditXCPDQueryEvent(RFC3881EventOutcomeCodes eventOutcome, String sourceUserId,
			String humanRequestorUserId, CodedValueType humanRequestorRoleIdCode,
			String XCPDRGUri, String homeCommunityId, String queryByParameter,
            List<CodedValueType> purposesOfUse)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		QueryEvent queryEvent = new QueryEvent(true, eventOutcome,
				new IHETransactionEventTypeCodes.CrossGatewayPatientDiscovery(), purposesOfUse);
		
		// Add the source active participant
		queryEvent.addSourceActiveParticipant(sourceUserId, getSystemAltUserId(), getSystemUserName(), getSystemNetworkId(), true);
		
		// if human requestor is not specified, try getting it from configuration
		if (EventUtils.isEmptyOrNull(humanRequestorUserId)){
			humanRequestorUserId = getHumanRequestor();
		}
		if (!EventUtils.isEmptyOrNull(humanRequestorUserId)){
			queryEvent.addHumanRequestorActiveParticipant(humanRequestorUserId, null, null, humanRequestorRoleIdCode);
		}
		
		// Set the destination active participant
		queryEvent.addDestinationActiveParticipant(XCPDRGUri, null, null, EventUtils.getAddressForUrl(XCPDRGUri, false), false);
		
		// set the audit source
		queryEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
				
		// add query parameters participant object
		byte[] queryByParameterBytes = null;
		if (queryByParameter != null) {
			try {
				queryByParameterBytes = queryByParameter.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("could not use UTF-8 encoding due to it being unsupported on this system, using system default which may cause problems downstream", e);
				queryByParameterBytes = queryByParameter.getBytes();
			}
		}
		queryEvent.addQueryParticipantObject(null, homeCommunityId, queryByParameterBytes, null,
                new IHETransactionEventTypeCodes.CrossGatewayQuery());
		
		audit(queryEvent);
	}
	

}
