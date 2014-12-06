package org.lapanen.stealth.spring.rmi;

public abstract class AbstractContextPropagatingRmiServiceClientConfig<S> extends AbstractRmiServiceClientConfig<S> {

    @Override
    public boolean requiresSpringAuthentication() {
        return true;
    }

    @Override
    public String name() {
        return "Secured" + super.name();
    }

}
