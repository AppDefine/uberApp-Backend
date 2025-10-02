package com.appdefine.uber.uberApp.services;

import com.appdefine.uber.uberApp.dto.DriverDto;
import com.appdefine.uber.uberApp.dto.RiderDto;
import com.appdefine.uber.uberApp.entities.Driver;
import com.appdefine.uber.uberApp.entities.Ride;
import com.appdefine.uber.uberApp.entities.Rider;

public interface RatingService {

    DriverDto rateDriver(Ride ride, Integer rating);
    RiderDto rateRider(Ride ride, Integer rating);
    void createNewRating(Ride ride);
}
