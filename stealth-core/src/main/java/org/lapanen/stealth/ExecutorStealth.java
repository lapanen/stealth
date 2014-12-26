package org.lapanen.stealth;

import java.util.concurrent.Executor;

import org.lapanen.stealth.event.StealthEvent;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class ExecutorStealth<T extends StealthEvent> implements Stealth<T> {

    private final static Logger LOG = LoggerFactory.getLogger(ExecutorStealth.class);

    private final Executor executor;
    private final Stealth delegate;

    public ExecutorStealth(final Executor executor, final Stealth delegate) {
        this.executor = Preconditions.checkNotNull(executor, "Executor must not be null");
        this.delegate = Preconditions.checkNotNull(delegate, "Delegate Stealth must not be null");
    }

    @Override
    public void handleEvent(final T event) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                delegate.handleEvent(event);
            }
        });

    }
}
