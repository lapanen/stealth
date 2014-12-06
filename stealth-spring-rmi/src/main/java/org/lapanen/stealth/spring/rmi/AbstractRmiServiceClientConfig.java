package org.lapanen.stealth.spring.rmi;

import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.security.remoting.rmi.ContextPropagatingRemoteInvocationFactory;

public abstract class AbstractRmiServiceClientConfig<S> extends AbstractRmiServiceConfig<S> implements RmiServiceClientConfig {

    @Override
    public String url() {
        return baseUrl() + "/" + name();
    }

    @Override
    public String host() {
        return "localhost";
    }

    private String baseUrl() {
        return "rmi://" + host() + ":" + registryPort();
    }

    @Override
    public boolean cacheStub() {
        return true;
    }

    @Override
    public boolean lookupStubOnStartup() {
        return false;
    }

    @Override
    public boolean refreshStubOnConnectFailure() {
        return true;
    }

    @Override
    public RemoteInvocationFactory remoteInvocationFactory() {
        if (requiresSpringAuthentication()) {
            return new ContextPropagatingRemoteInvocationFactory();
        }
        return new DefaultRemoteInvocationFactory();
    }

    @Override
    public RMIClientSocketFactory registryClientSocketFactory() {
        if (usesSSL()) {
            return new SslRMIClientSocketFactory();
        }
        return null;
    }

    @Override
    public S createClient() {
        final RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setLookupStubOnStartup(false);
        factory.setServiceInterface(serviceInterface());
        factory.setServiceUrl(url());
        factory.setCacheStub(cacheStub());
        factory.setLookupStubOnStartup(lookupStubOnStartup());
        factory.setRefreshStubOnConnectFailure(refreshStubOnConnectFailure());
        factory.setRemoteInvocationFactory(remoteInvocationFactory());
        final RMIClientSocketFactory clientSocketFactory = registryClientSocketFactory();
        if (clientSocketFactory != null) {
            factory.setRegistryClientSocketFactory(clientSocketFactory);
        }
        factory.afterPropertiesSet();
        return (S) factory.getObject();
    }
}
