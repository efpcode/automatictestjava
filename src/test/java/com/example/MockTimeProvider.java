package com.example;

import java.time.LocalDateTime;

public class MockTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.of(2025, 2, 2, 13, 30);
    }
}
