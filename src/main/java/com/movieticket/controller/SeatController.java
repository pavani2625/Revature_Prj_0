package com.movieticket.controller;

import com.movieticket.model.Seat;
import com.movieticket.model.Theatre;
import com.movieticket.service.SeatService;
import com.movieticket.service.TheatreService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class SeatController {

    private static final Logger logger =
            Logger.getLogger(SeatController.class.getName());

    private final SeatService seatService;
    private final TheatreService theatreService;
    private final Scanner scanner;

    public SeatController() {
        seatService = new SeatService();
        theatreService = new TheatreService();
        scanner = new Scanner(System.in);
    }

    public void showSeatMenu() {

        boolean running = true;

        while (running) {

            logger.info("");
            logger.info("========================================");
            logger.info("           SEAT MANAGEMENT");
            logger.info("========================================");
            logger.info("1. Add Seat");
            logger.info("2. View All Seats");
            logger.info("3. View Seat By ID");
            logger.info("4. Update Seat");
            logger.info("5. Delete Seat");
            logger.info("6. Back");
            logger.info("========================================");
            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        addSeat();
                        break;

                    case 2:
                        viewAllSeats();
                        break;

                    case 3:
                        viewSeatById();
                        break;

                    case 4:
                        updateSeat();
                        break;

                    case 5:
                        deleteSeat();
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

    private void addSeat() throws SQLException {

        logger.info("");
        logger.info("========== ADD SEAT ==========");

        Seat seat = new Seat();

        Theatre theatre = selectTheatre();

        if (theatre == null) {
            return;
        }

        seat.setTheatreId(theatre.getTheatreId());

        logger.info("Selected Theatre: " + theatre.getName());

        logger.info("Enter seat number:");
        seat.setSeatNumber(scanner.nextLine());

        logger.info("Enter seat type:");
        seat.setSeatType(scanner.nextLine());

        logger.info("Enter price:");
        seat.setPrice(scanner.nextBigDecimal());
        scanner.nextLine();

        seatService.addSeat(seat);

        logger.info("Seat added successfully.");
    }

    private void viewAllSeats() throws SQLException {

        logger.info("");
        logger.info("========== ALL SEATS ==========");

        List<Seat> seats =
                seatService.getAllSeats();

        List<Theatre> theatres =
                theatreService.getAllTheatres();

        if (seats.isEmpty()) {
            logger.info("No seats found.");
            return;
        }

        for (Theatre theatre : theatres) {

            List<Seat> theatreSeats =
                    new ArrayList<>();

            for (Seat seat : seats) {

                if (seat.getTheatreId() ==
                        theatre.getTheatreId()) {

                    theatreSeats.add(seat);
                }
            }

            if (theatreSeats.isEmpty()) {
                continue;
            }

            logger.info("");
            logger.info("THEATRE: " + theatre.getName());
            logger.info("----------------------------------------");

            for (Seat seat : theatreSeats) {

                logger.info(
                        "Seat Number : "
                                + seat.getSeatNumber()
                );

                logger.info(
                        "Seat Type   : "
                                + seat.getSeatType()
                );

                logger.info(
                        "Price       : ₹"
                                + seat.getPrice()
                );

                logger.info("");
            }
        }

        logger.info("========================================");
    }

    private void viewSeatById() throws SQLException {

        logger.info("");
        logger.info("========== FIND SEAT ==========");

        logger.info("Enter seat ID:");

        int seatId = scanner.nextInt();
        scanner.nextLine();

        Seat seat =
                seatService.getSeatById(seatId);

        if (seat == null) {
            logger.info("Seat not found.");
            return;
        }

        Theatre theatre =
                theatreService.getTheatreById(
                        seat.getTheatreId()
                );

        if (theatre != null) {

            logger.info(
                    "Theatre     : "
                            + theatre.getName()
            );
        }

        logger.info(
                "Seat ID     : "
                        + seat.getSeatId()
        );

        logger.info(
                "Seat Number : "
                        + seat.getSeatNumber()
        );

        logger.info(
                "Seat Type   : "
                        + seat.getSeatType()
        );

        logger.info(
                "Price       : ₹"
                        + seat.getPrice()
        );
    }

    private void updateSeat() throws SQLException {

        logger.info("");
        logger.info("========== UPDATE SEAT ==========");

        Theatre theatre = selectTheatre();

        if (theatre == null) {
            return;
        }

        logger.info(
                "Selected Theatre: "
                        + theatre.getName()
        );

        logger.info("Enter seat number:");

        String seatNumber =
                scanner.nextLine().trim();

        if (seatNumber.isEmpty()) {

            logger.info("Seat number cannot be empty.");
            return;
        }

        Seat seat =
                findSeat(
                        theatre.getTheatreId(),
                        seatNumber
                );

        if (seat == null) {

            logger.info(
                    "Seat "
                            + seatNumber
                            + " not found in "
                            + theatre.getName()
            );

            return;
        }

        logger.info("");
        logger.info(
                "Selected Seat: "
                        + seat.getSeatNumber()
        );

        logger.info(
                "Current seat number: "
                        + seat.getSeatNumber()
        );

        logger.info("Enter new seat number:");
        seat.setSeatNumber(scanner.nextLine());

        logger.info(
                "Current seat type: "
                        + seat.getSeatType()
        );

        logger.info("Enter new seat type:");
        seat.setSeatType(scanner.nextLine());

        logger.info(
                "Current price: "
                        + seat.getPrice()
        );

        logger.info("Enter new price:");
        seat.setPrice(scanner.nextBigDecimal());
        scanner.nextLine();

        seatService.updateSeat(seat);

        logger.info("Seat updated successfully.");
    }

    private void deleteSeat() throws SQLException {

        logger.info("");
        logger.info("========== DELETE SEAT ==========");

        Theatre theatre = selectTheatre();

        if (theatre == null) {
            return;
        }

        logger.info(
                "Selected Theatre: "
                        + theatre.getName()
        );

        logger.info("Enter seat number:");

        String seatNumber =
                scanner.nextLine().trim();

        if (seatNumber.isEmpty()) {

            logger.info("Seat number cannot be empty.");
            return;
        }

        Seat seat =
                findSeat(
                        theatre.getTheatreId(),
                        seatNumber
                );

        if (seat == null) {

            logger.info(
                    "Seat "
                            + seatNumber
                            + " not found in "
                            + theatre.getName()
            );

            return;
        }

        logger.info("");
        logger.info(
                "Selected Seat: "
                        + seat.getSeatNumber()
        );

        logger.info(
                "Are you sure you want to delete this seat? (Y/N):"
        );

        String confirmation =
                scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            seatService.deleteSeat(
                    seat.getSeatId()
            );

            logger.info("Seat deleted successfully.");

        } else {

            logger.info("Delete operation cancelled.");
        }
    }

    private Theatre selectTheatre() throws SQLException {

        logger.info("Enter theatre name:");

        String searchText =
                scanner.nextLine().trim();

        if (searchText.isEmpty()) {

            logger.info("Theatre name cannot be empty.");
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

            logger.info("No matching theatres found.");
            return null;
        }

        logger.info("");
        logger.info("Matching Theatres:");

        for (int i = 0;
             i < matchingTheatres.size();
             i++) {

            Theatre theatre =
                    matchingTheatres.get(i);

            logger.info(
                    (i + 1)
                            + ". "
                            + theatre.getName()
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

    private Seat findSeat(
            int theatreId,
            String seatNumber) throws SQLException {

        List<Seat> seats =
                seatService.getAllSeats();

        for (Seat seat : seats) {

            if (seat.getTheatreId() == theatreId &&
                    seat.getSeatNumber() != null &&
                    seat.getSeatNumber()
                            .equalsIgnoreCase(seatNumber)) {

                return seat;
            }
        }

        return null;
    }
}