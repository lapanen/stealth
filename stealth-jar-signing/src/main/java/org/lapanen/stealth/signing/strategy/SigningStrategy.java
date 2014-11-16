package org.lapanen.stealth.signing.strategy;

import org.lapanen.stealth.maven.artifact.Artifact;

public interface SigningStrategy {

    boolean shouldSign(Artifact artifact);

}
