package com.lusancode.HotelBooking.services;

import com.lusancode.HotelBooking.entities.BookingReference;
import com.lusancode.HotelBooking.repositories.BookingReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BokingCodeGenerator {

    private final BookingReferenceRepository bookingReferenceRepository;

    public String generateBookingReference(){
        String bookingReference;
        do {
            bookingReference = generateRandomAlphanumericCode(10);
        } while (isBookingReferenceExists(bookingReference));

        saveBookingReferenceToDatabase(bookingReference);
        return bookingReference;
    }

    private String generateRandomAlphanumericCode(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }

    private boolean isBookingReferenceExists(String bookingReference){
        return bookingReferenceRepository.findByReferenceNo(bookingReference).isPresent();
    }

    private void saveBookingReferenceToDatabase(String bookingReference){
        BookingReference bookingReferenceToSave = BookingReference
                .builder()
                .referenceNo(bookingReference)
                .build();

        bookingReferenceRepository.save(bookingReferenceToSave);
    }

}
