/*******************************************************************************
 * Copyright (c) 2006,2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.nodeauth;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread safe repository of configurations used by the ATNA Node Authentication Context
 * <br>This class is a singleton, all methods are static methods against the singleton instance.
 * No user initiated initialization is required.
 * 
 * <p>When the class is loaded, it will examine the System environment looking for the standard SSL/TLS environment variarbles. If 
 * present, they will be use to create a default SecurityDomain 
 * <ul>
 * <li>javax.net.ssl.keyStore
 * <li>javax.net.ssl.keyStorePassword
 * </ul>
 * <ul>Optional settings
 * <li>javax.net.ssl.trustStore
 * <li>javax.net.ssl.trustStorePassword
 * <li>http.ciphersuites=list of suites to use. IHE recommends
 * TLS_RSA_WITH_NULL_SHA and TLS_RSA_WITH_AES_CBC_SHA<br>
 * <li>javax.net.debug
 * </ol>
 * 
 * <br>This class also provides the control point to force TLS NOT TO BE USED for any connection. This is 
 * done with the {@link #setTLSEnabled(boolean)} method. Unless set, the default is to allow TLS.
 * 
 * 
 * @author Glenn Deen  <a href="mailto:glenn@almaden.ibm.com">glenn@almaden.ibm.com</a>
 *
 * @since OHF 1.0.0
 */
