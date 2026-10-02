package com.movieticket.service;

import com.movieticket.dao.PaymentDAO;
import com.movieticket.exception.MovieTicketException;
import com.movieticket.model.Payment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Test
    void testAddPayment() throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        Payment payment = new Payment();

        payment.setBookingId(1);
        payment.setAmount(new BigDecimal("300.00"));
        payment.setPaymentMethod("UPI");
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        paymentService.addPayment(payment);

        verify(mockPaymentDAO)
                .addPayment(payment);
    }

    @Test
    void testProcessPaymentWithUPI() throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        Payment payment = new Payment();

        payment.setBookingId(1);
        payment.setAmount(new BigDecimal("300.00"));
        payment.setPaymentMethod("UPI");
        payment.setPaymentDate(LocalDateTime.now());

        boolean result =
                paymentService.processPayment(payment);

        assertTrue(result);
        assertEquals("UPI", payment.getPaymentMethod());
        assertEquals("SUCCESS", payment.getPaymentStatus());

        verify(mockPaymentDAO)
                .addPayment(payment);
    }

    @Test
    void testProcessPaymentWithCard() throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        Payment payment = new Payment();

        payment.setBookingId(2);
        payment.setAmount(new BigDecimal("400.00"));
        payment.setPaymentMethod("CARD");
        payment.setPaymentDate(LocalDateTime.now());

        boolean result =
                paymentService.processPayment(payment);

        assertTrue(result);
        assertEquals("CARD", payment.getPaymentMethod());
        assertEquals("SUCCESS", payment.getPaymentStatus());

        verify(mockPaymentDAO)
                .addPayment(payment);
    }

    @Test
    void testProcessPaymentWithCash() throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        Payment payment = new Payment();

        payment.setBookingId(3);
        payment.setAmount(new BigDecimal("250.00"));
        payment.setPaymentMethod("CASH");
        payment.setPaymentDate(LocalDateTime.now());

        boolean result =
                paymentService.processPayment(payment);

        assertTrue(result);
        assertEquals("CASH", payment.getPaymentMethod());
        assertEquals("SUCCESS", payment.getPaymentStatus());

        verify(mockPaymentDAO)
                .addPayment(payment);
    }

    @Test
    void testProcessPaymentWithInvalidMethod()
            throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        Payment payment = new Payment();

        payment.setBookingId(1);
        payment.setAmount(new BigDecimal("300.00"));
        payment.setPaymentMethod("INVALID");
        payment.setPaymentDate(LocalDateTime.now());

        assertThrows(
                MovieTicketException.class,
                () -> paymentService.processPayment(payment)
        );

        verify(mockPaymentDAO, never())
                .addPayment(payment);
    }

    @Test
    void testGetAllPayments() throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        Payment payment1 = new Payment();

        payment1.setPaymentId(1);
        payment1.setBookingId(1);
        payment1.setAmount(new BigDecimal("300.00"));
        payment1.setPaymentMethod("UPI");
        payment1.setPaymentStatus("SUCCESS");

        Payment payment2 = new Payment();

        payment2.setPaymentId(2);
        payment2.setBookingId(2);
        payment2.setAmount(new BigDecimal("400.00"));
        payment2.setPaymentMethod("CARD");
        payment2.setPaymentStatus("SUCCESS");

        List<Payment> expectedPayments =
                Arrays.asList(payment1, payment2);

        when(mockPaymentDAO.getAllPayments())
                .thenReturn(expectedPayments);

        List<Payment> actualPayments =
                paymentService.getAllPayments();

        assertNotNull(actualPayments);
        assertEquals(2, actualPayments.size());

        assertEquals(
                1,
                actualPayments.get(0).getPaymentId()
        );

        assertEquals(
                2,
                actualPayments.get(1).getPaymentId()
        );

        assertEquals(
                new BigDecimal("300.00"),
                actualPayments.get(0).getAmount()
        );

        verify(mockPaymentDAO)
                .getAllPayments();
    }

    @Test
    void testGetPaymentById() throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        Payment expectedPayment = new Payment();

        expectedPayment.setPaymentId(1);
        expectedPayment.setBookingId(1);
        expectedPayment.setAmount(
                new BigDecimal("300.00")
        );
        expectedPayment.setPaymentMethod("UPI");
        expectedPayment.setPaymentStatus("SUCCESS");

        when(mockPaymentDAO.getPaymentById(1))
                .thenReturn(expectedPayment);

        Payment actualPayment =
                paymentService.getPaymentById(1);

        assertNotNull(actualPayment);

        assertEquals(
                1,
                actualPayment.getPaymentId()
        );

        assertEquals(
                1,
                actualPayment.getBookingId()
        );

        assertEquals(
                new BigDecimal("300.00"),
                actualPayment.getAmount()
        );

        assertEquals(
                "UPI",
                actualPayment.getPaymentMethod()
        );

        assertEquals(
                "SUCCESS",
                actualPayment.getPaymentStatus()
        );

        verify(mockPaymentDAO)
                .getPaymentById(1);
    }

    @Test
    void testUpdatePayment() throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        Payment payment = new Payment();

        payment.setPaymentId(1);
        payment.setBookingId(1);
        payment.setAmount(new BigDecimal("300.00"));
        payment.setPaymentMethod("CARD");
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        paymentService.updatePayment(payment);

        verify(mockPaymentDAO)
                .updatePayment(payment);
    }

    @Test
    void testDeletePayment() throws SQLException {

        PaymentDAO mockPaymentDAO =
                mock(PaymentDAO.class);

        PaymentService paymentService =
                new PaymentService(mockPaymentDAO);

        paymentService.deletePayment(1);

        verify(mockPaymentDAO)
                .deletePayment(1);
    }
}