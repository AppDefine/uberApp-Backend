package com.appdefine.uber.uberApp.controllers;

import com.appdefine.uber.uberApp.dto.RideRequestDto;
import com.appdefine.uber.uberApp.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDto> requestRide(@RequestBody RideRequestDto rideRequestDto){
        RideRequestDto rideRequestDto1 = riderService.requestRide(rideRequestDto);
        return ResponseEntity.ok(rideRequestDto1);
    }
}
