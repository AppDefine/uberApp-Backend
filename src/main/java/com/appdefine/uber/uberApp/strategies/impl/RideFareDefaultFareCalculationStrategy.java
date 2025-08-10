package com.appdefine.uber.uberApp.strategies.impl;

import com.appdefine.uber.uberApp.dto.RideRequestDto;
import com.appdefine.uber.uberApp.strategies.RideFareCalculationStrategy;

public class RideFareDefaultFareCalculationStrategy implements RideFareCalculationStrategy {
    @Override
    public double calculateFare(RideRequestDto rideRequestDto) {
        return 0;
    }
}
