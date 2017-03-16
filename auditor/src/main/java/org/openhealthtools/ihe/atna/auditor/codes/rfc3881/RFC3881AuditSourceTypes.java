/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openhealthtools.ihe.atna.auditor.codes.rfc3881;

import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditSourceType;

/**
 * Audit Source Type Codes
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @since OHT IHE Profiles 0.4.0
 */
public abstract class RFC3881AuditSourceTypes extends AuditSourceType
{
	protected RFC3881AuditSourceTypes(String value, String displayName)
	{
		setCodeSystemName("DCM");
		setOriginalText(displayName);
		setCode(value);
	}
	
	/**
	 * "1", "End User Display Device"
	 */
	public static class EndUserDisplayDevice extends RFC3881AuditSourceTypes
	{
		/**
		 * "1", "End User Display Device"
		 */
		public EndUserDisplayDevice()
		{
			super("1", "End User Display Device or Diagnostic Device");
		}
	}
	
	/**
	 * "2", "Data Acquisition Device"
	 */
	public static class DataAcquisitionDevice extends RFC3881AuditSourceTypes
	{
		/**
		 * "2", "Data Acquisition Device"
		 */
		public DataAcquisitionDevice()
		{
			super("2", "Data Acquisition Device or Instrument");
		}
	}
	
	/**
	 * "3", "Web Server Process"
	 */
	public static class WebServerProcess extends RFC3881AuditSourceTypes
	{
		/**
		 * "3", "Web Server Process"
		 */
		public WebServerProcess()
		{
			super("3", "Web Server Process or Thread");
		}
	}
	
	/**
	 * "4", "Application Server Process"
	 */
	public static class ApplicationServerProcess extends RFC3881AuditSourceTypes
	{
		/**
		 * "4", "Application Server Process"
		 */
		public ApplicationServerProcess()
		{
			super("4", "Application Server Process or Thread");
		}
	}
	
	/**
	 * "5", "Database Server Process"
	 */
	public static class DatabaseServerProcess extends RFC3881AuditSourceTypes
	{
		/**
		 * "5", "Database Server Process"
		 */
		public DatabaseServerProcess()
		{
			super("5", "Database Server Process or Thread");
		}
	}
	
	/**
	 * "6", "Security Server"
	 */
	public static class SecurityServer extends RFC3881AuditSourceTypes
	{
		/**
		 * "6", "Security Server"
		 */
		public SecurityServer()
		{
			super("6", "Security Server");
		}
	}
	
	/**
	 * "7", "Network Component"
	 */
	public static class NetworkComponent extends RFC3881AuditSourceTypes
	{
		/**
		 * "7", "Network Component"
		 */
		public NetworkComponent()
		{
			super("7", "Network Component");
		}
	}
	
	/**
	 * "8", "Operating Software"
	 */
	public static class OperatingSoftware extends RFC3881AuditSourceTypes
	{
		/**
		 * "8", "Operating Software"
		 */
		public OperatingSoftware()
		{
			super("8", "Operating Software");
		}
	}
	
	/**
	 * "9", "External Source"
	 */
	public static class Other extends RFC3881AuditSourceTypes
	{
		/**
		 * "9", "External Source"
		 */
		public Other()
		{
			super("9", "Other");
		}
	}
}
