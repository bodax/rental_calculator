package com.bodax.home.pricing.consumpiton;

/**
 * Strategy for calculating energy/fuel cost.
 * Fuel-based vehicles and electric vehicles have different implementations.
 */
public interface ConsumptionCostStrategy {
    double calculate(double amountConsumed);
}
