package com.movieticket.dao;

import com.movieticket.model.Payment;
import com.movieticket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    private static final String INSERT_PAYMENT =
            "INSERT INTO payments " +
                    "(booking_id, amount, payment_method, payment_status, payment_date) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_PAYMENTS =
            "SELECT * FROM payments";

    private static final String SELECT_PAYMENT_BY_ID =
            "SELECT * FROM payments WHERE payment_id = ?";

    private static final String UPDATE_PAYMENT =
            "UPDATE payments SET booking_id = ?, amount = ?, " +
                    "payment_method = ?, payment_status = ?, payment_date = ? " +
                    "WHERE payment_id = ?";

    private static final String DELETE_PAYMENT =
            "DELETE FROM payments WHERE payment_id = ?";


    public void addPayment(Payment payment)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_PAYMENT)) {

            statement.setInt(
                    1,
                    payment.getBookingId()
            );

            statement.setBigDecimal(
                    2,
                    payment.getAmount()
            );

            statement.setString(
                    3,
                    payment.getPaymentMethod()
            );

            statement.setString(
                    4,
                    payment.getPaymentStatus()
            );

            statement.setTimestamp(
                    5,
                    Timestamp.valueOf(
                            payment.getPaymentDate()
                    )
            );

            statement.executeUpdate();
        }
    }


    public List<Payment> getAllPayments()
            throws SQLException {

        List<Payment> payments = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SELECT_ALL_PAYMENTS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Payment payment = new Payment();

                payment.setPaymentId(
                        resultSet.getInt("payment_id")
                );

                payment.setBookingId(
                        resultSet.getInt("booking_id")
                );

                payment.setAmount(
                        resultSet.getBigDecimal("amount")
                );

                payment.setPaymentMethod(
                        resultSet.getString("payment_method")
                );

                payment.setPaymentStatus(
                        resultSet.getString("payment_status")
                );

                Timestamp paymentDate =
                        resultSet.getTimestamp("payment_date");

                if (paymentDate != null) {
                    payment.setPaymentDate(
                            paymentDate.toLocalDateTime()
                    );
                }

                payments.add(payment);
            }
        }

        return payments;
    }


    public Payment getPaymentById(int paymentId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SELECT_PAYMENT_BY_ID)) {

            statement.setInt(1, paymentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Payment payment = new Payment();

                    payment.setPaymentId(
                            resultSet.getInt("payment_id")
                    );

                    payment.setBookingId(
                            resultSet.getInt("booking_id")
                    );

                    payment.setAmount(
                            resultSet.getBigDecimal("amount")
                    );

                    payment.setPaymentMethod(
                            resultSet.getString("payment_method")
                    );

                    payment.setPaymentStatus(
                            resultSet.getString("payment_status")
                    );

                    Timestamp paymentDate =
                            resultSet.getTimestamp("payment_date");

                    if (paymentDate != null) {
                        payment.setPaymentDate(
                                paymentDate.toLocalDateTime()
                        );
                    }

                    return payment;
                }
            }
        }

        return null;
    }


    public void updatePayment(Payment payment)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_PAYMENT)) {

            statement.setInt(
                    1,
                    payment.getBookingId()
            );

            statement.setBigDecimal(
                    2,
                    payment.getAmount()
            );

            statement.setString(
                    3,
                    payment.getPaymentMethod()
            );

            statement.setString(
                    4,
                    payment.getPaymentStatus()
            );

            statement.setTimestamp(
                    5,
                    Timestamp.valueOf(
                            payment.getPaymentDate()
                    )
            );

            statement.setInt(
                    6,
                    payment.getPaymentId()
            );

            statement.executeUpdate();
        }
    }


    public void deletePayment(int paymentId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_PAYMENT)) {

            statement.setInt(1, paymentId);

            statement.executeUpdate();
        }
    }
}