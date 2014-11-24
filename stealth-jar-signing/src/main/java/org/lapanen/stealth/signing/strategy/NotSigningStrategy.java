package org.lapanen.stealth.signing.strategy;

import org.lapanen.stealth.maven.artifact.Artifact;

import com.google.common.base.Preconditions;

public class NotSigningStrategy implements SigningStrategy {

    private final SigningStrategy invertedStrategy;

    public NotSigningStrategy(final SigningStrategy invertedStrategy) {
        Preconditions.checkNotNull(invertedStrategy, "Strategy to invert cannot be null");
        this.invertedStrategy = invertedStrategy;
    }

    @Override
    public boolean shouldSign(final Artifact artifact) {
        return !invertedStrategy.shouldSign(artifact);
    }
}
