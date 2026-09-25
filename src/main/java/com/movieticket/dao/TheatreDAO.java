package com.movieticket.dao;

import com.movieticket.model.Theatre;
import com.movieticket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheatreDAO {

    private static final String INSERT_THEATRE =
            "INSERT INTO theatres " +
                    "(name, city, address, total_seats) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_THEATRES =
            "SELECT * FROM theatres";

    private static final String SELECT_THEATRE_BY_ID =
            "SELECT * FROM theatres WHERE theatre_id = ?";

    private static final String UPDATE_THEATRE =
            "UPDATE theatres SET name = ?, city = ?, address = ?, " +
                    "total_seats = ? WHERE theatre_id = ?";

    private static final String DELETE_THEATRE =
            "DELETE FROM theatres WHERE theatre_id = ?";


    public void addTheatre(Theatre theatre) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_THEATRE)) {

            statement.setString(1, theatre.getName());
            statement.setString(2, theatre.getCity());
            statement.setString(3, theatre.getAddress());
            statement.setInt(4, theatre.getTotalSeats());

            statement.executeUpdate();
        }
    }


    public List<Theatre> getAllTheatres() throws SQLException {

        List<Theatre> theatres = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_THEATRES);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Theatre theatre = new Theatre();

                theatre.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                theatre.setName(
                        resultSet.getString("name")
                );

                theatre.setCity(
                        resultSet.getString("city")
                );

                theatre.setAddress(
                        resultSet.getString("address")
                );

                theatre.setTotalSeats(
                        resultSet.getInt("total_seats")
                );

                theatres.add(theatre);
            }
        }

        return theatres;
    }


    public Theatre getTheatreById(int theatreId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_THEATRE_BY_ID)) {

            statement.setInt(1, theatreId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Theatre theatre = new Theatre();

                    theatre.setTheatreId(
                            resultSet.getInt("theatre_id")
                    );

                    theatre.setName(
                            resultSet.getString("name")
                    );

                    theatre.setCity(
                            resultSet.getString("city")
                    );

                    theatre.setAddress(
                            resultSet.getString("address")
                    );

                    theatre.setTotalSeats(
                            resultSet.getInt("total_seats")
                    );

                    return theatre;
                }
            }
        }

        return null;
    }


    public void updateTheatre(Theatre theatre)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_THEATRE)) {

            statement.setString(1, theatre.getName());
            statement.setString(2, theatre.getCity());
            statement.setString(3, theatre.getAddress());
            statement.setInt(4, theatre.getTotalSeats());
            statement.setInt(5, theatre.getTheatreId());

            statement.executeUpdate();
        }
    }


    public void deleteTheatre(int theatreId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_THEATRE)) {

            statement.setInt(1, theatreId);

            statement.executeUpdate();
        }
    }
}