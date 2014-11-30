package org.lapanen.stealth.si.process;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class ProcessExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessExecutor.class);

    private final ProcessBuilder builder;

    private boolean mergeOutput = false;

    public static final long DEFAULT_TIMEOUT_MILLIS = 2 * 1000;

    private Optional<Duration> timeOut = Optional.of(new Duration(DEFAULT_TIMEOUT_MILLIS));

    public ProcessExecutor(final String... command) {
        builder = new ProcessBuilder(command);
        LOG.debug("Created a new ProcessBuilder with environment {}", builder.environment());
    }

    public ProcessRunResult execute(final ExecutorService executorService) {

        if (mergeOutput) {
            builder.redirectErrorStream(true);
        }
        final Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            return ProcessRunResultImpl.exceptionAbortedExecution(e);
        }

        LOG.debug("Submit to {}", executorService);
        final Future<ProcessRunResult> future = executorService.submit(new Callable<ProcessRunResult>() {
            @Override
            public ProcessRunResult call() throws Exception {
                final InputStream errorStream = process.getErrorStream();
                final InputStream resultStream = process.getInputStream();
                final ByteArrayOutputStream resBaos = new ByteArrayOutputStream();
                int bite;
                while ((bite = resultStream.read()) != -1) {
                    resBaos.write(bite);
                }
                final byte[] resultBytes = resBaos.toByteArray();

                byte[] errBytes = new byte[0];
                if (!mergeOutput) {
                    final ByteArrayOutputStream errBaos = new ByteArrayOutputStream();
                    int errBite;
                    while ((bite = resultStream.read()) != -1) {
                        errBaos.write(bite);
                    }
                    errBytes = errBaos.toByteArray();
                }
                final int exit = process.waitFor();
                return ProcessRunResultImpl.normalExecution(exit, resultBytes, errBytes);
            }
        });
        boolean done = false;
        Instant startTime = new Instant();
        ProcessRunResult ret = null;
        while (!done) {
            if (future.isDone()) {
                done = true;
                try {
                    ret = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    return ProcessRunResultImpl.exceptionAbortedExecution(e);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return ProcessRunResultImpl.exceptionAbortedExecution(e);
            }
            if (timeOut.isPresent()) {
                final Instant now = new Instant();
                if (startTime.plus(timeOut.get()).isBefore(now)) {
                    LOG.warn("Process exceeded time out ({}), will destroy it", timeOut.get());
                    done = true;
                    process.destroy();
                    ret = ProcessRunResultImpl.abortedExecution();
                }
            }
        }
        return ret;
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
}
