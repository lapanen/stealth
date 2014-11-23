package org.lapanen.stealth.sample.si.subversion;

import java.util.List;

import org.lapanen.stealth.jenkins.JenkinsJobExtractor;
import org.lapanen.stealth.si.subversion.RegexBasedBasePathExtractor;
import org.tmatesoft.svn.core.SVNLogEntry;

public class SubversionCommitJenkinsJobExtractor implements JenkinsJobExtractor<SVNLogEntry> {

    private final RegexBasedBasePathExtractor extractor;

    public SubversionCommitJenkinsJobExtractor(final RegexBasedBasePathExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public List<String> extractJobNames(final SVNLogEntry from) {
        return extractor.extractUniqueBasePathsByRegexMatching(from);
    }
}
