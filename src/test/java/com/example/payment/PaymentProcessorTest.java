package com.example.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {
    @Mock
    private DatabaseService databaseService;

    @Mock
    private PaymentApi paymentApi;

    @Mock
    private EmailService emailService;

    @Mock
    PaymentApiResponse paymentApiResponse;

    @InjectMocks
    private PaymentProcessor paymentProcessor;

    private final double mockAmount = 100.0;
    private final String mockEmail = "user@example.com";


    @Nested
    class HappyPathTesting {
        @Test
        @DisplayName("Ensure that if response is not successful that return value is False")
        void ensureThatReturnValueIsFalse() {
            when(paymentApi.charge(isA(Double.class))).thenReturn(paymentApiResponse);
            when(paymentApiResponse.isSuccess()).thenReturn(false);
            var outcome = paymentProcessor.processPayment(mockAmount);
            assertThat(outcome).isFalse();
        }

        @Test
        @DisplayName("Ensure that when response is successful return is True")
        void ensureThatWhenResponseIsSuccessfulReturnIsTrue() {
            when(paymentApi.charge(isA(Double.class))).thenReturn(paymentApiResponse);
            when(paymentApiResponse.isSuccess()).thenReturn(true);
            var outcome = paymentProcessor.processPayment(mockAmount);
            assertThat(outcome).isTrue();
        }
    }

    @Nested
    class SpyTesting {

        @Test
        @DisplayName("Ensure that email Service been run at least once")
        void ensureThatEmailServiceBeenRunAtLeastOnce() {
            when(paymentApi.charge(isA(Double.class))).thenReturn(paymentApiResponse);
            when(paymentApiResponse.isSuccess()).thenReturn(true);
            paymentProcessor.processPayment(mockAmount);
            try {
                verify(emailService).sendPaymentConfirmation(mockEmail, mockAmount);
            } catch (EmailServiceException e) {
                throw new RuntimeException(e);
            }


        }

        @Test
        @DisplayName("Ensure that email service has not been run once when response is False test")
        void ensureThatEmailServiceHasNotBeenRunOnceWhenResponseIsFalseTest() {
            when(paymentApi.charge(isA(Double.class))).thenReturn(paymentApiResponse);
            when(paymentApiResponse.isSuccess()).thenReturn(false);
            paymentProcessor.processPayment(mockAmount);
            assertThatThrownBy(() -> verify(emailService).sendPaymentConfirmation(mockEmail, mockAmount))
                    .hasMessageContaining("\"user@example.com\",\n" + "    100.0d");

        }

        @Test
        @DisplayName("Ensure that database service method is run at least once")
        void ensureThatDatabaseServiceMethodIsRunAtLeastOnce() {
            when(paymentApi.charge(isA(Double.class))).thenReturn(paymentApiResponse);
            when(paymentApiResponse.isSuccess()).thenReturn(true);
            paymentProcessor.processPayment(mockAmount);
            try {
                verify(databaseService).databaseUpdate(mockAmount, "SUCCESS");

            } catch (DatabaseServiceException e) {
                throw new RuntimeException(e);
            }

        }

        @Test
        @DisplayName("Ensures that database service is not called when response is false")
        void ensuresThatDatabaseServiceIsNotCalledWhenResponseIsFalse() {
            when(paymentApi.charge(isA(Double.class))).thenReturn(paymentApiResponse);
            when(paymentApiResponse.isSuccess()).thenReturn(false);
            paymentProcessor.processPayment(mockAmount);
            assertThatThrownBy(() -> verify(databaseService).databaseUpdate(mockAmount, "SUCCESS"))
                    .hasMessageContaining("SUCCESS");

        }

    }
}
