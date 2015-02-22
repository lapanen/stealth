package org.lapanen.stealth.si.process.core;

import org.springframework.messaging.MessagingException;

public class OnNokExitValueExceptionThrowingExitValueHandler implements ExitValueResultHandler<ProcessRunResult> {

    private int okExitValue = 0;

    /**
     * @param result
     *
     * @return the original result
     *
     * @throws org.springframework.messaging.MessagingException
     *         if the {@code result}'s {@link ProcessRunResult#getExitCode()} is not present, or not the same
     *         as {@link #setOkExitValue(int)}.
     */
    @Override
    public ProcessRunResult handleResult(final ProcessRunResult result) throws MessagingException {
        if (result.getExitCode().isPresent()) {
            final int exit = result.getExitCode().get();
            if (exit != okExitValue) {
                final StringBuilder exceptionMessageBuilder = new StringBuilder(String.format("Non-OK exit value (%s)", exit));
                if (result.getError().isPresent()) {
                    exceptionMessageBuilder.append(": " + new String(result.getError().get()));
                }
                throw new MessagingException(exceptionMessageBuilder.toString());
            }
        } else {
            if (result.getThrowable().isPresent()) {
                throw new MessagingException("No exit value", result.getThrowable().get());
            } else {
                throw new MessagingException("No exit value and no Throwable attached");
            }
        }
        return result;
    }

    public void setOkExitValue(final int okExitValue) {
        this.okExitValue = okExitValue;
    }

}
