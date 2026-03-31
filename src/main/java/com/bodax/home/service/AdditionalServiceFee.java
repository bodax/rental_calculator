package com.bodax.home.service;

import com.bodax.home.enums.AdditionalService;
import com.bodax.home.model.RentalRecord;

/**
 * Interface for any additional service fee.
 * Each service provides its own fee label and calculation logic.
 */
public interface AdditionalServiceFee {

    String getLabel();

    double calculate(RentalRecord record);

    AdditionalService getServiceType();
}
