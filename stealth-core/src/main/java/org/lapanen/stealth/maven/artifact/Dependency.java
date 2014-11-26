package org.lapanen.stealth.maven.artifact;

import java.io.Serializable;

import com.google.common.base.Optional;

public interface Dependency extends Serializable {
    String DEFAULT_SCOPE = "compile";

    Artifact getDependency();

    String getScope();

    Optional<String> getClassifier();

    Optional<String> getType();
}