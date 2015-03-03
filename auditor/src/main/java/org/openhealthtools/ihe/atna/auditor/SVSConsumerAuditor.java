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
 * Implementation of a SVS Consumer Auditor to send audit messages for
 * transactions under the consumer-side actors in
 * the Sharing Value Set (SVS) profile, notably
 * the SVS Consumer.
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-48 Retrieve Value Set
 * 
 * @author <a href="mailto:seknoop@us.ibm.com">Sarah Knoop</a>
 *
 */
public class SVSConsumerAuditor extends IHEAuditor
{
	/**
	 * Get an instance of the SVS Consumer Auditor from the 
	 * global context
	 * 
	 * @return SVS Consumer Auditor instance
	 */
	public static SVSConsumerAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (SVSConsumerAuditor)ctx.getAuditor(SVSConsumerAuditor.class);
	}

	/**
	 * Audits an ITI-48 Retrieve Value Set event for SVS Consumer actors.
	 * 
	 * @param eventOutcome The event outcome indicator
	 * @param repositoryEndpointUri The Web service endpoint URI for the SVS repository
	 * @param valueSetUniqueId unique id (OID) of the returned value set
	 * @param valueSetName name associated with the unique id (OID) of the returned value set
	 * @param valueSetVersion version of the returned value set
	 */
	public void auditRetrieveValueSetEvent(RFC3881EventOutcomeCodes eventOutcome, 
			String repositoryEndpointUri, 
			String valueSetUniqueId, String valueSetName, 
			String valueSetVersion,
            List<CodedValueType> purposesOfUse)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		ImportEvent importEvent = new ImportEvent(false, eventOutcome, new IHETransactionEventTypeCodes.RetrieveValueSet(), purposesOfUse);
		importEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		importEvent.addSourceActiveParticipant(EventUtils.getAddressForUrl(repositoryEndpointUri, false), null, null, EventUtils.getAddressForUrl(repositoryEndpointUri, false), false);
		importEvent.addDestinationActiveParticipant(getSystemUserId(), getSystemAltUserId(), getSystemUserName(), getSystemNetworkId(), true);
		if (!EventUtils.isEmptyOrNull(getHumanRequestor())) {
			importEvent.addHumanRequestorActiveParticipant(getHumanRequestor(), null, null, null);
		}
		importEvent.addValueSetParticipantObject(valueSetUniqueId, valueSetName, valueSetVersion);
		audit(importEvent);
	}
}
