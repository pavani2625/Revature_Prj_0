package com.movieticket.controller;

import com.movieticket.model.Movie;
import com.movieticket.service.MovieService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class MovieController {

    private static final Logger logger =
            Logger.getLogger(MovieController.class.getName());

    private final MovieService movieService;
    private final Scanner scanner;

    public MovieController() {
        movieService = new MovieService();
        scanner = new Scanner(System.in);
    }

    public void showMovieMenu() {

        boolean running = true;

        while (running) {

            logger.info("");
            logger.info("========================================");
            logger.info("          MOVIE MANAGEMENT");
            logger.info("========================================");
            logger.info("1. Add Movie");
            logger.info("2. View All Movies");
            logger.info("3. View Movie By ID");
            logger.info("4. Update Movie");
            logger.info("5. Delete Movie");
            logger.info("6. Back");
            logger.info("========================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        addMovie();
                        break;

                    case 2:
                        viewAllMovies();
                        break;

                    case 3:
                        viewMovieById();
                        break;

                    case 4:
                        updateMovie();
                        break;

                    case 5:
                        deleteMovie();
                        break;

                    case 6:
                        running = false;
                        break;

                    default:
                        logger.warning("Invalid choice. Please try again.");
                }

            } catch (SQLException e) {

                logger.severe("Database error: " + e.getMessage());
            }
        }
    }

    private void addMovie() throws SQLException {

        logger.info("");
        logger.info("========== ADD MOVIE ==========");

        Movie movie = new Movie();

        System.out.print("Enter title: ");
        movie.setTitle(scanner.nextLine());

        System.out.print("Enter language: ");
        movie.setLanguage(scanner.nextLine());

        System.out.print("Enter genre: ");
        movie.setGenre(scanner.nextLine());

        System.out.print("Enter duration in minutes: ");
        movie.setDuration(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter release date (YYYY-MM-DD): ");
        String releaseDate = scanner.nextLine();

        movie.setReleaseDate(
                LocalDate.parse(releaseDate)
        );

        movieService.addMovie(movie);

        logger.info("");
        logger.info("Movie added successfully.");
    }

    private void viewAllMovies() throws SQLException {

        logger.info("");
        logger.info("========== ALL MOVIES ==========");

        List<Movie> movies =
                movieService.getAllMovies();

        if (movies.isEmpty()) {
            logger.info("No movies found.");
            return;
        }

        for (Movie movie : movies) {

            logger.info("----------------------------------------");
            logger.info("Movie ID   : " + movie.getMovieId());
            logger.info("Title      : " + movie.getTitle());
            logger.info("Language   : " + movie.getLanguage());
            logger.info("Genre      : " + movie.getGenre());
            logger.info("Duration   : " + movie.getDuration() + " minutes");
            logger.info("Release    : " + movie.getReleaseDate());
        }

        logger.info("----------------------------------------");
    }

    private void viewMovieById() throws SQLException {

        logger.info("");
        logger.info("========== FIND MOVIE ==========");

        System.out.print("Enter movie ID: ");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        Movie movie =
                movieService.getMovieById(movieId);

        if (movie == null) {
            logger.info("Movie not found.");
            return;
        }

        logger.info("");
        logger.info("Movie ID   : " + movie.getMovieId());
        logger.info("Title      : " + movie.getTitle());
        logger.info("Language   : " + movie.getLanguage());
        logger.info("Genre      : " + movie.getGenre());
        logger.info("Duration   : " + movie.getDuration() + " minutes");
        logger.info("Release    : " + movie.getReleaseDate());
    }

    private void updateMovie() throws SQLException {

        logger.info("");
        logger.info("========== UPDATE MOVIE ==========");

        System.out.print("Enter movie ID: ");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        Movie movie =
                movieService.getMovieById(movieId);

        if (movie == null) {
            logger.info("Movie not found.");
            return;
        }

        logger.info("Current title: " + movie.getTitle());
        System.out.print("Enter new title: ");
        movie.setTitle(scanner.nextLine());

        logger.info("Current language: " + movie.getLanguage());
        System.out.print("Enter new language: ");
        movie.setLanguage(scanner.nextLine());

        logger.info("Current genre: " + movie.getGenre());
        System.out.print("Enter new genre: ");
        movie.setGenre(scanner.nextLine());

        logger.info("Current duration: " + movie.getDuration());
        System.out.print("Enter new duration in minutes: ");
        movie.setDuration(scanner.nextInt());
        scanner.nextLine();

        logger.info("Current release date: " + movie.getReleaseDate());
        System.out.print("Enter new release date (YYYY-MM-DD): ");

        String releaseDate = scanner.nextLine();

        movie.setReleaseDate(
                LocalDate.parse(releaseDate)
        );

        movieService.updateMovie(movie);

        logger.info("");
        logger.info("Movie updated successfully.");
    }

    private void deleteMovie() throws SQLException {

        logger.info("");
        logger.info("========== DELETE MOVIE ==========");

        System.out.print("Enter movie ID: ");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        Movie movie =
                movieService.getMovieById(movieId);

        if (movie == null) {
            logger.info("Movie not found.");
            return;
        }

        logger.info("Movie: " + movie.getTitle());

        System.out.print(
                "Are you sure you want to delete this movie? (Y/N): "
        );

        String confirmation =
                scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            movieService.deleteMovie(movieId);

            logger.info("");
            logger.info("Movie deleted successfully.");

        } else {

            logger.info("");
            logger.info("Delete operation cancelled.");
        }
    }
}