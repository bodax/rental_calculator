package com.bodax.home;

import com.bodax.home.bonus.impl.EVanEcoBonus;
import com.bodax.home.calculator.RentalCostCalculator;
import com.bodax.home.input.RentalDataParser;
import com.bodax.home.model.RentalReceipt;
import com.bodax.home.model.RentalRecord;
import com.bodax.home.pricing.vehicle.VehiclePricingFactory;
import com.bodax.home.report.SummaryPrinter;
import com.bodax.home.service.AdditionalServiceFactory;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        var calculator = new RentalCostCalculator(
                new VehiclePricingFactory(),
                new AdditionalServiceFactory(),
                List.of(new EVanEcoBonus()));


        var parser = new RentalDataParser();
        List<RentalRecord> records = new ArrayList<>();

        for (String line : args) {
            RentalRecord record = parser.parse(line);
            records.add(record);
        }


        // --- Calculate and print ---
        List<RentalReceipt> rentalReceipts = calculator.calculateAll(records);
        SummaryPrinter printer = new SummaryPrinter();
        System.out.println(printer.print(rentalReceipts));
    }
}