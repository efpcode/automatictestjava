package com.example;

import java.time.LocalDateTime;

public class MockTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime getCurrentTime() {
        return null;
    }
}
