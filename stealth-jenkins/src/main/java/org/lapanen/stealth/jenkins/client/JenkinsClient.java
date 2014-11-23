package org.lapanen.stealth.jenkins.client;

public interface JenkinsClient {

    void triggerJobImmediately(String name);

    void triggerJob(String name, int timeout);
}
