package com.company.order_processing_engine.service;

public interface PricingService {
    double calculateDiscount(double baseTotal, boolean isPremium, boolean festivalEnabled);


     double calculateTax(double baseAmount, double discount) ;



}
