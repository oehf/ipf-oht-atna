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
package org.openhealthtools.ihe.atna.auditor;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;

import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.events.dicom.SecurityAlertEvent;
import org.openhealthtools.ihe.atna.auditor.events.dicom.UserAuthenticationEvent;
import org.openhealthtools.ihe.atna.auditor.events.dicom.ApplicationActivityEvent.ApplicationStartEvent;
import org.openhealthtools.ihe.atna.auditor.events.dicom.ApplicationActivityEvent.ApplicationStopEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base implementation of the IHE-enabled auditors.
 * Child classes of IHE Auditor should be IHE profile and/or actor-specific
 * instances that provide methods to perform event auditing in line with
 * ITI-19 Send Audit Message requirements for the actor's transaction(s).
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class IHEAuditor
{
	/**
	 * Logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(IHEAuditor.class);
	
	/**
	 * Configuration used by this auditor
	 */
	private AuditorModuleConfig config;
	
	/**
	 * Module context used by this auditor
	 */
	private AuditorModuleContext context;
	
	/**
	 * Human Requestor ActiveParticipant for audit messages
	 */
	protected String humanRequestor;
	
	/**
	 * System ActiveParticipant Alternate UserID for audit messages
	 */
	protected String systemAltUserId;
	
	/**
	 * System ActiveParticipant Network Access Point ID (e.g. IP Address) for audit messages
	 */
	protected String systemNetworkAccessPointId;
	
	/**
	 * System ActiveParticipant User Id for audit messages
	 */
	protected String systemUserId;
	
	/**
	 * System ActiveParticipant UserName for audit messages
	 */
	protected String systemUserName;
	
	/**
	 * AuditSource Enterprise Site ID
	 */
	protected String auditEnterpriseSiteId;
	
	/**
	 * Audit Source ID
	 */
	protected String auditSourceId;
	
	/**
	 * Get an instance of the XDS Document Consumer Auditor from the 
	 * global context
	 * 
	 * @return XDS Document Consumer Auditor instance
	 */
	public static IHEAuditor getAuditor()
	{
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (IHEAuditor)ctx.getAuditor(IHEAuditor.class);
	}
	
	/**
	 * Default constructor for the IHE Auditor.  Default instantiation contains 
	 * no context or configuration - used for indirect instantiation by an auditor
	 * factory.
	 */
	protected IHEAuditor()
	{
		this(null, null);
	}
	
	/**
	 * Instantiates the instance using a specified context
	 *  (and the specific context's configuration)
	 *  
	 * @param contextToUse Context the auditor will use
	 */
	protected IHEAuditor(AuditorModuleContext contextToUse)
	{
		this(contextToUse,contextToUse.getConfig());
	}
	
	/**
	 * Instantiates the instance using a specified context
	 * and a specified configuration.
	 * 
	 * @param contextToUse Context the auditor will use
	 * @param configToUse Configuration the auditor will uses
	 */
	protected IHEAuditor(AuditorModuleContext contextToUse, AuditorModuleConfig configToUse)
	{
		context = contextToUse;
		config = configToUse;
	}
	
	/**
	 * Set the configuration instance to be used by this auditor
	 * 
	 * @param configToUse Auditor configuration to use in this auditor	
	 */
	public void setConfig(AuditorModuleConfig configToUse)
	{
		config = configToUse;
	}
	
	/**
	 * Set the module context instance to be used by this auditor
	 * 
	 * @param contextToUse Auditor module context to use in this auditor
	 */
	public void setContext(AuditorModuleContext contextToUse)
	{
		context = contextToUse;
	}
	
	/**
	 * Get the module configuration instance used in this auditor
	 * @return Auditor configuration used in this auditor
	 */
	public AuditorModuleConfig getConfig()
	{
		if (null == config) {
			return getContext().getConfig();
		}
		return config;
	}
	
	/**
	 * Get the module context instance used in this auditor
	 * @return Auditor module context used in this auditor
	 */
	public AuditorModuleContext getContext()
	{
		if (null == context) {
			return AuditorModuleContext.getContext();
		}
		return context;
	}
	
	/**
	 * Sends an audit event message to a designated audit record 
	 * repository at the configured hostname and port.  Delivers message to the
	 * designated audit queue and transport sender (preset in the AuditEventMessage
	 * instance or by the auditor's configuration).
	 * 
	 * @param msg Audit message to send to the audit record repository
	 */
	public void audit(AuditEventMessage msg)
	{
		if (!isEnabled(msg)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Auditor disabled - message not sent");
			}
			return;
		}
		
		// Check if the target audit record repository hostname is set
		if (EventUtils.isEmptyOrNull(msg.getDestinationAddress())) {
			String auditHostName = getConfig().getAuditRepositoryHost();

			if (EventUtils.isEmptyOrNull(auditHostName)) {
				LOGGER.error("Audit Record Repository Address is not set, unable to send audit message");
				return;
			}
			
			InetAddress auditRepositoryAddress = null;
			try {
				auditRepositoryAddress = InetAddress.getByName(auditHostName);
			} catch (Exception e) {
				LOGGER.error("Audit Record Repository Address is invalid, unable to send audit message", e);
				return;
			}
			
			msg.setDestinationAddress(auditRepositoryAddress);
		}
		
		// Check if the target audit record repository port is set
		if (msg.getDestinationPort() == AuditorModuleConfig.AUDITOR_AUDIT_REPOSITORY_DEFAULT_PORT) {
			int auditPortNumber = getConfig().getAuditRepositoryPort();

			if (auditPortNumber == AuditorModuleConfig.AUDITOR_AUDIT_REPOSITORY_DEFAULT_PORT) {
				LOGGER.warn("Audit Record Repository Port not set, using transport default");
			}
			
			msg.setDestinationPort(auditPortNumber);
		}
		
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("\n"+msg.toString());
//		}

		if (getContext().getQueue() == null) {
			LOGGER.error("No auditing queue available, unable to send audit message");
			return;
		}
		// Send audit message to repository
		getContext().getQueue().sendAuditEvent(msg);
	}
	
	
	/////////////////////////////////////////////////
	// Audit Source Identification field accessors //
	/////////////////////////////////////////////////
	
	/**
	 * Get the Audit Source Identification's  
	 * AuditEnterpriseSiteID for audit messages
	 * 
	 * @return The Audit Enterprise Site ID
	 */
	public String getAuditEnterpriseSiteId()
	{
		return getConfig().getAuditEnterpriseSiteId();
	}
	
	/**
	 * Get the Audit Source Identification's 
	 * AuditSourceID for audit messages
	 * 
	 * @return The Audit Source ID
	 */
	public String getAuditSourceId()
	{
		return getConfig().getAuditSourceId();
	}
	

	/**
	 * Sets the Audit Source Identification's  
	 * AuditEnterpriseSiteID for audit messages
	 * @param auditEnterpriseSiteId the auditEnterpriseSiteId to set
	 */
	public void setAuditEnterpriseSiteId(String auditEnterpriseSiteId) 
	{
		this.auditEnterpriseSiteId = auditEnterpriseSiteId;
	}

	/**
	 * Get the Audit Source Identification's 
	 * AuditSourceID for audit messages
	 * @param auditSourceId the auditSourceId to set
	 */
	public void setAuditSourceId(String auditSourceId) 
	{
		this.auditSourceId = auditSourceId;
	}
	
	///////////////////////////////////////
	// ActiveParticipant field accessors //
	///////////////////////////////////////
	
	/**
	 * Get the UserID of the human requestor for
	 * audit messages.  This is set using global
	 * configuration in AuditorModulegetConfig().setHumanRequestor(String)
	 * 
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#setHumanRequestor(String)
	 * @return The human requestor user id
	 */
	public String getHumanRequestor()
	{
		return getConfig().getHumanRequestor();
	}
	
	/**
	 * Get the system's ActiveParticipant UserID as set 
	 * in configuration or by the auditor instance.  
	 * 
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#setSystemUserId(String)
	 * @return The system's ActiveParticipant User ID
	 */
	public String getSystemUserId()
	{
		if (!EventUtils.isEmptyOrNull(systemUserId)) {
			return systemUserId;
		}
		
		return getConfig().getSystemUserId();
	}
	
	/**
	 * Get alternate user id for the system's ActiveParticipant in
	 * audit messages.  This is either set in configuration or
	 * the the auditor will attempt to determine it from the JVM's process
	 * id.
	 * 
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#setSystemAltUserId(String)
	 * @return The alternate user id
	 */
	public String getSystemAltUserId()
	{
		// If a localized value is set, use it
		if (!EventUtils.isEmptyOrNull(systemAltUserId)) {
			return systemAltUserId;
		}
		
		// Check if a configuration parameter is set
		systemAltUserId = getConfig().getSystemAltUserId();
		if (!EventUtils.isEmptyOrNull(systemAltUserId)) {
			return systemAltUserId;
		}
		
		// If both are empty, attempt to determine the alternate user id
		// per IHE specifications (e.g. owner JVM's process id)
		String processId = null;
		try {
			// JVM specification says the runtime bean name may contain
			// the process and context information about the JVM, including
			// process ID.  Attempt to get this information
			RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
			processId = mx.getName();
	
			int pointer;
			if (( pointer = processId.indexOf('@')) != -1) {
				processId = processId.substring(0,pointer);
			}
		} catch (Throwable t) {
			// Ignore errors and exceptions, we'll just fake it
		}
		
		// If we can't get the process ID or if it's not set, fake it
		if (EventUtils.isEmptyOrNull(processId)) {
			processId = String.valueOf((int)(Math.random()*1000));
		}
		
		systemAltUserId = processId;

		return systemAltUserId;
	}

	/**
	 * Get the system's ActiveParticipant UserName as set 
	 * in configuration or by the auditor instance.  
	 * 
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#setSystemUserName(String)
	 * @return The system's ActiveParticipant UserName
	 */
	public String getSystemUserName()
	{
		if (!EventUtils.isEmptyOrNull(systemUserName)) {
			return systemUserName;
		}
		
		return getConfig().getSystemUserName();
	}
	
	
	/**
	 * Sets the UserID of the human requestor for
	 * audit messages.
	 * 
	 * @param humanRequestor the humanRequestor to set
	 */
	public void setHumanRequestor(String humanRequestor) 
	{
		this.humanRequestor = humanRequestor;
	}

	/**
	 * Overrides the alternative user id for the system's ActiveParticipant
	 * @param systemAltUserId the systemAltUserId to set
	 */
	public void setSystemAltUserId(String systemAltUserId)
	{
		this.systemAltUserId = systemAltUserId;
	}

	/**
	 * Sets the system's ActiveParticipant Network ID  for use
	 * in audit messages.  
	 * @param systemNetworkId the systemNetworkAccessPointId to set
	 */
	public void setSystemNetworkId(String systemNetworkId) 
	{
		this.systemNetworkAccessPointId = systemNetworkId;
	}

	/**
	 * Sets the system's ActiveParticipant UserID as set 
	 * in configuration or by the auditor instance.  
	 * @param systemUserId the systemUserId to set
	 */
	public void setSystemUserId(String systemUserId) 
	{
		this.systemUserId = systemUserId;
	}

	/**
	 * Sets the system's ActiveParticipant UserName as set 
	 * in configuration or by the auditor instance.  
	 * @param systemUserName the systemUserName to set
	 */
	public void setSystemUserName(String systemUserName) 
	{
		this.systemUserName = systemUserName;
	}


	/**
	 * Get the system's ActiveParticipant Network ID  for use
	 * in audit messages.  Configuration is the default value 
	 * used.  If no value is set in configuration, then Java
	 * will attempt to determine your system's IP address.  If 
	 * Java cannot determine your system's IP address, then localhost
	 * is used.
	 * 
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#setSystemIpAddress(String)
	 * @return The system's ActiveParticipant NetworkID
	 */
	public String getSystemNetworkId()
	{
		String ipAddress = getConfig().getSystemIpAddress();
		if (!EventUtils.isEmptyOrNull(ipAddress)) {
			return ipAddress;
		}
		if (EventUtils.isEmptyOrNull(systemNetworkAccessPointId)) {
			// No set System Address, will attempt to look it up
			try {
				InetAddress localAddress = InetAddress.getLocalHost();
				systemNetworkAccessPointId = localAddress.getHostAddress();
			} catch (Exception e) {
				LOGGER.error("Unable to get system IP address, defaulting to localhost");
				systemNetworkAccessPointId = "localhost";
			}
		}
		return systemNetworkAccessPointId;
	}

	/////////////////////////////////////////////
	// Auditor Enable Checks - Validate Sender //
	/////////////////////////////////////////////
	
	/**
	 * Determines whether this auditor instance is enabled. 
	 * Checks if global auditing is enabled in configuration and 
	 * if the auditor instance's class is in the list of auditors
	 * specifically disabled by configuration
	 * 
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#isAuditorEnabled()
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#getDisabledAuditors()
	 * @return Whether this auditor instance is enabled
	 */
	public boolean isAuditorEnabled()
	{
		if (!getConfig().isAuditorEnabled()) {
			return false;
		}
		if (getConfig().getDisabledAuditors().contains(this.getClass())) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Auditor "+ this.getClass().getName() + " is disabled by configuration");
			}
			return false;
		}
		return true;
	}

	/**
	 * Determines if the event that this audit message represents should be audited 
	 * or if the auditor is disabled by configuration. Examples of events that 
	 * may be disabled include "Export", "Import", "Patient Record", "Security Alert", etc.
	 * 
	 * This method is useful for disabling event-specific messages (such as those
	 * relating to actor and stopping embedded inside of actors) because they 
	 * need to be controlled at a higher level.  Also can be useful for 
	 * testing scenarios where only specific events need to be set at any given time.
	 * 
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#getDisabledEvents()
	 * @see org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes
	 * @param msg The audit event message to check
	 * @return Whether the message should be sent
	 */
	public boolean isAuditorEnabledForEventId(AuditEventMessage msg) 
	{
		CodedValueType eventIdCode = msg.getAuditMessage().getEventIdentification().getEventID();
		if (EventUtils.containsCode(eventIdCode, getConfig().getDisabledEvents())) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Auditor is disabled by configuration for event " + eventIdCode.getOriginalText() + " (" + eventIdCode.getCode() + ")");
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Determines if this audit message represented should be sent or not
	 * based on the IHE transaction it represents. Examples of  transactions
	 * that may be disabled are the ITI code ("ITI-14") or the name of
	 * the transaction ("Register Document Set").
	 * 
	 * @see org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig#getDisabledIHETransactions()
	 * @see org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes
	 * @param msg The audit event message to check
	 * @return Whether the message should be sent
	 */
	public boolean isAuditorEnabledForTransaction(AuditEventMessage msg) 
	{
		CodedValueType transactionCode = EventUtils.getIHETransactionCodeFromMessage(msg);
		return isAuditorEnabledForTransaction(transactionCode);
	}
	
	/**
	 * @see #isAuditorEnabledForEventId(AuditEventMessage)
	 * @param code Code to check
	 * @return Whether the message should be sent
	 */
	private boolean isAuditorEnabledForTransaction(CodedValueType code) 
	{
		if (EventUtils.containsCode(code, getConfig().getDisabledIHETransactions())) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Auditor is disabled by configuration for IHE transaction " + code.getOriginalText() + " (" + code.getCode() + ")");
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Determines if a specific audit message should be sent.  Returns true
	 * if all the following are true:<br />
	 * * The auditor instance is enabled generally
	 * * The auditor is NOT disabled for the message's EventID
	 * * The auditor is NOT disabled for the message's IHE Transaction code
	 * 
	 * @param msg The audit event message to check
	 * @return Whether the message should be sent
	 */
	public boolean isEnabled(AuditEventMessage msg)
	{
		// Check if the auditor is generally enabled
		if (!isAuditorEnabled()) {
			return false;
		}
		
		// Check if the auditor is enabled for a given event id
		if (!isAuditorEnabledForEventId(msg)) {
			return false;
		}
		
		// Check if the auditor is enabled for a given IHE transaction
		if (!isAuditorEnabledForTransaction(msg)) {
			return false;
		}
		
		// If all conditions are satisified, then auditor is fully enabled
		return true;
	}

	
	///////////////////////////////////////
	// Default auditor-wide audit events //
	///////////////////////////////////////
	
	/**
	 * Sends a DICOM Application Activity / Application Start Event audit message
	 * 
	 * @param eventOutcome Event Outcome Indicator
	 * @param actorName	Application Participant User ID (Actor Name)
	 * @param actorStarter Application Starter Participant User ID (Actor Starter Name)
	 */
	public void auditActorStartEvent(RFC3881EventOutcomeCodes eventOutcome, String actorName, String actorStarter)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		ApplicationStartEvent startEvent = new ApplicationStartEvent(eventOutcome);
		startEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		startEvent.addApplicationParticipant(actorName, null, null, getSystemNetworkId());
		if (!EventUtils.isEmptyOrNull(actorStarter)) {
			startEvent.addApplicationStarterParticipant(actorStarter, null, null, null);
		}
		audit(startEvent);
	}
	
	/**
	 * Sends a DICOM Application Activity / Application Stop Event audit message
	 * 
	 * @param eventOutcome Event Outcome Indicator
	 * @param actorName	Application Participant User ID (Actor Name)
	 * @param actorStopper Application Starter Participant User ID (Actor Starter Name)
	 */
	public void auditActorStopEvent(RFC3881EventOutcomeCodes eventOutcome, String actorName, String actorStopper) 
	{
		if (!isAuditorEnabled()) {
			return;
		}
		ApplicationStopEvent stopEvent = new ApplicationStopEvent(eventOutcome);
		stopEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		stopEvent.addApplicationParticipant(actorName, null, null, getSystemNetworkId());
		if (!EventUtils.isEmptyOrNull(actorStopper)) {
			stopEvent.addApplicationStarterParticipant(actorStopper, null, null, null);
		}
		audit(stopEvent);
	}
	
	
	/**
	 * Sends a DICOM Security Alert / Node Authentication event message for 
	 * node authentication failures.  Based on DICOM Supplement 95 specifications,
	 * if the security successfully mitigated a PHI leak, then 
	 * an event outcome indicator of "SERIOUS FAILURE" is sent.  Otherwise, an
	 * event outcome indicator of "MAJOR FAILURE" is sent.
	 * 
	 * @param mitigationSuccessful Whether the system's security mitigated a PHI leak
	 * @param reportingActor Name of the actor reporting the authentication failure
	 * @param reportingProcess Name of the process reporting the authentication failure
	 * @param failedActor The actor that failed to authenticate
	 * @param failedUri The URI the actor attempted to access
	 * @param failureDescription A textual description of the failure
	 */
	public void auditNodeAuthenticationFailure(boolean mitigationSuccessful,
			String reportingActor, String reportingProcess, 
			String failedActor,
    		String failedUri, String failureDescription)
	{
		if (!isAuditorEnabled()) {
			return;
		}
		RFC3881EventOutcomeCodes eventOutcome;
		if (mitigationSuccessful) {
			eventOutcome = RFC3881EventCodes.RFC3881EventOutcomeCodes.SERIOUS_FAILURE;
		} else {
			eventOutcome = RFC3881EventCodes.RFC3881EventOutcomeCodes.MAJOR_FAILURE;
		}
		SecurityAlertEvent alertEvent = new SecurityAlertEvent(eventOutcome, new DICOMEventTypeCodes.NodeAuthentication());
		alertEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		if (!EventUtils.isEmptyOrNull(reportingActor)) {
			alertEvent.addReportingUser(reportingActor);
		}
		if (!EventUtils.isEmptyOrNull(reportingProcess)) {
			alertEvent.addReportingUser(reportingProcess);
		}
		if (!EventUtils.isEmptyOrNull(failedActor)) {
			alertEvent.addActiveParticipant(failedActor);
		}
		alertEvent.addURIParticipantObject(failedUri, failureDescription);
		audit(alertEvent);
	}
	
	protected void auditUserAuthenticationEvent(
			RFC3881EventOutcomeCodes outcome, DICOMEventTypeCodes eventType,
			boolean isAuthenticatedSystem, String remoteUserId, String remoteIpAddress,
			String remoteUserNodeIpAddress
			)
	{
		UserAuthenticationEvent userEvent = new UserAuthenticationEvent(outcome,eventType);
		userEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
		if (isAuthenticatedSystem) {
			userEvent.addUserActiveParticipant(getSystemUserId(), null, null, remoteUserNodeIpAddress);
			userEvent.addNodeActiveParticipant(remoteUserId, null, null, remoteIpAddress);
		} else {
			userEvent.addUserActiveParticipant(remoteUserId, null, null, remoteIpAddress);
			userEvent.addNodeActiveParticipant(getSystemUserId(), null, null, remoteUserNodeIpAddress);
		}
		audit(userEvent);
	}
	
	/**
	 * Audit a User Authentication - Login Event
	 * 
	 * @param outcome Event outcome indicator
	 * @param isAuthenticatedSystem Whether the system auditing is the authenticated system (client) or authenticator (server, e.g. kerberos)
	 * @param remoteUserId	User ID of the system that is not sending the audit message
	 * @param remoteIpAddress IP Address of the system that is not sending the audit message
	 * @param remoteUserNodeIpAddress IP Address of the user system requesting the login authentication
	 */
	public void auditUserAuthenticationLoginEvent(RFC3881EventOutcomeCodes outcome,
			boolean isAuthenticatedSystem, String remoteUserId, String remoteIpAddress,
			String remoteUserNodeIpAddress) 
	{
		auditUserAuthenticationEvent(outcome, new DICOMEventTypeCodes.Login(), isAuthenticatedSystem, remoteUserId, remoteIpAddress, remoteUserNodeIpAddress);
	} 
	
	/**
	 * Audit a User Authentication - Logout Event
	 * 
	 * @param outcome Event outcome indicator
	 * @param isAuthenticatedSystem Whether the system auditing is the authenticated system (client) or authenticator (server, e.g. kerberos)
	 * @param remoteUserId	User ID of the system that is not sending the audit message
	 * @param remoteIpAddress IP Address of the system that is not sending the audit message
	 * @param remoteUserNodeIpAddress IP Address of the user system whose logout event triggered the audit
	 */
	public void auditUserAuthenticationLogoutEvent(RFC3881EventOutcomeCodes outcome,
			boolean isAuthenticatedSystem, String remoteUserId, String remoteIpAddress,
			String remoteUserNodeIpAddress) 
	{
		auditUserAuthenticationEvent(outcome, new DICOMEventTypeCodes.Logout(), isAuthenticatedSystem, remoteUserId, remoteIpAddress, remoteUserNodeIpAddress);
	} 
}
