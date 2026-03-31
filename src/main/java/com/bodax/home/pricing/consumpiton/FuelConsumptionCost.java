package com.bodax.home.pricing.consumpiton;

public class FuelConsumptionCost implements ConsumptionCostStrategy {
    private static final double RATE_PER_LITER = 1.95;

    @Override
    public double calculate(double liters) {
        return liters * RATE_PER_LITER;
    }
}
