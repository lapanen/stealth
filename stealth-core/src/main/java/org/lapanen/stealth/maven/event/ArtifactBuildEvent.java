package org.lapanen.stealth.maven.event;

import org.lapanen.stealth.event.AbstractStealthEvent;
import org.lapanen.stealth.maven.artifact.Artifact;

public class ArtifactBuildEvent extends AbstractStealthEvent {

    private final Artifact artifact;

    protected ArtifactBuildEvent(final Artifact artifact) {
        super("stealth.artifact.build");
        this.artifact = artifact;
    }

    public Artifact getArtifact() {
        return artifact;
    }
}
