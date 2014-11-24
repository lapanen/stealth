package org.lapanen.stealth.signing;

import java.security.cert.Certificate;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.lapanen.stealth.maven.artifact.Artifact;

public class ArtifactSigningImpl implements ArtifactSigning {

    public ArtifactSigningImpl() {

    }

    public ArtifactSigningImpl(final Artifact artifact, final Certificate cert, final DateTime dateTime) {
    }

    @Override
    public Artifact getArtifact() {
        return null;
    }

    @Override
    public Certificate getCertificate() {
        return null;
    }

    @Override
    public ReadableInstant getSigningTime() {
        return null;
    }
}
