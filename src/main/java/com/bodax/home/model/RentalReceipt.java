package com.bodax.home.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rental result for a single vehicle.
 * Line items are stored in insertion order for clean receipt printing.
 */
public class RentalReceipt {

    private final String vehicleLabel;
    private final LinkedHashMap<String, Double> lineItems = new LinkedHashMap<>();
    private double bonusDiscount = 0.0;
    private String bonusLabel;

    public RentalReceipt(String vehicleLabel) {
        this.vehicleLabel = vehicleLabel;
    }

    public void addLineItem(String description, double amount) {
        lineItems.merge(description, amount, Double::sum);
    }

    public void applyBonus(String label, double discount) {
        this.bonusLabel = label;
        this.bonusDiscount += discount;
    }

    public String getVehicleLabel() {
        return vehicleLabel;
    }

    public Map<String, Double> getLineItems() {
        return Collections.unmodifiableMap(lineItems);
    }

    public double getBonusDiscount() {
        return bonusDiscount;
    }

    public String getBonusLabel() {
        return bonusLabel;
    }

    public double getSubtotal() {
        double sum = lineItems.values().stream().mapToDouble(Double::doubleValue).sum();
        return sum - bonusDiscount;
    }
}
