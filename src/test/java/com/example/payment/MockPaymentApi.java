package com.example.payment;

public class MockPaymentApi implements PaymentApi {
    private boolean success;

    public MockPaymentApi(){
        this.success = false;

    }
    @Override
    public MockPaymentApiResponse charge(String apiKey, double amount) {

        if(apiKey == null || apiKey.isBlank() || amount <= 0){
            throw new IllegalArgumentException("apiKey cannot be null or empty and amount must be greater than 0");
        }

        if(apiKey.length() != 14){
            throw new IllegalArgumentException("apiKey must have length of 14");
        }

        if(amount >= Double.MAX_VALUE){
            throw new IllegalArgumentException("amount must be less than or equal to Double.MAX_VALUE");
        }

        if(!(isAPIKeyPatternMatch(apiKey))){
            throw new IllegalArgumentException("apiKey must follow the set key pattern");
        }

        isSuccess();

        return new MockPaymentApiResponse(apiKey, amount, success);




    }

    @Override
    public boolean isSuccess() {
        this.success = true;
        return true;
    }

    public boolean isAPIKeyPatternMatch(String apiKey){
        String pattern = "^[a-z]{2}_[a-z]{4}_[1-9]{6}$";
        return apiKey.matches(pattern);
    }
}
