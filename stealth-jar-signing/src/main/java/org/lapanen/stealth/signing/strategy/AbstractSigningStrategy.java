package org.lapanen.stealth.signing.strategy;


import org.lapanen.stealth.maven.artifact.Artifact;

import com.google.common.base.Preconditions;

public abstract class AbstractSigningStrategy implements SigningStrategy {

    @Override
    public boolean shouldSign(final Artifact artifact) {
        Preconditions.checkNotNull(artifact, "Cannot decide on a null Artifact");
        return doShouldSign(artifact);
    }

    protected abstract boolean doShouldSign(Artifact artifact);

}
