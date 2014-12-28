package org.lapanen.stealth.maven.event;

import org.lapanen.stealth.event.AbstractStealthEvent;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.naming.EventIdentifier;

public class ArtifactBuildEvent extends AbstractStealthEvent {

    private final Artifact artifact;

    protected ArtifactBuildEvent(final EventIdentifier eventIdentifier, final Artifact artifact) {
        super(eventIdentifier);
        this.artifact = artifact;
    }

    public Artifact getArtifact() {
        return artifact;
    }
}
