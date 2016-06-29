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

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.Locale;

public class TimestampUtils {

	// Conforms to String 
	// 		2008-08-14T09:47:04.350-07:00
	
	private static final String RFC3881_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	private static final FastDateFormat RFC3881_DATE_FORMATTER = FastDateFormat.getInstance(RFC3881_TIMESTAMP_FORMAT);
	
	private static final String BSDSYSLOG_TIMESTAMP_FORMAT = "MMM dd HH:mm:ss";
	
	private static final FastDateFormat BSDSYSLOG_DATE_FORMATTER = FastDateFormat.getInstance(BSDSYSLOG_TIMESTAMP_FORMAT, Locale.US);
	
	public static String getRFC3881Timestamp()
	{
		return TimestampUtils.getRFC3881Timestamp(new Date());
	}
	
	public static String getRFC3881Timestamp(Date date)
	{
		
		StringBuilder formattedDate = new StringBuilder(RFC3881_DATE_FORMATTER.format(date));
		formattedDate.insert((formattedDate.length()-2), ':');
		return formattedDate.toString();
	}
	
	public static String getBSDSyslogDate()
	{
		return getBSDSyslogDate(new Date());
	}
    public static String getBSDSyslogDate(Date date)
    {
    	StringBuilder sb = new StringBuilder(BSDSYSLOG_DATE_FORMATTER.format(date));
    	if (sb.charAt(4) == '0') {
    		sb.setCharAt(4, ' ');
    	}
    	return sb.toString();
    }
}
