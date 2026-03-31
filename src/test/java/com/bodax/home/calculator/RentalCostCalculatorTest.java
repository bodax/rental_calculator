package com.bodax.home.calculator;

import com.bodax.home.bonus.impl.EVanEcoBonus;
import com.bodax.home.enums.AdditionalService;
import com.bodax.home.enums.VehicleType;
import com.bodax.home.model.RentalReceipt;
import com.bodax.home.model.RentalRecord;
import com.bodax.home.pricing.vehicle.VehiclePricingFactory;
import com.bodax.home.service.AdditionalServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RentalCostCalculatorTest {

    private RentalCostCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new RentalCostCalculator(
                new VehiclePricingFactory(),
                new AdditionalServiceFactory(),
                List.of(new EVanEcoBonus())
        );
    }

    // ═══════════════════════════════════════════════════════════════════
    //  Base Cost
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Base distance cost")
    class BaseCost {

        @Test
        @DisplayName("Compact Van: 40 km × 0.82 = CHF 32.80")
        void compactVan() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.COMPACT_VAN, 40, 0));
            assertEquals(32.80, bd.getLineItems().get("Base distance cost: "), 0.001);
        }

        @Test
        @DisplayName("Large Van: 180 km × 1.05 = CHF 189.00")
        void largeVan() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.LARGE_VAN, 180, 0));
            assertEquals(189.00, bd.getLineItems().get("Base distance cost: "), 0.001);
        }

        @Test
        @DisplayName("E-Van: 95 km × 0.68 = CHF 64.60")
        void eVan() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.E_VAN, 95, 0));
            assertEquals(64.60, bd.getLineItems().get("Base distance cost: "), 0.001);
        }

        @Test
        @DisplayName("Zero distance = CHF 0.00")
        void zeroDistance() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.COMPACT_VAN, 0, 0));
            assertEquals(0.00, bd.getLineItems().get("Base distance cost: "), 0.001);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  Energy / Fuel Cost
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Energy / fuel cost")
    class EnergyCost {

        @Test
        @DisplayName("Compact Van: 5 L × 1.95 = CHF 9.75")
        void compactVanFuel() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.COMPACT_VAN, 40, 5));
            assertEquals(9.75, bd.getLineItems().get("Energy / fuel cost"), 0.001);
        }

        @Test
        @DisplayName("Large Van: 15 L × 1.95 = CHF 29.25")
        void largeVanFuel() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.LARGE_VAN, 180, 15));
            assertEquals(29.25, bd.getLineItems().get("Energy / fuel cost"), 0.001);
        }

        @Test
        @DisplayName("E-Van: 20 kWh × 0.30 = CHF 6.00")
        void eVanElectricity() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.E_VAN, 95, 20));
            assertEquals(6.00, bd.getLineItems().get("Energy / fuel cost"), 0.001);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  Motorway Vignette
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Motorway vignette")
    class MotorwayVignette {

        @Test
        @DisplayName("CHF 9.00 when service requested")
        void applied() {
            RentalRecord r = new RentalRecord.Builder().vehicleType(VehicleType.E_VAN).distanceKm(50)
                    .addService(AdditionalService.MOTORWAY_VIGNETTE).build();
            RentalReceipt bd = calculator.calculate(r);
            assertEquals(9.00, bd.getLineItems().get("Motorway day vignette"), 0.001);
        }

        @Test
        @DisplayName("Absent when not requested")
        void notApplied() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.COMPACT_VAN, 40, 5));
            assertNull(bd.getLineItems().get("Motorway day vignette"));
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  Gubrist Tunnel
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Gubrist tunnel fee")
    class GubristTunnel {

        @Test
        @DisplayName("1 passage = CHF 2.50")
        void onePassage() {
            RentalRecord r = gubristRecord(1);
            assertEquals(2.50, calculator.calculate(r).getLineItems().get("Gubrist tunnel fee"), 0.001);
        }

        @Test
        @DisplayName("2 passages = CHF 5.00")
        void twoPassages() {
            RentalRecord r = gubristRecord(2);
            assertEquals(5.00, calculator.calculate(r).getLineItems().get("Gubrist tunnel fee"), 0.001);
        }

        @Test
        @DisplayName("3 passages capped at CHF 5.00 (third is free)")
        void threePassages() {
            RentalRecord r = gubristRecord(3);
            assertEquals(5.00, calculator.calculate(r).getLineItems().get("Gubrist tunnel fee"), 0.001);
        }

        @Test
        @DisplayName("10 passages still capped at CHF 5.00")
        void manyPassages() {
            RentalRecord r = gubristRecord(10);
            assertEquals(5.00, calculator.calculate(r).getLineItems().get("Gubrist tunnel fee"), 0.001);
        }

        private RentalRecord gubristRecord(int passages) {
            return new RentalRecord.Builder().vehicleType(VehicleType.COMPACT_VAN).distanceKm(40)
                    .addService(AdditionalService.GUBRIST_TUNNEL)
                    .gubristPassages(passages).build();
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  City Congestion Fee
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("City congestion fee")
    class CityCongestion {

        @Test
        @DisplayName("30 city km × 1.00 = CHF 30.00")
        void applied() {
            RentalRecord r = new RentalRecord.Builder().vehicleType(VehicleType.LARGE_VAN).distanceKm(180)
                    .addService(AdditionalService.CITY_CONGESTION)
                    .cityKm(30).build();
            RentalReceipt bd = calculator.calculate(r);
            assertEquals(30.00, bd.getLineItems().get("City congestion fee"), 0.001);
        }

        @Test
        @DisplayName("Absent when not requested")
        void notApplied() {
            RentalReceipt bd = calculator.calculate(record(VehicleType.LARGE_VAN, 180, 15));
            assertNull(bd.getLineItems().get("City congestion fee"));
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  Eco-Bonus
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("E-Van eco-bonus")
    class EcoBonus {

        @Test
        @DisplayName("Applied: 95 km, 20 kWh → 21.05 kWh/100km < 22 → CHF 10.00 discount")
        void applied() {
            RentalRecord r = record(VehicleType.E_VAN, 95, 20);
            RentalReceipt bd = calculator.calculate(r);
            assertEquals(10.00, bd.getBonusDiscount(), 0.001);
        }

        @Test
        @DisplayName("Not applied: distance = 80 km (must be > 80)")
        void notAppliedExactly80km() {
            RentalRecord r = record(VehicleType.E_VAN, 80, 15);
            RentalReceipt bd = calculator.calculate(r);
            assertEquals(0.0, bd.getBonusDiscount(), 0.001);
        }

        @Test
        @DisplayName("Not applied: 81 km but consumption = 22 kWh/100km (must be < 22)")
        void notAppliedExactly22kwhPer100km() {
            // 81 km, 17.82 kWh → exactly 22.0 kWh/100km → not < 22 → no bonus
            RentalRecord r = record(VehicleType.E_VAN, 81, 17.82);
            RentalReceipt bd = calculator.calculate(r);
            assertEquals(0.0, bd.getBonusDiscount(), 0.001);
        }

        @Test
        @DisplayName("Not applied: consumption ≥ 22 kWh/100km")
        void notAppliedHighConsumption() {
            // 95 km, 21.5 kWh → 22.63 kWh/100km → no bonus
            RentalRecord r = record(VehicleType.E_VAN, 95, 21.5);
            RentalReceipt bd = calculator.calculate(r);
            assertEquals(0.0, bd.getBonusDiscount(), 0.001);
        }

        @Test
        @DisplayName("Not applied: non-E-Van vehicle")
        void notAppliedToFuelVan() {
            RentalRecord r = record(VehicleType.COMPACT_VAN, 95, 10);
            RentalReceipt bd = calculator.calculate(r);
            assertEquals(0.0, bd.getBonusDiscount(), 0.001);
        }

        @Test
        @DisplayName("Edge: 81 km, 17.80 kWh → 21.98 kWh/100km → bonus applies")
        void edgeCaseJustUnder() {
            RentalRecord r = record(VehicleType.E_VAN, 81, 17.80);
            RentalReceipt bd = calculator.calculate(r);
            assertEquals(10.00, bd.getBonusDiscount(), 0.001);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  Full Subtotal Integration
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Full subtotal (integration)")
    class FullSubtotal {

        @Test
        @DisplayName("E-Van #1: 64.60 + 6.00 + 9.00 + 5.00 − 10.00 = CHF 74.60")
        void eVanSubtotal() {
            RentalRecord r = new RentalRecord.Builder().vehicleType(VehicleType.E_VAN).distanceKm(95)
                    .label("E-Van #1").energyUsed(20)
                    .addService(AdditionalService.MOTORWAY_VIGNETTE)
                    .addService(AdditionalService.GUBRIST_TUNNEL)
                    .gubristPassages(3).build();
            assertEquals(74.60, calculator.calculate(r).getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Compact Van #2: 32.80 + 9.75 = CHF 42.55")
        void compactVanSubtotal() {
            RentalRecord r = new RentalRecord.Builder().vehicleType(VehicleType.COMPACT_VAN).distanceKm(40)
                    .label("Compact Van #2").energyUsed(5).build();
            assertEquals(42.55, calculator.calculate(r).getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Large Van #3: 189.00 + 29.25 + 30.00 = CHF 248.25")
        void largeVanSubtotal() {
            RentalRecord r = new RentalRecord.Builder()
                    .vehicleType(VehicleType.LARGE_VAN).distanceKm(180)
                    .label("Large Van #3").energyUsed(15)
                    .addService(AdditionalService.CITY_CONGESTION)
                    .cityKm(30).build();
            assertEquals(248.25, calculator.calculate(r).getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Grand total: 74.60 + 42.55 + 248.25 = CHF 365.40")
        void grandTotal() {
            List<RentalRecord> records = List.of(
                    new RentalRecord.Builder()
                            .vehicleType(VehicleType.E_VAN)
                            .distanceKm(95)
                            .energyUsed(20)
                            .addService(AdditionalService.MOTORWAY_VIGNETTE)
                            .addService(AdditionalService.GUBRIST_TUNNEL)
                            .gubristPassages(3).build(),
                    new RentalRecord.Builder()
                            .vehicleType(VehicleType.COMPACT_VAN)
                            .distanceKm(40)
                            .energyUsed(5).build(),
                    new RentalRecord.Builder()
                            .vehicleType(VehicleType.LARGE_VAN)
                            .distanceKm(180)
                            .energyUsed(15)
                            .addService(AdditionalService.CITY_CONGESTION)
                            .cityKm(30).build()
            );
            double total = calculator.calculateAll(records).stream()
                    .mapToDouble(RentalReceipt::getSubtotal).sum();
            assertEquals(365.40, total, 0.001);
        }
    }

    private RentalRecord record(VehicleType type, double km, double energy) {
        return new RentalRecord.Builder().vehicleType(type).distanceKm(km).energyUsed(energy).build();
    }
}
