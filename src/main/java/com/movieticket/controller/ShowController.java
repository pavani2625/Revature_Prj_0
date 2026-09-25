package com.movieticket.controller;

import com.movieticket.model.Show;
import com.movieticket.service.ShowService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ShowController {

    private static final Logger logger =
            Logger.getLogger(ShowController.class.getName());

    private final ShowService showService;
    private final Scanner scanner;

    public ShowController() {
        showService = new ShowService();
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

            System.out.print("Enter your choice: ");

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

        System.out.print("Enter theatre ID: ");
        show.setTheatreId(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter movie ID: ");
        show.setMovieId(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter show date (YYYY-MM-DD): ");
        String showDate = scanner.nextLine();

        show.setShowDate(
                LocalDate.parse(showDate)
        );

        System.out.print("Enter start time (HH:MM): ");
        String startTime = scanner.nextLine();

        show.setStartTime(
                LocalTime.parse(startTime)
        );

        System.out.print("Enter end time (HH:MM): ");
        String endTime = scanner.nextLine();

        show.setEndTime(
                LocalTime.parse(endTime)
        );

        showService.addShow(show);

        logger.info("Show added successfully.");
    }

    private void viewAllShows() throws SQLException {

        logger.info("");
        logger.info("========== ALL SHOWS ==========");

        List<Show> shows =
                showService.getAllShows();

        if (shows.isEmpty()) {
            logger.info("No shows found.");
            return;
        }

        for (Show show : shows) {

            logger.info("----------------------------------------");
            logger.info("Show ID     : " + show.getShowId());
            logger.info("Theatre ID  : " + show.getTheatreId());
            logger.info("Movie ID    : " + show.getMovieId());
            logger.info("Show Date   : " + show.getShowDate());
            logger.info("Start Time  : " + show.getStartTime());
            logger.info("End Time    : " + show.getEndTime());
        }

        logger.info("----------------------------------------");
    }

    private void viewShowById() throws SQLException {

        logger.info("");
        logger.info("========== FIND SHOW ==========");

        System.out.print("Enter show ID: ");

        int showId = scanner.nextInt();
        scanner.nextLine();

        Show show =
                showService.getShowById(showId);

        if (show == null) {
            logger.info("Show not found.");
            return;
        }

        logger.info("Show ID     : " + show.getShowId());
        logger.info("Theatre ID  : " + show.getTheatreId());
        logger.info("Movie ID    : " + show.getMovieId());
        logger.info("Show Date   : " + show.getShowDate());
        logger.info("Start Time  : " + show.getStartTime());
        logger.info("End Time    : " + show.getEndTime());
    }

    private void updateShow() throws SQLException {

        logger.info("");
        logger.info("========== UPDATE SHOW ==========");

        System.out.print("Enter show ID: ");

        int showId = scanner.nextInt();
        scanner.nextLine();

        Show show =
                showService.getShowById(showId);

        if (show == null) {
            logger.info("Show not found.");
            return;
        }

        logger.info("Current theatre ID: " + show.getTheatreId());
        System.out.print("Enter new theatre ID: ");
        show.setTheatreId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Current movie ID: " + show.getMovieId());
        System.out.print("Enter new movie ID: ");
        show.setMovieId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Current show date: " + show.getShowDate());
        System.out.print("Enter new show date (YYYY-MM-DD): ");

        String showDate = scanner.nextLine();

        show.setShowDate(
                LocalDate.parse(showDate)
        );

        logger.info("Current start time: " + show.getStartTime());
        System.out.print("Enter new start time (HH:MM): ");

        String startTime = scanner.nextLine();

        show.setStartTime(
                LocalTime.parse(startTime)
        );

        logger.info("Current end time: " + show.getEndTime());
        System.out.print("Enter new end time (HH:MM): ");

        String endTime = scanner.nextLine();

        show.setEndTime(
                LocalTime.parse(endTime)
        );

        showService.updateShow(show);

        logger.info("Show updated successfully.");
    }

    private void deleteShow() throws SQLException {

        logger.info("");
        logger.info("========== DELETE SHOW ==========");

        System.out.print("Enter show ID: ");

        int showId = scanner.nextInt();
        scanner.nextLine();

        Show show =
                showService.getShowById(showId);

        if (show == null) {
            logger.info("Show not found.");
            return;
        }

        logger.info("Show ID: " + show.getShowId());

        System.out.print(
                "Are you sure you want to delete this show? (Y/N): "
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            showService.deleteShow(showId);

            logger.info("Show deleted successfully.");

        } else {

            logger.info("Delete operation cancelled.");
        }
    }
}