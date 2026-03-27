package com.lusancode.HotelBooking.exceptions;

public class InvalidBookingStateAndDataException extends RuntimeException{

    public InvalidBookingStateAndDataException(String message) {
        super(message);
    }
}
