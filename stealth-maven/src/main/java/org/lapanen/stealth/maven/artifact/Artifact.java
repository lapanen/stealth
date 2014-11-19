package org.lapanen.stealth.maven.artifact;

import java.io.Serializable;
import java.util.List;

public interface Artifact extends Serializable {
    String DEFAULT_PACKAGING = "jar";
    String PARENT_PACKAGING = "pom";

    List<Artifact> getParents();

    List<Dependency> getDependencies();

    String getGroupId();

    String getArtifactId();

    String getVersion();

    String getPackaging();
}