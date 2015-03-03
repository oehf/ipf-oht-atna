/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.openhealthtools.ihe.atna.nodeauth.utils;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509KeyManager;

import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for X509 Key Managers to allow custom selection of key to use in 
 * handshake from a user-defined alias.  If a key is not defined, then the 
 * key selection method defaults to the specific X509 Key Manager's implementation
 * routine.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 *
 */
public class AliasSensitiveX509KeyManager implements X509KeyManager 
{
	/**
	 * 
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(AliasSensitiveX509KeyManager.class);
	/**
	 * 
	 */
	private SecurityDomain domain;
	
	/**
	 * 
	 */
	private X509KeyManager parent;
	
	/**
	 * @param domain
	 * @param parent
	 */
	public AliasSensitiveX509KeyManager(SecurityDomain domain, X509KeyManager parent)
	{
		this.parent = parent;
		this.domain = domain;
	}
	
	/* (non-Javadoc)
	 * @see javax.net.ssl.X509KeyManager#chooseClientAlias(java.lang.String[], java.security.Principal[], java.net.Socket)
	 */
	public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) 
	{
		// If not security domain is available, or the preferred alias is NULL, 
		// then default to the nested key manager's process for selecting the keystore
		if (null == domain 
				|| domain.getPreferredKeyAlias() == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("No preferred key alias defined.  Defaulting to JSSE certificate selection.");
			}
			return parent.chooseClientAlias(keyType, issuers, socket);
		}
		
		String preferredAlias = domain.getPreferredKeyAlias();
		
		String alias = null;
		if (keyType != null && keyType.length > 0) {
			for (int i=0; i<keyType.length; i++) {
				alias = chooseClientAliasForKey(preferredAlias, keyType[i], issuers, socket);
				if (alias != null && !"".equals(alias)) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("Found valid keystore alias: " + alias);
					}
					return alias;
				}
			}
		}
		
		LOGGER.warn("The requested key alias " + preferredAlias + " was not found in the keystore.  No certificate selected.  The transaction will probably fail.");
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.net.ssl.X509KeyManager#chooseServerAlias(java.lang.String, java.security.Principal[], java.net.Socket)
	 */
	public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) 
	{
		return parent.chooseServerAlias(keyType, issuers, socket);
	}

	/* (non-Javadoc)
	 * @see javax.net.ssl.X509KeyManager#getCertificateChain(java.lang.String)
	 */
	public X509Certificate[] getCertificateChain(String alias) 
	{
		return parent.getCertificateChain(alias);
	}

	/* (non-Javadoc)
	 * @see javax.net.ssl.X509KeyManager#getClientAliases(java.lang.String, java.security.Principal[])
	 */
	public String[] getClientAliases(String keyType, Principal[] issuers) 
	{
		return parent.getClientAliases(keyType, issuers);
	}

	/* (non-Javadoc)
	 * @see javax.net.ssl.X509KeyManager#getPrivateKey(java.lang.String)
	 */
	public PrivateKey getPrivateKey(String alias) 
	{
		return parent.getPrivateKey(alias);
	}

	/* (non-Javadoc)
	 * @see javax.net.ssl.X509KeyManager#getServerAliases(java.lang.String, java.security.Principal[])
	 */
	public String[] getServerAliases(String keyType, Principal[] issuers) 
	{
		return parent.getServerAliases(keyType, issuers);
	}
	
	/**
	 * Attempts to find a keystore 
	 * @param keyType
	 * @param issuers
	 * @param socket
	 * @return
	 */
	private String chooseClientAliasForKey(String preferredAlias, String keyType, Principal[] issuers, Socket socket)
	{
		String[] aliases = getClientAliases(keyType, issuers);

		if (aliases != null && aliases.length > 0) {
			for (int i=0; i<aliases.length;i++) {
				if (preferredAlias.equals(aliases[i])) {
					return aliases[i];
				}
			}
		}
		return null;
	}
}
