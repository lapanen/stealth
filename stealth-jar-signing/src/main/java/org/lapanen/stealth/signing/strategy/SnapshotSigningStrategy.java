package org.lapanen.stealth.signing.strategy;

import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.util.ArtifactUtils;

public class SnapshotSigningStrategy extends AbstractSigningStrategy implements SigningStrategy {

    @Override
    protected boolean doShouldSign(final Artifact artifact) {
        return ArtifactUtils.isSnapshot(artifact);
    }

}
