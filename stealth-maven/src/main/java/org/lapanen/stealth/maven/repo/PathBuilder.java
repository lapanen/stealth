package org.lapanen.stealth.maven.repo;

import java.io.File;

import org.lapanen.stealth.maven.artifact.Artifact;

public interface PathBuilder {

    File buildJarFilePath(Artifact artifact);

    String buildRelativeJarPath(Artifact artifact);

}