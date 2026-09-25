package com.movieticket.service;

import com.movieticket.dao.MovieDAO;
import com.movieticket.model.Movie;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    @Test
    void testAddMovie() throws SQLException {

        MovieDAO mockMovieDAO = mock(MovieDAO.class);

        MovieService movieService = new MovieService(mockMovieDAO);

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setLanguage("English");
        movie.setGenre("Action");
        movie.setDuration(120);
        movie.setReleaseDate(LocalDate.of(2026, 1, 1));

        movieService.addMovie(movie);

        verify(mockMovieDAO).addMovie(movie);
    }


    @Test
    void testGetAllMovies() throws SQLException {

        MovieDAO mockMovieDAO = mock(MovieDAO.class);

        MovieService movieService = new MovieService(mockMovieDAO);

        Movie movie1 = new Movie();
        movie1.setMovieId(1);
        movie1.setTitle("Interstellar");

        Movie movie2 = new Movie();
        movie2.setMovieId(2);
        movie2.setTitle("RRR");

        List<Movie> expectedMovies = Arrays.asList(movie1, movie2);

        when(mockMovieDAO.getAllMovies()).thenReturn(expectedMovies);

        List<Movie> actualMovies = movieService.getAllMovies();

        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
        assertEquals("Interstellar", actualMovies.get(0).getTitle());
        assertEquals("RRR", actualMovies.get(1).getTitle());

        verify(mockMovieDAO).getAllMovies();
    }


    @Test
    void testGetMovieById() throws SQLException {

        MovieDAO mockMovieDAO = mock(MovieDAO.class);

        MovieService movieService = new MovieService(mockMovieDAO);

        Movie expectedMovie = new Movie();
        expectedMovie.setMovieId(1);
        expectedMovie.setTitle("Interstellar");
        expectedMovie.setLanguage("English");
        expectedMovie.setGenre("Science Fiction");

        when(mockMovieDAO.getMovieById(1)).thenReturn(expectedMovie);

        Movie actualMovie = movieService.getMovieById(1);

        assertNotNull(actualMovie);
        assertEquals(1, actualMovie.getMovieId());
        assertEquals("Interstellar", actualMovie.getTitle());
        assertEquals("English", actualMovie.getLanguage());
        assertEquals("Science Fiction", actualMovie.getGenre());

        verify(mockMovieDAO).getMovieById(1);
    }


    @Test
    void testUpdateMovie() throws SQLException {

        MovieDAO mockMovieDAO = mock(MovieDAO.class);

        MovieService movieService = new MovieService(mockMovieDAO);

        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setTitle("Interstellar Updated");
        movie.setLanguage("English");
        movie.setGenre("Science Fiction");
        movie.setDuration(170);
        movie.setReleaseDate(LocalDate.of(2014, 11, 7));

        movieService.updateMovie(movie);

        verify(mockMovieDAO).updateMovie(movie);
    }


    @Test
    void testDeleteMovie() throws SQLException {

        MovieDAO mockMovieDAO = mock(MovieDAO.class);

        MovieService movieService = new MovieService(mockMovieDAO);

        movieService.deleteMovie(1);

        verify(mockMovieDAO).deleteMovie(1);
    }
}