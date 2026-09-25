package com.movieticket.service;

import com.movieticket.dao.MovieDAO;
import com.movieticket.model.Movie;

import java.sql.SQLException;
import java.util.List;

public class MovieService {

    private final MovieDAO movieDAO;

    public MovieService() {
        movieDAO = new MovieDAO();
    }
    // Constructor used for unit testing with Mockito
    public MovieService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    // CREATE
    public void addMovie(Movie movie) throws SQLException {
        movieDAO.addMovie(movie);
    }

    // READ ALL
    public List<Movie> getAllMovies() throws SQLException {
        return movieDAO.getAllMovies();
    }

    // READ BY ID
    public Movie getMovieById(int movieId) throws SQLException {
        return movieDAO.getMovieById(movieId);
    }

    // UPDATE
    public void updateMovie(Movie movie) throws SQLException {
        movieDAO.updateMovie(movie);
    }

    // DELETE
    public void deleteMovie(int movieId) throws SQLException {
        movieDAO.deleteMovie(movieId);
    }
}