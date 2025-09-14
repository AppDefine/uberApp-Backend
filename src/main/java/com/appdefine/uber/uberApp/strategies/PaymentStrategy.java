package com.appdefine.uber.uberApp.strategies;

import com.appdefine.uber.uberApp.entities.Payment;

public interface PaymentStrategy {

    Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);
}
