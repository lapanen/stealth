package org.lapanen.stealth.signing.repository;

import java.io.Serializable;
import java.util.List;

import org.lapanen.stealth.signing.ArtifactSigning;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface ArtifactSigningRepository extends CrudRepository<String, ArtifactSigning> {
    List<ArtifactSigning> findByGroupIdAndArtifactIdAndVersion(String groupId, String artifactId, String version, Sort sort);
}
