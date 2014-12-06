package org.lapanen.stealth.spring.rmi;

import java.rmi.server.RMIClientSocketFactory;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationFactory;

/**
 * 
 *
 * @param <S> the service interface type
 */
public interface RmiServiceClientConfig<S> extends RmiServiceConfig {

    String url();

    /**
     * @see RmiProxyFactoryBean#setCacheStub(boolean)
     */
    boolean cacheStub();

    /**
     * @see RmiProxyFactoryBean#setLookupStubOnStartup(boolean)
     */
    boolean lookupStubOnStartup();
    
    /**
     * @see RmiProxyFactoryBean#setRefreshStubOnConnectFailure(boolean)
     */
    boolean refreshStubOnConnectFailure();
    
    /**
     * @see RmiProxyFactoryBean#setRemoteInvocationFactory(RemoteInvocationFactory)
     */
    RemoteInvocationFactory remoteInvocationFactory();

    String host();

    /**
     * @see RmiProxyFactoryBean#setRegistryClientSocketFactory(java.rmi.server.RMIClientSocketFactory)
     */
    RMIClientSocketFactory registryClientSocketFactory();

    S createClient();

}
