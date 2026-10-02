package com.movieticket.controller;

import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import com.movieticket.model.Theatre;
import com.movieticket.service.MovieService;
import com.movieticket.service.ShowService;
import com.movieticket.service.TheatreService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ShowController {

    private static final Logger logger =
            Logger.getLogger(ShowController.class.getName());

    private final ShowService showService;
    private final TheatreService theatreService;
    private final MovieService movieService;
    private final Scanner scanner;

    public ShowController() {
        showService = new ShowService();
        theatreService = new TheatreService();
        movieService = new MovieService();
        scanner = new Scanner(System.in);
    }

    public void showShowMenu() {

        boolean running = true;

        while (running) {

            logger.info("");
            logger.info("========================================");
            logger.info("            SHOW MANAGEMENT");
            logger.info("========================================");
            logger.info("1. Add Show");
            logger.info("2. View All Shows");
            logger.info("3. View Show By ID");
            logger.info("4. Update Show");
            logger.info("5. Delete Show");
            logger.info("6. Back");
            logger.info("========================================");
            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        addShow();
                        break;

                    case 2:
                        viewAllShows();
                        break;

                    case 3:
                        viewShowById();
                        break;

                    case 4:
                        updateShow();
                        break;

                    case 5:
                        deleteShow();
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

    private void addShow() throws SQLException {

        logger.info("");
        logger.info("========== ADD SHOW ==========");

        Show show = new Show();

        Theatre theatre = selectTheatre();

        if (theatre == null) {
            return;
        }

        Movie movie = selectMovie();

        if (movie == null) {
            return;
        }

        show.setTheatreId(theatre.getTheatreId());
        show.setMovieId(movie.getMovieId());

        logger.info("Selected Theatre: " + theatre.getName());
        logger.info("Selected Movie: " + movie.getTitle());

        logger.info("Enter show date (YYYY-MM-DD):");

        LocalDate showDate =
                LocalDate.parse(scanner.nextLine());

        logger.info("Enter start time (HH:MM):");

        LocalTime startTime =
                LocalTime.parse(scanner.nextLine());

        logger.info("Enter end time (HH:MM):");

        LocalTime endTime =
                LocalTime.parse(scanner.nextLine());

        show.setShowDate(showDate);
        show.setStartTime(startTime);
        show.setEndTime(endTime);

        showService.addShow(show);

        logger.info("Show added successfully.");
    }

    private void viewAllShows() throws SQLException {

        logger.info("");
        logger.info("========== ALL SHOWS ==========");

        List<Show> shows =
                showService.getAllShows();

        List<Theatre> theatres =
                theatreService.getAllTheatres();

        List<Movie> movies =
                movieService.getAllMovies();

        if (shows.isEmpty()) {
            logger.info("No shows found.");
            return;
        }

        for (Theatre theatre : theatres) {

            List<Show> theatreShows =
                    new ArrayList<>();

            for (Show show : shows) {

                if (show.getTheatreId() ==
                        theatre.getTheatreId()) {

                    theatreShows.add(show);
                }
            }

            if (theatreShows.isEmpty()) {
                continue;
            }

            logger.info("");
            logger.info("THEATRE: " + theatre.getName());
            logger.info("========================================");

            List<Integer> displayedMovieIds =
                    new ArrayList<>();

            for (Show show : theatreShows) {

                if (displayedMovieIds.contains(
                        show.getMovieId())) {

                    continue;
                }

                displayedMovieIds.add(show.getMovieId());

                Movie movie = null;

                for (Movie currentMovie : movies) {

                    if (currentMovie.getMovieId() ==
                            show.getMovieId()) {

                        movie = currentMovie;
                        break;
                    }
                }

                if (movie == null) {
                    continue;
                }

                logger.info("");
                logger.info("MOVIE: " + movie.getTitle());
                logger.info("----------------------------------------");

                for (Show movieShow : theatreShows) {

                    if (movieShow.getMovieId() ==
                            movie.getMovieId()) {

                        logger.info(
                                "Show Date  : "
                                        + movieShow.getShowDate()
                        );

                        logger.info(
                                "Start Time : "
                                        + movieShow.getStartTime()
                        );

                        logger.info(
                                "End Time   : "
                                        + movieShow.getEndTime()
                        );

                        logger.info("");
                    }
                }
            }
        }

        logger.info("========================================");
    }

    private void viewShowById() throws SQLException {

        logger.info("");
        logger.info("========== FIND SHOW ==========");

        logger.info("Enter show ID:");

        int showId = scanner.nextInt();
        scanner.nextLine();

        Show show =
                showService.getShowById(showId);

        if (show == null) {
            logger.info("Show not found.");
            return;
        }

        Movie movie =
                movieService.getMovieById(
                        show.getMovieId()
                );

        Theatre theatre =
                theatreService.getTheatreById(
                        show.getTheatreId()
                );

        logger.info(
                "Show ID     : "
                        + show.getShowId()
        );

        if (movie != null) {

            logger.info(
                    "Movie       : "
                            + movie.getTitle()
            );
        }

        if (theatre != null) {

            logger.info(
                    "Theatre     : "
                            + theatre.getName()
            );
        }

        logger.info(
                "Show Date   : "
                        + show.getShowDate()
        );

        logger.info(
                "Start Time  : "
                        + show.getStartTime()
        );

        logger.info(
                "End Time    : "
                        + show.getEndTime()
        );
    }

    private void updateShow() throws SQLException {

        logger.info("");
        logger.info("========== UPDATE SHOW ==========");

        Theatre theatre = selectTheatre();

        if (theatre == null) {
            return;
        }

        Show show = selectShowFromTheatre(theatre);

        if (show == null) {
            return;
        }

        Movie movie =
                movieService.getMovieById(
                        show.getMovieId()
                );

        logger.info(
                "Selected Theatre: "
                        + theatre.getName()
        );

        if (movie != null) {

            logger.info(
                    "Current Movie: "
                            + movie.getTitle()
            );
        }

        Movie selectedMovie = selectMovie();

        if (selectedMovie == null) {
            return;
        }

        show.setMovieId(
                selectedMovie.getMovieId()
        );

        logger.info(
                "Selected Movie: "
                        + selectedMovie.getTitle()
        );

        logger.info(
                "Current show date: "
                        + show.getShowDate()
        );

        logger.info(
                "Enter new show date (YYYY-MM-DD):"
        );

        show.setShowDate(
                LocalDate.parse(
                        scanner.nextLine()
                )
        );

        logger.info(
                "Current start time: "
                        + show.getStartTime()
        );

        logger.info(
                "Enter new start time (HH:MM):"
        );

        show.setStartTime(
                LocalTime.parse(
                        scanner.nextLine()
                )
        );

        logger.info(
                "Current end time: "
                        + show.getEndTime()
        );

        logger.info(
                "Enter new end time (HH:MM):"
        );

        show.setEndTime(
                LocalTime.parse(
                        scanner.nextLine()
                )
        );

        showService.updateShow(show);

        logger.info("Show updated successfully.");
    }

    private void deleteShow() throws SQLException {

        logger.info("");
        logger.info("========== DELETE SHOW ==========");

        Theatre theatre = selectTheatre();

        if (theatre == null) {
            return;
        }

        Show show = selectShowFromTheatre(theatre);

        if (show == null) {
            return;
        }

        Movie movie =
                movieService.getMovieById(
                        show.getMovieId()
                );

        logger.info(
                "Selected Theatre: "
                        + theatre.getName()
        );

        if (movie != null) {

            logger.info(
                    "Selected Movie: "
                            + movie.getTitle()
            );
        }

        logger.info(
                "Show Date: "
                        + show.getShowDate()
        );

        logger.info(
                "Start Time: "
                        + show.getStartTime()
        );

        logger.info(
                "End Time: "
                        + show.getEndTime()
        );

        logger.info(
                "Are you sure you want to delete this show? (Y/N):"
        );

        String confirmation =
                scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            showService.deleteShow(
                    show.getShowId()
            );

            logger.info("Show deleted successfully.");

        } else {

            logger.info("Delete operation cancelled.");
        }
    }

    private Show selectShowFromTheatre(
            Theatre theatre) throws SQLException {

        List<Show> shows =
                showService.getAllShows();

        List<Movie> movies =
                movieService.getAllMovies();

        List<Show> theatreShows =
                new ArrayList<>();

        for (Show show : shows) {

            if (show.getTheatreId() ==
                    theatre.getTheatreId()) {

                theatreShows.add(show);
            }
        }

        if (theatreShows.isEmpty()) {

            logger.info(
                    "No shows found in "
                            + theatre.getName()
            );

            return null;
        }

        logger.info("");
        logger.info(
                "Shows available in "
                        + theatre.getName()
                        + ":"
        );

        for (int i = 0;
             i < theatreShows.size();
             i++) {

            Show show =
                    theatreShows.get(i);

            Movie movie = null;

            for (Movie currentMovie : movies) {

                if (currentMovie.getMovieId() ==
                        show.getMovieId()) {

                    movie = currentMovie;
                    break;
                }
            }

            String movieName =
                    movie != null
                            ? movie.getTitle()
                            : "Unknown Movie";

            logger.info(
                    (i + 1)
                            + ". "
                            + movieName
                            + " | "
                            + show.getShowDate()
                            + " | "
                            + show.getStartTime()
                            + " - "
                            + show.getEndTime()
            );
        }

        logger.info("Select show:");

        int showChoice =
                scanner.nextInt();

        scanner.nextLine();

        if (showChoice < 1 ||
                showChoice > theatreShows.size()) {

            logger.warning(
                    "Invalid show selection."
            );

            return null;
        }

        return theatreShows.get(
                showChoice - 1
        );
    }

    private Theatre selectTheatre()
            throws SQLException {

        logger.info("Enter theatre name:");

        String searchText =
                scanner.nextLine().trim();

        if (searchText.isEmpty()) {

            logger.info(
                    "Theatre name cannot be empty."
            );

            return null;
        }

        List<Theatre> theatres =
                theatreService.getAllTheatres();

        List<Theatre> matchingTheatres =
                new ArrayList<>();

        for (Theatre theatre : theatres) {

            if (theatre.getName() != null &&
                    theatre.getName()
                            .toLowerCase()
                            .contains(
                                    searchText.toLowerCase()
                            )) {

                matchingTheatres.add(theatre);
            }
        }

        if (matchingTheatres.isEmpty()) {

            logger.info(
                    "No matching theatres found."
            );

            return null;
        }

        logger.info("");
        logger.info("Matching Theatres:");

        for (int i = 0;
             i < matchingTheatres.size();
             i++) {

            logger.info(
                    (i + 1)
                            + ". "
                            + matchingTheatres
                            .get(i)
                            .getName()
            );
        }

        logger.info("Select theatre:");

        int theatreChoice =
                scanner.nextInt();

        scanner.nextLine();

        if (theatreChoice < 1 ||
                theatreChoice > matchingTheatres.size()) {

            logger.warning(
                    "Invalid theatre selection."
            );

            return null;
        }

        return matchingTheatres.get(
                theatreChoice - 1
        );
    }

    private Movie selectMovie()
            throws SQLException {

        logger.info("Enter movie name:");

        String searchText =
                scanner.nextLine().trim();

        if (searchText.isEmpty()) {

            logger.info(
                    "Movie name cannot be empty."
            );

            return null;
        }

        List<Movie> movies =
                movieService.getAllMovies();

        List<Movie> matchingMovies =
                new ArrayList<>();

        for (Movie movie : movies) {

            if (movie.getTitle() != null &&
                    movie.getTitle()
                            .toLowerCase()
                            .contains(
                                    searchText.toLowerCase()
                            )) {

                matchingMovies.add(movie);
            }
        }

        if (matchingMovies.isEmpty()) {

            logger.info(
                    "No matching movies found."
            );

            return null;
        }

        logger.info("");
        logger.info("Matching Movies:");

        for (int i = 0;
             i < matchingMovies.size();
             i++) {

            logger.info(
                    (i + 1)
                            + ". "
                            + matchingMovies
                            .get(i)
                            .getTitle()
            );
        }

        logger.info("Select movie:");

        int movieChoice =
                scanner.nextInt();

        scanner.nextLine();

        if (movieChoice < 1 ||
                movieChoice > matchingMovies.size()) {

            logger.warning(
                    "Invalid movie selection."
            );

            return null;
        }

        return matchingMovies.get(
                movieChoice - 1
        );
    }
}