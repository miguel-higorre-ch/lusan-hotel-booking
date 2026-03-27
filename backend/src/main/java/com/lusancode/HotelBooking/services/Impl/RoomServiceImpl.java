package com.lusancode.HotelBooking.services.Impl;

import com.lusancode.HotelBooking.dtos.Response;
import com.lusancode.HotelBooking.dtos.RoomDto;
import com.lusancode.HotelBooking.entities.Room;
import com.lusancode.HotelBooking.enums.RoomType;
import com.lusancode.HotelBooking.exceptions.InvalidBookingStateAndDataException;
import com.lusancode.HotelBooking.exceptions.NotFoundException;
import com.lusancode.HotelBooking.repositories.RoomRepository;
import com.lusancode.HotelBooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    //private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";

    private static final String IMAGE_DIRECTORY_FRONTEND = "D:\\Cursos\\HotelBookingProject\\hotel-booking-lusan\\angular-frontend\\public\\rooms\\";


    @Value("${file.upload-dir}")
    private String uploadDir;



    @Override
    public Response addRoom(RoomDto roomDto, MultipartFile imageFile) {
        Room roomToSave = modelMapper.map(roomDto, Room.class);
        if(imageFile != null) {
            String imagePath = saveImage(imageFile);
            roomToSave.setImageUrl(imagePath);
        }
        roomRepository.save(roomToSave);

        return Response.builder()
                .status(200)
                .message("Room successfully added")
                .build();
    }

    @Override
    public Response updateRoom(RoomDto roomDto, MultipartFile imageFile) {
        Room existingRoom = roomRepository.findById(roomDto.getId())
                .orElseThrow(() -> new NotFoundException("Room not found"));
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            existingRoom.setImageUrl(imagePath);
        }
        if (roomDto.getRoomNumber() != null && roomDto.getRoomNumber() >=0){
            existingRoom.setRoomNumber(roomDto.getRoomNumber());
        }
        if (roomDto.getPricePerNight() != null && roomDto.getPricePerNight().compareTo(BigDecimal.ZERO) >=0){
            existingRoom.setPricePerNight(roomDto.getPricePerNight());
        }
        if (roomDto.getCapacity() != null && roomDto.getCapacity()>0){
            existingRoom.setCapacity(roomDto.getCapacity());
        }
        if (roomDto.getType() != null) existingRoom.setType(roomDto.getType());

        if (roomDto.getDescription() != null ) existingRoom.setDescription(roomDto.getDescription());

        roomRepository.save(existingRoom);

        return Response.builder()
                .status(200)
                .message("Room updated successfully")
                .build();
    }

    @Override
    public Response getAllRooms() {
        var roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<RoomDto> roomDtoList = modelMapper.map(roomList, new TypeToken<List<RoomDto>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("Room list fetched successfully")
                .rooms(roomDtoList)
                .build();
    }

    @Override
    public Response getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room not found"));
        RoomDto roomDto = modelMapper.map(room, RoomDto.class);
        return Response.builder()
                .status(200)
                .message("Room fetched successfully")
                .room(roomDto)
                .build();
    }

    @Override
    public Response deleteRoom(Long id) {

        if(roomRepository.findById(id).isEmpty()){
            throw new NotFoundException("Room not found");
        }
        roomRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Room deleted successfully")
                .build();
    }

    @Override
    public Response gatAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {

        if (checkInDate.isBefore(LocalDate.now())){
            throw new InvalidBookingStateAndDataException("check in date cannot be before today");
        }
        if (checkOutDate.isBefore(checkInDate)){
            throw new InvalidBookingStateAndDataException("check out date cannot be before check in date");
        }
        if (checkInDate.isEqual(checkOutDate)){
            throw new InvalidBookingStateAndDataException("check in date cannot be same as check out date");
        }
        var roomList = roomRepository.findAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomDto> roomDotList = modelMapper.map(roomList,new TypeToken<List<RoomDto>>(){}.getType());
        return Response.builder()
                .status(200)
                .message("Available rooms fetched successfully")
                .rooms(roomDotList)
                .build();
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return Arrays.asList(RoomType.values());
    }

    @Override
    public Response searchRoom(String input) {
        var room = roomRepository.searchRooms(input);
        List<RoomDto> roomDtoList = modelMapper.map(room, new TypeToken<List<RoomDto>>(){}.getType());
        return Response.builder()
                .status(200)
                .message("Room search results fetched successfully")
                .rooms(roomDtoList)
                .build();

    }

    /** SAVE IMAGE TO DIRECTORY */
//    private String saveImage(MultipartFile imageFile){
//        if (!imageFile.getContentType().startsWith("image/")) {
//            throw new IllegalArgumentException("File is not an image");
//        }
//
//        // create directory if not exist
//        File directory = new File(IMAGE_DIRECTORY);
//        if(!directory.exists()){
//            directory.mkdir();
//        }
//        // generate unique file name
//        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
//        String imagePath = IMAGE_DIRECTORY + uniqueFileName;
//
//        try {
//            File destinationFile = new File(imagePath);
//            imageFile.transferTo(destinationFile);
//        } catch (Exception ex){
//            throw new IllegalArgumentException("Failed to save image file: " + ex.getMessage());
//        }
//
//        return imagePath;
//    }

    // SAVE IMAGE TO FRONTEND DIRECTORY
//    private String saveImageToFrontend(MultipartFile imageFile){
//        if (!imageFile.getContentType().startsWith("image/")) {
//            throw new IllegalArgumentException("File is not an image");
//        }
//
//        // create directory if not exist
//        File directory = new File(IMAGE_DIRECTORY_FRONTEND);
//        if(!directory.exists()){
//            directory.mkdir();
//        }
//        // generate unique file name
//        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
//        String imagePath = IMAGE_DIRECTORY_FRONTEND + uniqueFileName;
//
//        try {
//            File destinationFile = new File(imagePath);
//            imageFile.transferTo(destinationFile);
//        } catch (Exception ex){
//            throw new IllegalArgumentException("Failed to save image file: " + ex.getMessage());
//        }
//
//        return "/rooms/" + uniqueFileName;
//    }

    private String saveImage(MultipartFile imageFile) {
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
            log.info("Directory created at: {}", uploadDir);
        }

        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        String imagePath = directory.getAbsolutePath() + File.separator + uniqueFileName;

        try {
            imageFile.transferTo(new File(imagePath));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while saving image: " + e.getMessage());
        }

        // return only the relative URL, not the filesystem path
        return "/images/" + uniqueFileName;
    }

}
