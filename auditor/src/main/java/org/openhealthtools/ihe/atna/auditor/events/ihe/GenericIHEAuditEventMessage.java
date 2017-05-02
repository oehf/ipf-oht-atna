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

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMActiveParticipantRoleIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionParticipantObjectIDTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventActionCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes;
import org.openhealthtools.ihe.atna.auditor.events.GenericAuditEventMessageImpl;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * Generic audit message that creates a valid audit event message 
 * for IHE transactions.  This is intended to be further extended
 * by message pertaining to specific events.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class GenericIHEAuditEventMessage extends GenericAuditEventMessageImpl
{
	/**
	 * Whether the system sending the message is the event source
	 */
	private final boolean systemIsSource;
	
	/**
	 * IHE Transaction code used in event
	 */
	protected IHETransactionEventTypeCodes eventType;
	
	/**
	 * @param systemIsSource
	 * @param outcome
	 * @param action
	 * @param id
	 * @param type
	 */
	public GenericIHEAuditEventMessage(boolean systemIsSource, 
			RFC3881EventOutcomeCodes outcome, 
			RFC3881EventActionCodes action,
			CodedValueType id, 
			IHETransactionEventTypeCodes type,
            List<CodedValueType> purposesOfUse)
	{
		super(outcome,action,id,new IHETransactionEventTypeCodes[] {type}, purposesOfUse);
		this.systemIsSource = systemIsSource;	
		this.eventType = type;
	}

    @Deprecated
    public GenericIHEAuditEventMessage(boolean systemIsSource,
                                       RFC3881EventOutcomeCodes outcome,
                                       RFC3881EventActionCodes action,
                                       CodedValueType id,
                                       IHETransactionEventTypeCodes type)
    {
        this(systemIsSource, outcome, action, id, type, null);
    }

	/**
	 * Gets whether the system that generated the message is the 
	 * event source
	 * @return Whether the system is the event source
	 */
	public boolean systemIsSource()
	{
		return systemIsSource;
	}
	
	/**
	 * Adds an Active Participant block representing the source participant 
	 * @param userId The Active Participant's User ID
	 * @param altUserId The Active Participant's Alternate UserID
	 * @param userName The Active Participant's UserName
	 * @param networkId The Active Participant's Network Access Point ID
	 * @param isRequestor Whether the participant represents the requestor
	 */
	public void addSourceActiveParticipant(String userId, String altUserId, String userName, String networkId, boolean isRequestor)
	{
		addActiveParticipant(
				userId, 
				altUserId, 
				userName, 
				isRequestor, 
				new DICOMActiveParticipantRoleIdCodes[] {new DICOMActiveParticipantRoleIdCodes.Source()}, 
				networkId);
	}
	
	/**
	 * Adds an Active Participant block representing the destination participant 
	 * @param userId The Active Participant's User ID
	 * @param altUserId The Active Participant's Alternate UserID
	 * @param userName The Active Participant's UserName
	 * @param networkId The Active Participant's Network Access Point ID
	 * @param isRequestor Whether the participant represents the requestor
	 */
	public void addDestinationActiveParticipant(String userId, String altUserId, String userName, String networkId, boolean isRequestor)
	{
		addActiveParticipant(
				userId, 
				altUserId, 
				userName, 
				isRequestor, 
				new DICOMActiveParticipantRoleIdCodes[] {new DICOMActiveParticipantRoleIdCodes.Destination()}, 
				networkId);
	}
	
	/**
	 * Adds an Active Participant block representing the human requestor participant 
	 * @param userId The Active Participant's User ID
	 * @param altUserId The Active Participant's Alternate UserID
	 * @param userName The Active Participant's UserName
	 * @param role The participant's role
	 */
	public void addHumanRequestorActiveParticipant(String userId, String altUserId, String userName, CodedValueType role)
	{
		addActiveParticipant(
				userId, 
				altUserId, 
				userName, 
				true,
				new CodedValueType[] {role}, 
				null);
	}
	
	/**
	 * Adds a Participant Object Identification block that representing a patient 
	 * involved in the event
	 * 
	 * @param patientId Identifier of the patient involved
	 */
	public void addPatientParticipantObject(String patientId)
	{
		addParticipantObjectIdentification(
				new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.PatientNumber(), 
				null,
				null, 
				null, 
				patientId, 
				RFC3881ParticipantObjectTypeCodes.PERSON, 
				RFC3881ParticipantObjectTypeRoleCodes.PATIENT, 
				null, 
				null);
	}	

	/**
	 * Adds a Participant Object representing an XDS Submission Set
	 * 
	 * @param submissionSetUniqueId The Submission Set Unique ID of the Submission Set
	 */
	public void addSubmissionSetParticipantObject(String submissionSetUniqueId)
	{
		addParticipantObjectIdentification(
				new IHETransactionParticipantObjectIDTypeCodes.SubmissionSet(), 
				null, 
				null, 
				null, 
				submissionSetUniqueId, 
				RFC3881ParticipantObjectTypeCodes.SYSTEM, 
				RFC3881ParticipantObjectTypeRoleCodes.JOB,
				null, 
				null);
	}
	
	/**
	 * Adds a Participant Object representing a URI
	 * 
	 * @param documentRetrieveUri The URI of the Participant Object
	 * @param documentUniqueId The Document Entry Unique ID
	 */
	public void addDocumentUriParticipantObject(String documentRetrieveUri, String documentUniqueId)
	{
		List<TypeValuePairType> tvp = new LinkedList<>();
		if (documentUniqueId != null) {
			tvp.add(getTypeValuePair("XDSDocumentEntry.uniqueId", documentUniqueId.getBytes()));
		}
		addParticipantObjectIdentification(
				new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.URI(), 
				null, 
				null, 
				tvp, 
				documentRetrieveUri, 
				RFC3881ParticipantObjectTypeCodes.SYSTEM, 
				RFC3881ParticipantObjectTypeRoleCodes.REPORT,
				null, 
				null);
	}
	
	/**
	 * Adds a Participant Object representing a document for XDS Exports
	 * 
	 * @param documentUniqueId The Document Entry Unique Id
	 * @param repositoryUniqueId The Repository Unique Id of the Repository housing the document
	 * @param homeCommunityId The Home Community Id
	 */
	public void addDocumentParticipantObject(String documentUniqueId, String repositoryUniqueId, String homeCommunityId)
	{
		List<TypeValuePairType> tvp = new LinkedList<>();
        //SEK - 10/19/2011 - added check for empty or null, RE: Issue Tracker artifact artf2295 (was Issue 135)
 		if (!EventUtils.isEmptyOrNull(repositoryUniqueId)) {
			tvp.add(getTypeValuePair("Repository Unique Id", repositoryUniqueId.getBytes()));
		}
		if (!EventUtils.isEmptyOrNull(homeCommunityId)) {
			tvp.add(getTypeValuePair("ihe:homeCommunityID", homeCommunityId.getBytes()));
		}
		
		addParticipantObjectIdentification(
				new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.ReportNumber(), 
				null, 
				null, 
				tvp, 
				documentUniqueId, 
				RFC3881ParticipantObjectTypeCodes.SYSTEM, 
				RFC3881ParticipantObjectTypeRoleCodes.REPORT,
				null, 
				null);
	}
	
	/**
	 * Adds a Participant Object representing a value set for SVS Exports
	 * @param valueSetUniqueId unique id (OID) of the returned value set
	 * @param valueSetName name associated with the unique id (OID) of the returned value set
	 * @param valueSetVersion version of the returned value set
	 */
	public void addValueSetParticipantObject(String valueSetUniqueId, String valueSetName, String valueSetVersion)
	{
		if (valueSetName == null){
			valueSetName = "";
		}
		if (valueSetVersion == null){
			valueSetVersion = "";
		}
		List<TypeValuePairType> tvp = new LinkedList<>();
		tvp.add(getTypeValuePair("Value Set Version", valueSetVersion.getBytes()));
		addParticipantObjectIdentification(
				new RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectIDTypeCodes.ReportNumber(), 
				valueSetName, 
				null, 
				tvp, 
				valueSetUniqueId, 
				RFC3881ParticipantObjectTypeCodes.SYSTEM, 
				RFC3881ParticipantObjectTypeRoleCodes.REPORT,
				null, 
				null);
	}

	public void addRemovedRegistryObject(IHETransactionParticipantObjectIDTypeCodes registryObjectType, String registryObjectUuid) {
		addParticipantObjectIdentification(
				registryObjectType,
				null,
				null,
				null,
				registryObjectUuid,
				RFC3881ParticipantObjectTypeCodes.SYSTEM,
				RFC3881ParticipantObjectTypeRoleCodes.REPORT,
				null,
				null);
	}

}
