package com.movieticket.dao;

import com.movieticket.model.Seat;
import com.movieticket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    private static final String INSERT_SEAT =
            "INSERT INTO seats " +
                    "(theatre_id, seat_number, seat_type, price) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SEATS =
            "SELECT * FROM seats";

    private static final String SELECT_SEAT_BY_ID =
            "SELECT * FROM seats WHERE seat_id = ?";

    private static final String UPDATE_SEAT =
            "UPDATE seats SET theatre_id = ?, seat_number = ?, " +
                    "seat_type = ?, price = ? WHERE seat_id = ?";

    private static final String DELETE_SEAT =
            "DELETE FROM seats WHERE seat_id = ?";


    public void addSeat(Seat seat) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_SEAT)) {

            statement.setInt(1, seat.getTheatreId());
            statement.setString(2, seat.getSeatNumber());
            statement.setString(3, seat.getSeatType());
            statement.setBigDecimal(4, seat.getPrice());

            statement.executeUpdate();
        }
    }


    public List<Seat> getAllSeats() throws SQLException {

        List<Seat> seats = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_SEATS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Seat seat = new Seat();

                seat.setSeatId(
                        resultSet.getInt("seat_id")
                );

                seat.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                seat.setSeatNumber(
                        resultSet.getString("seat_number")
                );

                seat.setSeatType(
                        resultSet.getString("seat_type")
                );

                seat.setPrice(
                        resultSet.getBigDecimal("price")
                );

                seats.add(seat);
            }
        }

        return seats;
    }


    public Seat getSeatById(int seatId) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_SEAT_BY_ID)) {

            statement.setInt(1, seatId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Seat seat = new Seat();

                    seat.setSeatId(
                            resultSet.getInt("seat_id")
                    );

                    seat.setTheatreId(
                            resultSet.getInt("theatre_id")
                    );

                    seat.setSeatNumber(
                            resultSet.getString("seat_number")
                    );

                    seat.setSeatType(
                            resultSet.getString("seat_type")
                    );

                    seat.setPrice(
                            resultSet.getBigDecimal("price")
                    );

                    return seat;
                }
            }
        }

        return null;
    }


    public void updateSeat(Seat seat) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_SEAT)) {

            statement.setInt(1, seat.getTheatreId());
            statement.setString(2, seat.getSeatNumber());
            statement.setString(3, seat.getSeatType());
            statement.setBigDecimal(4, seat.getPrice());
            statement.setInt(5, seat.getSeatId());

            statement.executeUpdate();
        }
    }


    public void deleteSeat(int seatId) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_SEAT)) {

            statement.setInt(1, seatId);

            statement.executeUpdate();
        }
    }
}