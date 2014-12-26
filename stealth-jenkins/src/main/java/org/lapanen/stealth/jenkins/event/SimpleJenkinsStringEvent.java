package org.lapanen.stealth.jenkins.event;

import org.lapanen.stealth.event.AbstractStealthEvent;
import org.lapanen.stealth.jenkins.event.JenkinsEvent;
import org.lapanen.stealth.naming.EventIdentifier;

public class SimpleJenkinsStringEvent extends AbstractStealthEvent implements JenkinsEvent {

    private final String message;

    public SimpleJenkinsStringEvent(final String message) {
        super("jenkins");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SimpleJenkinsEvent{" +
                "message='" + message + '\'' +
                '}';
    }

}
