package com.bodax.home.enums;

import java.util.Arrays;
import java.util.Optional;

public enum VehicleType {
    COMPACT_VAN("Compact Van"),
    LARGE_VAN("Large Van"),
    E_VAN("E-Van");

    private final String name;

    VehicleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<VehicleType> fromName(String name) {
        return Arrays.stream(VehicleType.values()).filter(v -> v.name.equalsIgnoreCase(name)).findFirst();
    }

}
