package org.lapanen.stealth.maven.artifact.transport.spi;

import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;

import org.lapanen.stealth.maven.artifact.transport.ArtifactTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class ArtifactTransportService {

    private static final Logger LOG = LoggerFactory.getLogger(ArtifactTransportService.class);

    private static ArtifactTransportService service;
    private ServiceLoader<ArtifactTransport> loader;

    private ArtifactTransportService() {
        loader = ServiceLoader.load(ArtifactTransport.class);
    }

    public static synchronized ArtifactTransportService getInstance() {
        if (service == null) {
            service = new ArtifactTransportService();
        }
        return service;
    }

    public Optional<ArtifactTransport> allocateTransport() {
        return allocateTransport(Collections.<String, String> emptyMap());
    }

    public Optional<ArtifactTransport> allocateTransport(Map<String, String> configuration) {
        for (final ArtifactTransport transport : loader) {
            ArtifactTransport ret = transport;
            ret.configure(configuration);
            LOG.debug("Allocating configured transport: {}", ret);
            return Optional.of(ret);
        }
        return Optional.absent();
    }

}
