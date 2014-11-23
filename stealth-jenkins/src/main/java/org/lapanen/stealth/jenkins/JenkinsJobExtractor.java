package org.lapanen.stealth.jenkins;

import java.util.List;

public interface JenkinsJobExtractor<T> {
    List<String> extractJobNames(T from);
}
