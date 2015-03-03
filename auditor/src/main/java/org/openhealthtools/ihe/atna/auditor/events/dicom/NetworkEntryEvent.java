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

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.events.GenericAuditEventMessageImpl;


/**
 * Audit Event representing a DICOM 95 Network Entry event (DCM 110108)
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class NetworkEntryEvent extends GenericAuditEventMessageImpl
{
	/**
	 * Creates a Network Entry Event for a given event type (e.g. Attach, Detach)
	 * @param outcome The event outcome indicator
	 * @param type The type of event
	 */
	public NetworkEntryEvent(RFC3881EventOutcomeCodes outcome, DICOMEventTypeCodes type)
	{
		super(
				outcome, 
				RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE,
				new DICOMEventIdCodes.NetworkEntry(),
				new DICOMEventTypeCodes[] {type},
                null
		);
	}
	
	/**
	 * Add an Active Participant to this message representing the node doing
	 * the network entry
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
				false, 
				null, 
				networkId);
	}

	
	/**
	 * Audit Event representing a DICOM 95 Network Entry event (DCM 110108)
	 * with event type of "Attach" (DCM 110124)
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 */
	public static class NetworkAttachEvent extends NetworkEntryEvent
	{
		/**
		 * @param outcome The event outcome indicator
		 */
		public NetworkAttachEvent(RFC3881EventOutcomeCodes outcome)
		{
			super(outcome,new DICOMEventTypeCodes.Attach());
		}
	}
	
	/**
	 * Audit Event representing a DICOM 95 Network Entry event (DCM 110108)
	 * with event type of "Detach" (DCM 110125)
	 * 
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 */
	public static class NetworkDetachEvent extends NetworkEntryEvent
	{
		/**
		 * @param outcome The event outcome indicator
		 */
		public NetworkDetachEvent(RFC3881EventOutcomeCodes outcome)
		{
			super(outcome,new DICOMEventTypeCodes.Detach());
		}
	}

	
}
