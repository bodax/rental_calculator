package com.bodax.home.calculator;

import com.bodax.home.bonus.BonusRule;
import com.bodax.home.enums.AdditionalService;
import com.bodax.home.model.RentalReceipt;
import com.bodax.home.model.RentalRecord;
import com.bodax.home.pricing.vehicle.VehiclePricingFactory;
import com.bodax.home.service.AdditionalServiceFactory;
import com.bodax.home.service.AdditionalServiceFee;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Orchestrates the full cost calculation for one or more rental records.
 * Composed of pluggable strategies, service fees, and bonus rules.
 */
public class RentalCostCalculator {

    private final VehiclePricingFactory pricingFactory;
    private final AdditionalServiceFactory serviceFactory;
    private final List<BonusRule> bonusRules;

    public RentalCostCalculator(VehiclePricingFactory pricingFactory,
                                AdditionalServiceFactory serviceFactory,
                                List<BonusRule> bonusRules) {
        this.pricingFactory = pricingFactory;
        this.serviceFactory = serviceFactory;
        this.bonusRules = bonusRules;
    }

    public RentalReceipt calculate(RentalRecord record) {
        var rentalReceipt = new RentalReceipt(record.getLabel());

        //Base cost (strategy) per vehicle type and construction-based pricing
        var pricing = pricingFactory.getPricing(record.getVehicleType());
        rentalReceipt.addLineItem("Base distance cost: ", pricing.baseCost().calculate(record.getDistanceKm()));
        rentalReceipt.addLineItem("Energy / fuel cost", pricing.energyCost().calculate(record.getEnergyUsed()));

        calculateAdditionalServices(record, rentalReceipt);
        calculateBonuses(record, rentalReceipt);

        return rentalReceipt;
    }

    public List<RentalReceipt> calculateAll(List<RentalRecord> records) {
        return records.stream().map(this::calculate).toList();
    }

    private void calculateAdditionalServices(RentalRecord record, RentalReceipt rentalReceipt) {
//        Map<AdditionalService, Integer> serviceCounts = record.getAdditionalServices().stream().map(service
//                -> {
//
//        })


        // Resolve unique fees
        Set<AdditionalService> uniqueServices = new HashSet<>(record.getAdditionalServices());
        List<AdditionalServiceFee> fees = serviceFactory.resolveAll(uniqueServices);

        fees.forEach(f -> {
            // Count occurrences in the original list
            long count = record.getAdditionalServices().stream()
                    .filter(s -> s == f.getServiceType())
                    .count();

            // Calculate single unit fee
            double unitAmount = f.calculate(record);

            double total = unitAmount * count;

            if (total > 0) {
                String label = f.getLabel();
                if (count > 1) {
                    label += " x" + count; // show multiplier
                }
                rentalReceipt.addLineItem(label, total);
            }
        });
    }

    private void calculateBonuses(RentalRecord record, RentalReceipt rentalReceipt) {
        bonusRules.forEach(rule -> {
            double discount = rule.evaluate(record);
            if (discount > 0) {
                rentalReceipt.applyBonus(rule.getLabel(), discount);
            }
        });
    }

}
