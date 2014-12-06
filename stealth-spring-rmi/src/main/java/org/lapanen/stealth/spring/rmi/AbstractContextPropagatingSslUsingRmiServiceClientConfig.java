package org.lapanen.stealth.spring.rmi;

public abstract class AbstractContextPropagatingSslUsingRmiServiceClientConfig<S> extends AbstractContextPropagatingRmiServiceClientConfig<S> {

    @Override
    public boolean usesSSL() {
        return true;
    }

    @Override
    public String name() {
        return "SSL" + super.name();
    }

}
