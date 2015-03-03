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

package org.openhealthtools.ihe.atna.auditor.context;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.context.AbstractModuleConfig;

/**
 * Module Context Configuration manager for the Auditor Module.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public class AuditorModuleConfig extends AbstractModuleConfig
{
    /**
     * Serial Number
     */
    private static final long serialVersionUID = 2133618326408152606L;

    /**
     * Config reference key to enable/disable the global auditor
     */
    public static final String AUDITOR_ENABLED_KEY = "auditor.enabled";
    
    /**
     * Config reference key for the human requestor
     */
    public static final String AUDITOR_HUMAN_REQUESTOR_KEY = "auditor.human.requestor";

    /**
     * Config reference key for the system participant user id
     */
    public static final String AUDITOR_SYSTEM_USER_ID_KEY = "auditor.system.user.id";
 
    /**
     * Config reference key for the system participant alternate user id
     */
    public static final String AUDITOR_SYSTEM_ALT_USER_ID_KEY = "auditor.system.alt.user.id";
    
    /**
     * Config reference key for the system participant username
     */
    public static final String AUDITOR_SYSTEM_USERNAME_KEY = "auditor.system.username.id";
    
    /**
     * Config reference key for the system's audit source id
     */
    public static final String AUDITOR_AUDIT_SOURCE_ID_KEY = "auditor.audit.source.id";
    
    /**
     * Config reference key for the system's audit source enterprise site id
     */
    public static final String AUDITOR_AUDIT_ENTERPRISE_SITE_ID_KEY = "auditor.audit.enterprise.site.id";
    
    /**
     * Config reference key for the system's IP address or hostname
     */
    public static final String AUDITOR_SYSTEM_IP_ADDRESS_KEY = "auditor.system.ip.address";
    
    /**
     * Config reference key for the auditor's target audit repository address
     */
    public static final String AUDITOR_AUDIT_REPOSITORY_HOST_KEY = "auditor.audit.repository.host";
    
    /**
     * Config reference key for the auditor's target audit repository port
     */
    public static final String AUDITOR_AUDIT_REPOSITORY_PORT_KEY = "auditor.audit.repository.port";
    
    /**
     * Config reference key for the default port
     */
    public static final int AUDITOR_AUDIT_REPOSITORY_DEFAULT_PORT = -1;
    
    /**
     * Config reference key for the auditor's target audit repository transport mechanism (TLS, UDP, or BSD)
     */
    public static final String AUDITOR_AUDIT_REPOSITORY_TRANSPORT_KEY = "auditor.audit.repository.transport";
    
    /**
     * Config reference key for the default port
     */
    public static final String AUDITOR_AUDIT_REPOSITORY_DEFAULT_TRANSPORT = "UDP";
    
    /**
     * List of auditors disabled by this configuration instance
     */
    private final List<Class <? extends IHEAuditor>> disabledAuditors = Collections.synchronizedList(new ArrayList<Class <? extends IHEAuditor>>());
    
    /**
     * List of event codes disabled by this configuration instance
     */
    private final List<String> disabledEventCodes = Collections.synchronizedList(new ArrayList<String>());
    
    /**
     * List of IHE transactions disabled by this configuration instance
     */
    private final List<String> disabledIHETransactions = Collections.synchronizedList(new ArrayList<String>());
    
    /**
     * Default constructor for the Auditor Module configuration
     */
    public AuditorModuleConfig()
    {
        this(new Properties());
    }
    
    /**
     * Create a configuration instance from a properties file
     * @param p Properties to use
     */
    public AuditorModuleConfig(Properties p)
    {
        super(p);
        setAuditorEnabled(true);
    }
    
    /**
     * Clone the auditor module configuration instance
     */
    public AuditorModuleConfig clone()
    {
    	Properties newProperties = (Properties)getProperties().clone();
    	AuditorModuleConfig newConfig = new AuditorModuleConfig(newProperties);
    	return newConfig;
    }
    
    /**
     * Check whether this auditor globally enabled
     * @return If the auditor is enabled
     */
    public boolean isAuditorEnabled()
    {
        return Boolean.valueOf(getOption(AUDITOR_ENABLED_KEY));
    }
    
    /**
     * Set the auditor as enabled or disabled
     * @param enable Enable (true) or disable (false) the auditor
     */
    public void setAuditorEnabled(boolean enable)
    {
        setOption(AUDITOR_ENABLED_KEY, Boolean.toString(enable));
    }
    
    /**
     * Gets the hostname of the target audit repository
     * @return The hostname of the target audit repository
     */
    public String getAuditRepositoryHost()
    {
    	return getOption(AUDITOR_AUDIT_REPOSITORY_HOST_KEY);
    }
    
    /**
     * Set the hostname of the target audit repository
     * @param uri The hostname of the target audit repository
     */
    public void setAuditRepositoryHost(String uri)
    {
    	setOption(AUDITOR_AUDIT_REPOSITORY_HOST_KEY, uri);
    }
    
    /**
     * Get the port of the target audit repository
     * @return The port of the target audit repository
     */
    public int getAuditRepositoryPort()
    {
    	try {
    		return Integer.valueOf(getOption(AUDITOR_AUDIT_REPOSITORY_PORT_KEY));
    	} catch (Exception e) {
    		return AUDITOR_AUDIT_REPOSITORY_DEFAULT_PORT;
    	}
    }
    
    /**
     * Set the port of the target audit repository
     * @param port The port of the target audit repository
     */
    public void setAuditRepositoryPort(int port)
    {
    	setOption(AUDITOR_AUDIT_REPOSITORY_PORT_KEY, Integer.toString(port));
    }
    
    /**
     * Get the port of the target audit repository
     * @return The transport to access the target audit repository
     */
    public String getAuditRepositoryTransport()
    {
        String transport = getOption(AUDITOR_AUDIT_REPOSITORY_TRANSPORT_KEY);
        return (transport != null) ? transport : AUDITOR_AUDIT_REPOSITORY_DEFAULT_TRANSPORT;
    }
    
    /**
     * Set the transport of the target audit repository (TLS, UDP, or BSD)
     * @param transport The transport of the target audit repository
     */
    public void setAuditRepositoryTransport(String transport) throws IllegalArgumentException
    {
    	if (!transport.equalsIgnoreCase("SYSLOG") && !transport.equalsIgnoreCase("UDP") && !transport.equalsIgnoreCase("TLS") && !transport.equalsIgnoreCase("BSD"))
    		throw new IllegalArgumentException("Audit Repository transport must be set to one of: SYSLOG, UDP, TLS, or BSD.  Received: " + transport);
    	setOption(AUDITOR_AUDIT_REPOSITORY_TRANSPORT_KEY, transport.toUpperCase());
    }
    
    /**
     * Set the hostname and port of the target audit repository from 
     * a well-formed URI
     * 
     * @param uri URI containing the hostname and port to set
     */
    public void setAuditRepositoryUri(URI uri)
    {
    	setAuditRepositoryHost(uri.getHost());
    	setAuditRepositoryPort(uri.getPort());
    	setAuditRepositoryTransport(uri.getScheme());
    }
    
    /**
     * Set the hostname and port of the target audit repository from 
     * a URI string
     * 
     * @param uri String containing the hostname and port to set
     */
    public void setAuditRepositoryUri(String uri) throws Exception
    {	
    	setAuditRepositoryUri(new URI(uri));
    }
    
    /**
     * Gets the list of auditor classes currently disabled by the auditor 
     * module
     * 
     * @return List of disabled auditor classes
     */
    public List<Class<? extends IHEAuditor>> getDisabledAuditors()
    {
    	return disabledAuditors;
    }
    
    /**
     * Gets the list of events ids currently disabled by the auditor module
     * @return List of disabled event ids
     */
    public List<String> getDisabledEvents()
    {
    	return disabledEventCodes;
    }
    
    /**
     * Gets the list of IHE transactions currently disabled by the auditor module
     * @return List of disabled IHE transactions
     */
    public List<String> getDisabledIHETransactions()
    {
    	return disabledIHETransactions;
    }
    
    /**
     * @param ipAddr
     */
    public void setSystemIpAddress(String ipAddr)
    {
    	setOption(AUDITOR_SYSTEM_IP_ADDRESS_KEY, ipAddr);
    }
    
    /**
     * @return
     */
    public String getSystemIpAddress()
    {
    	return getOption(AUDITOR_SYSTEM_IP_ADDRESS_KEY);
    }
    
    /**
     * @param auditSourceId
     */
    public void setAuditSourceId(String auditSourceId)
    {
    	setOption(AUDITOR_AUDIT_SOURCE_ID_KEY, auditSourceId);
    }
    
    /**
     * @return
     */
    public String getAuditEnterpriseSiteId()
    {
    	return getOption(AUDITOR_AUDIT_ENTERPRISE_SITE_ID_KEY);
    }
    
    /**
     * @param auditSourceId
     */
    public void setAuditEnterpriseSiteId(String auditSourceId)
    {
    	setOption(AUDITOR_AUDIT_ENTERPRISE_SITE_ID_KEY, auditSourceId);
    }
    
    /**
     * @return
     */
    public String getAuditSourceId()
    {
    	return getOption(AUDITOR_AUDIT_SOURCE_ID_KEY);
    }
    
    /**
     * @param humanRequestor
     */
    public void setHumanRequestor(String humanRequestor)
    {
    	setOption(AUDITOR_HUMAN_REQUESTOR_KEY, humanRequestor);
    }
    
    /**
     * @return
     */
    public String getHumanRequestor()
    {
    	return getOption(AUDITOR_HUMAN_REQUESTOR_KEY);
    }
    
	/**
	 * @return
	 */
	public String getSystemAltUserId()
	{
		return getOption(AUDITOR_SYSTEM_ALT_USER_ID_KEY);
	}
	
	/**
	 * @return
	 */
	public String getSystemUserId()
	{
		return getOption(AUDITOR_SYSTEM_USER_ID_KEY);
	}
	
	/**
	 * @return
	 */
	public String getSystemUserName()
	{
		return getOption(AUDITOR_SYSTEM_USERNAME_KEY);
	}
	
	/**
	 * @param altUserId
	 */
	public void setSystemAltUserId(String altUserId)
	{
		setOption(AUDITOR_SYSTEM_ALT_USER_ID_KEY, altUserId);
	}
	
	/**
	 * @param userId
	 */
	public void setSystemUserId(String userId)
	{
		setOption(AUDITOR_SYSTEM_USER_ID_KEY, userId);
	}
	
	/**
	 * @param userName
	 */
	public void setSystemUserName(String userName)
	{
		setOption(AUDITOR_SYSTEM_USERNAME_KEY, userName);
	}
	

}

