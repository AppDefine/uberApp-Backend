package com.appdefine.uber.uberApp.services;

import com.appdefine.uber.uberApp.dto.DriverDto;
import com.appdefine.uber.uberApp.dto.RideDto;
import com.appdefine.uber.uberApp.dto.RideRequestDto;
import com.appdefine.uber.uberApp.dto.RiderDto;
import com.appdefine.uber.uberApp.entities.Rider;
import com.appdefine.uber.uberApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId,Integer rating);

    RiderDto getMyProfile();

    Page<RideDto> getMyAllRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider getCurrentRider();
}
