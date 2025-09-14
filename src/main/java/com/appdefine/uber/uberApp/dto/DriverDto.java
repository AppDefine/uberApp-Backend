package com.appdefine.uber.uberApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {

    private Long id;
    private UserDto user;
    private Double rating;
    private boolean available;
    private String vehicleId;
}
