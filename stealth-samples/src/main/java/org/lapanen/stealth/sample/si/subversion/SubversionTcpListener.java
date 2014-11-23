package org.lapanen.stealth.sample.si.subversion;

import org.lapanen.stealth.si.subversion.config.TcpReceivingChannelAdapterConfig;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
public class SubversionTcpListener extends TcpReceivingChannelAdapterConfig {

    public static final int DEFAULT_LISTENING_PORT = 3600;

    private int listeningPort = DEFAULT_LISTENING_PORT;

    @Override
    protected int subversionListeningSocketPort() {
        return listeningPort;
    }

    public void setListeningPort(final int listeningPort) {
        this.listeningPort = listeningPort;
    }
}
