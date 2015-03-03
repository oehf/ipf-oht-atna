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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openhealthtools.ihe.atna.auditor.AuditorFactory;
import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue;
import org.openhealthtools.ihe.atna.auditor.queue.SynchronousAuditQueue;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;
import org.openhealthtools.ihe.atna.auditor.sender.BSDSyslogSenderImpl;
import org.openhealthtools.ihe.atna.auditor.sender.TLSSyslogSenderImpl;
import org.openhealthtools.ihe.atna.auditor.sender.UDPSyslogSenderImpl;
import org.openhealthtools.ihe.atna.context.AbstractModuleContext;
import org.openhealthtools.ihe.atna.context.SecurityContext;
import org.openhealthtools.ihe.atna.context.SecurityContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Auditor module context that manages and controls the sending
 * and delivery of audit event messages in line with RFC 3881, 
 * DICOM supplement 95 and IHE ATNA.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com>Matthew Davis</a>
 *
 */
public class AuditorModuleContext extends AbstractModuleContext 
{
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -5100213505091193397L;

	/**
	 * Logger instance
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(AuditorModuleContext.class);
	
	/**
	 * Context ID for the Auditor Module Context
	 */
	public static final String CONTEXT_ID = "org.openhealthtools.ihe.atna.auditor";
	
	/**
	 * List of auditors currently cached in this module
	 */
	private final Map<String,IHEAuditor> auditors = Collections.synchronizedMap(new HashMap<String,IHEAuditor>());
	
	/**
	 * Audit message delivery queue
	 */
	private AuditMessageQueue queue;
	
    /**	 
     * Custom audit message transport sender	 
     */	 
    private AuditMessageSender customSender = null;
		
	/**
	 * Returns the current singleton instance of the Auditor Module Context from the
	 * ThreadLocal cache.  If the ThreadLocal cache has not been initialized or does not contain
	 * this context, then create and initialize module context, register in the ThreadLocal
	 * and return the new instance.
	 * 
	 * @return Context singleton
	 */  
	public static AuditorModuleContext getContext()
	{
		SecurityContext securityContext = SecurityContextFactory.getSecurityContext();
		if (!securityContext.isInitialized()) {
			securityContext.initialize();
		}
		AbstractModuleContext moduleContext = securityContext.getModuleContext(CONTEXT_ID);
		
		if (null == moduleContext || !(moduleContext instanceof AuditorModuleContext)) {
			moduleContext = ContextInitializer.defaultInitialize();
			securityContext.registerModuleContext(CONTEXT_ID, moduleContext);
		}
		
		return (AuditorModuleContext)moduleContext;
	}
	
	/**
	 * Default constructor for module context
	 */
	protected AuditorModuleContext() 
	{
		this(new AuditorModuleConfig());
	}
	
