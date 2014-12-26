package org.lapanen.stealth.jenkins.client;

public interface JenkinsJobClient {

    void triggerJobImmediately(String requestingHost, String jobName);

    void triggerJob(String requestingHost, String jobName, int delay);
}
