package org.lapanen.stealth.jenkins;

public class JenkinsException extends RuntimeException {
    public JenkinsException(final Throwable cause) {
        super(cause);
    }

    public JenkinsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JenkinsException() {
    }

    public JenkinsException(final String message) {
        super(message);
    }
}
