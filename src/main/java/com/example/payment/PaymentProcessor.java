package com.example.payment;

import java.sql.SQLException;


public class PaymentProcessor {
    private final String API_KEY; //"sk_test_123456";

    // DatabaseConnection is not testable and only Crud should exist, since direct connection to database is hard to test.
    private final Crud databaseService;
    private final PaymentApi paymentApi;
    private final EmailService emailService;



    public PaymentProcessor(String API_KEY, Crud databaseConnection, PaymentApi paymentApi , EmailService emailService ) {
        this.API_KEY = API_KEY;
        this.databaseService = databaseConnection;
        this.paymentApi = paymentApi;
        this.emailService = emailService;
    }

    public boolean processPayment(double amount) throws SQLException {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        PaymentApiResponse response = paymentApi.charge(API_KEY, amount);

        // Skriver till databas direkt
        if (response.isSuccess()) {
            try {
                databaseService
                        .executeUpdate(amount , "SUCCESS");
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            // Skickar e-post direkt
            emailService.sendPaymentConfirmation("user@example.com", amount);
        }

        return response.isSuccess();
    }
}
