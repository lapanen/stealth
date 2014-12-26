package org.lapanen.stealth.jenkins.event;

public class JenkinsJobRequestEvent extends GenericJenkinsEvent {

    private final String jobName;
    private final int delay;

    public JenkinsJobRequestEvent(final String jobName, final int delay) {
        this.jobName = jobName;
        this.delay = delay;
    }
    public JenkinsJobRequestEvent(final String jobName) {
        this(jobName, 0);
    }

    public String getSourceHost() {
        return getIdentifier().getHostName();
    }

    public String getJobName() {
        return jobName;
    }

    public int getDelay() {
        return delay;
    }
}
