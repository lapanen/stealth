package org.lapanen.stealth.signing.strategy;

import java.util.regex.Pattern;

import org.lapanen.stealth.maven.artifact.Artifact;

import com.google.common.base.Preconditions;

public class GroupIdMatchingSigningStrategy extends AbstractSigningStrategy implements SigningStrategy {

    private final Pattern groupIdPattern;

    public GroupIdMatchingSigningStrategy(final String groupIdPattern) {
        Preconditions.checkNotNull(groupIdPattern, "groupId pattern must not be null");
        this.groupIdPattern = Pattern.compile(groupIdPattern);
    }

    @Override
    protected boolean doShouldSign(final Artifact artifact) {
        return groupIdPattern.matcher(artifact.getGroupId()).matches();
    }

}
