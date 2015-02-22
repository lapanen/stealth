package org.lapanen.stealth.si.process.core;

public interface ExitValueResultHandler<T> {
    /**
     *
     * @param result
     * @return the result, if processing may continue.
     */
    T handleResult(ProcessRunResult result);
}
