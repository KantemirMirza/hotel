package com.kani.hotel.controller;

import com.kani.hotel.dto.BookedRoomResponse;
import com.kani.hotel.dto.RoomResponse;
import com.kani.hotel.exception.InvalidBookingRequestException;
import com.kani.hotel.exception.ResourceNotFoundException;
import com.kani.hotel.model.BookedRoom;
import com.kani.hotel.model.Room;
import com.kani.hotel.service.IBookedRoomService;
import com.kani.hotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookedRoomController {
    private final IBookedRoomService bookedRoomService;
    private final IRoomService roomService;

    @GetMapping("/getAll")
    public ResponseEntity<List<BookedRoomResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookedRoomService.getAllBookings();
        List<BookedRoomResponse> bookedRoomResponses = new ArrayList<>();
        for(BookedRoom booking : bookings){
            BookedRoomResponse bookedRoomResponse = getBookingResponse(booking);
            bookedRoomResponses.add(bookedRoomResponse);
        }
        return ResponseEntity.ok(bookedRoomResponses);
    }

    @GetMapping("/confirmationCode/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try{
            BookedRoom bookedRoom = bookedRoomService.findByBookedConfirmationCode(confirmationCode);
            BookedRoomResponse bookedRoomResponse = getBookingResponse(bookedRoom);
            return ResponseEntity.ok(bookedRoomResponse);
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody BookedRoom bookedRoom){
        try{
            String confirmationCode = bookedRoomService.saveBooking(roomId, bookedRoom);
            return ResponseEntity.ok("Room Booked Successfully, Confirmation Code is:" + confirmationCode);
        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cancel/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookedRoomService.cancelBooking(bookingId);
    }

    private BookedRoomResponse getBookingResponse(BookedRoom booking) {
        Room room = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse roomResponse = new RoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getRoomPrice());
        return new BookedRoomResponse(
                booking.getId(), booking.getCheckInDate(), booking.getCheckOutDate(),
                booking.getGuestFullName(), booking.getGuestEmail(), booking.getNumberOfAdults(),
                booking.getNumberOfChildren(), booking.getTotalOfGuest(),
                booking.getBookingConfirmationCode(), roomResponse);
    }
}
