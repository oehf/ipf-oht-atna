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
package org.openhealthtools.ihe.atna.auditor.events.ihe;

import java.util.LinkedList;
import java.util.List;

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;

/**
 * Audit Event representing a DICOM 95 Query event (DCM 110112)
 * with customizations for use in IHE ATNA.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class QueryEvent extends GenericIHEAuditEventMessage
{
	/**
	 * Creates an Export event for a given event outcome and 
	 * IHE transaction
	 * 
	 * @param systemIsSource Whether the generating is the source participant
	 * @param outcome Event outcome indicator
	 * @param eventType The IHE Transaction for this event
	 */
	public QueryEvent(boolean systemIsSource, RFC3881EventOutcomeCodes outcome, IHETransactionEventTypeCodes eventType,
                      List<CodedValueType> purposesOfUse)
	{
		super(  
				systemIsSource,
				outcome, 
				RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE,
				new DICOMEventIdCodes.Query(),
				eventType,
                purposesOfUse
		);
	}
	
	/** 
	 * Adds a Participant Object representing a Query Event (XDS Query / PIX Query, etc)
	 * 
	 * @param messageIdentifier Identifier for the message
	 * @param homeCommunityId Value of the homeCommunityId, if present
	 * @param queryPayload Payload of the query parameters
	 * @param objectDetail Query Details
	 * @param transaction Transaction event
	 */
	public void addQueryParticipantObject(String messageIdentifier, String homeCommunityId, byte[] queryPayload, byte[] objectDetail, IHETransactionEventTypeCodes transaction)
	{
		List<TypeValuePairType> tvp = new LinkedList<TypeValuePairType>();
		if (objectDetail != null) {
			// this is true ONLY for PIX/PDQ messages
			if (transaction.getCode().equalsIgnoreCase("ITI-45") || transaction.getCode().equalsIgnoreCase("ITI-46")) {
				// v3 message
				tvp.add(getTypeValuePair("II", objectDetail));
			} else if (transaction.getCode().equalsIgnoreCase("ITI-83")) {
                // FHIR message
			} else {
				// v2 message
				tvp.add(getTypeValuePair("MSH-10", objectDetail));
			}
		}
		// need to check null or "" because it is known to be passed as "" sometimes
        boolean isXdsQuery = transaction.getCode().equals("ITI-18") || transaction.getCode().equals("ITI-38") || transaction.getCode().equals("ITI-63");
		if (isXdsQuery && messageIdentifier != null && !messageIdentifier.equals("")){
			// this is true ONLY for Stored Query messages
			// set to the system's default charset because that's what we used to getBytes()
			// from the query message
			tvp.add(getTypeValuePair("QueryEncoding", java.nio.charset.Charset.defaultCharset().name().getBytes()));
		}
		if (homeCommunityId != null){
            String type = isXdsQuery ? "urn:ihe:iti:xca:2010:homeCommunityId" : "ihe:homeCommunityID";
			tvp.add(getTypeValuePair(type, homeCommunityId.getBytes()));
		}
		this.addParticipantObjectIdentification(
				this.eventType, 
				null, 
				queryPayload, 
				tvp, 
				messageIdentifier, 
				RFC3881ParticipantObjectTypeCodes.SYSTEM, 
				RFC3881ParticipantObjectTypeRoleCodes.QUERY,
				null, 
				null);
	}
	
    public void addQedParticipantObject(String queryId, byte[] queryByParameterBytes) {
        addParticipantObjectIdentification(
                this.eventType,
                null,
                queryByParameterBytes,
                null,
                queryId,
                RFC3881ParticipantObjectTypeCodes.SYSTEM,
                RFC3881ParticipantObjectTypeRoleCodes.QUERY,
                null,
                null);
    }
}
