package org.lapanen.stealth.signing.strategy;

import java.util.List;

import org.lapanen.stealth.maven.artifact.Artifact;

import com.google.common.base.Preconditions;

public class PackagingSigningStrategy implements SigningStrategy {
    private final String[] allowedPackagings;

    public PackagingSigningStrategy(final String ... allowedPackagings) {
        Preconditions.checkNotNull(allowedPackagings, "Packaging list must not be null");
        Preconditions.checkArgument(allowedPackagings.length > 0, "Packaging list must not be empty");
        this.allowedPackagings = allowedPackagings;
    }

    @Override
    public boolean shouldSign(final Artifact artifact) {
        for (final String packaging : allowedPackagings) {
            if (packaging.equals(artifact.getPackaging())) {
                return true;
            }
        }
        return false;
    }
}
