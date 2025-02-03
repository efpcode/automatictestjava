package com.example.payment;

public class MockEmailService implements EmailService {

    private boolean sendPaymentConfirmationSpy = false;

    @Override
    public void sendPaymentConfirmation(String email, double amount) {
        if( email == null || email.isBlank() || amount < 0){
            throw new IllegalArgumentException("Invalid email address or amount provided is less than zero");
        }

        if(amount > Double.MAX_VALUE){
            throw new IllegalArgumentException("Invalid amount provided is greater than " + Double.MAX_VALUE);
        }

        if (!(isEmailFormatPatternValid(email))) {
            throw new IllegalArgumentException("Email address has invalid format");
        }

        this.sendPaymentConfirmationSpy = true;

    }

    /**
     * The method returns true or false if
     *
     * email matches regex pattern. The pattern is not complete and only accounts for
     * format of email to be valid not if email is active or exists.
     *
     * @param email to check against regex pattern
     * @return True if regex pattern matches.
     */
    public boolean isEmailFormatPatternValid(String email) {
        String pattern = "^[a-zA-z0-9_]*@{1}[a-zA-Z0-9_]*\\.{1}[a-zA-Z]{2,3}$";
        return email.matches(pattern);

    }

    public boolean verify() {
        return this.sendPaymentConfirmationSpy;
    }
}
