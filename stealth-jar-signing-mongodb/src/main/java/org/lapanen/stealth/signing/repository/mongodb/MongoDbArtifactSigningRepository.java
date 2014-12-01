package org.lapanen.stealth.signing.repository.mongodb;

import java.util.List;

import org.lapanen.stealth.signing.repository.ArtifactSigningRepository;
import org.springframework.data.domain.Sort;

public interface MongoDbArtifactSigningRepository extends ArtifactSigningRepository<MongoDbArtifactSigning> {

    List<MongoDbArtifactSigning> findByGroupIdAndArtifactIdAndVersion(String groupId, String artifactId, String version);

    List<MongoDbArtifactSigning> findByGroupIdAndArtifactIdAndVersion(String groupId, String artifactId, String version, Sort sort);
}
