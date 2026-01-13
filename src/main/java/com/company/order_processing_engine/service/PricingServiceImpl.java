package com.company.order_processing_engine.service;

public class PricingServiceImpl implements PricingService {

    @Override
    public double calculateDiscount(double baseTotal,
                                    boolean isPremium,
                                    boolean festivalEnabled) {

        double discountPercent = 0;

        // Base slab
        if (baseTotal >= 25000) {
            discountPercent = 10;
        } else if (baseTotal >= 10000) {
            discountPercent = 5;
        }

        // Premium extra
        if (isPremium) {
            discountPercent += 5;
        }

        // Festival extra
        if (festivalEnabled) {
            discountPercent += 5;
        }

        // Max cap
        if (discountPercent > 25) {
            discountPercent = 25;
        }

        return baseTotal * (discountPercent / 100.0);
    }


    @Override
    public double calculateTax(double baseAmount, double discount) {

        double taxableAmount = baseAmount - discount;
        double tax = taxableAmount * 0.18;

        return Math.round(tax * 100.0) / 100.0;
    }


}
