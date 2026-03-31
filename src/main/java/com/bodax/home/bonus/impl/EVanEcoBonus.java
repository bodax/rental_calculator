package com.bodax.home.bonus.impl;

import com.bodax.home.bonus.BonusRule;
import com.bodax.home.enums.VehicleType;
import com.bodax.home.model.RentalRecord;

/**
 * E-Van eco-bonus: CHF 10.00 discount if an E-Van drives more than 80 km
 * and uses less than 22 kWh per 100 km.
 *
 * <p>Assumptions:
 * <ul>
 *   <li>"more than 80 km" means strictly &gt; 80 (not &ge;)</li>
 *   <li>"less than 22 kWh/100km" means strictly &lt; 22 (not &le;)</li>
 * </ul>
 */
public class EVanEcoBonus implements BonusRule {
    private static final double BONUS_AMOUNT = 10.00;
    private static final double MIN_DISTANCE_KM = 80.0;
    private static final double MAX_KWH_PER_100KM = 22.0;

    @Override
    public String getLabel() {
        return "E-Van eco-bonus";
    }

    @Override
    public double evaluate(RentalRecord record) {
        if (record.getVehicleType() != VehicleType.E_VAN) {
            return 0.0;
        }
        if (record.getDistanceKm() <= MIN_DISTANCE_KM) {
            return 0.0;
        }
        double kwhPer100km = (record.getEnergyUsed() / record.getDistanceKm()) * 100.0;
        if (kwhPer100km < MAX_KWH_PER_100KM) {
            return BONUS_AMOUNT;
        }
        return 0.0;
    }
}
