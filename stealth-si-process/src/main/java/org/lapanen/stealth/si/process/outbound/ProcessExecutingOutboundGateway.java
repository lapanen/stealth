package org.lapanen.stealth.si.process.outbound;

import java.io.IOException;

import org.lapanen.stealth.si.process.core.ProcessRunResult;
import org.lapanen.stealth.si.process.core.ExitValueResultHandler;
import org.lapanen.stealth.si.process.core.OnNokExitValueExceptionThrowingExitValueHandler;
import org.lapanen.stealth.si.process.support.ProcessExecutorIntegrationUtils;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;

public class ProcessExecutingOutboundGateway extends AbstractReplyProducingMessageHandler {

    private ExitValueResultHandler handler = new OnNokExitValueExceptionThrowingExitValueHandler();

    @Override
    protected Object handleRequestMessage(final Message<?> requestMessage) {
        return handler.handleResult(ProcessExecutorIntegrationUtils.prepareExecutor(requestMessage).execute());
    }

}
