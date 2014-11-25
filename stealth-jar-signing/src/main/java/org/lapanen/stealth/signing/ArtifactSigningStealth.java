package org.lapanen.stealth.signing;

import org.lapanen.stealth.Stealth;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.artifact.Dependency;
import org.lapanen.stealth.signing.strategy.SigningStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * Signs the artifact and its dependencies if so configured.
 */
public class ArtifactSigningStealth implements Stealth {

    Logger LOG = LoggerFactory.getLogger(ArtifactSigningStealth.class);

    private final SigningStrategy artifactSigningStrategy;
    private final Optional<SigningStrategy> dependencySigningStrategy;

    private final SignedJarRepo signedJarRepo;

    public ArtifactSigningStealth(final SignedJarRepo signedJarRepo, final SigningStrategy artifactSigningStrategy,
            final SigningStrategy dependencySigningStrategy) {
        this.signedJarRepo = signedJarRepo;
        this.artifactSigningStrategy = artifactSigningStrategy;
        this.dependencySigningStrategy = Optional.fromNullable(dependencySigningStrategy);
    }
    public ArtifactSigningStealth(final SignedJarRepo signedJarRepo, final SigningStrategy artifactSigningStrategy) {
        this(signedJarRepo, artifactSigningStrategy, null);
    }

    @Override
    public void handleArtifact(final Artifact artifact) {
        signIfAppropriate(artifact, artifactSigningStrategy);
        if (dependencySigningStrategy.isPresent()) {
            LOG.debug("Checking dependencies for signing");
            for (final Dependency dependency : artifact.getDependencies()) {
                signIfAppropriate(dependency.getDependency(), dependencySigningStrategy.get());
            }
        }
    }

    private void signIfAppropriate(final Artifact artifact, final SigningStrategy strategy) {
        if (strategy.shouldSign(artifact)) {
            LOG.debug("Signing {}", artifact);
            signedJarRepo.fetchAndSign(artifact);
        } else {
            LOG.debug("Not signing {}", artifact);
        }
    }
}
