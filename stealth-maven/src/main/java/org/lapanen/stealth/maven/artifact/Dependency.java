package org.lapanen.stealth.maven.artifact;

public interface Dependency extends java.io.Serializable {
    java.lang.String DEFAULT_SCOPE = "compile";

    Artifact getDependency();

    String getScope();
}