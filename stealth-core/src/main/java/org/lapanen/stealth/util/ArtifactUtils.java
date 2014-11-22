package org.lapanen.stealth.util;

import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.artifact.ArtifactDTO;

public class ArtifactUtils {

    private ArtifactUtils() {
    }

    public static String toIdString(final Artifact artifact) {
        return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion() + ":" + artifact.getPackaging();
    }

    public static boolean isSnapshot(final Artifact artifact) {
        return artifact.getVersion().endsWith("-SNAPSHOT");
    }

    public static Artifact parse(final String identifier) {
        final String[] parts = identifier.split(":");
        return new ArtifactDTO(parts[0], parts[1], parts[3], parts[2]);
    }

}
