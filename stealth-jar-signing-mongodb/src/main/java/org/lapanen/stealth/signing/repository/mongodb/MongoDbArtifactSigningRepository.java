package org.lapanen.stealth.signing.repository.mongodb;

import java.util.List;

import org.lapanen.stealth.signing.repository.ArtifactSigningRepository;
import org.springframework.data.domain.Sort;

public interface MongoDbArtifactSigningRepository extends ArtifactSigningRepository<MongoDbArtifactSigning> {
}
