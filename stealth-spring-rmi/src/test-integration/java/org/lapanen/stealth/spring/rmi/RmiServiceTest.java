package org.lapanen.stealth.spring.rmi;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lapanen.stealth.spring.rmi.api.IntegerService;
import org.lapanen.stealth.spring.rmi.api.StringService;
import org.lapanen.stealth.spring.rmi.api.StringUpdater;
import org.lapanen.stealth.spring.rmi.client.ClientConfig;
import org.lapanen.stealth.spring.rmi.client.IntegerServiceConfig;
import org.lapanen.stealth.spring.rmi.client.StringServiceConfig;
import org.lapanen.stealth.spring.rmi.client.StringUpdaterServiceConfig;
import org.lapanen.stealth.spring.rmi.server.RmiServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RmiServiceTest.RmiConfiguration.class)
public class RmiServiceTest {

    private static AnnotationConfigApplicationContext serverContext;
    private static AnnotationConfigApplicationContext clientContext;

    @BeforeClass
    public static void setupContexts() {
        serverContext = new AnnotationConfigApplicationContext(RmiServer.class);
//        serverContext.refresh();
        clientContext = new AnnotationConfigApplicationContext(ClientConfig.class/*IntegerServiceConfig.class, StringServiceConfig.class,
                StringUpdaterServiceConfig.class*/);
//        clientContext.refresh();
    }

    @Test
    public void string_is_updated() {
        final String toUdpate = "Lapanen päähän";
        clientContext.getBean(StringUpdater.class).updateString(toUdpate);
        assertEquals(toUdpate, clientContext.getBean(StringService.class).getString());
    }

    @Configuration
    @ComponentScan(basePackages = { "org.lapanen.stealth.spring.rmi.client", "org.lapanen.stealth.spring.rmi.server" })
    public static class RmiConfiguration {
    }
}
