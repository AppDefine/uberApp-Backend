package com.appdefine.uber.uberApp.services.impl;

import com.appdefine.uber.uberApp.dto.DriverDto;
import com.appdefine.uber.uberApp.dto.RideDto;
import com.appdefine.uber.uberApp.dto.RideRequestDto;
import com.appdefine.uber.uberApp.dto.RiderDto;
import com.appdefine.uber.uberApp.entities.Driver;
import com.appdefine.uber.uberApp.entities.RideRequest;
import com.appdefine.uber.uberApp.entities.Rider;
import com.appdefine.uber.uberApp.entities.User;
import com.appdefine.uber.uberApp.entities.enums.RideRequestStatus;
import com.appdefine.uber.uberApp.exceptions.ResourceNotFoundException;
import com.appdefine.uber.uberApp.repositories.RideRequestRepository;
import com.appdefine.uber.uberApp.repositories.RiderRepository;
import com.appdefine.uber.uberApp.services.RiderService;
import com.appdefine.uber.uberApp.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        Rider rider = getCurrentRider();

        RideRequest rideRequest = modelMapper.map(rideRequestDto,RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest  = rideRequestRepository.save(rideRequest);

        List<Driver> drivers = rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

        //todo :send notification to all the drivers about this ride request

        return modelMapper.map(savedRideRequest,RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RiderDto getMyProfile() {
        return null;
    }

    @Override
    public List<RideDto> getMyAllRides() {
        return List.of();
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        //todo
        return riderRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Rider not found with id: "+1));
    }
}
