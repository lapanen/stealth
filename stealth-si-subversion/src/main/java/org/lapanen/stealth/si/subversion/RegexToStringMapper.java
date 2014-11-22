package org.lapanen.stealth.si.subversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

/**
 */
public class RegexToStringMapper {

    private final Map<Pattern, String> patterns;

    public RegexToStringMapper(final Map<String, String> patternSourceMap) {
        this.patterns = new HashMap<>();
        for (Map.Entry<String, String> map : patternSourceMap.entrySet()) {
            patterns.put(Pattern.compile(map.getKey()), map.getValue());
        }
    }

    public List<String> getMatchingStrings(final SVNLogEntry logEntry) {
        final Map<String, SVNLogEntryPath> paths = logEntry.getChangedPaths();
        final Set<String> matches = new HashSet<>();
        for (final Map.Entry<String, SVNLogEntryPath> path : paths.entrySet()) {
            for (final Map.Entry<Pattern, String> patternStringEntry : patterns.entrySet()) {
                if (patternStringEntry.getKey().matcher(path.getValue().getPath()).matches()) {
                    matches.add(patternStringEntry.getValue());
                }
            }
        }
        return new ArrayList<>(matches);
    }

}
