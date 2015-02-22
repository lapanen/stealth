package org.lapanen.stealth.si.process;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lapanen.stealth.si.process.core.OnNokExitValueExceptionThrowingExitValueHandler;
import org.lapanen.stealth.si.process.core.ProcessRunResult;
import org.lapanen.stealth.si.process.core.ProcessRunResultImpl;
import org.springframework.messaging.MessagingException;

public class OnNokExitValueExceptionThrowingExitValueHandlerTest {

    private OnNokExitValueExceptionThrowingExitValueHandler sut = new OnNokExitValueExceptionThrowingExitValueHandler();

    @Test
    public void ok_exit_value_returns_original_result() {
        final ProcessRunResult ok = ProcessRunResultImpl.normalExecution(0, new byte[0], new byte[0]);
        assertTrue("Result must be the same as passed in", (sut.handleResult(ok) == ok));
    }

    @Test(expected = MessagingException.class)
    public void non_ok_exit_value_raises_MessagingException() {
        sut.handleResult(ProcessRunResultImpl.normalExecution(1, new byte[0], new byte[0]));
    }

    @Test
    public void non_ok_exit_value_MessagingException_contains_error() {
        final String error = "ERROR";
        try {
            sut.handleResult(ProcessRunResultImpl.normalExecution(1, new byte[0], error.getBytes()));
        } catch (MessagingException e) {
            assertTrue(e.getMessage().endsWith(error));
        }
    }

    @Test
    public void exception_containing_results_MessagingException_contains_Throwable() {
        final Throwable t = new NullPointerException();
        try {
            sut.handleResult(ProcessRunResultImpl.exceptionAbortedExecution(t));
        } catch (MessagingException e) {
            assertTrue(e.getCause() == t);
        }
    }

    @Test
    public void exceptional_result_with_no_Throwable_yields_no_cause() {
        try {
            sut.handleResult(ProcessRunResultImpl.exceptionAbortedExecution(null));
        } catch (MessagingException e) {
            assertTrue(e.getCause() == null);
        }
    }

    @Test(expected = MessagingException.class)
    public void exceptional_result_with_no_Throwable_raises_MessagingException() {
        sut.handleResult(ProcessRunResultImpl.exceptionAbortedExecution(null));
    }

}
