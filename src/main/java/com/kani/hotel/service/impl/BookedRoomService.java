package com.kani.hotel.service.impl;

import com.kani.hotel.exception.InvalidBookingRequestException;
import com.kani.hotel.model.BookedRoom;
import com.kani.hotel.model.Room;
import com.kani.hotel.repository.IBookedRoomRepository;
import com.kani.hotel.service.IBookedRoomService;
import com.kani.hotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookedRoomService implements IBookedRoomService {
    private final IBookedRoomRepository bookedRoomRepository;
    private final IRoomService roomService;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookedRoomRepository.findAll();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookedRoomRepository.findById(bookingId);
    }

    @Override
    public BookedRoom findByBookedConfirmationCode(String confirmationCode) {
        return bookedRoomRepository.findByBookedConfirmationCode(confirmationCode);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookedRoom) {
        if(bookedRoom.getCheckOutDate().isBefore(bookedRoom.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must be come Check-out date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBooking = room.getBookedRoomList();
        boolean roomIsAvailable = roomIsAvailable(bookedRoom, existingBooking);
        if(roomIsAvailable){
            room.addBooking(bookedRoom);
            bookedRoomRepository.save(bookedRoom);
        }else{
            throw new InvalidBookingRequestException("Sorry, this room is no selected dates!!!");
        }
        return bookedRoom.getBookingConfirmationCode();
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookedRoomRepository.findByRoomId(roomId);
    }

    private boolean roomIsAvailable(BookedRoom bookedRoom, List<BookedRoom> existingBooking) {
        return existingBooking.stream().noneMatch(
                booking -> bookedRoom.getCheckInDate().equals(booking.getCheckInDate())
                || bookedRoom.getCheckOutDate().isBefore(booking.getCheckOutDate())
                || bookedRoom.getCheckInDate().isAfter(booking.getCheckInDate())
                && bookedRoom.getCheckInDate().isBefore(booking.getCheckOutDate())
                || bookedRoom.getCheckInDate().isBefore(booking.getCheckInDate())
                && bookedRoom.getCheckOutDate().equals(booking.getCheckOutDate())
                || bookedRoom.getCheckInDate().isBefore(booking.getCheckInDate())
                && bookedRoom.getCheckOutDate().isBefore(booking.getCheckOutDate())
                || bookedRoom.getCheckInDate().equals(booking.getCheckOutDate())
                && bookedRoom.getCheckInDate().equals(booking.getCheckInDate())
                || bookedRoom.getCheckInDate().equals(booking.getCheckOutDate())
                && bookedRoom.getCheckOutDate().equals(booking.getCheckInDate())
        );
    }
}
