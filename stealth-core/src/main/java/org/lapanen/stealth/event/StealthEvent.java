package org.lapanen.stealth.event;

import java.io.Serializable;

import org.joda.time.ReadableInstant;
import org.lapanen.stealth.naming.EventIdentifier;

public interface StealthEvent extends Serializable {
    EventIdentifier getIdentifier();
    ReadableInstant getCreationTime();
}
