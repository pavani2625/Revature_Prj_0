package com.movieticket.controller;

import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import com.movieticket.model.Theatre;
import com.movieticket.service.SeatService;
import com.movieticket.service.ShowService;
import com.movieticket.service.TheatreService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class TheatreController {

    private static final Logger logger =
            Logger.getLogger(TheatreController.class.getName());

    private final TheatreService theatreService;
    private final SeatService seatService;
    private final ShowService showService;
    private final Scanner scanner;

    public TheatreController() {
        theatreService = new TheatreService();
        seatService = new SeatService();
        showService = new ShowService();
        scanner = new Scanner(System.in);
    }

    public void showTheatreMenu() {

        boolean running = true;

        while (running) {

            logger.info("");
            logger.info("========================================");
            logger.info("         THEATRE MANAGEMENT");
            logger.info("========================================");
            logger.info("1. Add Theatre");
            logger.info("2. View All Theatres");
            logger.info("3. View Theatre By ID");
            logger.info("4. Update Theatre");
            logger.info("5. Delete Theatre");
            logger.info("6. Back");
            logger.info("========================================");
            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        addTheatre();
                        break;

                    case 2:
                        viewAllTheatres();
                        break;

                    case 3:
                        viewTheatreById();
                        break;

                    case 4:
                        updateTheatre();
                        break;

                    case 5:
                        deleteTheatre();
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

    private void addTheatre() throws SQLException {

        logger.info("");
        logger.info("========== ADD THEATRE ==========");

        Theatre theatre = new Theatre();

        logger.info("Enter theatre name:");
        theatre.setName(scanner.nextLine());

        logger.info("Enter city:");
        theatre.setCity(scanner.nextLine());

        logger.info("Enter address:");
        theatre.setAddress(scanner.nextLine());

        logger.info("Enter total seats:");
        theatre.setTotalSeats(scanner.nextInt());
        scanner.nextLine();

        theatreService.addTheatre(theatre);

        logger.info("Theatre added successfully.");
    }

    private void viewAllTheatres() throws SQLException {

        logger.info("");
        logger.info("========== ALL THEATRES ==========");

        List<Theatre> theatres =
                theatreService.getAllTheatres();

        if (theatres.isEmpty()) {
            logger.info("No theatres found.");
            return;
        }

        for (Theatre theatre : theatres) {

            logger.info("----------------------------------------");
            logger.info("Theatre ID  : " + theatre.getTheatreId());
            logger.info("Name        : " + theatre.getName());
            logger.info("City        : " + theatre.getCity());
            logger.info("Address     : " + theatre.getAddress());
            logger.info("Total Seats : " + theatre.getTotalSeats());
        }

        logger.info("----------------------------------------");
    }

    private void viewTheatreById() throws SQLException {

        logger.info("");
        logger.info("========== FIND THEATRE ==========");

        logger.info("Enter theatre ID:");

        int theatreId = scanner.nextInt();
        scanner.nextLine();

        Theatre theatre =
                theatreService.getTheatreById(theatreId);

        if (theatre == null) {
            logger.info("Theatre not found.");
            return;
        }

        logger.info("Theatre ID  : " + theatre.getTheatreId());
        logger.info("Name        : " + theatre.getName());
        logger.info("City        : " + theatre.getCity());
        logger.info("Address     : " + theatre.getAddress());
        logger.info("Total Seats : " + theatre.getTotalSeats());
    }

    private void updateTheatre() throws SQLException {

        logger.info("");
        logger.info("========== UPDATE THEATRE ==========");

        logger.info("Enter theatre name:");

        String searchText = scanner.nextLine().trim();

        if (searchText.isEmpty()) {
            logger.info("Theatre name cannot be empty.");
            return;
        }

        List<Theatre> theatres =
                theatreService.getAllTheatres();

        List<Theatre> matchingTheatres =
                new ArrayList<>();

        for (Theatre theatre : theatres) {

            if (theatre.getName() != null &&
                    theatre.getName()
                            .toLowerCase()
                            .contains(searchText.toLowerCase())) {

                matchingTheatres.add(theatre);
            }
        }

        if (matchingTheatres.isEmpty()) {
            logger.info("No matching theatres found.");
            return;
        }

        logger.info("");
        logger.info("Matching Theatres:");

        for (int i = 0; i < matchingTheatres.size(); i++) {

            Theatre theatre =
                    matchingTheatres.get(i);

            logger.info(
                    (i + 1)
                            + ". "
                            + theatre.getName()
            );
        }

        logger.info("Select theatre:");

        int theatreChoice = scanner.nextInt();
        scanner.nextLine();

        if (theatreChoice < 1 ||
                theatreChoice > matchingTheatres.size()) {

            logger.warning("Invalid theatre selection.");
            return;
        }

        Theatre theatre =
                matchingTheatres.get(theatreChoice - 1);

        logger.info("");
        logger.info(
                "Selected Theatre: "
                        + theatre.getName()
        );

        logger.info("Current name: " + theatre.getName());
        logger.info("Enter new name:");
        theatre.setName(scanner.nextLine());

        logger.info("Current city: " + theatre.getCity());
        logger.info("Enter new city:");
        theatre.setCity(scanner.nextLine());

        logger.info("Current address: " + theatre.getAddress());
        logger.info("Enter new address:");
        theatre.setAddress(scanner.nextLine());

        logger.info("Current total seats: " + theatre.getTotalSeats());
        logger.info("Enter new total seats:");
        theatre.setTotalSeats(scanner.nextInt());
        scanner.nextLine();

        theatreService.updateTheatre(theatre);

        logger.info("Theatre updated successfully.");
    }

    private void deleteTheatre() throws SQLException {

        logger.info("");
        logger.info("========== DELETE THEATRE ==========");

        logger.info("Enter theatre name:");

        String searchText = scanner.nextLine().trim();

        if (searchText.isEmpty()) {
            logger.info("Theatre name cannot be empty.");
            return;
        }

        List<Theatre> theatres =
                theatreService.getAllTheatres();

        List<Theatre> matchingTheatres =
                new ArrayList<>();

        for (Theatre theatre : theatres) {

            if (theatre.getName() != null &&
                    theatre.getName()
                            .toLowerCase()
                            .contains(searchText.toLowerCase())) {

                matchingTheatres.add(theatre);
            }
        }

        if (matchingTheatres.isEmpty()) {
            logger.info("No matching theatres found.");
            return;
        }

        logger.info("");
        logger.info("Matching Theatres:");

        for (int i = 0; i < matchingTheatres.size(); i++) {

            Theatre theatre =
                    matchingTheatres.get(i);

            logger.info(
                    (i + 1)
                            + ". "
                            + theatre.getName()
            );
        }

        logger.info("Select theatre:");

        int theatreChoice = scanner.nextInt();
        scanner.nextLine();

        if (theatreChoice < 1 ||
                theatreChoice > matchingTheatres.size()) {

            logger.warning("Invalid theatre selection.");
            return;
        }

        Theatre theatre =
                matchingTheatres.get(theatreChoice - 1);

        logger.info("");
        logger.info(
                "Selected Theatre: "
                        + theatre.getName()
        );

        List<Seat> seats =
                seatService.getAllSeats();

        for (Seat seat : seats) {

            if (seat.getTheatreId() == theatre.getTheatreId()) {

                logger.info("");
                logger.info(
                        "Cannot delete theatre because it is used by one or more shows."
                );
                logger.info(
                        "Delete the related shows and seats first."
                );

                return;
            }
        }

        List<Show> shows =
                showService.getAllShows();

        for (Show show : shows) {

            if (show.getTheatreId() == theatre.getTheatreId()) {

                logger.info("");
                logger.info(
                        "Cannot delete theatre because it is used by one or more shows."
                );
                logger.info(
                        "Delete the related shows first."
                );

                return;
            }
        }

        logger.info(
                "Are you sure you want to delete this theatre? (Y/N):"
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            theatreService.deleteTheatre(
                    theatre.getTheatreId()
            );

            logger.info("Theatre deleted successfully.");

        } else {

            logger.info("Delete operation cancelled.");
        }
    }
}