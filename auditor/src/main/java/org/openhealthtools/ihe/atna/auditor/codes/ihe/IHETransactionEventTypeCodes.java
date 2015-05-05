/*******************************************************************************
 * Copyright (c) 2007,2008 IBM Corporation and others.
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
 * Audit Event ID codes for IHE Transactions
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @since Eclipse OHF IHE 0.3.0
 */
public abstract class IHETransactionEventTypeCodes extends CodedValueType
{
	/**
	 * Create a new IHE Event Code using a specific code and value
	 * 
	 * @param value Coded value for IHE event code
	 * @param meaning Display name for IHE event code
	 */
	protected IHETransactionEventTypeCodes(String value, String meaning)
	{
		
		setCodeSystemName("IHE Transactions");
		setCode(value);
		setOriginalText(meaning);
	}
	

	/**
	 * "IHE Transactions","ITI8","Patient Identity Feed"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PatientIdentityFeed extends IHETransactionEventTypeCodes
	{
		public PatientIdentityFeed()
		{
			super("ITI-8","Patient Identity Feed");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI8","Patient Identity Feed"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PatientIdentityFeedV3 extends IHETransactionEventTypeCodes
	{
		public PatientIdentityFeedV3()
		{
			super("ITI-44","Patient Identity Feed HL7 V3");
		}	
	}	
	
	/**
	 * "IHE Transactions","ITI-9","PIX Query"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PIXQuery extends IHETransactionEventTypeCodes
	{
		public PIXQuery()
		{
			super("ITI-9","PIX Query");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-9","PIX Query"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PIXQueryV3 extends IHETransactionEventTypeCodes
	{
		public PIXQueryV3()
		{
			super("ITI-45","PIXV3 Query");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-10","PIX Update Notification"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PIXUpdateNotification extends IHETransactionEventTypeCodes
	{
		public PIXUpdateNotification()
		{
			super("ITI-10","PIX Update Notification");
		}	
	}	
	
	/**
	 * "IHE Transactions","ITI-10","PIX Update Notification"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PIXUpdateNotificationV3 extends IHETransactionEventTypeCodes
	{
		public PIXUpdateNotificationV3()
		{
			super("ITI-46","PIXV3 Update Notification");
		}	
	}	
	
	/**
	 * "IHE Transactions","ITI-21","PDQ Query"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PatientDemographicsQuery extends IHETransactionEventTypeCodes
	{
		public PatientDemographicsQuery()
		{
			super("ITI-21","Patient Demographics Query");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-21","PDQ Query"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PatientDemographicsQueryV3 extends IHETransactionEventTypeCodes
	{
		public PatientDemographicsQueryV3()
		{
			super("ITI-47","Patient Demographics Query HL7 V3");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-22","PDVQ Query"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class PatientDemographicsAndVisitQuery extends IHETransactionEventTypeCodes
	{
		public PatientDemographicsAndVisitQuery()
		{
			super("ITI-22","Patient Demographics and Visit Query");
		}	
	}	
	
	
	/**
	 * "IHE Transactions","ITI-14","Register Document Set"
	 *  
	 * @since OHT IHE 0.3.0
	 */
	public static final class RegisterDocumentSet extends IHETransactionEventTypeCodes
	{
		public RegisterDocumentSet()
		{
			super("ITI-14","Register Document Set");
		}	
	}	
	
	/**
	 * "IHE Transactions","ITI-15","Provide and Register Document Set"
	 * 
	 * @since OHT IHE 0.3.0
	 */
	public static final class ProvideAndRegisterDocumentSet extends IHETransactionEventTypeCodes
	{
		public ProvideAndRegisterDocumentSet()
		{
			super("ITI-15","Provide and Register Document Set");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-16","Registry SQL Query"
	 *  
	 * @since OHT IHE 0.3.0
	 */
	public static final class RegistrySQLQuery extends IHETransactionEventTypeCodes
	{
		public RegistrySQLQuery()
		{
			super("ITI-16","Registry SQL Query");
		}	
	}	

	/**
	 * "IHE Transactions","ITI-17","Retrieve Document"
	 *  
	 * @since OHT IHE 0.3.0
	 */
	public static final class RetrieveDocument extends IHETransactionEventTypeCodes
	{
		public RetrieveDocument()
		{
			super("ITI-17","Retrieve Document");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-18","Registry Stored Query"
	 *  
	 * @since OHT IHE 0.3.0
	 */
	public static final class RegistryStoredQuery extends IHETransactionEventTypeCodes
	{
		public RegistryStoredQuery()
		{
			super("ITI-18","Registry Stored Query");
		}	
	}

	/**
	 * "IHE Transactions","ITI-32","Distribute Document Set on Media"
	 *  
	 * @since OHT IHE 0.4.0
	 */
	public static final class DistributeDocumentSetOnMedia extends IHETransactionEventTypeCodes
	{
		public DistributeDocumentSetOnMedia()
		{
			super("ITI-32","Distribute Document Set on Media");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-30","Patient Identity Management"
	 *  
	 * @since OHT IHE 0.4.0
	 */
	public static final class PatientDemographicsSupplier extends IHETransactionEventTypeCodes
	{
		public PatientDemographicsSupplier()
		{
			super("ITI-30","Patient Identity Management");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-38","Cross Gateway Query"
	 *  
	 * @since OHT IHE 0.4.0
	 */
	public static final class CrossGatewayQuery extends IHETransactionEventTypeCodes
	{
		public CrossGatewayQuery()
		{
			super("ITI-38","Cross Gateway Query");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-39","Cross Gateway Retrieve"
	 *  
	 * @since OHT IHE 0.4.0
	 */
	public static final class CrossGatewayRetrieve extends IHETransactionEventTypeCodes
	{
		public CrossGatewayRetrieve()
		{
			super("ITI-39","Cross Gateway Retrieve");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-41","Provide and Register Document Set-b"
	 *  
	 * @since OHT IHE 0.3.0
	 */
	public static final class ProvideAndRegisterDocumentSetB extends IHETransactionEventTypeCodes
	{
		public ProvideAndRegisterDocumentSetB()
		{
			super("ITI-41","Provide and Register Document Set-b");
		}	
	}
	
	/**
	 * "IHE Transactions","ITI-42","Register Document Set-b"
	 *  
	 * @since OHT IHE 0.3.0
	 */
	public static final class RegisterDocumentSetB extends IHETransactionEventTypeCodes
	{
		public RegisterDocumentSetB()
		{
			super("ITI-42","Register Document Set-b");
		}	
	}	
	
	/**
	 * "IHE Transactions","ITI-43","Retrieve Document Set"
	 *  
	 * @since OHT IHE 0.3.0
	 */
	public static final class RetrieveDocumentSet extends IHETransactionEventTypeCodes
	{
		public RetrieveDocumentSet()
		{
			super("ITI-43","Retrieve Document Set");
		}	
	}	
	
	/**
	 * "IHE Transactions","ITI-48","Retrieve Value Set"
	 *  
	 * @since OHT IHE 0.4.0
	 */
	public static final class RetrieveValueSet extends IHETransactionEventTypeCodes
	{
		public RetrieveValueSet()
		{
			super("ITI-48","Retrieve Value Set");
		}	
	}	
	
	/**
	 * "IHE Transactions","ITI-55","Cross Gateway Patient Discovery"
	 *  
	 * @since OHT IHE 1.2.0
	 */
	public static final class CrossGatewayPatientDiscovery extends IHETransactionEventTypeCodes
	{
		public CrossGatewayPatientDiscovery()
		{
			super("ITI-55","Cross Gateway Patient Discovery");
		}	
	}
}


