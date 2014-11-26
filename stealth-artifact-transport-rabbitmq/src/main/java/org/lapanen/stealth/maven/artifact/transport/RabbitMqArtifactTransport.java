package org.lapanen.stealth.maven.artifact.transport;

import java.util.Map;

import org.lapanen.stealth.StealthException;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.google.common.base.Preconditions;

public class RabbitMqArtifactTransport implements ArtifactTransport {

    private int port;
    private String host;
    private String username;
    private String password;
    private String exchange;
    private String routingKey;

    @Override
    public void transport(final Artifact artifact) throws StealthException {
        try {
            final CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
            if (username != null) {
                connectionFactory.setUsername(username);
                connectionFactory.setPassword(password);
            }
            final RabbitTemplate template = new RabbitTemplate(connectionFactory);
            template.setExchange(exchange);
            template.convertAndSend(routingKey, artifact);
        } catch (AmqpException e) {
            throw new StealthException(e);
        }
    }

    @Override
    public void configure(final Map<String, String> configuration) {
        port = Integer.valueOf(configuration.get("port").toString());
        host = configuration.get("host").toString();
        if (configuration.containsKey("username")) {
            username = configuration.get("username").toString();
            if (!configuration.containsKey("password")) {
                throw new StealthException("Username was set, but no password");
            }
            password = configuration.get("password").toString();
        }
        exchange = configuration.get("exchange").toString();
        routingKey = configuration.get("routingKey").toString();
    }

    @Override
    public String toString() {
        return "RabbitMqArtifactTransport{" +
                "port=" + port +
                ", host='" + host + '\'' +
                ", username='" + username + '\'' +
                ", password=********" +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                '}';
    }
}
