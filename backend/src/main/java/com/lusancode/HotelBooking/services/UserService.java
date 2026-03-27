package com.lusancode.HotelBooking.services;

import com.lusancode.HotelBooking.dtos.LoginRequest;
import com.lusancode.HotelBooking.dtos.RegistrationRequest;
import com.lusancode.HotelBooking.dtos.Response;
import com.lusancode.HotelBooking.dtos.UserDto;
import com.lusancode.HotelBooking.entities.User;

public interface UserService {
    Response registerUser(RegistrationRequest registrationRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    Response getOwnAccountDetails();
    User getCurrentLoggedInUser();
    Response updateOwnAccount(UserDto userDto);
    Response deleteOwnAccount();
    Response getMyBookingHistory();


}
