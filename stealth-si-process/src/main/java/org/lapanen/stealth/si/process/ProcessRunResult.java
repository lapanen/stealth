package org.lapanen.stealth.si.process;

import java.io.Serializable;

import com.google.common.base.Optional;

public interface ProcessRunResult extends Serializable {

    /**
     * Was the process completed normally, or was {@link Process#destroy()} called.
     * @return
     */
    boolean completed();

    /**
     * @return Same as {@link Process#exitValue()}. Value {@link com.google.common.base.Optional#absent()} implies that the process did not terminate
     * normally (was timed out or was not started at all).
     */
    Optional<Integer> getExitCode();

    Optional<byte[]> getError();

    Optional<byte[]> getOutput();

    Optional<Throwable> getThrowable();
}
