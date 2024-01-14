package com.kani.hotel.repository;

import com.kani.hotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookedRoomRepository extends JpaRepository<BookedRoom,Long> {
    BookedRoom findByBookedConfirmationCode(String confirmationCode);
    List<BookedRoom> findByRoomId(Long roomId);
}
