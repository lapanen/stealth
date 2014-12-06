package org.lapanen.stealth.spring.rmi;

import java.rmi.registry.Registry;

import org.springframework.remoting.rmi.RmiServiceExporter;

import com.google.common.base.Optional;

public interface RmiServiceServerConfig extends RmiServiceConfig {

    boolean alwaysCreateRegistry();

    Optional<String> registryHost();

    RmiServiceExporter createServiceExporter(Object implementation);

    Optional<Registry> registry();

}
