package com.example.payment;

public interface PaymentApi extends PaymentApiResponse {
    PaymentApiResponse charge(String apiKey, double amount);
}
