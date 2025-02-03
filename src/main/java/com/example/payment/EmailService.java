package com.example.payment;

public interface EmailService {
    static void sendPaymentConfirmation(String email, double amount){};
}
