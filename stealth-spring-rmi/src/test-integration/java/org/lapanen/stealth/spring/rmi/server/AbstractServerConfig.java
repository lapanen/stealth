package org.lapanen.stealth.spring.rmi.server;

import org.lapanen.stealth.spring.rmi.AbstractRmiServiceServerConfig;
import org.lapanen.stealth.spring.rmi.impl.ServiceImpl;
import org.springframework.context.annotation.Bean;

public abstract class AbstractServerConfig<S> extends AbstractRmiServiceServerConfig<S> {

}
