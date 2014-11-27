package org.lapanen.stealth.signing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class BinaryJarSignerUsingJarSigner implements JarSigner {

    private static final Logger LOG = LoggerFactory.getLogger(BinaryJarSignerUsingJarSigner.class);

    private final String keystorePath;

    private final String keystoreType;

    private final String alias;

    private final String storepass;

    private final String keypass;

    private final String jarSignerBinaryPath;

    private Certificate signingCertificate;

    private boolean isInitialised = false;

    public BinaryJarSignerUsingJarSigner(final String keystore, final String keystoreType, final String alias, final String storepass, final String keypass,
            final String jarSignerBinaryPath) {
        Preconditions.checkNotNull(keystore, "keystore must not be null");
        Preconditions.checkNotNull(keystoreType, "keystoreType must not be null");
        Preconditions.checkNotNull(alias, "alias must not be null");
        Preconditions.checkNotNull(storepass, "storepass must not be null");
        Preconditions.checkNotNull(keypass, "keypass must not be null");
        Preconditions.checkNotNull(jarSignerBinaryPath, "jarSignerBinaryPath must not be null");
        this.jarSignerBinaryPath = jarSignerBinaryPath;
        this.keystorePath = keystore;
        this.alias = alias;
        this.storepass = storepass;
        this.keypass = keypass;
        this.keystoreType = keystoreType;
    }

    public void init() {
        assertIsExecutableFile(jarSignerBinaryPath);
        try (final InputStream keystoreIn = new FileInputStream(jarSignerBinaryPath)) {
            LOG.debug("Trying to load keystore of type '{}' from path '{}'", keystoreType, keystorePath);
            final KeyStore store = KeyStore.getInstance(keystoreType);
            store.load(keystoreIn, storepass.toCharArray());
            if (store.containsAlias(alias)) {
                LOG.debug("Getting signing certificate with alias '{}'", alias);
                signingCertificate = store.getCertificate(alias);
                LOG.debug("Got signing certificate {}", signingCertificate);
            } else {
                throw new SigningException(String.format("Keystore '%s' does not contain alias '%s'.", keystorePath, alias));
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SigningException(e);
        }
        isInitialised = true;
        LOG.debug("Initialiased {}", this);
    }

    @Override
    public Certificate signJar(final String sourcePath, final String fileSystemTargetPath) throws SigningException {
        if (!isInitialised) {
            throw new IllegalStateException("Not initialised, call init() first");
        }
        assertIsFile(sourcePath);
        final File target = new File(fileSystemTargetPath);
        final File targetParentDirectory = target.getParentFile();
        if (targetParentDirectory == null || !targetParentDirectory.exists()) {
            LOG.debug("Target file's {} parent {} does not exist, creating", target, targetParentDirectory);
            if (!targetParentDirectory.mkdirs()) {
                throw new SigningException(String.format("Creating directory '%s' failed", targetParentDirectory));
            }
        }
        runJarSigner(sourcePath, fileSystemTargetPath);
        return signingCertificate;
    }

    private void runJarSigner(final String sourcePath, final String targetPath) throws SigningException {
        try {
            final ProcessBuilder builder = new ProcessBuilder(jarSignerBinaryPath, "-storepass", storepass, "-keypass", keypass, "-keystore", keystorePath,
                    "-signedjar", targetPath, sourcePath, alias);
            builder.redirectErrorStream(true);
            final Process p = builder.start();
            final InputStream input = p.getInputStream();
            final StringBuilder jarsignerOutput = new StringBuilder("");
            int i;
            while ((i = input.read()) != -1) {
                jarsignerOutput.append((char) i);
            }
            input.close();
            try {
                int exit = p.waitFor();
                if (exit != 0) {
                    throw new SigningException("Non-zero (" + exit + ") exit value from jarsigner: " + jarsignerOutput.toString());
                }
            } catch (InterruptedException e) {
                LOG.debug("", e);
            }
        } catch (IOException e) {
            throw new SigningException(e);
        }

    }

    private void assertIsExecutableFile(final String path) {
        final File file = assertIsFile(path);
        if (!file.canExecute()) {
            throw new SigningException(path + " is not executable");
        }
    }

    private File assertIsFile(final String path) throws SigningException {
        final File ret = new File(path);
        if (!ret.getAbsoluteFile().isFile()) {
            throw new SigningException("File " + path + " is not a file");
        }
        return ret;
    }

    @Override
    public String toString() {
        return "JarSignerImpl [keystore=" + keystorePath + ", keystoreType=" + keystoreType + ", alias=" + alias + ", jarSignerBinary=" + jarSignerBinaryPath
                + ", signingCertificate=" + signingCertificate + ", isInitialised=" + isInitialised + "]";
    }

}
