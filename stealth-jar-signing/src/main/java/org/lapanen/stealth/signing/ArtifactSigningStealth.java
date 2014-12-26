package org.lapanen.stealth.signing;

import java.util.concurrent.Executor;

import org.lapanen.stealth.Stealth;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.artifact.Dependency;
import org.lapanen.stealth.maven.event.ArtifactBuildEvent;
import org.lapanen.stealth.signing.strategy.SigningStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Signs the artifact and its dependencies if so configured.
 */
public class ArtifactSigningStealth implements Stealth<ArtifactBuildEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ArtifactSigningStealth.class);

    private final SignedJarRepo signedJarRepo;

    private final SigningStrategy artifactSigningStrategy;

    private final Optional<SigningStrategy> dependencySigningStrategy;

    private Optional<Executor> signingExecutor = Optional.absent();

    public ArtifactSigningStealth(final SignedJarRepo signedJarRepo, final SigningStrategy artifactSigningStrategy,
            final Optional<SigningStrategy> dependencySigningStrategy) {
        this.signedJarRepo = Preconditions.checkNotNull(signedJarRepo, "Signed jar repo must not be null");
        this.artifactSigningStrategy = Preconditions.checkNotNull(artifactSigningStrategy, "Artifact signing strategy must not be null");
        this.dependencySigningStrategy = Preconditions.checkNotNull(dependencySigningStrategy, "Dependency signing strategy is optional, but must not be null");
    }

    public ArtifactSigningStealth(final SignedJarRepo signedJarRepo, final SigningStrategy artifactSigningStrategy) {
        this(signedJarRepo, artifactSigningStrategy, Optional.<SigningStrategy> absent());
    }

    @Override
    public void handleEvent(final ArtifactBuildEvent event) {
        handleArtifact(event.getArtifact());
    }

    private void handleArtifact(final Artifact artifact) {
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
            if (signingExecutor.isPresent()) {
                LOG.debug("Delegating signing to executor");
                signingExecutor.get().execute(new Runnable() {
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
        this.signingExecutor = Optional.of(signingExecutor);
    }

}
