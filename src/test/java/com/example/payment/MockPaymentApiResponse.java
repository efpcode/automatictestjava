package com.example.payment;

public record MockPaymentApiResponse(String apiKey, double amount, boolean response) implements PaymentApiResponse {

    @Override
    public boolean isSuccess() {
        return this.response;
    }
}
