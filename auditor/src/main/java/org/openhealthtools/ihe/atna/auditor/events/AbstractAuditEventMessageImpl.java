/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.events;

import java.net.InetAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881AuditSourceTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ActiveParticipantCodes.RFC3881NetworkAccessPointTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881AuditSourceTypes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventActionCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectDataLifeCycleCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ActiveParticipantType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditMessage;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditSourceIdentificationType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.EventIdentificationType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ParticipantObjectIdentificationType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;
import org.openhealthtools.ihe.atna.auditor.utils.TimestampUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base implementation of an Audit Event Message that can be 
 * sent through the auditor.  Contains message construct helper methods
 * to create audit message payloads along with additional metadata such as
 * the date and time.  Finally, the message contains a destination address
 * and port that determines where the message is sent upon delivery to a 
 * specific transport sender.
 *
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public abstract class AbstractAuditEventMessageImpl implements AuditEventMessage {
    /**
     * Logger instance
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuditEventMessageImpl.class);

    /**
     * Root audit message payload
     */
    private final AuditMessage auditMessage;

    /**
     * Date and time the event was generated
     */
    private final Date eventDateTime;

    /**
     * Message destination address
     */
    private InetAddress destination;

    /**
     * Message destination port
     */
    private int port = AuditorModuleConfig.AUDITOR_AUDIT_REPOSITORY_DEFAULT_PORT;

    /**
     * RFC 3881 Object Creation Factory
     * TODO Remove
     */
//	protected static final RFC3881ObjectFactory OBJECT_FACTORY = new RFC3881ObjectFactory();

    /**
     * Constructor for creating an Audit Event Message
     */
    protected AbstractAuditEventMessageImpl(Date timestamp) {
        eventDateTime = timestamp;
        auditMessage = new AuditMessage();
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage#getAuditMessage()
     */
    public AuditMessage getAuditMessage() {
        return auditMessage;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage#getDateTime()
     */
    public Date getDateTime() {
        return eventDateTime;
    }


    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage#setDestinationUri(java.net.URI)
     */
    public void setDestinationUri(URI uri) {
        if (EventUtils.isEmptyOrNull(uri)) {
            LOGGER.error("The destination URI cannot be null");
            throw new IllegalArgumentException("The destination URI cannot be null");
        }

        // Try determining the host address
        InetAddress address;
        try {
            address = InetAddress.getByName(uri.getHost());
        } catch (Exception e) {
            LOGGER.error("The specified address could not be resolved");
            throw new IllegalArgumentException("The specified address could not be resolved");
        }

        setDestinationAddress(address);

        // Try determining the port
        int port = uri.getPort();
        if (port == -1) {
            LOGGER.warn("Invalid port specified in URI, using transport default port");
            port = AuditorModuleConfig.AUDITOR_AUDIT_REPOSITORY_DEFAULT_PORT;
        }

        setDestinationPort(port);
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage#getDestinationAddress()
     */
    public InetAddress getDestinationAddress() {
        return destination;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage#setDestinationAddress(java.net.InetAddress)
     */
    public void setDestinationAddress(InetAddress destination) {
        this.destination = destination;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage#getDestinationPort()
     */
    public int getDestinationPort() {
        return port;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage#setDestinationPort(int)
     */
    public void setDestinationPort(int port) {
        this.port = port;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        byte[] msg = getSerializedMessage(true);
        if (EventUtils.isEmptyOrNull(msg)) {
            return "";
        }
        return new String(msg);
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage#getSerializedMessage(boolean)
     */
    public byte[] getSerializedMessage(boolean useSpacing) {
        byte[] buf = auditMessage.toString(useSpacing).getBytes();
        return buf;
    }


    ////////////////////////////////////////////
    // Protected message construction methods //
    ////////////////////////////////////////////

    /**
     * Clear and set the individual elements of the audit message payload
     * in line with RFC 3881
     *
     * @param eventId The EventIdentification block
     * @param participants The ActiveParticipant blocks
     * @param sourceIds The AuditSourceIdentification blocks
     * @param objectIds The ParticipantObjectIdentification blocks
     */
    protected void setAuditMessageElements(EventIdentificationType eventId, ActiveParticipantType[] participants,
                                           AuditSourceIdentificationType[] sourceIds, ParticipantObjectIdentificationType[] objectIds) {
        AuditMessage auditMessage = getAuditMessage();
        auditMessage.setEventIdentification(eventId);

        // Reset active participants
        auditMessage.getActiveParticipant().clear();
        if (!EventUtils.isEmptyOrNull(participants, true)) {
            auditMessage.getActiveParticipant().addAll(Arrays.asList(participants));
        }

        // reset audit source ids
        auditMessage.getAuditSourceIdentification().clear();
        if (!EventUtils.isEmptyOrNull(sourceIds, true)) {
            auditMessage.getAuditSourceIdentification().addAll(Arrays.asList(sourceIds));
        }

        // Reset participant object ids
        auditMessage.getParticipantObjectIdentification().clear();
        if (!EventUtils.isEmptyOrNull(objectIds, true)) {
            auditMessage.getParticipantObjectIdentification().addAll(Arrays.asList(objectIds));
        }
    }

    /**
     * Create and set an Event Identification block for this audit event message
     *
     * @param outcome The Event Outcome Indicator
     * @param action The Event Action Code
     * @param id The Event ID
     * @param type The Event Type Code
     * @return The Event Identification block created
     */
    protected EventIdentificationType setEventIdentification(
            RFC3881EventOutcomeCodes outcome,
            RFC3881EventActionCodes action,
            CodedValueType id, CodedValueType[] type,
            List<CodedValueType> purposesOfUse) {
        EventIdentificationType eventBlock = new EventIdentificationType();

        eventBlock.setEventID(id);
        eventBlock.setEventDateTime(TimestampUtils.getRFC3881Timestamp(eventDateTime));
        if (!EventUtils.isEmptyOrNull(action)) {
            eventBlock.setEventActionCode(action.getCode());
        }
        if (!EventUtils.isEmptyOrNull(outcome)) {
            eventBlock.setEventOutcomeIndicator(outcome.getCode());
        }
        if (!EventUtils.isEmptyOrNull(type, true)) {
            eventBlock.getEventTypeCode().addAll(Arrays.asList(type));
        }
        eventBlock.setPurposesOfUse(purposesOfUse);

        getAuditMessage().setEventIdentification(eventBlock);

        return eventBlock;
    }

    /**
     * Create and add an Active Participant block to this audit event message
     *
     * @param userID The Active Participant's UserID
     * @param altUserID The Active Participant's Alternate UserID
     * @param userName The Active Participant's UserName
     * @param userIsRequestor Whether this Active Participant is a requestor
     * @param roleIdCodes The Active Participant's Role Codes
     * @param networkAccessPointID The Active Participant's Network Access Point ID (IP / Hostname)
     * @param networkAccessPointTypeCode The type code for the Network Access Point ID
     * @return The Active Participant block created
     */
    protected ActiveParticipantType addActiveParticipant(String userID, String altUserID, String userName,
                                                         Boolean userIsRequestor, List<CodedValueType> roleIdCodes, String networkAccessPointID, RFC3881NetworkAccessPointTypeCodes networkAccessPointTypeCode) {
        ActiveParticipantType activeParticipantBlock = new ActiveParticipantType();

        activeParticipantBlock.setUserID(userID);
        activeParticipantBlock.setAlternativeUserID(altUserID);
        activeParticipantBlock.setUserName(userName);
        activeParticipantBlock.setUserIsRequestor(userIsRequestor);
        if (!EventUtils.isEmptyOrNull(roleIdCodes, true)) {
            activeParticipantBlock.getRoleIDCode().addAll(roleIdCodes);
        }
        activeParticipantBlock.setNetworkAccessPointID(networkAccessPointID);
        if (!EventUtils.isEmptyOrNull(networkAccessPointTypeCode)) {
            activeParticipantBlock.setNetworkAccessPointTypeCode(networkAccessPointTypeCode.getCode());
        }

        getAuditMessage().getActiveParticipant().add(activeParticipantBlock);

        return activeParticipantBlock;
    }

    /**
     * Create and add an Audit Source Identification block to this audit event message
     *
     * @param sourceID The Audit Source ID
     * @param enterpriseSiteID The Audit Enterprise Site ID
     * @param typeCodes The Audit Source Type Codes
     * @return The Audit Source Identification block created
     *
     * @deprecated use {@link #addAuditSourceIdentification(String, String, RFC3881AuditSourceTypes...)}
     */
    protected AuditSourceIdentificationType addAuditSourceIdentification(String sourceID,
                                                                         String enterpriseSiteID,
                                                                         RFC3881AuditSourceTypeCodes... typeCodes) {
        AuditSourceIdentificationType sourceBlock = new AuditSourceIdentificationType();

        if (!EventUtils.isEmptyOrNull(typeCodes, true)) {
            sourceBlock.setAuditSourceTypeCode(typeCodes[0]);
        }
        sourceBlock.setAuditSourceID(sourceID);
        sourceBlock.setAuditEnterpriseSiteID(enterpriseSiteID);

        getAuditMessage().getAuditSourceIdentification().add(sourceBlock);

        return sourceBlock;
    }

    protected AuditSourceIdentificationType addAuditSourceIdentification(String sourceID,
                                                                         String enterpriseSiteID,
                                                                         RFC3881AuditSourceTypes... typeCodes) {
        AuditSourceIdentificationType sourceBlock = new AuditSourceIdentificationType();

        if (!EventUtils.isEmptyOrNull(typeCodes, true)) {
            sourceBlock.getAuditSourceType().addAll(Arrays.asList(typeCodes));
        }
        sourceBlock.setAuditSourceID(sourceID);
        sourceBlock.setAuditEnterpriseSiteID(enterpriseSiteID);

        getAuditMessage().getAuditSourceIdentification().add(sourceBlock);

        return sourceBlock;
    }

    /**
     * Create and add an Participant Object Identification block to this audit event message
     *
     * @param objectIDTypeCode The Participant Object ID Type code
     * @param objectName The Participant Object Name
     * @param objectQuery The Participant Object Query data
     * @param objectDetail The Participant Object detail
     * @param objectID The Participant Object ID
     * @param objectTypeCode The Participant Object Type Code
     * @param objectTypeCodeRole The Participant Object Type Code's ROle
     * @param objectDataLifeCycle The Participant Object Data Life Cycle
     * @param objectSensitivity The Participant Object sensitivity
     * @return The Participant Object Identification block created
     */
    public ParticipantObjectIdentificationType addParticipantObjectIdentification(CodedValueType objectIDTypeCode,
                                                                                     String objectName, byte[] objectQuery, List<TypeValuePairType> objectDetail,
                                                                                     String objectID,
                                                                                     RFC3881ParticipantObjectTypeCodes objectTypeCode,
                                                                                     RFC3881ParticipantObjectTypeRoleCodes objectTypeCodeRole,
                                                                                     RFC3881ParticipantObjectDataLifeCycleCodes objectDataLifeCycle,
                                                                                     String objectSensitivity) {
        ParticipantObjectIdentificationType participantBlock = new ParticipantObjectIdentificationType();

        participantBlock.setParticipantObjectIDTypeCode(objectIDTypeCode);
        participantBlock.setParticipantObjectName(objectName);
        participantBlock.setParticipantObjectQuery(objectQuery);
        if (!EventUtils.isEmptyOrNull(objectDetail, true)) {
            participantBlock.getParticipantObjectDetail().addAll(objectDetail);
        }
        participantBlock.setParticipantObjectID(objectID);
        if (!EventUtils.isEmptyOrNull(objectTypeCode)) {
            participantBlock.setParticipantObjectTypeCode(objectTypeCode.getCode());
        }
        if (!EventUtils.isEmptyOrNull(objectTypeCodeRole)) {
            participantBlock.setParticipantObjectTypeCodeRole(objectTypeCodeRole.getCode());
        }
        if (!EventUtils.isEmptyOrNull(objectDataLifeCycle)) {
            participantBlock.setParticipantObjectDataLifeCycle(objectDataLifeCycle.getCode());
        }
        participantBlock.setParticipantObjectSensitivity(objectSensitivity);

        getAuditMessage().getParticipantObjectIdentification().add(participantBlock);

        return participantBlock;
    }

    /**
     * Create and add an Active Participant block to this audit event message but automatically
     * determine the Network Access Point ID Type Code
     *
     * @param userID The Active Participant's UserID
     * @param altUserID The Active Participant's Alternate UserID
     * @param userName The Active Participant's UserName
     * @param userIsRequestor Whether this Active Participant is a requestor
     * @param roleIdCodes The Active Participant's Role Codes
     * @param networkAccessPointID The Active Participant's Network Access Point ID (IP / Hostname)
     * @return The Active Participant block created
     */
    protected ActiveParticipantType addActiveParticipant(String userID, String altUserID, String userName,
                                                         Boolean userIsRequestor, List<CodedValueType> roleIdCodes, String networkAccessPointID) {
        // Does lookup to see if using IP Address or hostname in Network Access Point ID
        return addActiveParticipant(
                userID, altUserID, userName, userIsRequestor,
                roleIdCodes, networkAccessPointID, getNetworkAccessPointCodeFromAddress(networkAccessPointID));
    }


    /**
     * Attempt to determine the proper Active Participant
     *  Network Access Point Type Code from a given string.
     *
     * @param address Access point type code to look at
     * @return An RFC 3881 Network Access Point Type Code
     */
    protected RFC3881NetworkAccessPointTypeCodes getNetworkAccessPointCodeFromAddress(String address) {
        if (EventUtils.isEmptyOrNull(address)) {
            return null;
        }

        if (address.matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")) {
            return RFC3881NetworkAccessPointTypeCodes.IP_ADDRESS;
        }

        return RFC3881NetworkAccessPointTypeCodes.MACHINE_NAME;
    }

    /**
     * Create and set a Type Value Pair instance for a given type and value
     *
     * @param type The type to set
     * @param value The value to set
     * @return The Type Value Pair instance
     */
    protected TypeValuePairType getTypeValuePair(String type, byte[] value) {
        TypeValuePairType tvp = new TypeValuePairType();
        tvp.setType(type);
        //tvp.setValue(Base64.encodeBase64Chunked(value));
        // the TVP itself base64 encodes now, no need for this
        tvp.setValue(value);
        return tvp;
    }

    /**
     * Create and set a Type Value Pair instance for a given type and value
     *
     * @param type The type to set
     * @param value The value to set
     * @return The Type Value Pair instance
     */
    public TypeValuePairType getTypeValuePair(String type, String value) {
        return getTypeValuePair(type, value.getBytes(StandardCharsets.UTF_8));
    }
}
