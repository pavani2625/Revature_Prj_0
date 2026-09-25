package com.movieticket.dao;

import com.movieticket.model.Movie;
import com.movieticket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    private static final String INSERT_MOVIE =
            "INSERT INTO movies " +
                    "(title, language, genre, duration, release_date) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_MOVIES =
            "SELECT * FROM movies";

    private static final String SELECT_MOVIE_BY_ID =
            "SELECT * FROM movies WHERE movie_id = ?";

    private static final String UPDATE_MOVIE =
            "UPDATE movies SET title = ?, language = ?, genre = ?, " +
                    "duration = ?, release_date = ? WHERE movie_id = ?";

    private static final String DELETE_MOVIE =
            "DELETE FROM movies WHERE movie_id = ?";


    public void addMovie(Movie movie) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_MOVIE)) {

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getLanguage());
            statement.setString(3, movie.getGenre());
            statement.setInt(4, movie.getDuration());
            statement.setDate(
                    5,
                    movie.getReleaseDate() != null
                            ? Date.valueOf(movie.getReleaseDate())
                            : null
            );

            statement.executeUpdate();
        }
    }


    public List<Movie> getAllMovies() throws SQLException {

        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_MOVIES);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Movie movie = new Movie();

                movie.setMovieId(resultSet.getInt("movie_id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setLanguage(resultSet.getString("language"));
                movie.setGenre(resultSet.getString("genre"));
                movie.setDuration(resultSet.getInt("duration"));

                Date releaseDate =
                        resultSet.getDate("release_date");

                if (releaseDate != null) {
                    movie.setReleaseDate(
                            releaseDate.toLocalDate()
                    );
                }

                movies.add(movie);
            }
        }

        return movies;
    }


    public Movie getMovieById(int movieId) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SELECT_MOVIE_BY_ID)) {

            statement.setInt(1, movieId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Movie movie = new Movie();

                    movie.setMovieId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setLanguage(resultSet.getString("language"));
                    movie.setGenre(resultSet.getString("genre"));
                    movie.setDuration(resultSet.getInt("duration"));

                    Date releaseDate =
                            resultSet.getDate("release_date");

                    if (releaseDate != null) {
                        movie.setReleaseDate(
                                releaseDate.toLocalDate()
                        );
                    }

                    return movie;
                }
            }
        }

        return null;
    }


    public void updateMovie(Movie movie) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_MOVIE)) {

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getLanguage());
            statement.setString(3, movie.getGenre());
            statement.setInt(4, movie.getDuration());

            statement.setDate(
                    5,
                    movie.getReleaseDate() != null
                            ? Date.valueOf(movie.getReleaseDate())
                            : null
            );

            statement.setInt(6, movie.getMovieId());

            statement.executeUpdate();
        }
    }


    public void deleteMovie(int movieId) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_MOVIE)) {

            statement.setInt(1, movieId);

            statement.executeUpdate();
        }
    }
}