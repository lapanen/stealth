package org.lapanen.stealth;

import java.util.concurrent.Executor;

import org.lapanen.stealth.maven.artifact.Artifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class ExecutorStealth implements Stealth {

    private final static Logger LOG = LoggerFactory.getLogger(ExecutorStealth.class);

    private final Executor executor;
    private final Stealth delegate;

    public ExecutorStealth(final Executor executor, final Stealth delegate) {
        Preconditions.checkNotNull(executor, "Executor must not be null");
        Preconditions.checkNotNull(delegate, "Delegate Stealth must not be null");
        this.delegate = delegate;
        this.executor = executor;
    }

    @Override
    public void handleArtifact(final Artifact artifact) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                delegate.handleArtifact(artifact);
            }
        });
    }
}
