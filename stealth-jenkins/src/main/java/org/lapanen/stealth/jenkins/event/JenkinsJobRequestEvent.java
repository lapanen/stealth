package org.lapanen.stealth.jenkins.event;

import org.lapanen.stealth.naming.EventIdentifier;

public class JenkinsJobRequestEvent extends GenericJenkinsEvent {

    private final String jobName;
    private final int delay;

    public JenkinsJobRequestEvent(final EventIdentifier eventIdentifier, final String jobName) {
        this(eventIdentifier, jobName, 0);
    }

    public JenkinsJobRequestEvent(final EventIdentifier eventIdentifier, final String jobName, final int delay) {
        super(eventIdentifier);
        this.jobName = jobName;
        this.delay = delay;
    }

    public String getSourceHost() {
        return getIdentifier().getSourceComponent().getHostName();
    }

    public String getJobName() {
        return jobName;
    }

    public int getDelay() {
        return delay;
    }
}
