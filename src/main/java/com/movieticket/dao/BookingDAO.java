package com.movieticket.dao;

import com.movieticket.model.Booking;
import com.movieticket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private static final String INSERT_BOOKING =
            "INSERT INTO bookings " +
                    "(show_id, user_id, booking_date, total_amount, booking_status) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_BOOKINGS =
            "SELECT * FROM bookings";

    private static final String SELECT_BOOKING_BY_ID =
            "SELECT * FROM bookings WHERE booking_id = ?";

    private static final String UPDATE_BOOKING =
            "UPDATE bookings SET show_id = ?, user_id = ?, " +
                    "booking_date = ?, total_amount = ?, booking_status = ? " +
                    "WHERE booking_id = ?";

    private static final String DELETE_BOOKING =
            "DELETE FROM bookings WHERE booking_id = ?";


    public int addBooking(Booking booking) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_BOOKING,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, booking.getShowId());
            statement.setInt(2, booking.getUserId());

            statement.setTimestamp(
                    3,
                    Timestamp.valueOf(booking.getBookingDate())
            );

            statement.setBigDecimal(
                    4,
                    booking.getTotalAmount()
            );

            statement.setString(
                    5,
                    booking.getBookingStatus()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }


    public List<Booking> getAllBookings() throws SQLException {

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_BOOKINGS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Booking booking = new Booking();

                booking.setBookingId(
                        resultSet.getInt("booking_id")
                );

                booking.setShowId(
                        resultSet.getInt("show_id")
                );

                booking.setUserId(
                        resultSet.getInt("user_id")
                );

                Timestamp bookingDate =
                        resultSet.getTimestamp("booking_date");

                if (bookingDate != null) {
                    booking.setBookingDate(
                            bookingDate.toLocalDateTime()
                    );
                }

                booking.setTotalAmount(
                        resultSet.getBigDecimal("total_amount")
                );

                booking.setBookingStatus(
                        resultSet.getString("booking_status")
                );

                bookings.add(booking);
            }
        }

        return bookings;
    }


    public Booking getBookingById(int bookingId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SELECT_BOOKING_BY_ID)) {

            statement.setInt(1, bookingId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Booking booking = new Booking();

                    booking.setBookingId(
                            resultSet.getInt("booking_id")
                    );

                    booking.setShowId(
                            resultSet.getInt("show_id")
                    );

                    booking.setUserId(
                            resultSet.getInt("user_id")
                    );

                    Timestamp bookingDate =
                            resultSet.getTimestamp("booking_date");

                    if (bookingDate != null) {
                        booking.setBookingDate(
                                bookingDate.toLocalDateTime()
                        );
                    }

                    booking.setTotalAmount(
                            resultSet.getBigDecimal("total_amount")
                    );

                    booking.setBookingStatus(
                            resultSet.getString("booking_status")
                    );

                    return booking;
                }
            }
        }

        return null;
    }


    public void updateBooking(Booking booking)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_BOOKING)) {

            statement.setInt(1, booking.getShowId());
            statement.setInt(2, booking.getUserId());

            statement.setTimestamp(
                    3,
                    Timestamp.valueOf(booking.getBookingDate())
            );

            statement.setBigDecimal(
                    4,
                    booking.getTotalAmount()
            );

            statement.setString(
                    5,
                    booking.getBookingStatus()
            );

            statement.setInt(
                    6,
                    booking.getBookingId()
            );

            statement.executeUpdate();
        }
    }


    public void deleteBooking(int bookingId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_BOOKING)) {

            statement.setInt(1, bookingId);

            statement.executeUpdate();
        }
    }
}