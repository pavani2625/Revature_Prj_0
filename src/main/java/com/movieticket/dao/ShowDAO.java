package com.movieticket.dao;

import com.movieticket.model.Show;
import com.movieticket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowDAO {

    private static final String INSERT_SHOW =
            "INSERT INTO shows " +
                    "(theatre_id, movie_id, show_date, start_time, end_time) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SHOWS =
            "SELECT * FROM shows";

    private static final String SELECT_SHOW_BY_ID =
            "SELECT * FROM shows WHERE show_id = ?";

    private static final String UPDATE_SHOW =
            "UPDATE shows SET theatre_id = ?, movie_id = ?, " +
                    "show_date = ?, start_time = ?, end_time = ? " +
                    "WHERE show_id = ?";

    private static final String DELETE_SHOW =
            "DELETE FROM shows WHERE show_id = ?";


    public void addShow(Show show) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_SHOW)) {

            statement.setInt(1, show.getTheatreId());
            statement.setInt(2, show.getMovieId());
            statement.setDate(
                    3,
                    Date.valueOf(show.getShowDate())
            );
            statement.setTime(
                    4,
                    Time.valueOf(show.getStartTime())
            );
            statement.setTime(
                    5,
                    Time.valueOf(show.getEndTime())
            );

            statement.executeUpdate();
        }
    }


    public List<Show> getAllShows() throws SQLException {

        List<Show> shows = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_SHOWS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Show show = new Show();

                show.setShowId(
                        resultSet.getInt("show_id")
                );

                show.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                show.setMovieId(
                        resultSet.getInt("movie_id")
                );

                show.setShowDate(
                        resultSet.getDate("show_date")
                                .toLocalDate()
                );

                show.setStartTime(
                        resultSet.getTime("start_time")
                                .toLocalTime()
                );

                show.setEndTime(
                        resultSet.getTime("end_time")
                                .toLocalTime()
                );

                shows.add(show);
            }
        }

        return shows;
    }


    public Show getShowById(int showId) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_SHOW_BY_ID)) {

            statement.setInt(1, showId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Show show = new Show();

                    show.setShowId(
                            resultSet.getInt("show_id")
                    );

                    show.setTheatreId(
                            resultSet.getInt("theatre_id")
                    );

                    show.setMovieId(
                            resultSet.getInt("movie_id")
                    );

                    show.setShowDate(
                            resultSet.getDate("show_date")
                                    .toLocalDate()
                    );

                    show.setStartTime(
                            resultSet.getTime("start_time")
                                    .toLocalTime()
                    );

                    show.setEndTime(
                            resultSet.getTime("end_time")
                                    .toLocalTime()
                    );

                    return show;
                }
            }
        }

        return null;
    }


    public void updateShow(Show show) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_SHOW)) {

            statement.setInt(1, show.getTheatreId());
            statement.setInt(2, show.getMovieId());

            statement.setDate(
                    3,
                    Date.valueOf(show.getShowDate())
            );

            statement.setTime(
                    4,
                    Time.valueOf(show.getStartTime())
            );

            statement.setTime(
                    5,
                    Time.valueOf(show.getEndTime())
            );

            statement.setInt(6, show.getShowId());

            statement.executeUpdate();
        }
    }


    public void deleteShow(int showId) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_SHOW)) {

            statement.setInt(1, showId);

            statement.executeUpdate();
        }
    }
}