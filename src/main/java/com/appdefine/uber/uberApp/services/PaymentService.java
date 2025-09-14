package com.appdefine.uber.uberApp.services;

import com.appdefine.uber.uberApp.entities.Payment;
import com.appdefine.uber.uberApp.entities.Ride;
import com.appdefine.uber.uberApp.entities.enums.PaymentMethod;
import com.appdefine.uber.uberApp.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);
}
