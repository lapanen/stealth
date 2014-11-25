package org.lapanen.stealth.signing.strategy;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

public abstract class AbstractBooleanSigningStrategy extends AbstractSigningStrategy {

    private final SigningStrategy[] strategies;

    protected AbstractBooleanSigningStrategy(final SigningStrategy ... strategies) {
        Preconditions.checkNotNull(strategies, "Strategies must not be null");
        Preconditions.checkArgument(strategies.length > 0, "Strategies must not be empty");
        this.strategies = strategies;
    }

    protected SigningStrategy[] getStrategies() {
        return strategies;
    }

}
