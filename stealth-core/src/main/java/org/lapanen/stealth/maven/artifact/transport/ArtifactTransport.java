package org.lapanen.stealth.maven.artifact.transport;

import java.util.Map;

import org.lapanen.stealth.StealthException;
import org.lapanen.stealth.maven.artifact.Artifact;

public interface ArtifactTransport {
    void transport(Artifact artifact) throws StealthException;
    void configure(Map<String, String> configuration);
}
