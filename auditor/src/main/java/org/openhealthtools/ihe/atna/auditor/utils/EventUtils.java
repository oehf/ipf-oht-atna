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
package org.openhealthtools.ihe.atna.auditor.utils;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

public class EventUtils 
{

    /**
     * Extract host name from the given endpoint URI.
     * @see <a href="http://tools.ietf.org/html/rfc3986#section-3">RFC 3986, Section 3</a>
     *
     * @param address
     *      endpoint URI or bare IP address.
     * @param resolveForIp
     *      dummy.
     * @return
     *      host name contained in the URI.
     */
    public static String getAddressForUrl(String address, boolean resolveForIp) {
        if (address == null) {
            return null;
        }

        // drop schema
        int pos = address.indexOf("://");
        if (pos > 0) {
            address = address.substring(pos + 3);
        }

        // drop user authentication information
        pos = address.indexOf('@');
        if (pos > 0) {
            address = address.substring(pos + 1);
        }

        // drop trailing parts: port number, query parameters, path, fragment
        for (int i = 0; i < address.length(); ++i) {
            char c = address.charAt(i);
            if ((c == ':') || (c == '?') || (c == '/') || (c == '#')) {
                return address.substring(0, i);
            }
        }
        return address;
    }

	
	public static CodedValueType getIHETransactionCodeFromMessage(AuditEventMessage msg)
	{
		List<CodedValueType> eventTypeCodes = msg.getAuditMessage().getEventIdentification().getEventTypeCode();
		if (!EventUtils.isEmptyOrNull(eventTypeCodes)) {
			Iterator<CodedValueType> iter = eventTypeCodes.iterator();
			CodedValueType code = null;
			while (iter.hasNext()) {
				code=iter.next();
				if (code.getCodeSystemName().equals("IHE Transactions")) {
					return code;
				}
			}
		}
		return null;
	}

	public static boolean containsCode(CodedValueType codeToCheck, List<String> codesToCheckAgainst)
	{
		if (null != codeToCheck) {
			if (codesToCheckAgainst.contains(codeToCheck.getCode()) ||
					codesToCheckAgainst.contains(codeToCheck.getOriginalText())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsCode(List<CodedValueType> codesToCheck, List<String> codesToCheckAgainst)
	{
		if (!EventUtils.isEmptyOrNull(codesToCheck)) {
			Iterator<CodedValueType> iter = codesToCheck.iterator();
			CodedValueType code = null;
			while (iter.hasNext()) {
				code=iter.next();
				if (EventUtils.containsCode(code, codesToCheckAgainst)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String concatHL7FacilityApplication(String facility, String application)
	{
		return facility + "|" + application;
	}
	
	public static byte[] encodeBase64(String str)
	{
		if (null == str) {
			return new byte[0];
		}
		return encodeBase64(str.getBytes());
	}
	
	public static byte[] encodeBase64(byte[] bytes)
	{
		if (null == bytes) {
			return new byte[0];
		}
		return org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
	}
	

	
	public static boolean isEmptyOrNull(Object obj) 
	{
		return (null == obj);
	}
	
	public static boolean isEmptyOrNull(String str) 
	{
		return (null == str || str.length() == 0);
	}
	
	public static boolean isEmptyOrNull(Object[] array) 
	{
		return (null == array || array.length == 0);
	}
	
	public static boolean isEmptyOrNull(Object[] array, boolean checkItems) 
	{
		if (isEmptyOrNull(array)) {
			return true;
		}
		for (int i=0; i<array.length; i++) {
			if (!isEmptyOrNull(array[i])) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isEmptyOrNull(Collection<?> collection)
	{
		return ( null == collection || collection.size() == 0);
	}
	
	public static boolean isEmptyOrNull(Collection<?> collection, boolean checkItems)
	{
		if (isEmptyOrNull(collection)) {
			return true;
		}
		Iterator<?> i = collection.iterator();
		while (i.hasNext()) {
			if (!isEmptyOrNull(i.next())) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isEmptyOrNull(byte[] array) 
	{
		return (null == array || array.length == 0);
	}
}
