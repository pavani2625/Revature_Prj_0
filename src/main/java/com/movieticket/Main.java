package com.movieticket;

import com.movieticket.controller.MovieController;
import com.movieticket.controller.SeatController;
import com.movieticket.controller.TheatreController;
import com.movieticket.controller.ShowController;
import com.movieticket.controller.CustomerController;
import com.movieticket.service.BookingService;
import com.movieticket.service.PaymentService;
import com.movieticket.model.Booking;
import com.movieticket.model.Payment;
import com.movieticket.util.LoggerConfig;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger =
            Logger.getLogger(Main.class.getName());

    private static final Scanner scanner =
            new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;
        LoggerConfig.configure();

        while (running) {

            logger.info("");
            logger.info("========================================");
            logger.info("       MOVIE TICKET WEBSITE");
            logger.info("========================================");
            logger.info("1. Admin");
            logger.info("2. Customer");
            logger.info("3. Exit");
            logger.info("========================================");

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    openAdminMenu();
                    break;

                case 2:
                    openCustomerMenu();
                    break;

                case 3:
                    running = false;

                    logger.info("");
                    logger.info(
                            "Thank you for using Movie Ticket Website!"
                    );
                    break;

                default:
                    logger.warning(
                            "Invalid choice. Please try again."
                    );
            }
        }

        scanner.close();
    }

    private static void openAdminMenu() {

        boolean running = true;

        MovieController movieController =
                new MovieController();

        TheatreController theatreController =
                new TheatreController();

        SeatController seatController =
                new SeatController();

        ShowController showController =
                new ShowController();

        BookingService bookingService =
                new BookingService();

        PaymentService paymentService =
                new PaymentService();

        while (running) {

            logger.info("");
            logger.info("========================================");
            logger.info("             ADMIN MENU");
            logger.info("========================================");
            logger.info("1. Manage Movies");
            logger.info("2. Manage Theatres");
            logger.info("3. Manage Seats");
            logger.info("4. Manage Shows");
            logger.info("5. View Bookings");
            logger.info("6. View Payments");
            logger.info("7. Back");
            logger.info("========================================");

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    movieController.showMovieMenu();
                    break;

                case 2:
                    theatreController.showTheatreMenu();
                    break;

                case 3:
                    seatController.showSeatMenu();
                    break;

                case 4:
                    showController.showShowMenu();
                    break;

                case 5:
                    viewBookings(bookingService);
                    break;

                case 6:
                    viewPayments(paymentService);
                    break;

                case 7:
                    running = false;
                    break;

                default:
                    logger.warning(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private static void viewBookings(
            BookingService bookingService) {

        try {

            List<Booking> bookings =
                    bookingService.getAllBookings();

            logger.info("");
            logger.info("========================================");
            logger.info("             ALL BOOKINGS");
            logger.info("========================================");

            if (bookings.isEmpty()) {

                logger.info("No bookings found.");
                return;
            }

            for (Booking booking : bookings) {

                logger.info("----------------------------------------");
                logger.info(
                        "Booking ID     : " + booking.getBookingId()
                );
                logger.info(
                        "Show ID        : " + booking.getShowId()
                );
                logger.info(
                        "User ID        : " + booking.getUserId()
                );
                logger.info(
                        "Booking Date   : " + booking.getBookingDate()
                );
                logger.info(
                        "Total Amount   : ₹" + booking.getTotalAmount()
                );
                logger.info(
                        "Booking Status : " + booking.getBookingStatus()
                );
            }

            logger.info("----------------------------------------");

        } catch (SQLException e) {

            logger.severe(
                    "Unable to retrieve bookings: "
                            + e.getMessage()
            );
        }
    }

    private static void viewPayments(
            PaymentService paymentService) {

        try {

            List<Payment> payments =
                    paymentService.getAllPayments();

            logger.info("");
            logger.info("========================================");
            logger.info("             ALL PAYMENTS");
            logger.info("========================================");

            if (payments.isEmpty()) {

                logger.info("No payments found.");
                return;
            }

            for (Payment payment : payments) {

                logger.info("----------------------------------------");
                logger.info(
                        "Payment ID     : " + payment.getPaymentId()
                );
                logger.info(
                        "Booking ID     : " + payment.getBookingId()
                );
                logger.info(
                        "Amount         : ₹" + payment.getAmount()
                );
                logger.info(
                        "Payment Method : "
                                + payment.getPaymentMethod()
                );
                logger.info(
                        "Payment Status : "
                                + payment.getPaymentStatus()
                );
                logger.info(
                        "Payment Date   : "
                                + payment.getPaymentDate()
                );
            }

            logger.info("----------------------------------------");

        } catch (SQLException e) {

            logger.severe(
                    "Unable to retrieve payments: "
                            + e.getMessage()
            );
        }
    }

    private static void openCustomerMenu() {

        CustomerController customerController =
                new CustomerController();

        customerController.showCustomerMenu();
    }
}