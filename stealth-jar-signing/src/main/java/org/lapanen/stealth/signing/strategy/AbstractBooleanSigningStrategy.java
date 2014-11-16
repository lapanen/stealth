package org.lapanen.stealth.signing.strategy;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

public abstract class AbstractBooleanSigningStrategy extends AbstractSigningStrategy {

    private final List<SigningStrategy> strategies;

    protected AbstractBooleanSigningStrategy(final List<SigningStrategy> strategies) {
        Preconditions.checkNotNull(strategies, "Strategies must not be null");
        Preconditions.checkArgument(!strategies.isEmpty(), "Strategies must not be empty");
        this.strategies = Collections.unmodifiableList(strategies);
    }

    protected List<SigningStrategy> getStrategies() {
        return Collections.unmodifiableList(strategies);
    }

}
