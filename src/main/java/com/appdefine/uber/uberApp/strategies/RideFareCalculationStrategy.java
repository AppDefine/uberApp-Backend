package com.appdefine.uber.uberApp.strategies;

import com.appdefine.uber.uberApp.dto.RideRequestDto;

public interface RideFareCalculationStrategy {

    double calculateFare(RideRequestDto rideRequestDto);
}
