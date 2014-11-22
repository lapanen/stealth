package org.lapanen.stealth.si.subversion.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

@Configuration
@EnableIntegration
public class SubversionComponents {

    public static final String REGEX_CAPTURE_GROUP_INDEX_HEADER_NAME = "subversion_regex_capture_group_index";

    public static final String REGEX_HEADER_NAME = "subversion_path_regex";

    private static final Logger LOG = LoggerFactory.getLogger(SubversionComponents.class);

    @Bean
    public SubscribableChannel subversionLogChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public SubscribableChannel subversionBasePathChannel() {
        return new PublishSubscribeChannel();
    }

    @Splitter(inputChannel = "subversionLogChannel", outputChannel = "subversionBasePathChannel")
    public List<String> extractUniqueBasePathsByRegexMatching(final Message<SVNLogEntry> logEntryMessage) {
        final Integer regexCaptureGroupIndex = getMandatoryHeader(logEntryMessage, REGEX_CAPTURE_GROUP_INDEX_HEADER_NAME, Integer.class);
        final Pattern pattern = getRegexPattern(logEntryMessage);
        final SVNLogEntry logEntry = logEntryMessage.getPayload();
        final Map<String, SVNLogEntryPath> paths = logEntry.getChangedPaths();
        final Set<String> matchingPaths = new HashSet<>();
        for (final Map.Entry<String, SVNLogEntryPath> path : paths.entrySet()) {
            final Matcher matcher = pattern.matcher(path.getValue().getPath());
            if (matcher.matches()) {
                final int groupCount = matcher.groupCount();
                if (groupCount >= regexCaptureGroupIndex) {
                    String group = matcher.group(regexCaptureGroupIndex);
                    LOG.debug("Found candidate '{}'", group);
                    matchingPaths.add(group);
                } else {
                    LOG.warn("capture group index set at {}, but only {} groups found in {}", new Object[] { regexCaptureGroupIndex, groupCount, pattern });
                }
            } else {
                LOG.debug("'{}' is no match for {}", path, pattern);
            }
        }
        return new ArrayList<String>(matchingPaths);
    }

    private static <T> T getMandatoryHeader(final Message<?> message, final String name, final Class<T> type) {
        if (!message.getHeaders().containsKey(name)) {
            throw new MessagingException(message, String.format("Mandatory header '%s' missing", name));
        }
        return message.getHeaders().get(name, type);
    }

    private static Pattern getRegexPattern(final Message<SVNLogEntry> message) {
        final String regex = getMandatoryHeader(message, REGEX_HEADER_NAME, String.class);
        try {
            return Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            throw new MessagingException(String.format("While compiling regex '%s'", regex), e);
        }
    }
}
