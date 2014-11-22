package org.lapanen.stealth.si.subversion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lapanen.stealth.si.subversion.annotation.SubversionLogEventConsumingServiceActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Payload;
import org.springframework.messaging.handler.annotation.Header;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

/**
 */
public class RegexBasedBasePathExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(RegexBasedBasePathExtractor.class);

    private final Pattern pattern;

    private final int regexCaptureGroupIndex;

    public RegexBasedBasePathExtractor(final String regex, final int regexCaptureGroupIndex) {
        this.pattern = Pattern.compile(regex);
        this.regexCaptureGroupIndex = regexCaptureGroupIndex;
    }

    public List<String> extractUniqueBasePathsByRegexMatching(final SVNLogEntry logEntry) {
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
        return new ArrayList<>(matchingPaths);
    }

}
