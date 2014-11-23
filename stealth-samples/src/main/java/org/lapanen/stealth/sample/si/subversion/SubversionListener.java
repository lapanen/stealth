package org.lapanen.stealth.sample.si.subversion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lapanen.stealth.jenkins.JenkinsJobExtractor;
import org.lapanen.stealth.jenkins.client.JenkinsClient;
import org.lapanen.stealth.jenkins.client.SimpleJenkinsClient;
import org.lapanen.stealth.si.subversion.RegexBasedBasePathExtractor;
import org.lapanen.stealth.si.subversion.RegexToStringMapper;
import org.lapanen.stealth.si.subversion.annotation.SubversionLogEventConsumingServiceActivator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.client.RestTemplate;
import org.tmatesoft.svn.core.SVNLogEntry;

/**
 */
@Configuration
@EnableIntegration
public class SubversionListener {

    @Bean
    public JenkinsJobExtractor<SVNLogEntry> jenkinsJobExtractor() {
        return new SubversionCommitJenkinsJobExtractor(new RegexBasedBasePathExtractor("/foo/([a-z0-9\\-]*)/(.*)", 1));
    }

    @SubversionLogEventConsumingServiceActivator(outputChannel = "jenkinsJobChannel")
    public List<String> extractJobNames(SVNLogEntry logEntry) {
        return jenkinsJobExtractor().extractJobNames(logEntry);
    }

    @ServiceActivator(inputChannel = "jenkinsJobChannel")
    public void triggerJenkinsJobs(final List<String> names) {
        for (final String name : names) {
            jenkins().triggerJobImmediately(name);
        }
    }

    @Bean
    public JenkinsClient jenkins() {
        return new SimpleJenkinsClient("http://lapanen.org/jenkins", new RestTemplate());
    }

    @Bean
    public SubscribableChannel jenkinsJobChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public RegexToStringMapper shellScriptAllocator() {
        final Map<String, String> scripts = new HashMap<>();
        return new RegexToStringMapper(scripts);
    }

    @SubversionLogEventConsumingServiceActivator(outputChannel = "shellScriptChannel")
    public List<String> getScripts(SVNLogEntry logEntry) {
        return shellScriptAllocator().getMatchingStrings(logEntry);
    }

    @Bean
    public SubscribableChannel shellScriptChannel() {
        return new PublishSubscribeChannel();
    }

}
