package com.bodax.home.service.impl;

import com.bodax.home.enums.AdditionalService;
import com.bodax.home.model.RentalRecord;
import com.bodax.home.service.AdditionalServiceFee;

public class MotorwayVignetteFee implements AdditionalServiceFee {
    private static final double PRICE = 9.00;

    @Override
    public String getLabel() {
        return "Motorway day vignette";
    }

    @Override
    public double calculate(RentalRecord record) {
        return PRICE;
    }

    @Override
    public AdditionalService getServiceType() {
        return AdditionalService.MOTORWAY_VIGNETTE;
    }
}
