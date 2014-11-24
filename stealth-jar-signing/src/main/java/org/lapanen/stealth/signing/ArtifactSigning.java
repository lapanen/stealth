package org.lapanen.stealth.signing;

import java.io.Serializable;
import java.security.cert.Certificate;

import org.joda.time.ReadableInstant;
import org.lapanen.stealth.maven.artifact.Artifact;

public interface ArtifactSigning extends Serializable {

    Artifact getArtifact();

    Certificate getCertificate();

    ReadableInstant getSigningTime();
}