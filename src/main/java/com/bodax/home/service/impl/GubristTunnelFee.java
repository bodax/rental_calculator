package com.bodax.home.service.impl;

import com.bodax.home.enums.AdditionalService;
import com.bodax.home.model.RentalRecord;
import com.bodax.home.service.AdditionalServiceFee;

/**
 * Gubrist Tunnel Fee: CHF 2.50 per passage.
 * After 2 paid passages per day, additional passages are free.
 */
public class GubristTunnelFee implements AdditionalServiceFee {
    private static final double PRICE_PER_PASSAGE = 2.50;
    private static final int MAX_PAID_PASSAGES = 2;

    @Override
    public String getLabel() {
        return "Gubrist tunnel fee";
    }

    @Override
    public double calculate(RentalRecord record) {
        int charged = Math.min(record.getGubristPassages(), MAX_PAID_PASSAGES);
        return charged * PRICE_PER_PASSAGE;
    }

    @Override
    public AdditionalService getServiceType() {
        return AdditionalService.GUBRIST_TUNNEL;
    }
}
