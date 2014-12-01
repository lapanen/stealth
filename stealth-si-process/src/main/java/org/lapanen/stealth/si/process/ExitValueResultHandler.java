package org.lapanen.stealth.si.process;

public interface ExitValueResultHandler {
    /**
     *
     * @param result
     * @return the original result, if processing may continue.
     */
    ProcessRunResult handleResult(ProcessRunResult result);
}
