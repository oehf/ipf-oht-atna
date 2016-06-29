/*******************************************************************************
 * Copyright (c) 2006,2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.nodeauth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Properties;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

import org.openhealthtools.ihe.atna.nodeauth.utils.AliasSensitiveX509KeyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains Keystore and Truststore instances for use by secure socket and https connections, as well as 
 * protocol, debug, and cipher suite choices for secure connections.
 * <br>
 * <br>SecurityDomain objects are managed via the {@link SecurityDomainManager}
 *
 * @author Glenn Deen  <a href="mailto:glenn@almaden.ibm.com">glenn@almaden.ibm.com</a>
 *
 * @since OHF 0.1.0
 */
public class SecurityDomain implements Cloneable {
    private static final Logger logger = LoggerFactory.getLogger(SecurityDomain.class);

    public static final String JAVAX_NET_DEBUG = "javax.net.debug";
    public static final String JAVAX_NET_SSL_TRUSTSTORE = "javax.net.ssl.trustStore";
    public static final String JAVAX_NET_SSL_KEYSTORE = "javax.net.ssl.keyStore";
    public static final String JAVAX_NET_SSL_TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword";
    public static final String JAVAX_NET_SSL_KEYSTORE_PASSWORD = "javax.net.ssl.keyStorePassword";
    public static final String HTTPS_CIPHERSUITES = "https.ciphersuites";
    public static final String HTTPS_PROTOCOLS = "https.protocols";

    private static final String[] ENVNAMES = {
            JAVAX_NET_DEBUG,
            JAVAX_NET_SSL_KEYSTORE, JAVAX_NET_SSL_KEYSTORE_PASSWORD,
            JAVAX_NET_SSL_TRUSTSTORE, JAVAX_NET_SSL_TRUSTSTORE_PASSWORD,
            HTTPS_CIPHERSUITES, HTTPS_PROTOCOLS
    };


    private static final String[] SECURITY_STORE_FORMATS = new String[]{
            KeyStore.getDefaultType(),
            "jks",
            "pkcs12"
    };

    /**
     * TLS_RSA_WITH_AES_128_CBC_SHA
     */
    public static String TLS_RSA_WITH_AES_128_CBC_SHA = "TLS_RSA_WITH_AES_128_CBC_SHA";

    /**
     * TLS_RSA_WITH_NULL_SHA
     */
    public static String TLS_RSA_WITH_NULL_SHA = "SSL_RSA_WITH_NULL_SHA";

    /**
     * Default CIPHER SUITE which will be used unless overriddent:
     * "TLS_RSA_WITH_NULL_SHA,TLS_RSA_WITH_AES_128_CBC_SHA"
     */
    public static String DEFAULT_HTTPS_CIPHERSUITES = TLS_RSA_WITH_NULL_SHA + "," + TLS_RSA_WITH_AES_128_CBC_SHA;

    /**
     * Default https.protocols value unless specified otherwise: "TLSv1"
     */
    public static String DEFAULT_HTTPS_PROTOCOLS = "TLSv1";

    /**
     * Name of the default security domain
     */
    public static String DEFAULT_SECURITY_DOMAIN = "_DEFAULT_";

    /**
     *
     */
    private String name;

    /**
     *
     */
    KeyManagerFactory keyManagerFactory = null;

    /**
     *
     */
    KeyStore keyStore = null;

    /**
     *
     */
    TrustManagerFactory trustManagerFactory = null;

    /**
     *
     */
    KeyStore trustStore = null;

    /**
     *
     */
    private KeyManager[] keyManagers;

    /**
     *
     */
    String debug = null;

    /**
     *
     */
    String systemDebug = null;

    /**
     *
     */
    Properties domainProperties = null;

    /**
     *
     */
    Properties systemProperties = null;

    /**
     *
     */
    private boolean domainSpoofCheck = false;

    /**
     *
     */
    private String preferredKeyAlias;

    /**
     *
     */
    protected boolean keystoreInitialized = false;

    /**
     *
     */
    protected boolean truststoreInitialized = false;

    /**
     * Creates a security domain that uses the default key alias from the keystore
     * @param name
     * @param properties
     * @throws SecurityDomainException
     */
    public SecurityDomain(String name, final Properties properties) throws SecurityDomainException {
        this(name, null, properties);
    }

