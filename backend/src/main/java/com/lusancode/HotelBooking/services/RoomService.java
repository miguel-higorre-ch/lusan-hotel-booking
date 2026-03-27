package com.lusancode.HotelBooking.services;

import com.lusancode.HotelBooking.dtos.Response;
import com.lusancode.HotelBooking.dtos.RoomDto;
import com.lusancode.HotelBooking.enums.RoomType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    Response addRoom(RoomDto roomDto, MultipartFile imageFile);
    Response updateRoom(RoomDto roomDto, MultipartFile imageFile);
    Response getAllRooms();
    Response getRoomById(Long id);
    Response deleteRoom(Long id);
    Response gatAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType);
    List<RoomType> getAllRoomTypes();
    Response searchRoom(String input);
}
