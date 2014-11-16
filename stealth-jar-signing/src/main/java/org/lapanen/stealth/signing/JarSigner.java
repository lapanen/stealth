package org.lapanen.stealth.signing;

import java.security.cert.Certificate;

public interface JarSigner {

    Certificate signJar(String sourcePath, String fileSystemTargetPath) throws SigningException;

}
