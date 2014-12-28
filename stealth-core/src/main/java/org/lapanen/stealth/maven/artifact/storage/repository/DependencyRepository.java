package org.lapanen.stealth.maven.artifact.storage.repository;

import java.util.List;

import org.lapanen.stealth.maven.artifact.Dependency;
import org.springframework.data.repository.CrudRepository;

public interface DependencyRepository extends CrudRepository<Dependency, String> {
    List<Dependency> findByGroupIdAndArtifactIdAndVersion(String groupId, String artifactId, String version);
}
