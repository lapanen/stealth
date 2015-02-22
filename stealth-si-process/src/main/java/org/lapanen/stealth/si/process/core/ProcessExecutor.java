package org.lapanen.stealth.si.process.core;

import org.joda.time.Duration;

import com.google.common.base.Optional;

public interface ProcessExecutor {

    /**
     * @return a {@link ProcessBuilder#start() started} {@link java.lang.Process} that has been created with the underlying {@link java.lang.ProcessBuilder}.
     * <p>The main intent is that a client obtains a started {@code Process} here to be able to get to the {@link Process#getOutputStream() Process' input
     * stream}.</p>
     */
    Process start();

    ProcessRunResult execute();

    ProcessRunResult execute(byte[] input);

    /**
     * Executes an already-started {@code Process}.
     * @param process
     * @param input bytes to hand to the process as input.
     * @return
     */
    ProcessRunResult execute(Process process, byte[] input);

    /**
     * Should the {@link org.lapanen.stealth.si.process.core.ProcessRunResult} have stdout and stderr combined?
     * @return
     */
    boolean isMergeOutput();

    /**
     *
     * @return the time out to use when waiting for the {@code Process} to finish before {@link Process#destroy() destroying} it.
     * {@link com.google.common.base.Optional#absent()} implies to wait forever.
     */
    Optional<Duration> getTimeOut();
}
