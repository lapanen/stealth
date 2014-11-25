package org.lapanen.stealth.signing.strategy;

import java.util.List;

import org.lapanen.stealth.maven.artifact.Artifact;

public class AndSigningStrategy extends AbstractBooleanSigningStrategy implements SigningStrategy {

    public AndSigningStrategy(final SigningStrategy ... strategies) {
        super(strategies);
    }

    @Override
    protected boolean doShouldSign(final Artifact artifact) {
        for (final SigningStrategy strategy : getStrategies()) {
            if (!strategy.shouldSign(artifact)) {
                return false;
            }
        }
        return true;
    }

}