	/**
	 * Constructor that sets a specific module configuration
	 * 
	 * @param config Module configuration to use in the context
	 */
	protected AuditorModuleContext(final AuditorModuleConfig config) 
	{
		super(config);
		queue = new SynchronousAuditQueue(this);
	}
	
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.context.AbstractModuleContext#getConfig()
	 */
	public AuditorModuleConfig getConfig()
	{
		return (AuditorModuleConfig)config;
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.context.AbstractModuleContext#getContextId()
	 */
	public String getContextId()
	{
		return CONTEXT_ID;
	}
	
	/**
	 * Gets the transport-specific sending instance used to 
	 * deliver audit messages to their destination
	 * 
	 * @return Audit message sender
	 */
	public AuditMessageSender getSender()
	{
		if (customSender == null){
	    	String transport = AuditorModuleContext.getContext().getConfig().getAuditRepositoryTransport();
			if (transport.equalsIgnoreCase("TLS") ) {
				return new TLSSyslogSenderImpl();
			} else if (transport.equalsIgnoreCase("UDP") ){
				return new UDPSyslogSenderImpl();
			} else {
				return new BSDSyslogSenderImpl();
			}
		}else
			return customSender;
	}
	
    /**
     * Sets a custom sending instance used to	 
     * deliver audit messages to their destination.
     * Only set this if you have a custom sender (perhaps
     * for testing).  Typically users shouldn't set
     * this and instead the sender will by dynamically
     * created based on the audit repository transport
     * setting.	 
     *	 
     * @param sender Audit Transport to use	 
     */	 
    public void setSender(AuditMessageSender sender)	 
    {	 
            this.customSender = sender;	 
    }	 


	/**
	 * Gets the rule-based handler queue used to prepare
	 * and time audit messages for sending to their destination
	 * 
	 * @return Audit message queue
	 */
	public synchronized AuditMessageQueue getQueue()
	{
		return queue;
	}

	/**
	 * Sets the rule-based handler queue used to prepare
	 * and time audit messages for sending to their destination
	 * 
	 * @param queue Audit message delivery queue to use
	 */
	public void setQueue(AuditMessageQueue queue)
	{
		this.queue = queue;
	}

	/**
	 * Get all auditor instances registered in this context
	 * @return The auditor instances
	 */
	public Map<String,IHEAuditor> getAllRegisteredAuditors()
	{
		return Collections.unmodifiableMap(auditors);
	}
	
	/**
	 * Get an auditor instance from the context registry 
	 * by name.  Generally the name is the fully-qualified class
	 * name of the auditor instance
	 * 
	 * @param auditorName Registered auditor name to get
	 * @return The auditor instance or null if none is registered
	 */
	public IHEAuditor getRegisteredAuditor(String auditorName)
	{
		return auditors.get(auditorName);
	}
	
	/**
	 * Register an auditor instance in the context registry with a 
	 * give name.  Generally the auditor name should be the fully-qualified
	 * class name of the auditor instance.
	 * 
	 * @param auditorName Name to register the auditor under
	 * @param auditor Auditor instance to register in the cache
	 */
	public void registerAuditor(String auditorName, IHEAuditor auditor)
	{
		auditors.put(auditorName, auditor);
	}
	

	/**
	 * Instantiate (or get from cache) an auditor instance for a given Class instance
	 * @param clazz The class instance to instantiate the auditor for
	 * @return An auditor instance
	 */
	public synchronized IHEAuditor getAuditor(Class<? extends IHEAuditor> clazz)
	{
		return getAuditor(clazz,true);
	}
	
	/**
	 * Instantiate (or get from cache) an auditor instance for a given Class instance
	 * @param clazz The class instance to instantiate the auditor for
	 * @param useContextAuditorRegistry Whether to use a cached auditor
	 * @return An auditor instance
	 */
	public synchronized IHEAuditor getAuditor(final Class<? extends IHEAuditor> clazz, boolean useContextAuditorRegistry)
	{
		if (null == clazz) {
			return null;
		}
		
		IHEAuditor auditor = null;
		if (useContextAuditorRegistry) {
			auditor = getRegisteredAuditor(clazz.getName());
			if (auditor != null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Reusing auditor " + auditor.toString());
				}
				return auditor;
			}
		}
		
		auditor = AuditorFactory.getAuditor(clazz, null, null);

		if (useContextAuditorRegistry) {
			registerAuditor(clazz.getName(), auditor);
		}
		
		return auditor;
	}
	
	/**
	 * Get an IHE Auditor instance from a fully-qualified class name
	 * 
	 * @param className Auditor class to use
	 * @return Auditor instance
	 */
	public IHEAuditor getAuditor(String className)
	{
		return getAuditor(className, true);
	}
	
	/**
	 * Get an IHE Auditor instance from a fully-qualified class name
	 * 
	 * @param className Auditor class to use
	 * @param useContextAuditorRegistry Whether to reuse cached auditors from context
	 * @return Auditor instance
	 */
	public IHEAuditor getAuditor(String className, boolean useContextAuditorRegistry)
	{
		return getAuditor(AuditorFactory.getAuditorClassForClassName(className),useContextAuditorRegistry);
	}

}
