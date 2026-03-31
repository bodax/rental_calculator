package com.bodax.home.report;

import com.bodax.home.model.RentalReceipt;

import java.util.List;
import java.util.Map;

/**
 * Formats a list of CostBreakdowns into a human-readable receipt-style daily summary.
 */
public class SummaryPrinter {

    private static final String LINE = "--------------------------------------------";
    private static final String DOUBLE_LINE = "============================================";

    public String print(List<RentalReceipt> breakdowns) {
        StringBuilder sb = new StringBuilder();

        sb.append(DOUBLE_LINE).append("\n");
        sb.append("   JEFF'S CAR RENTAL – DAILY COST SUMMARY  \n");
        sb.append(DOUBLE_LINE).append("\n");

        double grandTotal = 0.0;

        for (RentalReceipt bd : breakdowns) {
            sb.append("\n");
            sb.append("Vehicle : ").append(bd.getVehicleLabel()).append("\n");
            sb.append(LINE).append("\n");

            for (Map.Entry<String, Double> entry : bd.getLineItems().entrySet()) {
                sb.append(formatLine(entry.getKey(), entry.getValue()));
            }

            if (bd.getBonusDiscount() > 0) {
                sb.append(formatLine(bd.getBonusLabel() + " (discount)", -bd.getBonusDiscount()));
            }

            sb.append(LINE).append("\n");
            sb.append(formatLine("SUBTOTAL", bd.getSubtotal()));

            grandTotal += bd.getSubtotal();
        }

        sb.append("\n").append(DOUBLE_LINE).append("\n");
        sb.append(formatLine("GRAND TOTAL", grandTotal));
        sb.append(DOUBLE_LINE).append("\n");

        return sb.toString();
    }

    private String formatLine(String label, double amount) {
        return String.format("  %-30s CHF %8.2f%n", label, amount);
    }
}
