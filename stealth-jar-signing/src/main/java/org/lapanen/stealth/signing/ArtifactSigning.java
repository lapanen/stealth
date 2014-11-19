package org.lapanen.stealth.signing;

import java.security.cert.Certificate;

import org.joda.time.ReadableInstant;
import org.lapanen.stealth.maven.artifact.Artifact;

public interface ArtifactSigning {
    Artifact getArtifact();

    Certificate getCertificate();

    ReadableInstant getSigningTime();
}