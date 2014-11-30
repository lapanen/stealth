package org.lapanen.stealth.si.process;

import java.util.Arrays;

import com.google.common.base.Optional;

public class ProcessRunResultImpl implements ProcessRunResult {

    private final Optional<Integer> exitCode;
    private final boolean completed;
    private final byte[] output;
    private final byte[] error;
    private final Throwable throwable;

    private ProcessRunResultImpl(final Optional<Integer> exitCode, final boolean completed, final byte[] output, final byte[] error, final Throwable throwable) {
        this.exitCode = exitCode;
        this.completed = completed;
        this.output = output;
        this.error = error;
        this.throwable = throwable;
    }

    public static ProcessRunResult normalExecution(final int exitCode, byte[] output, byte[] error) {
        return new ProcessRunResultImpl(Optional.of(exitCode), true, output, error, null);
    }

    public static ProcessRunResult abortedExecution() {
        return new ProcessRunResultImpl(Optional.<Integer> absent(), false, null, null, null);
    }

    public static ProcessRunResult exceptionAbortedExecution(final Throwable throwable) {
        return new ProcessRunResultImpl(Optional.<Integer>absent(), false, null, null, throwable);
    }

    @Override
    public boolean completed() {
        return completed;
    }

    @Override
    public Optional<Integer> getExitCode() {
        return exitCode;
    }

    @Override
    public Optional<byte[]> getError() {
        return Optional.fromNullable(error);
    }

    @Override
    public Optional<byte[]> getOutput() {
        return Optional.fromNullable(output);
    }

    @Override
    public Optional<Throwable> getThrowable() {
        return null;
    }

    @Override
    public String toString() {
        return "ProcessRunResultImpl{" +
                "exitCode=" + exitCode +
                ", completed=" + completed +
                ", output=" + output + " bytes" +
                ", error=" + error + " bytes" +
                ", throwable=" + throwable +
                '}';
    }
}
