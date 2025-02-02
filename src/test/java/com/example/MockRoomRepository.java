package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockRoomRepository implements RoomRepository {
    List<Room> rooms = new ArrayList<>();
    @Override
    public Optional<Room> findById(String id) {
        return rooms.stream()
                .filter(r -> id.equals(r.getId()))
                .findFirst();
    }

    @Override
    public List<Room> findAll() {
        return List.copyOf(rooms);
    }

    @Override
    public void save(Room room) {
        rooms.add(room);

    }
}
