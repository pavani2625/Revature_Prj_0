package com.movieticket.controller;

import com.movieticket.model.Seat;
import com.movieticket.service.SeatService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class SeatController {

    private static final Logger logger =
            Logger.getLogger(SeatController.class.getName());

    private final SeatService seatService;
    private final Scanner scanner;

    public SeatController() {
        seatService = new SeatService();
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

            System.out.print("Enter your choice: ");

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

        System.out.print("Enter theatre ID: ");
        seat.setTheatreId(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter seat number: ");
        seat.setSeatNumber(scanner.nextLine());

        System.out.print("Enter seat type: ");
        seat.setSeatType(scanner.nextLine());

        System.out.print("Enter price: ");
        seat.setPrice(scanner.nextBigDecimal());
        scanner.nextLine();

        seatService.addSeat(seat);

        logger.info("Seat added successfully.");
    }

    private void viewAllSeats() throws SQLException {

        logger.info("");
        logger.info("========== ALL SEATS ==========");

        List<Seat> seats = seatService.getAllSeats();

        if (seats.isEmpty()) {
            logger.info("No seats found.");
            return;
        }

        for (Seat seat : seats) {

            logger.info("----------------------------------------");
            logger.info("Seat ID     : " + seat.getSeatId());
            logger.info("Theatre ID  : " + seat.getTheatreId());
            logger.info("Seat Number : " + seat.getSeatNumber());
            logger.info("Seat Type   : " + seat.getSeatType());
            logger.info("Price       : ₹" + seat.getPrice());
        }

        logger.info("----------------------------------------");
    }

    private void viewSeatById() throws SQLException {

        logger.info("");
        logger.info("========== FIND SEAT ==========");

        System.out.print("Enter seat ID: ");

        int seatId = scanner.nextInt();
        scanner.nextLine();

        Seat seat = seatService.getSeatById(seatId);

        if (seat == null) {
            logger.info("Seat not found.");
            return;
        }

        logger.info("Seat ID     : " + seat.getSeatId());
        logger.info("Theatre ID  : " + seat.getTheatreId());
        logger.info("Seat Number : " + seat.getSeatNumber());
        logger.info("Seat Type   : " + seat.getSeatType());
        logger.info("Price       : ₹" + seat.getPrice());
    }

    private void updateSeat() throws SQLException {

        logger.info("");
        logger.info("========== UPDATE SEAT ==========");

        System.out.print("Enter seat ID: ");

        int seatId = scanner.nextInt();
        scanner.nextLine();

        Seat seat = seatService.getSeatById(seatId);

        if (seat == null) {
            logger.info("Seat not found.");
            return;
        }

        logger.info("Current theatre ID: " + seat.getTheatreId());
        System.out.print("Enter new theatre ID: ");
        seat.setTheatreId(scanner.nextInt());
        scanner.nextLine();

        logger.info("Current seat number: " + seat.getSeatNumber());
        System.out.print("Enter new seat number: ");
        seat.setSeatNumber(scanner.nextLine());

        logger.info("Current seat type: " + seat.getSeatType());
        System.out.print("Enter new seat type: ");
        seat.setSeatType(scanner.nextLine());

        logger.info("Current price: " + seat.getPrice());
        System.out.print("Enter new price: ");
        seat.setPrice(scanner.nextBigDecimal());
        scanner.nextLine();

        seatService.updateSeat(seat);

        logger.info("Seat updated successfully.");
    }

    private void deleteSeat() throws SQLException {

        logger.info("");
        logger.info("========== DELETE SEAT ==========");

        System.out.print("Enter seat ID: ");

        int seatId = scanner.nextInt();
        scanner.nextLine();

        Seat seat = seatService.getSeatById(seatId);

        if (seat == null) {
            logger.info("Seat not found.");
            return;
        }

        logger.info("Seat: " + seat.getSeatNumber());

        System.out.print(
                "Are you sure you want to delete this seat? (Y/N): "
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            seatService.deleteSeat(seatId);

            logger.info("Seat deleted successfully.");

        } else {

            logger.info("Delete operation cancelled.");
        }
    }
}