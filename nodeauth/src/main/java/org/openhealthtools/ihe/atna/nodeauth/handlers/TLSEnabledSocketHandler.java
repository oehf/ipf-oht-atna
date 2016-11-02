/*******************************************************************************
 * Copyright (c) 2006,2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.openhealthtools.ihe.atna.nodeauth.handlers;

import org.openhealthtools.ihe.atna.nodeauth.NoSecurityDomainException;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomainManager;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of the Node Authentication's SocketHandler
 * to create of sockets secured using Transport Layer
 * Security version 1.  The TLS-enabled Socket Handler constructs
 * sockets using a specified keystore, truststore, and cipher suite
 * via a SecurityDomain.  These settings are used to mututally-authenticate
 * and negotiate the parties involved in the transaction.
 * <p>
 * This handler implements the IHE ITI-19 Node Authentication transaction.
 * <p>
 * To access this handler, use NodeAuthModule.getContext().getSocketHandler().
 *
 * @author <a href="mailto:rgd@us.ibm.com">Glenn Deen</a>
 * @author <a href="mailto:rstevens@us.ibm.com">Rick Stevens</a>
 * @author <a href="mailto:srrenly@us.ibm.com">Sondra Renly</a>
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @see org.openhealthtools.ihe.atna.nodeauth.SocketHandler
 */
public class TLSEnabledSocketHandler extends AbstractSecureSocketHandler {
    /**
     * Logger instance
     */
    private static final Logger logger = LoggerFactory.getLogger(TLSEnabledSocketHandler.class);

    /**
     * Default constructor for the TLS-enabled Socket Handler
     *
     * @param context The NodeAuth Module Context to use
     */
    public TLSEnabledSocketHandler(NodeAuthModuleContext context) {
        super(context);
    }

//	private void doTunnelHandshake(Socket tunnel, String host, int port)throws IOException
//	{
//		OutputStream out = tunnel.getOutputStream();
//		String msg = "CONNECT " + host + ":" + port + " HTTP/1.0\n"
//		+ "User-Agent: asdf"
//		+ "\r\n\r\n";
//		byte b[];
//		try {
//			/*
//			* We really do want ASCII7 -- the http protocol doesn't change
//			* with locale.
//			*/
//			b = msg.getBytes("ASCII7");
//		} catch (UnsupportedEncodingException ignored) {
//			/*
//			* If ASCII7 isn't there, something serious is wrong, but
//			* Paranoia Is Good (tm)
//			*/
//			b = msg.getBytes();
//		}
//		out.write(b);
//		out.flush();
//		
//		/*
//		* We need to store the reply so we can create a detailed
//		* error message to the user.
//		*/
//		byte reply[] = new byte[200];
//		int	replyLen = 0;
//		int	newlinesSeen = 0;
//		boolean	headerDone = false;	/* Done on first newline */
//		
//		InputStream	in = tunnel.getInputStream();
//		boolean	error = false;
//		
//		while (newlinesSeen < 2) {
//			int i = in.read();
//			if (i < 0) {
//				throw new IOException("Unexpected EOF from proxy");
//			}
//			if (i == '\n') {
//				headerDone = true;
//				++newlinesSeen;
//			} else if (i != '\r') {
//				newlinesSeen = 0;
//				if (!headerDone && replyLen < reply.length) {
//					reply[replyLen++] = (byte) i;
//				}
//			}
//		}
//
//		/*
//		 * Converting the byte array to a string is slightly wasteful
//		 * in the case where the connection was successful, but it's
//		 * insignificant compared to the network overhead.
//		 */
//		String replyStr;
//		try {
//			replyStr = new String(reply, 0, replyLen, "ASCII7");
//		} catch (UnsupportedEncodingException ignored) {
//			replyStr = new String(reply, 0, replyLen);
//		}
//
//		/* Look for 200 connection established */
//		if(replyStr.toLowerCase().indexOf("200 connection established") == -1){
//			throw new IOException("Unable to tunnel through "
//					+ host + ":" + port
//					+ ".  Proxy returns \"" + replyStr + "\"");
//		}
//
//		/* tunneling Handshake was successful! */
//	}


