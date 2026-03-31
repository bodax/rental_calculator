package com.bodax.home.service;

import com.bodax.home.enums.AdditionalService;
import com.bodax.home.service.impl.CityCongestionFee;
import com.bodax.home.service.impl.GubristTunnelFee;
import com.bodax.home.service.impl.MotorwayVignetteFee;

import java.util.*;

/**
 * Factory that maps service keys (strings) to AdditionalServiceFee implementations.
 * New services are registered here; consumers only pass string keys.
 *
 * <p>This acts as a simple service registry. For larger systems, consider a
 * DI container or SPI-based discovery.</p>
 */
public class AdditionalServiceFactory {

    private final Map<AdditionalService, AdditionalServiceFee> registry = new HashMap<>();

    public AdditionalServiceFactory() {
        registry.put(AdditionalService.MOTORWAY_VIGNETTE, new MotorwayVignetteFee());
        registry.put(AdditionalService.GUBRIST_TUNNEL, new GubristTunnelFee());
        registry.put(AdditionalService.CITY_CONGESTION, new CityCongestionFee());
    }

    public List<AdditionalServiceFee> resolveAll(Set<AdditionalService> additionalServices) {
        return additionalServices.stream()
                .map(registry::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
