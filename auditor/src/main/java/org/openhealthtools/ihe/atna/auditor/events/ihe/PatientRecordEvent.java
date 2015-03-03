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
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventActionCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;

/**
 * Audit Event representing a DICOM 95 Patient Record event (DCM 110110)
 * with customizations for use in IHE ATNA.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class PatientRecordEvent extends GenericIHEAuditEventMessage
{
	/**
	 * Creates an Export event for a given event outcome and 
	 * IHE transaction
	 * 
	 * @param systemIsSource Whether the generating is the source participant
	 * @param outcome Event outcome indicator
	 * @param eventActionCode The event's action ("C" = create, "U" = update, "D" = delete)
	 * @param eventType The IHE Transaction for this event
	 */
	public PatientRecordEvent(boolean systemIsSource, 
			RFC3881EventOutcomeCodes outcome, 
			RFC3881EventActionCodes eventActionCode, 
			IHETransactionEventTypeCodes eventType,
            List<CodedValueType> purposesOfUse)
	{
		super(  
				systemIsSource,
				outcome, 
				eventActionCode,
				new DICOMEventIdCodes.PatientRecord(),
				eventType,
                purposesOfUse
		);
	}

	/**
	 * Adds a Participant Object Identification block that representing a patient 
	 * involved in the event
	 * 
	 * @param patientId Identifier of the patient involved
	 * @param messageId	The message control id for this event
	 * @param transaction The transaction event
	 */
	public void addPatientParticipantObject(String patientId, byte[] messageId, IHETransactionEventTypeCodes transaction)
	{
		List<TypeValuePairType> tvp = new LinkedList<TypeValuePairType>();
		if (messageId != null) {
			if (transaction.getCode().equalsIgnoreCase("ITI-44")) {
				// v3 message
				tvp.add(getTypeValuePair("II", messageId));
			}
			else {
				// v2 message
				tvp.add(getTypeValuePair("MSH-10", messageId));
			}
		}
		addParticipantObjectIdentification(
				new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.PatientNumber(), 
				null,
				null, 
				tvp, 
				patientId, 
				RFC3881ParticipantObjectTypeCodes.PERSON, 
				RFC3881ParticipantObjectTypeRoleCodes.PATIENT, 
				null, 
				null);
	}
	

}
