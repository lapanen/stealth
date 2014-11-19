package org.lapanen.stealth.signing.strategy;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.signing.ArtifactSigning;
import org.lapanen.stealth.signing.SignedJarRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public abstract class AbstractLastModifiedSigningStrategy extends AbstractSigningStrategy implements SigningStrategy {

    private static final Logger log = LoggerFactory.getLogger(AbstractLastModifiedSigningStrategy.class);

    private long timeDiscrepancyInMillis = 0;

    private final SignedJarRepo signedJarRepo;

    protected AbstractLastModifiedSigningStrategy(final SignedJarRepo signedJarRepo) {
        super();
        Preconditions.checkNotNull(signedJarRepo, "Signed jar repo must not be null");
        this.signedJarRepo = signedJarRepo;
    }

    @Override
    protected boolean doShouldSign(final Artifact artifact) {
        return !isUpToDate(artifact);
    }

    public void setTimeDiscrepancyInMillis(final long timeDiscrepancyInMillis) {
        this.timeDiscrepancyInMillis = timeDiscrepancyInMillis;
    }

    private boolean isUpToDate(final Artifact artifact) {
        final DateTime lastModified = new DateTime(getLastModifiedOriginal(artifact));
        log.debug("{} last modified at {}", artifact, lastModified);
        final Optional<ArtifactSigning> lastSigning = signedJarRepo.findLastSigningFor(artifact);
        if (lastSigning.isPresent()) {
            final ReadableInstant lastSigningTime = lastSigning.get().getSigningTime();
            if (lastSigningTime.isAfter(lastModified.plus(timeDiscrepancyInMillis))) {
                log.debug("{} up-to-date, last signed at {}", artifact, lastSigningTime);
                return true;
            } else {
                log.debug("{} not up-to-date, last signed at {}", artifact, lastSigningTime);
            }
        } else {
            log.debug("{} apparently never signed, so not up-to-date", artifact);
        }
        return false;
    }

    protected abstract ReadableInstant getLastModifiedOriginal(Artifact artifact);

}