    /**
     * Properites file requires the following properties to be set<br>
     * Key Store:<br>
     * javax.net.ssl.keyStore=XXX<br>
     * javax.net.ssl.keyStorePassword=XXX  (if not present, defaults to "")<br>
     * <br>
     * optional:<br>
     * Trust Store:<br>
     * javax.net.ssl.trustStore=XXX If this is not present, then no truststore is initialized<br>
     * javax.net.ssl.trustStorePassword=XXX - if not present, defaults to ""
     * http.ciphersuites=XXX list of suites to use. IHE recommends
     * TLS_RSA_WITH_NULL_SHA and TLS_RSA_WITH_AES_CBC_SHA<br>
     *
     * http.protocols=XXX comma seperarate list of protocols to use. IHE recommends TLSv1
     * <br>
     * Debugging options:<br>
     * javax.net.debug<br>
     *
     * @param name name of the name
     * @param preferredAlias Keystore alias to use when doing secure handshake
     * @param properties
     * @throws SecurityDomainException
     */
    public SecurityDomain(String name, String preferredAlias, final Properties properties) throws SecurityDomainException {
        if (name == null || name.trim().length() < 1)
            throw new IllegalArgumentException("SecurityDomain(String name, Properties properties) - name cannot be null or blank");

        if (properties == null)
            throw new IllegalArgumentException("SecurityDomain(String name, Properties properties) - properties cannot be null");

        this.name = name;

        this.preferredKeyAlias = preferredAlias;

        if (logger.isDebugEnabled())
            logger.debug("Begin: Security name " + name + " setup.");

        setProperties(properties);
    }


    /**
     * @param properties
     * @throws SecurityDomainException
     */
    public void setProperties(Properties properties) throws SecurityDomainException {
        domainProperties = cloneProperites(properties);
        // unless overridding in the properties, use the IHE required TLS ciphers
        if (domainProperties.getProperty(HTTPS_CIPHERSUITES) == null) {
            domainProperties.setProperty(HTTPS_CIPHERSUITES, DEFAULT_HTTPS_CIPHERSUITES);
        }
        if (domainProperties.getProperty(HTTPS_PROTOCOLS) == null) {
            domainProperties.setProperty(HTTPS_PROTOCOLS, DEFAULT_HTTPS_PROTOCOLS);
        }
        initStores();
        if (logger.isDebugEnabled())
            logger.debug("Success: Security name " + name + " configured.");
    }

    /**
     * Sets the System environment to support the Security Domain's settings. This is used for protocols which only read their crypto
     * settings via environment variables.
     * <br>Also see {@link #restoreSystemEnvironment()}
     */
    public void setDomainEnvironment() {
        if (logger.isDebugEnabled()) logger.debug("Setting System environment properties to Security Domain values");
        systemProperties = System.getProperties();
        for (int i = 0; i < ENVNAMES.length; i++) {
            setOrClearSystemProperties(ENVNAMES[i], domainProperties);
        }
    }

    /**
     * Restores the System environmnet to what it was before
     * <br>Also see {@link #setDomainEnvironment()}
     */
    public void restoreSystemEnvironment() {
        if (systemProperties == null)
            throw new NullPointerException("Must call SecurityDomain.setDomainEnvironment() first to record existing System environment");
        if (logger.isDebugEnabled())
            logger.debug("Swapping back to original System environment properties values");
        for (int i = 0; i < ENVNAMES.length; i++) {
            setOrClearSystemProperties(ENVNAMES[i], systemProperties);
        }
    }

    private Properties cloneProperites(Properties source) {
        Properties clone = new Properties();
        String v;
        for (int i = 0; i < ENVNAMES.length; i++) {
            v = source.getProperty(ENVNAMES[i]);
            if (v != null) clone.setProperty(ENVNAMES[i], v);
        }
        return clone;
    }


    private void setOrClearSystemProperties(String name, Properties source) {
        String v = source.getProperty(name);
        if (v != null) {
            if (logger.isDebugEnabled()) {
                if (!name.contains("assword")) {
                    logger.debug("System property " + name + " set to " + v);
                } else {
                    logger.debug("System property " + name + " set to XXX (password not shown)");
                }

            }
            System.setProperty(name, v);
        } else {
            System.setProperty(name, "");
            if (logger.isDebugEnabled()) logger.debug("System property " + name + " cleared.");
        }
    }

