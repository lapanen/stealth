package org.lapanen.stealth.maven.artifact;

import java.io.Serializable;

public interface Artifact extends Serializable {
    java.lang.String DEFAULT_PACKAGING = "jar";
    java.lang.String PARENT_PACKAGING = "pom";

    java.util.List<Artifact> getParents();

    java.util.List<Dependency> getDependencies();

    java.lang.String getGroupId();

    java.lang.String getArtifactId();

    java.lang.String getVersion();

    java.lang.String getPackaging();
}