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
    public ProcessInboundChannelAdapter(final ProcessExecutor processExecutor, final ExecutorService executorService) {
        this(processExecutor, executorService, new OnNokExitValueExceptionThrowingExitValueHandler());
    }

    @Override
    public Message<ProcessRunResult> receive() {
        return MessageBuilder.withPayload(processExecutor.execute(executorService)).build();
    }
}
