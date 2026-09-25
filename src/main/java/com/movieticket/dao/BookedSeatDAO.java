package com.movieticket.dao;

import com.movieticket.model.BookedSeat;
import com.movieticket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookedSeatDAO {

    private static final String INSERT_BOOKED_SEAT =
            "INSERT INTO booked_seats (seat_id, booking_id) " +
                    "VALUES (?, ?)";

    private static final String SELECT_ALL_BOOKED_SEATS =
            "SELECT * FROM booked_seats";

    private static final String SELECT_BOOKED_SEAT_BY_ID =
            "SELECT * FROM booked_seats WHERE booked_seat_id = ?";

    private static final String SELECT_BOOKED_SEAT_IDS_BY_SHOW_ID =
            "SELECT bs.seat_id " +
                    "FROM booked_seats bs " +
                    "JOIN bookings b ON bs.booking_id = b.booking_id " +
                    "WHERE b.show_id = ? " +
                    "AND b.booking_status = 'CONFIRMED'";

    private static final String UPDATE_BOOKED_SEAT =
            "UPDATE booked_seats SET seat_id = ?, booking_id = ? " +
                    "WHERE booked_seat_id = ?";

    private static final String DELETE_BOOKED_SEAT =
            "DELETE FROM booked_seats WHERE booked_seat_id = ?";


    public void addBookedSeat(BookedSeat bookedSeat)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_BOOKED_SEAT)) {

            statement.setInt(
                    1,
                    bookedSeat.getSeatId()
            );

            statement.setInt(
                    2,
                    bookedSeat.getBookingId()
            );

            statement.executeUpdate();
        }
    }


    public List<BookedSeat> getAllBookedSeats()
            throws SQLException {

        List<BookedSeat> bookedSeats = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SELECT_ALL_BOOKED_SEATS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                BookedSeat bookedSeat = new BookedSeat();

                bookedSeat.setBookedSeatId(
                        resultSet.getInt("booked_seat_id")
                );

                bookedSeat.setSeatId(
                        resultSet.getInt("seat_id")
                );

                bookedSeat.setBookingId(
                        resultSet.getInt("booking_id")
                );

                bookedSeats.add(bookedSeat);
            }
        }

        return bookedSeats;
    }


    public BookedSeat getBookedSeatById(int bookedSeatId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SELECT_BOOKED_SEAT_BY_ID)) {

            statement.setInt(1, bookedSeatId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    BookedSeat bookedSeat = new BookedSeat();

                    bookedSeat.setBookedSeatId(
                            resultSet.getInt("booked_seat_id")
                    );

                    bookedSeat.setSeatId(
                            resultSet.getInt("seat_id")
                    );

                    bookedSeat.setBookingId(
                            resultSet.getInt("booking_id")
                    );

                    return bookedSeat;
                }
            }
        }

        return null;
    }


    public List<Integer> getBookedSeatIdsByShowId(int showId)
            throws SQLException {

        List<Integer> seatIds = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SELECT_BOOKED_SEAT_IDS_BY_SHOW_ID)) {

            statement.setInt(1, showId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    seatIds.add(
                            resultSet.getInt("seat_id")
                    );
                }
            }
        }

        return seatIds;
    }


    public void updateBookedSeat(BookedSeat bookedSeat)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_BOOKED_SEAT)) {

            statement.setInt(
                    1,
                    bookedSeat.getSeatId()
            );

            statement.setInt(
                    2,
                    bookedSeat.getBookingId()
            );

            statement.setInt(
                    3,
                    bookedSeat.getBookedSeatId()
            );

            statement.executeUpdate();
        }
    }


    public void deleteBookedSeat(int bookedSeatId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_BOOKED_SEAT)) {

            statement.setInt(1, bookedSeatId);

            statement.executeUpdate();
        }
    }
}