package com.example.payment;

public interface PaymentApi extends PaymentApiResponse {
    PaymentApiResponse charge(double amount);
}
