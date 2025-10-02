package com.appdefine.uber.uberApp.services.impl;

import com.appdefine.uber.uberApp.dto.DriverDto;
import com.appdefine.uber.uberApp.dto.SignupDto;
import com.appdefine.uber.uberApp.dto.UserDto;
import com.appdefine.uber.uberApp.entities.Driver;
import com.appdefine.uber.uberApp.entities.User;
import com.appdefine.uber.uberApp.entities.enums.Role;
import com.appdefine.uber.uberApp.exceptions.ResourceNotFoundException;
import com.appdefine.uber.uberApp.exceptions.RunTimeConflictException;
import com.appdefine.uber.uberApp.repositories.UserRepository;
import com.appdefine.uber.uberApp.services.AuthService;
import com.appdefine.uber.uberApp.services.DriverService;
import com.appdefine.uber.uberApp.services.RiderService;
import com.appdefine.uber.uberApp.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.appdefine.uber.uberApp.entities.enums.Role.DRIVER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;

    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    @Transactional
    public UserDto signUp(SignupDto signupDto) {
        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);
        if(user != null){
            throw new RunTimeConflictException("Cannot signup, User already exist with email "+signupDto.getEmail());
        }

        User mappedUser = modelMapper.map(signupDto,User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepository.save(mappedUser);

        riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId,String vehicleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+userId));

        if(user.getRoles().contains(DRIVER))
            throw new RunTimeConflictException("User with id "+userId+" is already a Driver");

        Driver createDriver = Driver.builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();
        user.getRoles().add(DRIVER);
        userRepository.save(user);
        Driver savedDriver = driverService.createNewDriver(createDriver);
        return modelMapper.map(savedDriver,DriverDto.class);
    }
}
