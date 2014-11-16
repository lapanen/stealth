package org.lapanen.stealth.maven.repo;

import java.net.URI;

import org.lapanen.stealth.maven.artifact.Artifact;

public interface UrlBuilder {

    URI buildUri(Artifact artifact);

}
