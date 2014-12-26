package org.lapanen.stealth.naming;

import java.util.UUID;

public interface EventIdentifier {
    UUID getUUID();
    String getName();
    String getHostName();
}
