package org.lapanen.stealth.signing;

import java.security.cert.Certificate;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.lapanen.stealth.maven.artifact.Artifact;

public class ArtifactSigningImpl implements ArtifactSigning {

    private  DateTime signingTime;
    private  Certificate certificate;
    private  Artifact artifact;

    public ArtifactSigningImpl() {
    }

    public ArtifactSigningImpl(final Artifact artifact, final Certificate cert, final DateTime dateTime) {
        this.artifact = artifact;
        this.certificate = cert;
        this.signingTime = dateTime;
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
