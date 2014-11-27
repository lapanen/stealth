package org.lapanen.stealth.maven.artifact.storage.repository;

import org.lapanen.stealth.maven.artifact.Artifact;
import org.springframework.data.repository.CrudRepository;

public interface ParentRepository extends CrudRepository<Artifact, String> {
}
