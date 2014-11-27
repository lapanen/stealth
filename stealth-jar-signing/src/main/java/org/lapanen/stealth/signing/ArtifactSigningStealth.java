package org.lapanen.stealth.signing;

import java.util.concurrent.Executor;

import org.lapanen.stealth.Stealth;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.artifact.Dependency;
import org.lapanen.stealth.signing.strategy.SigningStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Signs the artifact and its dependencies if so configured.
 */
public class ArtifactSigningStealth implements Stealth {

    private static final  Logger LOG = LoggerFactory.getLogger(ArtifactSigningStealth.class);

    private Executor signingExecutor = null;

    private final SigningStrategy artifactSigningStrategy;

    private final Optional<SigningStrategy> dependencySigningStrategy;

    private final SignedJarRepo signedJarRepo;

    public ArtifactSigningStealth(final SignedJarRepo signedJarRepo, final SigningStrategy artifactSigningStrategy,
            final SigningStrategy dependencySigningStrategy) {
        Preconditions.checkNotNull(signedJarRepo, "Signed jar repo must not be null");
        Preconditions.checkNotNull(artifactSigningStrategy, "Artifact signing strategy must not be null");
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
            if (signingExecutor != null) {
                LOG.debug("Delegating signing to executor");
                signingExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        doSign(artifact);
                    }
                });
            } else {
                LOG.debug("Signing in the caller's thread");
                doSign(artifact);
            }
        } else {
            LOG.debug("Not signing {}", artifact);
        }
    }

    private void doSign(final Artifact artifact) {
        LOG.debug("Signing {}", artifact);
        signedJarRepo.fetchAndSign(artifact);
    }

    public void setSigningExecutor(final Executor signingExecutor) {
        this.signingExecutor = signingExecutor;
    }
}
