package com.bodax.home.pricing.vehicle;

/**
 * Strategy for calculating base distance cost.
 * Each vehicle type provides its own implementation.
 */
public interface BaseCostStrategy {
    double calculate(double distanceKm);
}
