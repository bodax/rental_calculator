package com.bodax.home.service.impl;

import com.bodax.home.enums.AdditionalService;
import com.bodax.home.model.RentalRecord;
import com.bodax.home.service.AdditionalServiceFee;

/**
 * City of Zurich Congestion Fee: CHF 1.00 per km driven inside city limits.
 * Does not apply to freeway driving (cityKm is a separate input).
 */
public class CityCongestionFee implements AdditionalServiceFee {
    private static final double RATE_PER_KM = 1.00;

    @Override
    public String getLabel() {
        return "City congestion fee";
    }

    @Override
    public double calculate(RentalRecord record) {
        return record.getCityKm() * RATE_PER_KM;
    }

    @Override
    public AdditionalService getServiceType() {
        return AdditionalService.CITY_CONGESTION;
    }
}
