package org.lapanen.stealth.signing.strategy;

import java.io.File;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.lapanen.stealth.StealthException;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.repo.PathBuilder;
import org.lapanen.stealth.signing.SignedJarRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemRepoLastModifiedSigningStrategy extends AbstractLastModifiedSigningStrategy {

    private static final Logger log = LoggerFactory.getLogger(FileSystemRepoLastModifiedSigningStrategy.class);

    private final PathBuilder pathBuilder;

    public FileSystemRepoLastModifiedSigningStrategy(final SignedJarRepo signedJarRepo, final PathBuilder pathBuilder) {
        super(signedJarRepo);
        this.pathBuilder = pathBuilder;
    }

    protected ReadableInstant getLastModifiedOriginal(final Artifact artifact) {
        final File path = pathBuilder.buildJarFilePath(artifact);
        log.debug("Looking at artifact at path {}", path);
        if (path.exists()) {
            return new DateTime(path.lastModified());
        }
        throw new StealthException("No artifact at path " + path);
    }

}
