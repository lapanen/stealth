package org.lapanen.stealth.jenkins.event;

import org.lapanen.stealth.event.AbstractStealthEvent;
import org.lapanen.stealth.naming.EventIdentifier;

public class GenericJenkinsEvent extends AbstractStealthEvent implements JenkinsEvent {

    protected GenericJenkinsEvent(final EventIdentifier eventIdentifier) {
        super(eventIdentifier);
    }
}
