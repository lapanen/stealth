package org.lapanen.stealth.maven.artifact;

public interface Dependency extends java.io.Serializable {
    String DEFAULT_SCOPE = "compile";

    Artifact getDependency();

    String getScope();
}