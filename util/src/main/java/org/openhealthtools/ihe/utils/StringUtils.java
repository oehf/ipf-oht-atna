package org.openhealthtools.ihe.utils;

public class StringUtils 
{
	public static final String EMPTY_STRING = "";
	
	public static boolean isEmptyOrNull(String str)
	{
		return (null == str || EMPTY_STRING.equals(str));
	}
	                                               
}
