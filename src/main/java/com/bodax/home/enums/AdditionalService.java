package com.bodax.home.enums;

import java.util.Arrays;
import java.util.Optional;

public enum AdditionalService {
    MOTORWAY_VIGNETTE("Motorway Vignette"),
    GUBRIST_TUNNEL("Gubrist Tunnel"),
    CITY_CONGESTION("City Congestion");

    private final String name;

    AdditionalService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<AdditionalService> fromName(String name) {
        String normalized = name.trim().toLowerCase();
        return Arrays.stream(AdditionalService.values())
                .filter(s -> s.getName().trim().toLowerCase().equals(normalized))
                .findFirst();
    }
}
