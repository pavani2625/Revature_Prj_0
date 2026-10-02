package com.movieticket.service;

import com.movieticket.dao.PaymentDAO;
import com.movieticket.exception.MovieTicketException;
import com.movieticket.model.Payment;

import java.sql.SQLException;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentService() {
        paymentDAO = new PaymentDAO();
    }

    // Constructor used for unit testing with Mockito
    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    // CREATE
    public void addPayment(Payment payment) throws SQLException {
        paymentDAO.addPayment(payment);
    }

    // PROCESS PAYMENT
    public boolean processPayment(Payment payment) throws SQLException {

        if (payment == null) {
            throw new MovieTicketException(
                    "Payment information is required."
            );
        }

        if (payment.getPaymentMethod() == null ||
                payment.getPaymentMethod().trim().isEmpty()) {

            throw new MovieTicketException(
                    "Payment method is required."
            );
        }

        String paymentMethod =
                payment.getPaymentMethod().trim().toUpperCase();

        if (!paymentMethod.equals("UPI") &&
                !paymentMethod.equals("CARD") &&
                !paymentMethod.equals("CASH")) {

            throw new MovieTicketException(
                    "Invalid payment method. Use UPI, CARD or CASH."
            );
        }

        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus("SUCCESS");

        paymentDAO.addPayment(payment);

        return true;
    }

    // READ ALL
    public List<Payment> getAllPayments() throws SQLException {
        return paymentDAO.getAllPayments();
    }

    // READ BY ID
    public Payment getPaymentById(int paymentId) throws SQLException {
        return paymentDAO.getPaymentById(paymentId);
    }

    // UPDATE
    public void updatePayment(Payment payment) throws SQLException {
        paymentDAO.updatePayment(payment);
    }

    // DELETE
    public void deletePayment(int paymentId) throws SQLException {
        paymentDAO.deletePayment(paymentId);
    }
}