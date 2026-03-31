package com.bodax.home.pricing.vehicle;


public abstract class DefaultCostStrategyImpl implements BaseCostStrategy {
    @Override
    public double calculate(double distanceKm) {
        return distanceKm * getRate();
    }

    public abstract double getRate();
}
