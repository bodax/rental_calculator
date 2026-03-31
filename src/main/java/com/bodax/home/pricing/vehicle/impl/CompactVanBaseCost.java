package com.bodax.home.pricing.vehicle.impl;

import com.bodax.home.pricing.vehicle.BaseCostStrategy;
import com.bodax.home.pricing.vehicle.DefaultCostStrategyImpl;

public class CompactVanBaseCost extends DefaultCostStrategyImpl implements BaseCostStrategy {
    private static final double RATE = 0.82;

    @Override
    public double getRate() {
        return RATE;
    }
}
