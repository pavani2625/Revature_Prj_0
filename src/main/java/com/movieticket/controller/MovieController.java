package com.movieticket.controller;

import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import com.movieticket.service.MovieService;
import com.movieticket.service.ShowService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class MovieController {

    private static final Logger logger =
            Logger.getLogger(MovieController.class.getName());

    private final MovieService movieService;
    private final ShowService showService;
    private final Scanner scanner;

    public MovieController() {
        movieService = new MovieService();
        showService = new ShowService();
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
            logger.info("Enter your choice:");

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

        logger.info("Enter title:");
        movie.setTitle(scanner.nextLine());

        logger.info("Enter language:");
        movie.setLanguage(scanner.nextLine());

        logger.info("Enter genre:");
        movie.setGenre(scanner.nextLine());

        logger.info("Enter duration in minutes:");
        movie.setDuration(scanner.nextInt());
        scanner.nextLine();

        logger.info("Enter release date (YYYY-MM-DD):");
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

        logger.info("Enter movie ID:");
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

        logger.info("Enter movie name:");

        String searchText = scanner.nextLine().trim();

        if (searchText.isEmpty()) {
            logger.info("Movie name cannot be empty.");
            return;
        }

        List<Movie> movies =
                movieService.getAllMovies();

        List<Movie> matchingMovies = new ArrayList<>();

        for (Movie movie : movies) {

            if (movie.getTitle() != null &&
                    movie.getTitle()
                            .toLowerCase()
                            .contains(searchText.toLowerCase())) {

                matchingMovies.add(movie);
            }
        }

        if (matchingMovies.isEmpty()) {
            logger.info("No matching movies found.");
            return;
        }

        logger.info("");
        logger.info("Matching Movies:");

        for (int i = 0; i < matchingMovies.size(); i++) {

            Movie movie = matchingMovies.get(i);

            logger.info(
                    (i + 1)
                            + ". "
                            + movie.getTitle()
            );
        }

        logger.info("Select movie:");

        int movieChoice = scanner.nextInt();
        scanner.nextLine();

        if (movieChoice < 1 ||
                movieChoice > matchingMovies.size()) {

            logger.warning("Invalid movie selection.");
            return;
        }

        Movie movie =
                matchingMovies.get(movieChoice - 1);

        logger.info("");
        logger.info("Selected Movie: " + movie.getTitle());

        logger.info("Current title: " + movie.getTitle());
        logger.info("Enter new title:");
        movie.setTitle(scanner.nextLine());

        logger.info("Current language: " + movie.getLanguage());
        logger.info("Enter new language:");
        movie.setLanguage(scanner.nextLine());

        logger.info("Current genre: " + movie.getGenre());
        logger.info("Enter new genre:");
        movie.setGenre(scanner.nextLine());

        logger.info("Current duration: " + movie.getDuration());
        logger.info("Enter new duration in minutes:");
        movie.setDuration(scanner.nextInt());
        scanner.nextLine();

        logger.info("Current release date: " + movie.getReleaseDate());
        logger.info("Enter new release date (YYYY-MM-DD):");

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

        logger.info("Enter movie name:");

        String searchText = scanner.nextLine().trim();

        if (searchText.isEmpty()) {
            logger.info("Movie name cannot be empty.");
            return;
        }

        List<Movie> movies =
                movieService.getAllMovies();

        List<Movie> matchingMovies = new ArrayList<>();

        for (Movie movie : movies) {

            if (movie.getTitle() != null &&
                    movie.getTitle()
                            .toLowerCase()
                            .contains(searchText.toLowerCase())) {

                matchingMovies.add(movie);
            }
        }

        if (matchingMovies.isEmpty()) {
            logger.info("No matching movies found.");
            return;
        }

        logger.info("");
        logger.info("Matching Movies:");

        for (int i = 0; i < matchingMovies.size(); i++) {

            Movie movie = matchingMovies.get(i);

            logger.info(
                    (i + 1)
                            + ". "
                            + movie.getTitle()
            );
        }

        logger.info("Select movie:");

        int movieChoice = scanner.nextInt();
        scanner.nextLine();

        if (movieChoice < 1 ||
                movieChoice > matchingMovies.size()) {

            logger.warning("Invalid movie selection.");
            return;
        }

        Movie movie =
                matchingMovies.get(movieChoice - 1);

        logger.info("");
        logger.info("Selected Movie: " + movie.getTitle());

        List<Show> shows =
                showService.getAllShows();

        for (Show show : shows) {

            if (show.getMovieId() == movie.getMovieId()) {

                logger.info("");
                logger.info(
                        "Cannot delete movie because it is used by one or more shows."
                );
                logger.info(
                        "Delete the related shows first."
                );

                return;
            }
        }

        logger.info(
                "Are you sure you want to delete this movie? (Y/N):"
        );

        String confirmation =
                scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            movieService.deleteMovie(movie.getMovieId());

            logger.info("");
            logger.info("Movie deleted successfully.");

        } else {

            logger.info("");
            logger.info("Delete operation cancelled.");
        }
    }
}