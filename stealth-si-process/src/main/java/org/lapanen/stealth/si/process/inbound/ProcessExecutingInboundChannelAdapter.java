package org.lapanen.stealth.si.process.inbound;

import org.lapanen.stealth.si.process.core.ProcessExecutor;
import org.lapanen.stealth.si.process.core.ExitValueResultHandler;
import org.lapanen.stealth.si.process.core.OnNokExitValueExceptionThrowingExitValueHandler;
import org.lapanen.stealth.si.process.core.ProcessRunResult;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class ProcessExecutingInboundChannelAdapter implements MessageSource<ProcessRunResult> {

    private final ProcessExecutor processExecutor;

    private final ExitValueResultHandler<ProcessRunResult> exitValueResultHandler;

    public ProcessExecutingInboundChannelAdapter(final ProcessExecutor processExecutor, final ExitValueResultHandler exitValueResultHandler) {
        this.processExecutor = processExecutor;
        this.exitValueResultHandler = exitValueResultHandler;
    }

    /**
     * Create a new adapter with {@link org.lapanen.stealth.si.process.core.OnNokExitValueExceptionThrowingExitValueHandler} as the exit value handler.
     * @param processExecutor
     */
    public ProcessExecutingInboundChannelAdapter(final ProcessExecutor processExecutor) {
        this(processExecutor, new OnNokExitValueExceptionThrowingExitValueHandler());
    }

    @Override
    public Message<ProcessRunResult> receive() {
        final ProcessRunResult result = exitValueResultHandler.handleResult(processExecutor.execute());
        return MessageBuilder.withPayload(result).build();
    }
}