    /* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.nodeauth.handlers.AbstractSecureSocketHandler#createSecureSocket(java.lang.String, int, org.openhealthtools.ihe.atna.nodeauth.SecurityDomain)
     */
    protected SSLSocket createSecureSocket(String host, int port, SecurityDomain securityDomain, Socket nestedSocket) throws NoSecurityDomainException, NoSuchAlgorithmException, KeyManagementException, UnknownHostException, IOException {
        if (!CONTEXT.isTLSEnabled())
            throw new NoSuchAlgorithmException("TLS has been disabled for ATNA connections via " + SecurityDomainManager.class.getName() + ".setSetTLSEnabled(false)");

        SSLContext ctx = null;

        // Attempt to get an instance of the first support TLS protocol
        try {
            ctx = SSLContext.getInstance(securityDomain.getJdkTlsClientProtocols()[0]);
        } catch (NoSuchAlgorithmException e) {
            securityDomain.restoreSystemEnvironment();
            throw e;
        }

        // Initialize the instance for the key and trust stores
        try {
            KeyManager[] keyMgrs = securityDomain.getKeyManagers();
            TrustManager[] trustMgrs = securityDomain.getTrustManagers();

            ctx.init(keyMgrs, trustMgrs, null);
        } catch (KeyManagementException e) {
            throw e;
        }

        SSLSocketFactory factory = ctx.getSocketFactory();
        SSLSocket socket = null;

        if (logger.isDebugEnabled()) {
            String[] supportedSuites = factory.getSupportedCipherSuites();
            logger.debug("\n\nSupported cipher suites are:");
            for (int i = 0; i < supportedSuites.length; i++) {
                logger.debug("\t" + supportedSuites[i]);
            }
        }

        int retries = 0;

        Throwable cause = null;
        // Loop to get a connection or until we've exhausted number of retries
        while (retries < CONTEXT.getConfig().getSocketRetries()) {
            try {

                if (logger.isDebugEnabled()) {
                    logger.debug("Connecting to " + host + " on port " + port +
                            " (timeout: " + CONTEXT.getConfig().getConnectTimeout() + " ms) using factory " + factory.getClass().getName());
                }

                //SocketAddress address = new InetSocketAddress(host,port);
                if (nestedSocket instanceof Socket) {
                    socket = (SSLSocket) (factory.createSocket(nestedSocket, host, port, true));
                } else {
                    socket = (SSLSocket) (factory.createSocket(host, port));
                }

                //socket.connect(address, CONTEXT.getConfig().getConnectTimeout());
                // Set amount of time to wait on socket read before timing out
                socket.setSoTimeout(CONTEXT.getConfig().getSocketTimeout());
                socket.setKeepAlive(true);

                socket.setEnabledProtocols(securityDomain.getJdkTlsClientProtocols());
                socket.setEnabledCipherSuites(securityDomain.getCipherSuites());

                if (logger.isDebugEnabled()) {
                    logger.debug("\n\nEnabled Cipher suites for connection are: ");
                    String[] suites = socket.getEnabledCipherSuites();
                    for (int i = 0; i < suites.length; i++) {
                        logger.debug("\t" + suites[i]);
                    }
                }

                // Force the TLS handshake at this point so we can catch any authentication errors
                socket.startHandshake();
                break;

            } catch (SSLHandshakeException e) {
                logger.error("Handshake failed with server " + host + " on port " + port + " reason " + e.getLocalizedMessage(), e);
                try {
                    socket.close();
                } catch (IOException e1) {
                    logger.error("Error trying to close socket for " + host + " on port " + port + " reason " + e1.getLocalizedMessage(), e1);
                }
                //securityDomain.restoreSystemEnvironment();
                throw e;
            } catch (UnknownHostException e) {
                logger.error("Unable to establish connection to " + host + " on port " + port + " reason " + e.getLocalizedMessage(), e);
                //securityDomain.restoreSystemEnvironment();
                throw e;
            } catch (SocketException e) {
                logger.error("Error connecting to " + host + " on port " + port + ". Will retry in "
                        + CONTEXT.getConfig().getSocketRetryWait() / 1000 + " seconds.", e);
                retries++;
                cause = e;

                try {
                    Thread.sleep(CONTEXT.getConfig().getSocketRetryWait());
                    continue;
                } catch (InterruptedException ie) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Sleep awoken early");
                    }
                    continue;
                }

            } catch (IOException e) {
                logger.error("Error connecting to " + host + " on port " + port + ". Will retry in "
                        + CONTEXT.getConfig().getSocketRetryWait() / 1000 + " seconds." + " reason " + e.getLocalizedMessage(), e);
                retries++;
                cause = e;

                try {
                    Thread.sleep(CONTEXT.getConfig().getSocketRetryWait());
                    continue;
                } catch (InterruptedException ie) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Sleep awoken early");
                    }
                    continue;
                }
            }

        }

        // If socket retries exceed maximum allowed, throw an exception
        if (retries >= CONTEXT.getConfig().getSocketRetries()) {
            //securityDomain.restoreSystemEnvironment();
            logger.error("Secure Socket Connect Retries Exhausted.", cause);
            throw new ConnectException("Secure socket retries exhausted");
        }

        //securityDomain.restoreSystemEnvironment();
        return socket;
    }

    /**
     * TODO: To be removed in the 2009 post-Connectathon Branch
     */

	/* (non-Javadoc)
     * @see org.openhealthtools.ihe.atna.nodeauth.SocketHandler#getInputStream(java.net.URI, org.openhealthtools.ihe.atna.nodeauth.SecurityDomain)
	 */
    public InputStream getInputStream(URI uri, SecurityDomain securityDomain) throws NoSecurityDomainException, MalformedURLException, IOException, NoSuchAlgorithmException {
        final NodeAuthModuleContext context = NodeAuthModuleContext.getContext();
        if (!context.isTLSEnabled() && !context.isNonTLSConnectionsPermitted()) {
            throw new NoSuchAlgorithmException("TLS has been disabled for ATNA connections");
        }

        boolean tlsURI = uri.getScheme().equalsIgnoreCase("https");
        boolean useTLS = tlsURI && context.isTLSEnabled() || !context.isNonTLSConnectionsPermitted();

        if (useTLS && null == securityDomain) {
            throw new NoSecurityDomainException(uri, "Security domain provided is null");
        }

        if (useTLS) {
            securityDomain.setDomainEnvironment();
        }
        InputStream is = null;
        try {
            URLConnection connection = uri.toURL().openConnection();

            if (useTLS && !securityDomain.doDomainSpoofCheck()) {
                ((HttpsURLConnection) connection).setHostnameVerifier((urlHostName, session) -> true);
            }

            is = connection.getInputStream();
            if (useTLS) {
                logger.info("Secure connection successfully made using TLS to " + uri.toString());
            } else {
                logger.info("Unsecure connection successfully made to " + uri.toString());
            }
        } catch (IOException e) {
            if (useTLS) securityDomain.restoreSystemEnvironment();
            throw e;
        }

        if (useTLS) securityDomain.restoreSystemEnvironment();
        return is;
    }


    /*
     * Note that this method always uses the system environment (specifically https.protocols)
     * in order to derive the HTTPS parameters. As such {@link SecurityDomain#SET_DOMAIN_ENVIRONMENT} must be
     * set to true if these parameters are not configured via system properties anyway.
     *
     * @see org.openhealthtools.ihe.atna.nodeauth.SocketHandler#getInputStream(java.net.URI)
     */
    public InputStream getInputStream(URI uri) throws Exception {
        boolean tlsURI = uri.getScheme().equalsIgnoreCase("https");
        SecurityDomain securityDomain = null;
        if (tlsURI) {
            final NodeAuthModuleContext context = NodeAuthModuleContext.getContext();
            securityDomain = context.getSecurityDomainManager().getSecurityDomain(uri);
        }
        return getInputStream(uri, securityDomain);
    }

}
