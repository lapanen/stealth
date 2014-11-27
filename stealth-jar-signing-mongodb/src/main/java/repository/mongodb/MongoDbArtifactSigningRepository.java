package repository.mongodb;

import java.io.Serializable;
import java.util.List;

import org.lapanen.stealth.signing.repository.ArtifactSigningRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface MongoDbArtifactSigningRepository extends ArtifactSigningRepository<MongoDbArtifactSigning> {

    List<MongoDbArtifactSigning> findByGroupIdAndArtifactIdAndVersion(String groupId, String artifactId, String version);

    List<MongoDbArtifactSigning> findByGroupIdAndArtifactIdAndVersion(String groupId, String artifactId, String version, Sort sort);
}
