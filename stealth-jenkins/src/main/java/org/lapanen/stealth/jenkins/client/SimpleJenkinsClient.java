package org.lapanen.stealth.jenkins.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

public class SimpleJenkinsClient implements JenkinsClient {

    private static final String TRIGGER_JOB_PATTERN = "/job/{jobName}/build?delay={delaySeconds}sec";

    private final String jenkinsBaseUri;

    private final RestOperations rest;

    public SimpleJenkinsClient(final String jenkinsBaseUri, final RestOperations rest) {
        this.jenkinsBaseUri = jenkinsBaseUri;
        this.rest = rest;
    }

    @Override
    public void triggerJobImmediately(final String name) {
        triggerJob(name, 0);
    }

    @Override
    public void triggerJob(final String name, final int timeout) {
        executeGet(TRIGGER_JOB_PATTERN, name, timeout);
    }

    private void executeGet(final String pattern, final Object...uriVariables) {
        rest.execute(jenkinsBaseUri + pattern, HttpMethod.GET, null, null, uriVariables);
    }
}
