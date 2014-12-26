package org.lapanen.stealth.jenkins.event;

import org.lapanen.stealth.event.AbstractStealthEvent;

public class GenericJenkinsEvent extends AbstractStealthEvent implements JenkinsEvent {

    protected GenericJenkinsEvent() {
        super("jenkins");
    }
}
