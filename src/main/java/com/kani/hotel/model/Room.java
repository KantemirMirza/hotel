package com.kani.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.maven.surefire.shared.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String roomType;

    @Column(nullable = false)
    private BigDecimal roomPrice;
    private boolean isBooked = false;

    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookedRoomList;

    public Room() {
        this.bookedRoomList = new ArrayList<>();
    }

    public void addBooking(BookedRoom bookedRoom){
        if(bookedRoomList == null){
            bookedRoomList = new ArrayList<>();
        }
        bookedRoomList.add(bookedRoom);
        bookedRoom.setRoom(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        bookedRoom.setBookingConfirmationCode(bookingCode);
    }

}
