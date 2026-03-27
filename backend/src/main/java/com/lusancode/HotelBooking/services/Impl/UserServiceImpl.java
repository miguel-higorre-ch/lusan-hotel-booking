package com.lusancode.HotelBooking.services.Impl;

import com.lusancode.HotelBooking.dtos.*;
import com.lusancode.HotelBooking.entities.User;
import com.lusancode.HotelBooking.enums.UserRole;
import com.lusancode.HotelBooking.exceptions.InvalidCredentialException;
import com.lusancode.HotelBooking.repositories.BookingRepository;
import com.lusancode.HotelBooking.repositories.UserRepository;
import com.lusancode.HotelBooking.security.JwtUtils;
import com.lusancode.HotelBooking.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;

    @Override
    public Response registerUser(RegistrationRequest registrationRequest) {
        log.info("Inside registerUser");

        UserRole role = UserRole.CUSTOMER;

        if (registrationRequest.getRole() != null) {
            role = registrationRequest.getRole();
        }

        User userToSave = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .active(true)
                .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User  registered successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        log.info("Inside loginUser");
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialException("Password does not match");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User logged in successfully")
                .role(user.getRole())
                .token(token)
                .active(user.isActive())
                .expirationTime("6 Months")
                .build();
    }

    @Override
    public Response getAllUsers() {
        log.info("Inside getAllUsers");
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<UserDto> userDtoList = modelMapper.map(users, new TypeToken<List<UserDto>>(){}.getType());
        return Response.builder()
                .status(200)
                .message("User list fetched successfully")
                .users(userDtoList)
                .build();
    }

    @Override
    public Response getOwnAccountDetails() {

        log.info("Inside get own account details");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return Response.builder()
                .status(200)
                .message("User details fetched successfully")
                .user(userDto)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        log.info("Inside getCurrentLoggedInUser");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public Response updateOwnAccount(UserDto userDto) {
        log.info("Inside updateOwnAccount");
        User existingUser = getCurrentLoggedInUser();
        if (userDto.getEmail() != null) existingUser.setEmail(userDto.getEmail());
        if (userDto.getFirstName() != null) existingUser.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) existingUser.setLastName(userDto.getLastName());
        if (userDto.getPhoneNumber() != null) existingUser.setPhoneNumber(userDto.getPhoneNumber());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User details updated successfully")
                .build();
    }

    @Override
    public Response deleteOwnAccount() {
        log.info("Inside deleteOwnAccount");
        User existingUser = getCurrentLoggedInUser();
        userRepository.delete(existingUser);
        return Response.builder()
                .status(200)
                .message("User account deleted successfully")
                .build();
    }

    @Override
    public Response getMyBookingHistory() {
        log.info("Inside getMyBookingHistory");

        User currentUser = getCurrentLoggedInUser();
        var bookingList = bookingRepository.findByUserId(currentUser.getId());

        List<BookingDto> bookingDTOList = modelMapper
                .map(bookingList, new TypeToken<List<BookingDto>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("Booking history fetched successfully")
                .bookings(bookingDTOList)
                .build();
    }
}
