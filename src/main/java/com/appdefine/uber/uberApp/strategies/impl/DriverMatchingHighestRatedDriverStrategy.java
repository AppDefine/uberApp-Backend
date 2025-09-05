package com.appdefine.uber.uberApp.strategies.impl;

import com.appdefine.uber.uberApp.entities.Driver;
import com.appdefine.uber.uberApp.entities.RideRequest;
import com.appdefine.uber.uberApp.repositories.DriverRepository;
import com.appdefine.uber.uberApp.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findTenNearbyTopRatedDrivers(rideRequest.getPickupLocation());
    }
}
