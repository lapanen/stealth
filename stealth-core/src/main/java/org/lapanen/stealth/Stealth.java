package org.lapanen.stealth;

import org.lapanen.stealth.event.StealthEvent;
import org.lapanen.stealth.maven.artifact.Artifact;

public interface Stealth<T extends StealthEvent> {

    void handleEvent(T event);

}
