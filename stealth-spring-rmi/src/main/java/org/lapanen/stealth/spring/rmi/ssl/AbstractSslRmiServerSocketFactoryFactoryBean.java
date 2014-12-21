package org.lapanen.stealth.spring.rmi.ssl;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import com.google.common.base.Preconditions;

/**
 * Creates and configures an {@link javax.rmi.ssl.SslRMIServerSocketFactory} by configuring
 * the underlying {@link javax.net.ssl.SSLContext}. See property setters for details.
 *
 * @author pedro@lapanen.org
 */
public abstract class AbstractSslRmiServerSocketFactoryFactoryBean implements FactoryBean<SslRMIServerSocketFactory> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSslRmiServerSocketFactoryFactoryBean.class);

    public static final String DEFAULT_KEY_MANAGER_ALGORITHM = "SunX509";
    public static final String DEFAULT_SSL_PROTOCOL = "SSL";

    private String keyManagerAlgorithm = DEFAULT_KEY_MANAGER_ALGORITHM;
    private String sslProtocol = DEFAULT_SSL_PROTOCOL;
    private SecureRandom secureRandom = new SecureRandom();
    private final String keyStorePassword;
    private String keyStoreType = KeyStore.getDefaultType();
    private boolean needsClientAuth = false;

    public AbstractSslRmiServerSocketFactoryFactoryBean(final String keyStorePassword) {
        this.keyStorePassword = Preconditions.checkNotNull(keyStorePassword, "Keystore password must not be null");
    }

    @Override
    public SslRMIServerSocketFactory getObject() throws Exception {
        final SSLContext sslContext = SSLContext.getInstance(sslProtocol);
        LOG.debug("Obtaining instance of KeyManagerFactory with algorithm '{}'", keyManagerAlgorithm);
        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(keyManagerAlgorithm);
        LOG.debug("Creating KeyStore of type '{}'", keyStoreType);
        final KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        loadKeyStore(keyStore, getPassword());
        LOG.debug("Initialising KeyManagerFactory");
        keyManagerFactory.init(keyStore, getPassword());
        LOG.debug("Creating key managers");
        final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
        LOG.debug("Initialising SSLContext with KeyManagers {}, null TrustManagers and SecureRandom {}", Arrays.asList(keyManagers), secureRandom);
        sslContext.init(keyManagers, null, secureRandom);
        LOG.debug("Returning SslRMIServerSocketFactory with SSLContext {}", sslContext);
        return new SslRMIServerSocketFactory(sslContext, null, null, needsClientAuth);
    }

    @Override
    public Class<?> getObjectType() {
        return SslRMIServerSocketFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * Set the {@link java.security.SecureRandom} for the {@link javax.net.ssl.SSLContext} to use.
     *
     * @param secureRandom
     */
    public void setSecureRandom(final SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    /**
     * Default is {@value #DEFAULT_KEY_MANAGER_ALGORITHM}.
     *
     * @param algorithm
     */
    public void setKeyManagerAlgorithm(final String algorithm) {
        this.keyManagerAlgorithm = algorithm;
    }

    /**
     * @param needsClientAuth
     *
     * @see javax.rmi.ssl.SslRMIServerSocketFactory#getNeedClientAuth()
     */
    public void setNeedsClientAuth(final boolean needsClientAuth) {
        this.needsClientAuth = needsClientAuth;
    }

    /**
     * Default is {@link java.security.KeyStore#getDefaultType()}.
     *
     * @param keyStoreType
     */
    public void setKeyStoreType(final String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    /**
     * Default is {@value #DEFAULT_SSL_PROTOCOL}.
     *
     * @param sslProtocol
     */
    public void setSslProtocol(final String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    private char[] getPassword() {
        return keyStorePassword.toCharArray();
    }

    protected abstract void loadKeyStore(KeyStore keyStore, char[] password);
}
