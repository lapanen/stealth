package org.lapanen.stealth.spring.rmi.server;

import org.lapanen.stealth.spring.rmi.impl.ServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

@Configuration
public class RmiServer {

    @Bean
    public RmiServiceExporter stringService() {
        return new StringServiceConfig().createServiceExporter(serviceImpl());
    }

    @Bean
    public RmiServiceExporter stringUpdaterService() {
        return new StringUpdaterServiceConfig().createServiceExporter(serviceImpl());
    }

    @Bean
    public RmiServiceExporter integerService() {
        return new IntegerServiceConfig().createServiceExporter(serviceImpl());
    }

    @Bean
    public ServiceImpl serviceImpl() {
        return new ServiceImpl();
    }

}
