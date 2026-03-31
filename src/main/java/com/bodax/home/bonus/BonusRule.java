package com.bodax.home.bonus;

import com.bodax.home.model.RentalRecord;

/**
 * Interface for any reward / bonus rule.
 * Return 0.0 if the rule does not apply.
 */
public interface BonusRule {

    String getLabel();

    /**
     * @return the discount amount (positive value), or 0.0 if not applicable
     */
    double evaluate(RentalRecord record);
}
