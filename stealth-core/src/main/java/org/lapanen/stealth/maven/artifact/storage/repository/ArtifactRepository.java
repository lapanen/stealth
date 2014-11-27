package org.lapanen.stealth.maven.artifact.storage.repository;

import org.lapanen.stealth.maven.artifact.Artifact;
import org.springframework.data.repository.CrudRepository;

public interface ArtifactRepository<T extends Artifact> extends CrudRepository<T, String> {
}
