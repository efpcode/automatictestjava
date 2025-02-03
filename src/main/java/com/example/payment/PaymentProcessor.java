package com.example.payment;

import java.sql.SQLException;


public class PaymentProcessor {
    private final String API_KEY; //"sk_test_123456";
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private final PaymentApi paymentApi;
    private final EmailService emailService;



    public PaymentProcessor(String API_KEY, DatabaseConnection databaseConnection, PaymentApi paymentApi , EmailService emailService ) {
        this.API_KEY = API_KEY;
        this.databaseConnection = databaseConnection;
        this.paymentApi = paymentApi;
        this.emailService = emailService;
    }

    public boolean processPayment(double amount) throws SQLException {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        PaymentApiResponse response = paymentApi.charge(API_KEY, amount);

        // Skriver till databas direkt
        if (response.isSuccess()) {
            try {
                databaseConnection
                        .executeUpdate("INSERT INTO payments (amount, status) VALUES (" + amount + ", 'SUCCESS')");
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // Skickar e-post direkt
        if (response.isSuccess()) {
            emailService.sendPaymentConfirmation("user@example.com", amount);
        }

        return response.isSuccess();
    }
}
