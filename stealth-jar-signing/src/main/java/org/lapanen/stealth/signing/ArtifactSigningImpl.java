package org.lapanen.stealth.signing;

import java.security.cert.Certificate;

import org.joda.time.ReadableInstant;
import org.lapanen.stealth.maven.artifact.Artifact;

public class ArtifactSigningImpl implements ArtifactSigning {

    private final Artifact artifact;

    private final Certificate certificate;

    private final ReadableInstant signingTime;

    public ArtifactSigningImpl(final Artifact artifact, final Certificate certificate, final ReadableInstant signingTime) {
        this.artifact = artifact;
        this.certificate = certificate;
        this.signingTime = signingTime;
    }

    @Override
    public Artifact getArtifact() {
        return artifact;
    }

    @Override
    public Certificate getCertificate() {
        return certificate;
    }

    @Override
    public ReadableInstant getSigningTime() {
        return signingTime;
    }

}
