package com.example;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class BookingSystemTest {
    MockTimeProvider mockTimeProvider;
    MockRoomRepository mockerRoomRepository;
    MockNotificationService mockNotificationService;
    BookingSystem bookingSystem;
    LocalDateTime startTime;
    LocalDateTime endTime;
    LocalDateTime constantTime;
    LocalDateTime pastTime;

    @BeforeEach
            void setUp() {

        mockTimeProvider = new MockTimeProvider();
        mockerRoomRepository = new MockRoomRepository();
        mockNotificationService = new MockNotificationService();
       var mockerRoomRepository1 = addRoomsToRoomRepository(mockerRoomRepository);

        this.bookingSystem = new BookingSystem(mockTimeProvider, mockerRoomRepository1, mockNotificationService);
        this.constantTime = mockTimeProvider.getCurrentTime();
        this.pastTime = LocalDateTime.of(LocalDate.from(constantTime), LocalTime.of(10, 30));
        this.startTime = LocalDateTime.of(LocalDate.from(constantTime), LocalTime.of(13, 45));
        this.endTime = LocalDateTime.of(LocalDate.from(constantTime), LocalTime.of(15, 45));
    }



    static MockRoomRepository addRoomsToRoomRepository(MockRoomRepository mockRoomRepository) {
        Room room1  = new Room("1111", "Regular") ;
        Room room2  = new Room("2222", "Regular") ;
        Room room3  = new Room("3333", "Terrace") ;
        mockRoomRepository.save(room1);
        mockRoomRepository.save(room2);
        mockRoomRepository.save(room3);
        return mockRoomRepository;

    }

    static Stream<Arguments> roomPoster(){
        return Stream.of(
                Arguments.of(new Room("1111", "Regular")),
                Arguments.of(new Room("2222", "Suite")),
                Arguments.of(new Room("3333", "Terrace"))
        );
    }





    @ParameterizedTest
    @MethodSource("roomPoster")
    @DisplayName("Test rooms are available and can be booked Test")
    void testRoomsAreAvailableAndCanBeBookedTest(Room room) {
        var outcomes = bookingSystem.bookRoom(room.getId(), startTime, endTime);
        assertThat(outcomes).isTrue();


    }



}
