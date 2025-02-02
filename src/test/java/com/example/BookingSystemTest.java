package com.example;


import com.example.exercise2.MockTimeStartAndEnd;
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
    Room room1 = new Room("1111", "Regular") ;

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


    static Stream<Arguments> TimeNullPoster(){
        var endTime = LocalDateTime.of(2025, 3, 2, 15, 45);
        var startTime = LocalDateTime.of(2025, 4, 2, 15, 45);

        return Stream.of(
                Arguments.of(new MockTimeStartAndEnd(null, endTime)),
                Arguments.of(new MockTimeStartAndEnd(startTime, null)),
                Arguments.of(new MockTimeStartAndEnd(null, null))
        );
    }





    @ParameterizedTest
    @MethodSource("roomPoster")
    @DisplayName("Test rooms are available and can be booked Test")
    void testRoomsAreAvailableAndCanBeBookedTest(Room room) {
        var outcomes = bookingSystem.bookRoom(room.getId(), this.startTime, this.endTime);
        assertThat(outcomes).isTrue();

    }


    @ParameterizedTest
    @MethodSource("TimeNullPoster")
    @DisplayName("Rooms cannot not be booked with null values Test")
    void testRoomsCannotBeBookedWithNullValuesTest (MockTimeStartAndEnd mockTime) {
        assertThatThrownBy(() ->bookingSystem.bookRoom(this.room1.getId(), mockTime.start(), mockTime.end()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bokning kräver giltiga start- och sluttider samt rum-id");

    }


    @Test
    @DisplayName("Rooms cannot be booked in the past Test")

    void roomsCannotBeBookedInThePastTest() {
        assertThatThrownBy(() -> bookingSystem.bookRoom(this.room1.getId(), this.pastTime, this.endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Kan inte boka tid i dåtid");

        assertThatCode(() -> bookingSystem.bookRoom(this.room1.getId(), this.startTime, this.endTime))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Rooms booking end time is greater than start time Test ")
    void roomsBookingEndTimeIsGreaterThanStartTimeTest() {
        assertThatThrownBy(() ->bookingSystem.bookRoom(this.room1.getId(), this.endTime, this.startTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Sluttid måste vara efter starttid");

        assertThatCode(() -> bookingSystem.bookRoom(this.room1.getId(), this.startTime, this.endTime))
                .doesNotThrowAnyException();

    }


    @Test
    @DisplayName("Rooms need to exists to be booked Test")
    void roomsNeedToExistsToBeBookedTest() {
        Room room2 = new Room("4444", "Fancy Suite");

        assertThatThrownBy(() -> bookingSystem.bookRoom(room2.getId(), this.startTime, this.endTime))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Rummet existerar inte");

        assertThatCode(() -> bookingSystem.bookRoom(this.room1.getId(), this.startTime, this.endTime)).doesNotThrowAnyException();

    }


    @ParameterizedTest
    @MethodSource("roomPoster")
    @DisplayName("Room that is booked is not available for booking again")
    void roomThatIsBookedIsNotAvailableForBookingAgain(Room room) {
        var outcomes = bookingSystem.bookRoom(room.getId(), this.startTime, this.endTime);
        assertThat(outcomes).isTrue();
        var outcomes2 = bookingSystem.bookRoom(room.getId(), this.startTime, this.endTime);
        System.out.println(room.getName());
        assertThat(outcomes2).isFalse();

        var outcomes3 = bookingSystem.bookRoom(room.getId(), this.constantTime, this.endTime);
        assertThat(outcomes3).isFalse();


    }


    @ParameterizedTest
    @MethodSource("TimeNullPoster")
    @DisplayName("Search for available rooms time inputs must not be null test")
    void searchForAvailableRoomsTimeInputsMustNotBeNullTest(MockTimeStartAndEnd mockTime) {
        assertThatThrownBy(() -> bookingSystem.getAvailableRooms(mockTime.start(), mockTime.end()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Måste ange både start- och sluttid");


    }





}