    /**
     * @param truststoreInputStream
     * @param truststorePassword
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    protected void initTrustStore(InputStream truststoreInputStream, char[] truststorePassword) throws SecurityDomainException, NoSuchAlgorithmException, CertificateException, IOException {
        if (null == truststoreInputStream) {
            truststoreInitialized = true;
            logger.warn("Truststore input stream is null.  Using JVM default trust store.");
            return;
        }

        for (int typeIndex = 0; typeIndex < SECURITY_STORE_FORMATS.length; typeIndex++) {
            String storeType = SECURITY_STORE_FORMATS[typeIndex];
            try {
                truststoreInputStream.reset();
                // Create the truststore
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(truststoreInputStream, truststorePassword);
                // Create and init the trust manager
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
                // If we're here, then it was successful
                truststoreInitialized = true;

                if (logger.isDebugEnabled()) {
                    logger.debug("Trust store for security domain " + name + " initialized successfully");
                }
                break;
            } catch (KeyStoreException kse) {
                logger.warn("Unable to initialize trust store with type " + storeType, kse);
            } catch (IOException ioe) {
                logger.warn("Error while loading truststore", ioe);
            }
        }

        if (!truststoreInitialized) {
            logger.error("Error initializing the trust manager. Trust store type cannot be loaded.");
            throw new SecurityDomainException("Error initializing the trust manager. Trust store type cannot be loaded.");
        }
    }


    /**
     * @param keystoreInputStream
     * @param keystorePassword
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws IOException
     */
    protected void initKeyStore(InputStream keystoreInputStream, char[] keystorePassword) throws SecurityDomainException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, IOException {
        for (int typeIndex = 0; typeIndex < SECURITY_STORE_FORMATS.length; typeIndex++) {
            String storeType = SECURITY_STORE_FORMATS[typeIndex];
            try {
                keystoreInputStream.reset();
                // Create the keystore
                keyStore = KeyStore.getInstance(storeType);
                keyStore.load(keystoreInputStream, keystorePassword);
                // Create and init the key manager
                keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, keystorePassword);
                // Run the X509 checker and add aliases to the keys
                fixKeyManagers();
                // If we're here, success
                keystoreInitialized = true;

                if (logger.isDebugEnabled()) {
                    logger.debug("Key store for security domain " + name + " initialized successfully");
                }
                break;
            } catch (KeyStoreException kse) {
                logger.warn("Unable to initialize key store with type " + storeType, kse);
            } catch (IOException ioe) {
                logger.warn("IO Error while loading keystore", ioe);
            }
        }

