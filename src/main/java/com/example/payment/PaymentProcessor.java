package com.example.payment;


public class PaymentProcessor {

    // DatabaseConnection is not testable and only Crud should exist, since direct connection to database is hard to test.
    // API Key was also remove since that should be handled when paymentApi.charge is implemented in the future.
    private final DatabaseService databaseService;
    private final PaymentApi paymentApi;
    private final EmailService emailService;



    public PaymentProcessor(DatabaseService databaseConnection, PaymentApi paymentApi , EmailService emailService ) {
        this.databaseService = databaseConnection;
        this.paymentApi = paymentApi;
        this.emailService = emailService;
    }

    public boolean processPayment(double amount) {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        PaymentApiResponse response = paymentApi.charge(amount);

        // Skriver till databas direkt
        // changed named from executeUpdate to databaseUpdate
        if (response.isSuccess()) {
            try {
                databaseService
                        .databaseUpdate(amount , "SUCCESS");
            }catch (DatabaseServiceException e) {
                // Should continue to work or execute
            }
            // Skickar e-post direkt
            try{emailService.sendPaymentConfirmation("user@example.com", amount);}
            catch (EmailServiceException e) {
                // Should continue to work or execute
            }
        }

        return response.isSuccess();
    }
}
