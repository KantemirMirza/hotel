package com.kani.hotel.service;

import com.kani.hotel.model.BookedRoom;

import java.util.List;

public interface IBookedRoomService {
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    List<BookedRoom> getAllBookings();

    BookedRoom findByBookedConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookedRoom);

    void cancelBooking(Long bookingId);

}
