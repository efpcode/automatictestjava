package com.example;

public class MockNotificationService implements NotificationService {
    @Override
    public void sendBookingConfirmation(Booking booking) throws NotificationException {
        System.out.println("Notification booking confirmed for: " + booking);

    }

    @Override
    public void sendCancellationConfirmation(Booking booking) throws NotificationException {
        System.out.println("Notification booking cancelled for: " + booking);

    }
}
