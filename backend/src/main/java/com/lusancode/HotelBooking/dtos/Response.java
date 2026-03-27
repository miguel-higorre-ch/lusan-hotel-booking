package com.lusancode.HotelBooking.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lusancode.HotelBooking.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int status;
    private String message;

    // for login
    private String token;
    private UserRole role;
    private Boolean active;
    private String expirationTime;

    // user data
    private UserDto user;
    private List<UserDto> users;

    // booking dat
    private BookingDto booking;
    private List<BookingDto> bookings;

    // room data
    private RoomDto room;
    private List<RoomDto> rooms;

    // room payments
    private String transactionId;
    private PaymentDto payment;
    private List<PaymentDto> payments;

    // room notifications
    private NotificationDto notification;
    private List<NotificationDto> notifications;

    private final LocalDateTime time = LocalDateTime.now();

}
