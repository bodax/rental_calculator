package com.bodax.home.pricing.vehicle.impl;

import com.bodax.home.pricing.vehicle.BaseCostStrategy;
import com.bodax.home.pricing.vehicle.DefaultCostStrategyImpl;

public class LargeVanBaseCost extends DefaultCostStrategyImpl implements BaseCostStrategy {
    private static final double RATE = 1.05;

    @Override
    public double getRate() {
        return RATE;
    }
}
