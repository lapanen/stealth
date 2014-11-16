package org.lapanen.stealth.signing;

import org.lapanen.stealth.maven.artifact.Artifact;

import com.google.common.base.Optional;

public interface SignedJarRepo {

    boolean hasJar(final Artifact artifact);

    ArtifactSigning fetchAndSign(final Artifact artifact) throws SigningException;

    Optional<ArtifactSigning> findLastSigningFor(final Artifact artifact);

}