public class SecurityDomainManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityDomainManager.class);

    private Map<String,SecurityDomain> securityDomains = Collections.synchronizedMap(new HashMap<String,SecurityDomain>());

    private Map<String,String> uriToSecurityDomain = Collections.synchronizedMap(new HashMap<String,String>());

    private boolean isDefaultSecurityDomainRegistered = false;
    
	/**
	 * 
	 */
	public SecurityDomainManager() {}

	/**
	 * Registers the SecurityDomain object with the ConfigurationManager.  
	 * <p>About the Security Domain name
	 * <br>The security domain name {@link SecurityDomain#getName() is used as a key by subsequent calls such as {@link #registerURItoSecurityDomain(URI, String)}
	 * to associate the security properties registered here. 
	 * <ul>
	 * <li>The name DEFAULT is a special name name which specifies the name which will be used for a URI in the absence
	 * of a explicit name registered the URI
	 * <li>Other than DEFAULT any other name is allowed, and is up to the calling application to choose.
	 * </ul>  
	 * @param securityDomain domain to add to the configuration manager
	 * @return
	 */
	public void registerSecurityDomain(SecurityDomain securityDomain) 
	{
		securityDomains.put(securityDomain.getName(), securityDomain);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Security Domain "+securityDomain.getName()+" has been successfully registered to the configuration manager.");
	}
	
	/**
	 * Registers the properies file to be used as the default security domain. The security domain object is also registered under its
	 * given name taken from the {@link SecurityDomain#getName()} method. 
	 * <p>About the Security Domain name
	 * <br>The security domain name is used as a key by subsequent calls such as {@link #registerURItoSecurityDomain(URI, String)}
	 * to associate the security properties registered here. 
	 * <ul>
	 * <li>The name DEFAULT is a special domain name which specifies the domain which will be used for a URI in the absence
	 * of a explicit domain registered the URI
	 * <li>Other than DEFAULT any other name is allowed, and is up to the calling application to choose.
	 * </ul>  
	 * @param securityDomain
	 * @return
	 */
	public void registerDefaultSecurityDomain(SecurityDomain securityDomain) 
	{
		securityDomains.put(SecurityDomain.DEFAULT_SECURITY_DOMAIN,securityDomain);
		registerSecurityDomain(securityDomain);
		isDefaultSecurityDomainRegistered = true;
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Security domain "+securityDomain.getName()+" has been successfully registered as the Default security domain.");

	}
	/**
	 * Registers the association of the given URI to the named security domain. Registration is only needed when a URI needs to use
	 * a security domain other than the default domain.
	 * <br>If the URI was previously registered with another domain that association is replaced with this new one.
	 * @param uri URI to register, may not be null
	 * @param name of SecurityDomain to associate
	 * @throws URISyntaxException 
	 * @throws {@link IllegalArgumentException If the specified domain doesn't exist, or if the URI is null
	 */
	public void registerURItoSecurityDomain(URI uri, String name) throws URISyntaxException
	{
		if (uri == null) throw new IllegalArgumentException("URI parameter cannot be null");
		if (! securityDomains.containsKey(name) ) throw new IllegalArgumentException("Security domain "+name+" is not a configured security domain.");
		if (uriToSecurityDomain.containsKey(uri)) uriToSecurityDomain.remove(uri);
		uriToSecurityDomain.put(formatKey(uri), name);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Security domain "+name+" has been registered for URI "+uri.toString());
	}
	

	/**
	 * Removes the URI from registered set of URI's
	 * @param uri
	 */
	public void unregisterURItoSecurityDomain(URI uri)
	{
		try {
			uriToSecurityDomain.remove(formatKey(uri));
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Unregisters the default security domain, if it exists
	 */
	public void unregisterDefaultSecurityDomain()
	{
		try {
			uriToSecurityDomain.remove(SecurityDomain.DEFAULT_SECURITY_DOMAIN);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Returns the security domain with the given name.  Returns null if so matching domain could be found.
	 * @param name
	 * @return
	 */
	public synchronized SecurityDomain getSecurityDomain(String name) 
	{
		return securityDomains.get(name);
	}
	
	/**
	 * Returns the SecurityDomain object registered as associated with the URI. 
	 * <br>If no association has been registered for the URI, then the DEFAULT SecurityDomain is 
	 * returned, if a DEFAULT domain has been registered.
	 *
	 * @param uri
	 * @return SecurityDomain associated with the URI
	 * @throws NoSecurityDomainException  if no security name has been associated with the URI
	 * @throws URISyntaxException 
	 */
	public synchronized SecurityDomain getSecurityDomain(URI uri) throws NoSecurityDomainException, URISyntaxException
	{
		return getSecurityDomain(uri.getHost(), uri.getPort());
	}
	
	/**
	 * @param host
	 * @param port
	 * @return
	 * @throws NoSecurityDomainException
	 * @throws URISyntaxException
	 */
	public synchronized SecurityDomain getSecurityDomain(String host, int port) throws NoSecurityDomainException, URISyntaxException
	{
		String name = uriToSecurityDomain.get(formatKey(host,port));
		if (name == null)
		{	
			if (!isDefaultSecurityDomainRegistered) {
				registerDefaultSecurityDomain();
			}
			
			LOGGER.debug("No explicit Security Domain was registered for host "+host+" and port "+ port);
			if (securityDomains.containsKey(SecurityDomain.DEFAULT_SECURITY_DOMAIN)) {
				name = SecurityDomain.DEFAULT_SECURITY_DOMAIN;
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Default Security Domain ["+name+"] has been found and will be used for host "+host+" and port "+ port);
				}
			} else {
				throw new NoSecurityDomainException(null,"No DEFAULT Security Domain available for host "+host+" and port "+ port);
			}
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Security Domain ["+name+"] has been found and will be used for host "+host+" and port "+ port);
			}	
		}
		
		SecurityDomain domain = securityDomains.get(name);

		if (domain == null) {		
			String msg="Security Domain ["+name+"] was set for host "+host+" and port "+ port+
			           " but it could not be located in the set of registered domains.";
			LOGGER.error(msg);
			throw  new NoSecurityDomainException(null,msg);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Security Domain ["+name+"] successfully retrieved.");
		}
		return domain;
	}

	/**
	 * Returns the names of the registered security domains.
	 * <br>The returned set is a read only unmodifiable set
	 * @return set 
	 */
	public Set<String> getRegisteredSecurityDomains()
	{                             
		return  Collections.unmodifiableSet(securityDomains.keySet());
	}
	
	/**
	 * Converts a well-formed URI containing a hostname and port into 
	 * string which allows for lookups in the Security Domain table
	 * @param uri URI to convert
	 * @return A string with "host:port" concatenated
	 * @throws URISyntaxException
	 */
	public String formatKey(URI uri) throws URISyntaxException
	{
		if (uri == null) {
			throw new URISyntaxException("","URI specified is null");
		}
		return formatKey(uri.getHost(), uri.getPort());
	}
	
	/**
	 * Concatenates a host string and port integer into a "host:port" string
	 * @param host
	 * @param port
	 * @return
	 * @throws URISyntaxException
	 */
	public String formatKey(String host, int port) throws URISyntaxException
	{
		if (port < 1) {
			throw new URISyntaxException("","The port value must be greater than zero");
		}
		
		if (!"".equals(host)) {
			return host + ":" + port;
		} else {
			throw new URISyntaxException("","The host cannot be null");
		}
	}
	
	/**
	 * 
	 */
	private void registerDefaultSecurityDomain()
	{
		if (System.getProperty(SecurityDomain.JAVAX_NET_SSL_KEYSTORE) != null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("KeyStore information has been detected in the system environment variables. Creating a default security domain with them.");
			}
			try {
				SecurityDomain domain = new SecurityDomain(SecurityDomain.DEFAULT_SECURITY_DOMAIN,System.getProperties());
				registerDefaultSecurityDomain(domain);
				isDefaultSecurityDomainRegistered = true;
			} catch (SecurityDomainException e) {
				LOGGER.error("Error initializing default keystore. No default SecurityDomain defined.", e);
			}
		} else {
			LOGGER.warn("No default security domain defined.");
		}
	}
	
}