        if (!keystoreInitialized) {
            logger.error("Error initializing the key manager. Key store type cannot be loaded.");
            throw new SecurityDomainException("Error initializing the key manager. Key store type cannot be loaded.");
        }
    }


    /**
     * Initialize the key and trust stores
     * @throws SecurityDomainException
     */
    protected void initStores() throws SecurityDomainException {
        setDomainEnvironment();

        char[] keyStorePasswd = domainProperties.getProperty(JAVAX_NET_SSL_KEYSTORE_PASSWORD, "").toCharArray();
        char[] trustStorePasswd = domainProperties.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD, "").toCharArray();

        String keyStoreName = domainProperties.getProperty(JAVAX_NET_SSL_KEYSTORE, null);
        if (logger.isDebugEnabled())
            logger.debug("Name of key store for security domain " + name + " is " + keyStoreName);
        if (keyStoreName == null) {
            restoreSystemEnvironment();
            throw new SecurityDomainException(name, "Key Store file is undefined");
        }

        String trustStoreName = domainProperties.getProperty(JAVAX_NET_SSL_TRUSTSTORE, null);
        if (logger.isDebugEnabled()) {
            if (trustStoreName != null) {
                logger.debug("Name of trust store for security domain " + name + " is " + trustStoreName);
            } else {
                logger.debug("Name of trust store for security domain " + name + " was not defined. Default trust store from JVM will be used");
            }
        }

        // Key Store
        try {
            InputStream keystoreInputStream = preBufferInputStream(new FileInputStream(keyStoreName));
            initKeyStore(keystoreInputStream, keyStorePasswd);
            if (logger.isDebugEnabled())
                logger.debug("Key store for security domain " + name + " initialized successfully");
        } catch (NoSuchAlgorithmException e) {
            String msg = "Error: Key Store Manager Algorithm " + KeyManagerFactory.getDefaultAlgorithm() + " is not supported. " + e.getLocalizedMessage();
            logger.error(msg);
            restoreSystemEnvironment();
            throw new SecurityDomainException(name, msg, e);
        } catch (CertificateException | IOException | UnrecoverableKeyException e) {
            String msg = "Error loading key store file " + keyStoreName + ".  " + e.getLocalizedMessage();
            logger.error(msg);
            restoreSystemEnvironment();
            throw new SecurityDomainException(name, msg, e);
        }

        if (trustStoreName != null) {
            // Trust Store
            try {
                InputStream trustoreInputStream = preBufferInputStream(new FileInputStream(trustStoreName));
                initTrustStore(trustoreInputStream, trustStorePasswd);
                if (logger.isDebugEnabled())
                    logger.debug("Trust store for security domain " + name + " initialized successfully");

            } catch (NoSuchAlgorithmException e) {
                String msg = "Error: Key Store Manager Algorithm " + KeyManagerFactory.getDefaultAlgorithm() + " is not supported. " + e.getLocalizedMessage();
                logger.error(msg);
                restoreSystemEnvironment();
                throw new SecurityDomainException(name, msg, e);
            } catch (CertificateException e) {
                String msg = "Error loading trust store file " + trustStoreName + ".  " + e.getLocalizedMessage();
                logger.error(msg);
                restoreSystemEnvironment();
                throw new SecurityDomainException(name, msg, e);
            } catch (IOException e) {
                String msg = "Error loading trust store file " + trustStoreName + ".  " + e.getLocalizedMessage();
                logger.error(msg);
                restoreSystemEnvironment();
                throw new SecurityDomainException(name, msg, e);
            }
        }
        restoreSystemEnvironment();
    }

    /**
     * If a keystore alias is defined, then override the key manager assigned
     * to with an alias-sensitive wrapper that selects the proper key from your
     * assigned key alias.
     */
    private void fixKeyManagers() {
        // If the key manager factory is null, do not continue
        if (null == keyManagerFactory || null == keyManagerFactory.getKeyManagers()) {
            return;
        }
        KeyManager[] defaultKeyManagers = keyManagerFactory.getKeyManagers();
        KeyManager[] newKeyManagers = new KeyManager[defaultKeyManagers.length];

        KeyManager mgr = null;
        for (int i = 0; i < defaultKeyManagers.length; i++) {
            mgr = defaultKeyManagers[i];
            // If we're looking at an X509 Key manager, then wrap it in our
            // alias-selective manager
            if (mgr instanceof X509KeyManager) {
                mgr = new AliasSensitiveX509KeyManager(this, (X509KeyManager) mgr);
            }
            newKeyManagers[i] = mgr;
        }

        keyManagers = newKeyManagers;
    }


    /**
     * @return
     */
    public KeyStore getKeyStore() {
        return keyStore;
    }


    /**
     * @return
     */
    public KeyStore getTrustStore() {
        return trustStore;
    }

    /**
     * @return
     */
    public KeyManager[] getKeyManagers() {
        if (null == keyManagers) {
            return keyManagerFactory.getKeyManagers();
        }
        return keyManagers;
    }

    /**
     * @return
     */
    public TrustManager[] getTrustManagers() {
        return trustManagerFactory.getTrustManagers();
    }


    /**
     * Returns the key manager factory, to retrieve key managers for this security domain
     * @deprecated
     * @return
     */
    public KeyManagerFactory getKeyManagerFactory() {
        return keyManagerFactory;
    }


    /**
     * Returns the trust manager factory, to retrieve trust managers for this security domain
     * @deprecated
     * @return
     */
    public TrustManagerFactory getTrustManagerFactory() {
        return trustManagerFactory;
    }

    /**
     * Returns the name of the security domain
     * @return
     */
    public String getName() {
        return this.name;
    }

    public String[] getCipherSuites() {
        String suitestring = domainProperties.getProperty(HTTPS_CIPHERSUITES);
        String[] suites = suitestring.split(",");
        return suites;
    }

    public String getPreferredKeyAlias() {
        return preferredKeyAlias;
    }

    public void setPreferredKeyAlias(String preferredKeyAlias, boolean validate) throws SecurityDomainException {
        if (validate) {
            boolean found = false;
            try {
                Enumeration<String> keystoreAliases = getKeyStore().aliases();
                while (keystoreAliases.hasMoreElements()) {
                    if (preferredKeyAlias.equals(keystoreAliases.nextElement())) {
                        found = true;
                        break;
                    }
                }

            } catch (Exception e) {
                throw new SecurityDomainException(getName(), "Validation failed.  Unable to find the alias " + preferredKeyAlias + " in the Security Domain keystore.", e);
            }
            if (!found) {
                throw new SecurityDomainException(getName(), "Validation failed.  Unable to find the alias " + preferredKeyAlias + " in the Security Domain keystore.");
            }
        }

        this.preferredKeyAlias = preferredKeyAlias;
    }

    public boolean doDomainSpoofCheck() {
        return domainSpoofCheck;
    }

    public void setDomainSpoofCheck(boolean check) {
        domainSpoofCheck = check;
    }


    private static ByteArrayInputStream preBufferInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int buf;
        while ((buf = in.read()) != -1) {
            baos.write(buf);
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public SecurityDomain clone() {
        SecurityDomain clone = null;
        try {
            clone = (SecurityDomain) super.clone();
            clone.fixKeyManagers();
        } catch (Exception e) {

        }
        return clone;
    }

    public SecurityDomain clone(final String newName) {
        SecurityDomain clone = this.clone();
        if (clone != null) {
            clone.name = newName;
        }
        return clone;
    }

}
