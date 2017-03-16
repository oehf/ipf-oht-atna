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
package org.openhealthtools.ihe.atna.auditor.events;

import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881AuditSourceTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881AuditSourceTypes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventActionCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

import java.util.List;

/**
 * Generic audit message that creates a valid audit event message 
 * (e.g. one that contains an Event Identification block) as well as 
 * helpers to set the Audit Source Identification.  Instances of this
 * message are not intended to be created and, instead, this class should
 * be extended to create appropriate RFC 3881 or DICOM supplement 95-specific
 * Audit Events.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class GenericAuditEventMessageImpl extends AbstractAuditEventMessageImpl
{

    public GenericAuditEventMessageImpl( RFC3881EventOutcomeCodes outcome,
                                         RFC3881EventActionCodes action,
                                         CodedValueType id, CodedValueType[] type,
                                         List<CodedValueType> purposesOfUse)
    {
        setEventIdentification(outcome,action,id,type, purposesOfUse);
    }

    @Deprecated
    public GenericAuditEventMessageImpl( RFC3881EventOutcomeCodes outcome,
                                         RFC3881EventActionCodes action,
                                         CodedValueType id, CodedValueType[] type)
    {
        this(outcome, action, id, type, null);
    }


    /**
	 * Sets a Audit Source Identification block for a given Audit Source ID
	 * @param sourceId The Audit Source ID to use
	 */
	public void setAuditSourceId(String sourceId)
	{
		setAuditSourceId(sourceId, null, (RFC3881AuditSourceTypes[])null);
	}
	
	/**
	 * Sets a Audit Source Identification block for a given Audit Source ID
	 * and Audit Source Enterprise Site ID
	 * @param sourceId The Audit Source ID to use
	 * @param enterpriseSiteId The Audit Enterprise Site ID to use
	 */
	public void setAuditSourceId(String sourceId, String enterpriseSiteId)
	{
		setAuditSourceId(sourceId, enterpriseSiteId, (RFC3881AuditSourceTypes[])null);
	}
	
	/**
	 * Sets a Audit Source Identification block for a given Audit Source ID,
	 * Audit Source Enterprise Site ID, and a list of audit source type codes
	 * @param sourceId The Audit Source ID to use
	 * @param enterpriseSiteId The Audit Enterprise Site ID to use
	 * @param typeCodes The RFC 3881 Audit Source Type codes to use
	 *
	 * @deprecated use {@link #setAuditSourceId(String, String, RFC3881AuditSourceTypes[])}
	 */
	public void setAuditSourceId(String sourceId, String enterpriseSiteId, RFC3881AuditSourceTypeCodes[] typeCodes)
	{
		addAuditSourceIdentification(sourceId, enterpriseSiteId, typeCodes);
	}

	/**
	 * Sets a Audit Source Identification block for a given Audit Source ID,
	 * Audit Source Enterprise Site ID, and a list of audit source type codes
	 * @param sourceId The Audit Source ID to use
	 * @param enterpriseSiteId The Audit Enterprise Site ID to use
	 * @param typeCodes The RFC 3881 Audit Source Type codes to use
	 */
	public void setAuditSourceId(String sourceId, String enterpriseSiteId, RFC3881AuditSourceTypes[] typeCodes)
	{
		addAuditSourceIdentification(sourceId, enterpriseSiteId, typeCodes);
	}

}
