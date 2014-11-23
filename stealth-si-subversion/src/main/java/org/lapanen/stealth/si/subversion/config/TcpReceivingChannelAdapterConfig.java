package org.lapanen.stealth.si.subversion.config;

import java.util.Collections;

import org.lapanen.stealth.si.subversion.SubversionQueryExecutingMessageHandler;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.config.TcpConnectionFactoryFactoryBean;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.connection.AbstractConnectionFactory;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNLogClient;

/**
 */
@Configuration
public abstract class TcpReceivingChannelAdapterConfig {

    @Autowired
    private SubversionTcpComponents subversionTcpComponents;

    @Autowired
    private SubversionComponents subversionComponents;

    public static final String TCP_RAW_MESSAGE_SPLIT_PATTERN = " ";

    @Bean
    public final SubscribableChannel subversionTcpEventInChannel() {
        return new DirectChannel();
    }

    @Bean
    public AbstractConnectionFactory tcpServerConnectionFactory() {
        TcpConnectionFactoryFactoryBean factory = new TcpConnectionFactoryFactoryBean();
        factory.setPort(subversionListeningSocketPort());
        try {
            return factory.getObject();
        } catch (Exception e) {
            throw new BeanCreationException("While trying to create a TCP connection factory", e);
        }
    }

    @InboundChannelAdapter(value = "subversionTcpEventInChannel")
    public TcpReceivingChannelAdapter subversionSocketListener() {
        TcpReceivingChannelAdapter ret = new TcpReceivingChannelAdapter();
        ret.setClientMode(false);
        ret.setConnectionFactory(tcpServerConnectionFactory());
        return ret;
    }

    @Transformer(inputChannel = "subversionTcpEventInChannel")
    private void mapByteArrayToMessageHeaders(@Payload final byte[] messageBytes) {
        final String message = new String(messageBytes);
        final String[] messageBits = message.split(TCP_RAW_MESSAGE_SPLIT_PATTERN);
        subversionTcpComponents.subversionCommitEventChannel()
                .send(MessageBuilder.withPayload(messageBits[1]).setHeader(SubversionQueryExecutingMessageHandler.REPOSITORY_URL_HEADER_NAME, messageBits[0])
                        .build());
    }

    @Bean
    @ServiceActivator
    public SubversionQueryExecutingMessageHandler subversionQueryExecutingMessageHandler() {
        SubversionQueryExecutingMessageHandler handler = new SubversionQueryExecutingMessageHandler(Collections.<SVNURL, SVNLogClient> emptyMap(),
                subversionComponents.subversionLogChannel());
        subversionTcpComponents.subversionCommitEventChannel().subscribe(handler);
        return handler;
    }

    protected abstract int subversionListeningSocketPort();

}
