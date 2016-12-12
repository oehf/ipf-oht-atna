/*******************************************************************************
 * Copyright (c) 2006,2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.openhealthtools.ihe.atna.auditor.codes.ihe;

import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

/**
 * Participant Object ID Type Codes for IHE transactions
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @since OHF IHE 0.3.0
 */
public abstract class IHETransactionParticipantObjectIDTypeCodes extends CodedValueType
{
	/**
	 * Create a new IHE Event Code using a specific code and value
	 * 
	 * @param value Coded value for IHE event code
	 * @param meaning Display name for IHE event code
	 */
	protected IHETransactionParticipantObjectIDTypeCodes(String value, String meaning, String codeSystemName)
	{
		
		setCodeSystemName(codeSystemName);
		setCode(value);
		setOriginalText(meaning);
	}
	

	/**
	 *  "IHE Transactions","ITI8","Patient Identity Feed"
	 *
	 * @since OHF IHE 0.3.0
	 */
	public static final class SubmissionSet extends IHETransactionParticipantObjectIDTypeCodes
	{
		public SubmissionSet()
		{
			super("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd", "submission set classificationNode", "IHE XDS Metadata");
		}	
	}

	public static class StableXdsDocumentEntry extends IHETransactionParticipantObjectIDTypeCodes {
		public StableXdsDocumentEntry() {
			super("urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1", "document entry classificationNode", "IHE XDS Metadata");
		}
	}

	public static class OnDemandXdsDocumentEntry extends IHETransactionParticipantObjectIDTypeCodes {
		public OnDemandXdsDocumentEntry() {
			super("urn:uuid:34268e47-fdf5-41a6-ba33-82133c465248", "on-demand document entry classificationNode", "IHE XDS Metadata");
		}
	}

}


