package org.lapanen.stealth.signing.repo.mongo;

import java.util.List;

import org.springframework.data.domain.Sort;

/**
 * Created by pekka on 11/19/14.
 */
public interface ArtifactSigningRepository {
    List<MongoDbArtifactSigning> findByGroupIdAndArtifactIdAndVersion(String groupId, String artifactId, String version, Sort signingTime);
}
