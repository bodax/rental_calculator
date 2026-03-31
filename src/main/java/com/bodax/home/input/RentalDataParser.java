package com.bodax.home.input;

import com.bodax.home.enums.AdditionalService;
import com.bodax.home.enums.VehicleType;
import com.bodax.home.model.RentalRecord;

import java.util.Collections;
import java.util.List;

public class RentalDataParser {

    public RentalRecord parse(String input) {
        String[] parts = input.split(",");

        var vehicleType = VehicleType.fromName(parts[0].trim())
                .orElseThrow(() -> new IllegalArgumentException("Unknown vehicle type: " + parts[0].trim()));

        var builder = new RentalRecord.Builder()
                .label(input)
                .vehicleType(vehicleType);

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();

            if (part.contains("km") && !part.toLowerCase().contains("city")) {
                builder.distanceKm(extractNumber(part));
            } else if (part.contains("kWh") || part.contains("liters")) {
                builder.energyUsed(extractNumber(part));
            } else if (part.toLowerCase().contains("city")) {
                builder.cityKm(extractNumber(part));
                builder.addService(AdditionalService.CITY_CONGESTION);
            } else {
                builder.addServices(parseService(part, builder));
            }
        }

        return builder.build();
    }

    private double extractNumber(String text) {
        String cleaned = text.replaceAll("[^0-9.]", "");
        return cleaned.isEmpty() ? 0 : Double.parseDouble(cleaned);
    }

    private List<AdditionalService> parseService(String input, RentalRecord.Builder builder) {
        int count = 1;

        if (input.contains("x")) {
            String[] split = input.split("x");
            input = split[0].trim();
            count = Integer.parseInt(split[1].trim());
        }

        String normalized = input.replaceAll("\\(.*\\)", "")
                .replaceAll("(?i)fee$", "")
                .trim();

        String finalInput = input;
        var service = AdditionalService.fromName(normalized)
                .orElseThrow(() -> new IllegalArgumentException("Unknown service type: " + finalInput));

        if (service == AdditionalService.GUBRIST_TUNNEL) {
            builder.gubristPassages(count);
            return Collections.singletonList(service);
        }

        return Collections.nCopies(count, service);
    }
}
