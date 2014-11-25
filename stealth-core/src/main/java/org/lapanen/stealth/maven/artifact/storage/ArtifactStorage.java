package org.lapanen.stealth.maven.artifact.storage;

import java.util.List;

import org.lapanen.stealth.maven.artifact.Artifact;

import com.google.common.base.Optional;

public interface ArtifactStorage {
    void storeArtifact(Artifact artifact);

    Optional<Artifact> getArtifact(String groupId, String artifactId, String version);

    List<Artifact> findArtifacts(Optional<String> groupId, boolean includeSubGroups);

    List<Artifact> findParents(Optional<String> groupIdPattern);
}
