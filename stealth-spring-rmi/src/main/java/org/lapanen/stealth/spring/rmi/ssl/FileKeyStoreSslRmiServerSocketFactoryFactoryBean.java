package org.lapanen.stealth.spring.rmi.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;

import com.google.common.base.Preconditions;

public class FileKeyStoreSslRmiServerSocketFactoryFactoryBean extends AbstractSslRmiServerSocketFactoryFactoryBean {

    private static final Logger LOG = LoggerFactory.getLogger(FileKeyStoreSslRmiServerSocketFactoryFactoryBean.class);

    private final String keyStorePath;

    public FileKeyStoreSslRmiServerSocketFactoryFactoryBean(final String keyStorePath, final String keyStorePassword) {
        super(keyStorePassword);
        this.keyStorePath = Preconditions.checkNotNull(keyStorePath, "Keystore path must not be null");
    }

    @Override
    protected void loadKeyStore(final KeyStore keyStore, final char[] password) {
        final File keyStoreFile = new File(keyStorePath);
        if (!keyStoreFile.exists() || !keyStoreFile.isFile() || !keyStoreFile.canRead()) {
            throw new BeanCreationException(String.format("Keystore file '%s' is not a readable file"));
        }
        try (final FileInputStream fis = new FileInputStream(keyStoreFile)) {
            LOG.debug("Loading KeyStore from file {}", keyStoreFile);
            keyStore.load(fis, password);
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new BeanCreationException(String.format("While trying to load keystore from file '%s'", keyStoreFile), e);
        }

    }
}
