package org.lapanen.stealth.naming;

import java.util.UUID;

public interface EventIdentifier {
    UUID getUUID();
    ComponentIdentifier getSourceComponent();
}
