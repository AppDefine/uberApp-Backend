package com.appdefine.uber.uberApp.strategies.impl;

import com.appdefine.uber.uberApp.dto.RideRequestDto;
import com.appdefine.uber.uberApp.entities.Driver;
import com.appdefine.uber.uberApp.entities.RideRequest;
import com.appdefine.uber.uberApp.strategies.DriverMatchingStrategy;
import com.appdefine.uber.uberApp.strategies.RideFareCalculationStrategy;

import java.util.List;

public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {
    @Override
    public double calculateFare(RideRequest rideRequest) {
        return 0;
    }
}
