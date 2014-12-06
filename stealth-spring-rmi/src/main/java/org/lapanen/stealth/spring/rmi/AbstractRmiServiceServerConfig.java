package org.lapanen.stealth.spring.rmi;

import java.rmi.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.google.common.base.Optional;

public abstract class AbstractRmiServiceServerConfig<S> extends AbstractRmiServiceConfig implements RmiServiceServerConfig {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public boolean alwaysCreateRegistry() {
        return false;
    }

    @Override
    public Optional<String> registryHost() {
        return Optional.absent();
    }

    @Override
    public Optional<Registry> registry() {
        return Optional.absent();
    }

    @Override
    public RmiServiceExporter createServiceExporter(final Object implementation) {
        final RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setRegistryPort(registryPort());
        exporter.setService(implementation);
        exporter.setServiceName(name());
        exporter.setServiceInterface(serviceInterface());
        exporter.setAlwaysCreateRegistry(alwaysCreateRegistry());
        if (registry().isPresent()) {
            LOG.info("Registry explicitly set");
            exporter.setRegistry(registry().get());
        } else {
           if (registryHost().isPresent()) {
               LOG.info("Registry host name explicitly set");
               exporter.setRegistryHost(registryHost().get());
           }
        }
        return exporter;
    }

}
