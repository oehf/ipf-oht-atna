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
package org.openhealthtools.ihe.atna.auditor.events.dicom;

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMActiveParticipantRoleIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.events.GenericAuditEventMessageImpl;

import java.util.Date;
import java.util.Collections;
/**
 * Audit Event representing a DICOM 95 User Authentication event (DCM 110114)
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class UserAuthenticationEvent extends GenericAuditEventMessageImpl
{
	/**
	 * Creates a User Authentication Event for a given event type (e.g. Login, Logout)
	 * @param outcome The event outcome indicator
	 * @param type The type of event
	 * @param eventDateTime timestamp of the event
	 */
	public UserAuthenticationEvent(RFC3881EventOutcomeCodes outcome, DICOMEventTypeCodes type, Date eventDateTime)
	{
		super(
				outcome,
				RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE,
				new DICOMEventIdCodes.UserAuthentication(),
				new DICOMEventTypeCodes[] {type},
				eventDateTime,
				null
		);
	}

	/**
	 * Creates a User Authentication Event for a given event type (e.g. Login, Logout)
	 * @param outcome The event outcome indicator
	 * @param type The type of event
	 */
	public UserAuthenticationEvent(RFC3881EventOutcomeCodes outcome, DICOMEventTypeCodes type)
	{
		super(
				outcome,
				RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE,
				new DICOMEventIdCodes.UserAuthentication(),
				new DICOMEventTypeCodes[] {type},
				null
		);
	}

	/**
	 * Adds an Active Participant representing the user requesting the authentication
	 * 
	 * @param userId The Active Participant's User ID
	 * @param altUserId The Active Participant's Alternate UserID
	 * @param userName The Active Participant's UserName
	 * @param networkId The Active Participant's Network Access Point ID
	 */
	public void addUserActiveParticipant(String userId, String altUserId, String userName, String networkId)
	{
		addActiveParticipant(
				userId, 
				altUserId, 
				userName, 
				true, 
				null, 
				networkId);
	}

	
	/**
	 * Adds an Active Participant representing the node that is performing the authentication
	 * 
	 * @param userId The Active Participant's User ID
	 * @param altUserId The Active Participant's Alternate UserID
	 * @param userName The Active Participant's UserName
	 * @param networkId The Active Participant's Network Access Point ID
	 */
	public void addNodeActiveParticipant(String userId, String altUserId, String userName, String networkId)
	{
		addActiveParticipant(
				userId, 
				altUserId, 
				userName, 
				true,
				Collections.singletonList(new DICOMActiveParticipantRoleIdCodes.Application()),
				networkId);
	}

	/**
	 * Audit Event representing a DICOM 95 User Authentication event (DCM 110114)
	 * with event type of "Login" (DCM 110122)
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 */
	public static class UserLoginEvent extends UserAuthenticationEvent
	{
		/**
		 * @param outcome The event outcome indicator
		 */
		public UserLoginEvent(RFC3881EventOutcomeCodes outcome)
		{
			super(outcome,new DICOMEventTypeCodes.Login());
		}
	}
	
	/**
	 * Audit Event representing a DICOM 95 User Authentication event (DCM 110114)
	 * with event type of "Logout" (DCM 110123)
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 */
	public static class UserLogoutEvent extends UserAuthenticationEvent
	{
		/**
		 * @param outcome The event outcome indicator
		 */
		public UserLogoutEvent(RFC3881EventOutcomeCodes outcome)
		{
			super(outcome,new DICOMEventTypeCodes.Logout());
		}
	}

	
}
