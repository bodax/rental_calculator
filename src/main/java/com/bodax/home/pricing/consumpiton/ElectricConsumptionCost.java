package com.bodax.home.pricing.consumpiton;

public class ElectricConsumptionCost implements ConsumptionCostStrategy {
    private static final double RATE_PER_KWH = 0.30;

    @Override
    public double calculate(double kWh) {
        return kWh * RATE_PER_KWH;
    }
}
