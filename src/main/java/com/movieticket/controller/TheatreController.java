package com.movieticket.controller;

import com.movieticket.model.Theatre;
import com.movieticket.service.TheatreService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class TheatreController {

    private static final Logger logger =
            Logger.getLogger(TheatreController.class.getName());

    private final TheatreService theatreService;
    private final Scanner scanner;

    public TheatreController() {
        theatreService = new TheatreService();
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

            System.out.print("Enter your choice: ");

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

        System.out.print("Enter theatre name: ");
        theatre.setName(scanner.nextLine());

        System.out.print("Enter city: ");
        theatre.setCity(scanner.nextLine());

        System.out.print("Enter address: ");
        theatre.setAddress(scanner.nextLine());

        System.out.print("Enter total seats: ");
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

        System.out.print("Enter theatre ID: ");

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

        System.out.print("Enter theatre ID: ");

        int theatreId = scanner.nextInt();
        scanner.nextLine();

        Theatre theatre =
                theatreService.getTheatreById(theatreId);

        if (theatre == null) {
            logger.info("Theatre not found.");
            return;
        }

        logger.info("Current name: " + theatre.getName());
        System.out.print("Enter new name: ");
        theatre.setName(scanner.nextLine());

        logger.info("Current city: " + theatre.getCity());
        System.out.print("Enter new city: ");
        theatre.setCity(scanner.nextLine());

        logger.info("Current address: " + theatre.getAddress());
        System.out.print("Enter new address: ");
        theatre.setAddress(scanner.nextLine());

        logger.info("Current total seats: " + theatre.getTotalSeats());
        System.out.print("Enter new total seats: ");
        theatre.setTotalSeats(scanner.nextInt());
        scanner.nextLine();

        theatreService.updateTheatre(theatre);

        logger.info("Theatre updated successfully.");
    }

    private void deleteTheatre() throws SQLException {

        logger.info("");
        logger.info("========== DELETE THEATRE ==========");

        System.out.print("Enter theatre ID: ");

        int theatreId = scanner.nextInt();
        scanner.nextLine();

        Theatre theatre =
                theatreService.getTheatreById(theatreId);

        if (theatre == null) {
            logger.info("Theatre not found.");
            return;
        }

        logger.info("Theatre: " + theatre.getName());

        System.out.print(
                "Are you sure you want to delete this theatre? (Y/N): "
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            theatreService.deleteTheatre(theatreId);

            logger.info("Theatre deleted successfully.");

        } else {

            logger.info("Delete operation cancelled.");
        }
    }
}