package org.lapanen.stealth.signing.strategy;

import org.lapanen.stealth.maven.artifact.Artifact;

public class AlwaysSignSigningStrategy implements SigningStrategy {
    @Override
    public boolean shouldSign(final Artifact artifact) {
        return true;
    }
}
