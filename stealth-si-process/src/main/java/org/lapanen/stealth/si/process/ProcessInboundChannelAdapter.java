package org.lapanen.stealth.si.process;

import java.util.concurrent.ExecutorService;

import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class ProcessInboundChannelAdapter implements MessageSource<ProcessRunResult> {

    private final ProcessExecutor processExecutor;

    private final ExecutorService executorService;

    private final ExitValueResultHandler exitValueResultHandler;

    public ProcessInboundChannelAdapter(final ProcessExecutor processExecutor, final ExecutorService executorService,
            final ExitValueResultHandler exitValueResultHandler) {
        this.processExecutor = processExecutor;
        this.executorService = executorService;
        this.exitValueResultHandler = exitValueResultHandler;
    }

    /**
     * Create a new adapter with {@link org.lapanen.stealth.si.process.OnNokExitValueExceptionThrowingExitValueHandler} as the exit value handler.
     * @param processExecutor
     * @param executorService
     */
    public ProcessInboundChannelAdapter(final ProcessExecutor processExecutor, final ExecutorService executorService) {
        this(processExecutor, executorService, new OnNokExitValueExceptionThrowingExitValueHandler());
    }

    @Override
    public Message<ProcessRunResult> receive() {
        final ProcessRunResult result = exitValueResultHandler.handleResult(processExecutor.execute(executorService));
        return MessageBuilder.withPayload(result).build();
    }
}
