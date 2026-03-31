package com.bodax.home.model;

import com.bodax.home.enums.AdditionalService;
import com.bodax.home.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable input record for one vehicle rental in a single day.
 *
 * <p>Assumptions:
 * <ul>
 *   <li>{@code energyUsed} is in liters for COMPACT_VAN/LARGE_VAN and kWh for E_VAN</li>
 *   <li>{@code cityKm} is a subset of {@code distanceKm}, provided separately by the operator</li>
 *   <li>{@code gubristPassages} counts how many times the vehicle crossed the Gubrist tunnel</li>
 *   <li>{@code serviceKeys} are arbitrary string keys mapped to concrete service fee implementations via factory</li>
 * </ul>
 */
public class RentalRecord {

    private final String label;
    private final VehicleType vehicleType;
    private final double distanceKm;
    private final double energyUsed;
    private final List<AdditionalService> additionalServices;
    private final double cityKm;
    private final int gubristPassages;

    private RentalRecord(Builder builder) {
        this.label = builder.label;
        this.vehicleType = builder.vehicleType;
        this.distanceKm = builder.distanceKm;
        this.energyUsed = builder.energyUsed;
        this.additionalServices = List.copyOf(builder.serviceKeys);
        this.cityKm = builder.cityKm;
        this.gubristPassages = builder.gubristPassages;
    }

    public String getLabel() {
        return label;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getEnergyUsed() {
        return energyUsed;
    }

    public List<AdditionalService> getAdditionalServices() {
        return additionalServices;
    }

    public double getCityKm() {
        return cityKm;
    }

    public int getGubristPassages() {
        return gubristPassages;
    }

    public static class Builder {
        private String label = "";
        private VehicleType vehicleType;
        private double distanceKm;
        private double energyUsed;
        private final List<AdditionalService> serviceKeys = new ArrayList<>();
        private double cityKm;
        private int gubristPassages;


        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder vehicleType(VehicleType vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }

        public Builder distanceKm(double distanceKm) {
            this.distanceKm = distanceKm;
            return this;
        }

        public Builder energyUsed(double val) {
            this.energyUsed = val;
            return this;
        }

        public Builder addService(AdditionalService key) {
            this.serviceKeys.add(key);
            return this;
        }

        public Builder addServices(List<AdditionalService> keys) {
            this.serviceKeys.addAll(keys);
            return this;
        }

        public Builder cityKm(double val) {
            this.cityKm = val;
            return this;
        }

        public Builder gubristPassages(int val) {
            this.gubristPassages = val;
            return this;
        }

        public RentalRecord build() {
            validate();
            return new RentalRecord(this);
        }

        private void validate() {
            if (vehicleType == null) {
                throw new IllegalStateException("Vehicle type is required");
            }
        }
    }
}
