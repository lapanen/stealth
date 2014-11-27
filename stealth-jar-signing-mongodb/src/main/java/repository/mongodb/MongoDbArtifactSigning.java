package repository.mongodb;

import java.security.cert.Certificate;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.signing.ArtifactSigning;
import org.lapanen.stealth.signing.ArtifactSigningImpl;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MongoDbArtifactSigning extends ArtifactSigningImpl {

    @Id
    private String identifier;

    public MongoDbArtifactSigning() {
        super();
    }

    public MongoDbArtifactSigning(final Artifact artifact, final Certificate cert, final DateTime dateTime) {
        super(artifact, cert, dateTime);
    }

}
