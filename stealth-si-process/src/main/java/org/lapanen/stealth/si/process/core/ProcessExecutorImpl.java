package org.lapanen.stealth.si.process.core;

import java.io.IOException;
import java.util.Map;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.lapanen.stealth.si.process.support.InputStreamReader;
import org.lapanen.stealth.si.process.support.InputStreamReaderHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class ProcessExecutorImpl implements ProcessExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessExecutorImpl.class);

    private final ProcessBuilder builder;

    private boolean mergeOutput = false;

    public static final long DEFAULT_TIMEOUT_MILLIS = 2 * 1000;

    private Optional<Duration> timeOut = Optional.of(new Duration(DEFAULT_TIMEOUT_MILLIS));

    public ProcessExecutorImpl(final String... command) {
        builder = new ProcessBuilder(command);
    }

    /**
     * {@link com.google.common.base.Optional#absent()} implies no timeout, so wait forever.
     *
     * @param timeOut
     */
    public void setTimeOut(final Optional<Duration> timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * Should we call {@link java.lang.ProcessBuilder#redirectErrorStream(boolean)} with {@code true}.
     *
     * @param mergeOutput
     */
    public void setMergeOutput(final boolean mergeOutput) {
        this.mergeOutput = mergeOutput;
    }

    public void setEnvironmentVariable(final String name, final String value) {
        builder.environment().put(name, value);
    }

    public void setEnvironmentVariables(final Map<String, String> environmentVariables) {
        builder.environment().putAll(environmentVariables);
    }

    @Override
    public Process start() {
        try {
            return builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProcessRunResult execute() {
        return execute(null);
    }

    @Override
    public ProcessRunResult execute(final byte[] input) {
        return execute(start(), input);
    }

    @Override
    public ProcessRunResult execute(final Process process, final byte[] input) {
        try {
            if (input != null) {
                process.getOutputStream().write(input);
                process.getOutputStream().flush();
                process.getOutputStream().close();
            }
            InputStreamReaderHolder readerHolder;
            if (mergeOutput) {
                builder.redirectErrorStream(true);
                readerHolder = new InputStreamReaderHolder(new InputStreamReader(process.getInputStream()));
            } else {
                readerHolder = new InputStreamReaderHolder(new InputStreamReader(process.getInputStream()), new InputStreamReader(process.getErrorStream()));
            }
            readerHolder.start();
            final Optional<Integer> result = waitForExit(process);
            if (result.isPresent()) {
                return ProcessRunResultImpl.normalExecution(result.get(), readerHolder.getResultOrEmptyByteArray(0), readerHolder.getResultOrEmptyByteArray(1));
            }
            return ProcessRunResultImpl.abortedExecution();
        } catch (InterruptedException | IOException e) {
            return ProcessRunResultImpl.exceptionAbortedExecution(e);
        }
    }

    private Optional<Integer> waitForExit(final Process process) throws InterruptedException {
        if (timeOut.isPresent()) {
            final Instant startingTime = new Instant();
            while (startingTime.plus(timeOut.get()).isAfter(new Instant())) {
                try {
                    return Optional.of(process.exitValue());
                } catch (Exception e) {
                    Thread.sleep(100);
                }
            }
            LOG.warn("Timed out after waiting for  {} ms., destroying process", timeOut.get());
            process.destroy();
            return Optional.absent();
        } else {
            return Optional.of(process.waitFor());
        }
    }

    @Override
    public boolean isMergeOutput() {
        return mergeOutput;
    }

    @Override
    public Optional<Duration> getTimeOut() {
        return timeOut;
    }
}
