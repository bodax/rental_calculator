package com.bodax.home.pricing.vehicle;

import com.bodax.home.enums.VehicleType;
import com.bodax.home.pricing.consumpiton.ElectricConsumptionCost;
import com.bodax.home.pricing.consumpiton.FuelConsumptionCost;
import com.bodax.home.pricing.vehicle.impl.CompactVanBaseCost;
import com.bodax.home.pricing.vehicle.impl.EVanBaseCost;
import com.bodax.home.pricing.vehicle.impl.LargeVanBaseCost;
import com.bodax.home.pricing.consumpiton.ConsumptionCostStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Factory that returns the correct base-cost and energy-cost strategies
 * for a given vehicle type. To add a new vehicle type, simply add a case
 * and create new strategy implementations—no existing code changes are needed.
 */
public class VehiclePricingFactory {

    public record PricingPair(BaseCostStrategy baseCost, ConsumptionCostStrategy energyCost) {
    }

    private final Map<VehicleType, PricingPair> registry = new HashMap<>();

    public VehiclePricingFactory() {
        registry.put(VehicleType.COMPACT_VAN, new PricingPair(new CompactVanBaseCost(), new FuelConsumptionCost()));
        registry.put(VehicleType.LARGE_VAN, new PricingPair(new LargeVanBaseCost(), new FuelConsumptionCost()));
        registry.put(VehicleType.E_VAN, new PricingPair(new EVanBaseCost(), new ElectricConsumptionCost()));
    }

    public PricingPair getPricing(VehicleType type) {
        return Optional.ofNullable(registry.get(type))
                .orElseThrow(() -> new IllegalArgumentException("Unknown vehicle type: " + type));
    }
}
