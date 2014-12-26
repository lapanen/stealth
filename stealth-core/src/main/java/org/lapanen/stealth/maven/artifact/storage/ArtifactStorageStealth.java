package org.lapanen.stealth.maven.artifact.storage;

import org.lapanen.stealth.Stealth;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.artifact.storage.repository.ArtifactRepository;
import org.lapanen.stealth.maven.event.ArtifactBuildEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;

public class ArtifactStorageStealth implements Stealth<ArtifactBuildEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ArtifactStorageStealth.class);

    @Autowired
    private ArtifactRepository<Artifact> artifactRepository;

    private void handleArtifact(final Artifact artifact) {
        LOG.debug("Storing {}", artifact);
        artifactRepository.save(artifact);
    }

    @Override
    public void handleEvent(final ArtifactBuildEvent event) {
        handleArtifact(event.getArtifact());
    }
}
