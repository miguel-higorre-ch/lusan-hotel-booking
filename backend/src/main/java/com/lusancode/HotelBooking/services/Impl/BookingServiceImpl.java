package com.lusancode.HotelBooking.services.Impl;

import com.lusancode.HotelBooking.dtos.BookingDto;
import com.lusancode.HotelBooking.dtos.NotificationDto;
import com.lusancode.HotelBooking.dtos.Response;
import com.lusancode.HotelBooking.entities.Booking;
import com.lusancode.HotelBooking.entities.Room;
import com.lusancode.HotelBooking.entities.User;
import com.lusancode.HotelBooking.enums.BookingStatus;
import com.lusancode.HotelBooking.enums.PaymentStatus;
import com.lusancode.HotelBooking.exceptions.InvalidBookingStateAndDataException;
import com.lusancode.HotelBooking.exceptions.NotFoundException;
import com.lusancode.HotelBooking.notification.NotificationService;
import com.lusancode.HotelBooking.repositories.BookingRepository;
import com.lusancode.HotelBooking.repositories.RoomRepository;
import com.lusancode.HotelBooking.services.BokingCodeGenerator;
import com.lusancode.HotelBooking.services.BookingService;
import com.lusancode.HotelBooking.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final BokingCodeGenerator booBokingCodeGenerator;


    @Override
    public Response getAllBookings() {
        var bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BookingDto> bookingDtoList = bookingList.stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .toList();
        for (BookingDto bookingDto: bookingDtoList){
            bookingDto.setUser(null);
            bookingDto.setRoom(null);
        }

        return Response.builder()
                .status(200)
                .message("Booking List fetched successfully")
                .bookings(bookingDtoList)
                .build();
    }

    @Override
    public Response createBooking(BookingDto bookingDto) {

        User currentUser = userService.getCurrentLoggedInUser();

        Room room = roomRepository.findById(bookingDto.getRoomId())
                .orElseThrow(()-> new NotFoundException("Room not found"));

        // Validation: check in date cannot be before today
        if (bookingDto.getCheckInDate().isBefore(LocalDate.now())){
            throw new InvalidBookingStateAndDataException("check in date cannot be before today");
        }
        // Validation: check out date cannot be before check in date
        if (bookingDto.getCheckOutDate().isBefore(bookingDto.getCheckInDate())){
            throw new InvalidBookingStateAndDataException("check out date cannot be before check in date");
        }
        // Validation: check in date cannot be same as checkout date
        if (bookingDto.getCheckInDate().isEqual(bookingDto.getCheckOutDate())){
            throw new InvalidBookingStateAndDataException("check in date cannot be same as check out date");
        }

        // Validate room availability
        boolean isRoomAvailable = bookingRepository.isRoomAvailable(room.getId(), bookingDto.getCheckInDate(), bookingDto.getCheckOutDate());
        if (!isRoomAvailable) {
            throw new InvalidBookingStateAndDataException("Room is not available to be booked");
        }
        BigDecimal totalPrice = calculateTotalPrice(room, bookingDto);
        String bookingReference = booBokingCodeGenerator.generateBookingReference();

        // Create and save to db
        Booking booking =  new Booking();
        booking.setUser(currentUser);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setBookingReference(bookingReference);
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        String paymentUrl = "http://localhost:4200/payment/"+ bookingReference + "/" + totalPrice;
        log.info("Payment Link: {}", paymentUrl);
        // Send notification
        NotificationDto notificationDto = NotificationDto.builder()
                .recipient(currentUser.getEmail())
                .subject("BOOKING CONFIRMATION")
                .body(String.format("Your booking has been created successfully. Please proceed with your payment using the payment link below " +
                        "\n%s", paymentUrl))
                .bookingReference(bookingReference)
                .build();

        notificationService.sendEmail(notificationDto);

        return Response.builder()
                .status(200)
                .message("Booking created successfully. Please check your email for payment details")
                .build();
    }

    @Override
    public Response findBookingByReferenceNo(String bookingReference) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(()-> new NotFoundException("Booking not found"));

        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);

        return Response.builder()
                .status(200)
                .message("Booking fetched successfully")
                .booking(bookingDto)
                .build();
    }

    @Override
    public Response updateBooking(BookingDto bookingDto) {
        if (bookingDto.getId() == null)
            throw new InvalidBookingStateAndDataException("Booking id is required for update");

        Booking existingBooking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(()-> new NotFoundException("Booking not found"));

        if(bookingDto.getBookingStatus() !=null)
            existingBooking.setBookingStatus(bookingDto.getBookingStatus());

        if(bookingDto.getPaymentStatus() !=null)
            existingBooking.setPaymentStatus(bookingDto.getPaymentStatus());

        bookingRepository.save(existingBooking);

        return Response.builder()
                .status(200)
                .message("Booking updated successfully")
                .build();
    }

    private BigDecimal calculateTotalPrice(Room room, BookingDto bookingDto){
        BigDecimal pricePerNight = room.getPricePerNight();
        long days = ChronoUnit.DAYS.between(bookingDto.getCheckInDate(), bookingDto.getCheckOutDate());
        return pricePerNight.multiply(BigDecimal.valueOf(days));
    }
}
