package com.lusancode.HotelBooking.services;

import com.lusancode.HotelBooking.dtos.BookingDto;
import com.lusancode.HotelBooking.dtos.Response;

public interface BookingService {
    Response getAllBookings();
    Response createBooking(BookingDto bookingDto);
    Response findBookingByReferenceNo(String bookingReference);
    Response updateBooking(BookingDto bookingDto);
}
