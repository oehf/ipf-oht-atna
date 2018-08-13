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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.08.13 at 04:13:48 PM PDT 
//


package org.openhealthtools.ihe.atna.auditor.models.rfc3881;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringEscapeUtils;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;


/**
 * <p>Java class for EventIdentificationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventIdentificationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventID" type="{}CodedValueType"/>
 *         &lt;element name="EventTypeCode" type="{}CodedValueType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="EventActionCode">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="C"/>
 *             &lt;enumeration value="R"/>
 *             &lt;enumeration value="U"/>
 *             &lt;enumeration value="D"/>
 *             &lt;enumeration value="E"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="EventDateTime" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="EventOutcomeIndicator" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *             &lt;enumeration value="0"/>
 *             &lt;enumeration value="4"/>
 *             &lt;enumeration value="8"/>
 *             &lt;enumeration value="12"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "EventIdentificationType", propOrder = {"eventID", "eventTypeCode" })
public class EventIdentificationType {

    //@XmlElement(name = "EventID", required = true)
    protected CodedValueType eventID;
    //@XmlElement(name = "EventTypeCode")
    protected List<CodedValueType> eventTypeCode;
    //@XmlAttribute(name = "EventActionCode")
    protected String eventActionCode;
    //@XmlAttribute(name = "EventDateTime", required = true)
    ////@XmlSchemaType(name = "dateTime")
    protected String eventDateTime;
    //@XmlAttribute(name = "EventOutcomeIndicator", required = true)
    protected Integer eventOutcomeIndicator;
    protected List<CodedValueType> purposesOfUse;

	public String toString(boolean useSpacing)
	{
    	StringBuilder sb = new StringBuilder();
    	if (useSpacing) {
    		sb.append("\n");
    	}
    	sb.append("<EventIdentification");
    	//EventActionCode
    	sb.append(" EventActionCode=\"");
    	sb.append(StringEscapeUtils.escapeXml10(eventActionCode));
    	sb.append("\"");
    	//EventDateTime
    	sb.append(" EventDateTime=\"");
    	sb.append(StringEscapeUtils.escapeXml10(eventDateTime));
    	sb.append("\"");
    	//EventOutcomeIndicator
    	sb.append(" EventOutcomeIndicator=\"");
    	sb.append(eventOutcomeIndicator);
    	sb.append("\"");
    	sb.append(">");
    	if (!EventUtils.isEmptyOrNull(eventID)) {
    		if (useSpacing) {
    			sb.append("\n");
    		}
    		sb.append(eventID.toString("EventID"));
    	}
    	if (!EventUtils.isEmptyOrNull(eventTypeCode)) {
            for (CodedValueType anEventTypeCode : eventTypeCode) {
                if (useSpacing) {
                    sb.append("\n");
                }
                sb.append(anEventTypeCode.toString("EventTypeCode"));
            }
    	}

        if (purposesOfUse != null) {
            for (CodedValueType cvp : purposesOfUse) {
                sb.append(cvp.toString("PurposeOfUse"));
            }
        }

    	if (useSpacing) {
    		sb.append("\n");
    	}
    	sb.append("</EventIdentification>");
    	
    	return sb.toString();
	}
    
    public String toString()
    {
    	return toString(true);
    }
    
    
    /**
     * Gets the value of the eventID property.
     * 
     * @return
     *     possible object is
     *     {@link CodedValueType }
     *     
     */
    public CodedValueType getEventID() {
        return eventID;
    }

    /**
     * Sets the value of the eventID property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodedValueType }
     *     
     */
    public void setEventID(CodedValueType value) {
        this.eventID = value;
    }

    /**
     * Gets the value of the eventTypeCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventTypeCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventTypeCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodedValueType }
     * 
     * 
     */
    public List<CodedValueType> getEventTypeCode() {
        if (eventTypeCode == null) {
            eventTypeCode = new ArrayList<>();
        }
        return this.eventTypeCode;
    }

    /**
     * Gets the value of the eventActionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventActionCode() {
        return eventActionCode;
    }

    /**
     * Sets the value of the eventActionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventActionCode(String value) {
        this.eventActionCode = value;
    }

    /**
     * Gets the value of the eventDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Sets the value of the eventDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventDateTime(String value) {
        this.eventDateTime = value;
    }

    /**
     * Gets the value of the eventOutcomeIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public int getEventOutcomeIndicator() {
        return eventOutcomeIndicator;
    }

    /**
     * Sets the value of the eventOutcomeIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEventOutcomeIndicator(int value) {
        this.eventOutcomeIndicator = value;
    }


    public List<CodedValueType> getPurposesOfUse() {
        return purposesOfUse;
    }

    public void setPurposesOfUse(List<CodedValueType> purposesOfUse) {
        this.purposesOfUse = purposesOfUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventIdentificationType)) return false;
        EventIdentificationType that = (EventIdentificationType) o;
        return Objects.equals(eventID, that.eventID) &&
                Objects.equals(eventTypeCode, that.eventTypeCode) &&
                Objects.equals(eventActionCode, that.eventActionCode) &&
                Objects.equals(eventDateTime, that.eventDateTime) &&
                Objects.equals(eventOutcomeIndicator, that.eventOutcomeIndicator) &&
                Objects.equals(purposesOfUse, that.purposesOfUse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, eventTypeCode, eventActionCode, eventDateTime, eventOutcomeIndicator, purposesOfUse);
    }
}
