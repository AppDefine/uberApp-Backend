package com.appdefine.uber.uberApp.services;

import com.appdefine.uber.uberApp.entities.RideRequest;


public interface RideRequestService {

    RideRequest findRideRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
