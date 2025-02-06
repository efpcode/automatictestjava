package com.example.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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


    @Test
    @DisplayName("Ensure that if response is not successful that return value is False")
    void ensureThatReturnValueIsFalse() {
        when(paymentApi.charge(isA(Double.class))).thenReturn(paymentApiResponse);
        when(paymentApiResponse.isSuccess()).thenReturn(false);
        var outcome = paymentProcessor.processPayment(100.0);
        assertThat(outcome).isFalse();

    }

    @Test
    @DisplayName("Ensure that when response is successful return is True")
    void ensureThatWhenResponseIsSuccessfulReturnIsTrue() {
        when(paymentApi.charge(isA(Double.class))).thenReturn(paymentApiResponse);
        when(paymentApiResponse.isSuccess()).thenReturn(true);
        var outcome = paymentProcessor.processPayment(100.0);
        assertThat(outcome).isTrue();

    }
}
