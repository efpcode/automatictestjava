package com.example.payment;

public interface PaymentApi extends PaymentApiResponse{
    static PaymentApiResponse charge (String apiKey, double amount) {
        return null;
    };

    @Override
    default boolean isSuccess() {
        return false;
    }

}
