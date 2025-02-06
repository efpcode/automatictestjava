package com.example.payment;


public class PaymentProcessor {

    // DatabaseConnection is not testable and only Crud should exist, since direct connection to database is hard to test.
    private final DatabaseService databaseService;
    private final PaymentApi paymentApi;
    private final EmailService emailService;



    public PaymentProcessor(DatabaseService databaseConnection, PaymentApi paymentApi , EmailService emailService ) {
        this.databaseService = databaseConnection;
        this.paymentApi = paymentApi;
        this.emailService = emailService;
    }

    public boolean processPayment(double amount) throws EmailServiceException {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        PaymentApiResponse response = paymentApi.charge(amount);

        // Skriver till databas direkt
        if (response.isSuccess()) {
            try {
                databaseService
                        .executeUpdate(amount , "SUCCESS");
            }catch (DatabaseServiceException e) {
                System.out.println(e.getMessage());
            }
            // Skickar e-post direkt
            emailService.sendPaymentConfirmation("user@example.com", amount);
        }

        return response.isSuccess();
    }
}
