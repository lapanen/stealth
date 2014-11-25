package org.lapanen.stealth.maven.artifact.storage;

import org.lapanen.stealth.Stealth;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class ArtifactStorageStealth implements Stealth {

    private static final Logger LOG = LoggerFactory.getLogger(ArtifactStorageStealth.class);

    private final ArtifactStorage artifactStorage;

    public ArtifactStorageStealth(final ArtifactStorage artifactStorage) {
        Preconditions.checkNotNull(artifactStorage, "Artifact storage must not be null");
        this.artifactStorage = artifactStorage;
    }

    @Override
    public void handleArtifact(final Artifact artifact) {
        LOG.debug("Storing {}", artifact);
        artifactStorage.storeArtifact(artifact);
    }
}
