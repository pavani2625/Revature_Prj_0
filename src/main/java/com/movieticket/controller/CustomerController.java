package com.movieticket.controller;

import com.movieticket.model.Booking;
import com.movieticket.model.Movie;
import com.movieticket.model.Payment;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import com.movieticket.service.BookedSeatService;
import com.movieticket.service.BookingService;
import com.movieticket.service.MovieService;
import com.movieticket.service.PaymentService;
import com.movieticket.service.SeatService;
import com.movieticket.service.ShowService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;
import java.util.logging.Logger;

public class CustomerController {

    private static final Logger logger =
            Logger.getLogger(CustomerController.class.getName());

    private final MovieService movieService;
    private final ShowService showService;
    private final SeatService seatService;
    private final BookedSeatService bookedSeatService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    private final Scanner scanner;

    public CustomerController() {

        movieService = new MovieService();
        showService = new ShowService();
        seatService = new SeatService();
        bookedSeatService = new BookedSeatService();
        bookingService = new BookingService();
        paymentService = new PaymentService();

        scanner = new Scanner(System.in);
    }

    public void showCustomerMenu() {

        boolean running = true;

        while (running) {

            logger.info("");
            logger.info("========================================");
            logger.info("           CUSTOMER MENU");
            logger.info("========================================");
            logger.info("1. View Movies");
            logger.info("2. View Shows");
            logger.info("3. View Available Seats");
            logger.info("4. Book Ticket");
            logger.info("5. View My Bookings");
            logger.info("6. Back");
            logger.info("========================================");

            logger.info("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        viewMovies();
                        break;

                    case 2:
                        viewShows();
                        break;

                    case 3:
                        viewAvailableSeats();
                        break;

                    case 4:
                        bookTicket();
                        break;

                    case 5:
                        viewMyBookings();
                        break;

                    case 6:
                        running = false;
                        break;

                    default:
                        logger.warning(
                                "Invalid choice. Please try again."
                        );
                }

            } catch (SQLException e) {

                logger.severe(
                        "Database error: " + e.getMessage()
                );
            }
        }
    }

    private void viewMovies() throws SQLException {

        logger.info("");
        logger.info("========== AVAILABLE MOVIES ==========");

        List<Movie> movies =
                movieService.getAllMovies();

        if (movies.isEmpty()) {
            logger.info("No movies available.");
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

    private void viewShows() throws SQLException {

        logger.info("");
        logger.info("========== AVAILABLE SHOWS ==========");

        List<Show> shows =
                showService.getAllShows();

        if (shows.isEmpty()) {
            logger.info("No shows available.");
            return;
        }

        List<Movie> movies =
                movieService.getAllMovies();

        for (Show show : shows) {

            String movieTitle = "Unknown";

            for (Movie movie : movies) {

                if (movie.getMovieId() == show.getMovieId()) {
                    movieTitle = movie.getTitle();
                    break;
                }
            }

            logger.info("----------------------------------------");
            logger.info("Show ID     : " + show.getShowId());
            logger.info("Movie       : " + movieTitle);
            logger.info("Theatre ID  : " + show.getTheatreId());
            logger.info("Date        : " + show.getShowDate());
            logger.info("Start Time  : " + show.getStartTime());
            logger.info("End Time    : " + show.getEndTime());
        }

        logger.info("----------------------------------------");
    }

    private void viewAvailableSeats() throws SQLException {

        logger.info("");
        logger.info("========== AVAILABLE SEATS ==========");

        logger.info("Enter show ID:");

        int showId = scanner.nextInt();
        scanner.nextLine();

        Show show =
                showService.getShowById(showId);

        if (show == null) {
            logger.info("Show not found.");
            return;
        }

        List<Seat> seats =
                seatService.getAllSeats();

        List<Integer> bookedSeatIds =
                bookedSeatService.getBookedSeatIdsByShowId(showId);

        Set<Integer> bookedSeatSet =
                new HashSet<>(bookedSeatIds);

        boolean found = false;

        for (Seat seat : seats) {

            if (seat.getTheatreId() != show.getTheatreId()) {
                continue;
            }

            if (bookedSeatSet.contains(seat.getSeatId())) {
                continue;
            }

            logger.info(
                    seat.getSeatNumber()
                            + " - "
                            + seat.getSeatType()
                            + " - ₹"
                            + seat.getPrice()
            );

            found = true;
        }

        if (!found) {
            logger.info("No available seats for this show.");
        }
    }

    private void bookTicket() throws SQLException {

        logger.info("");
        logger.info("========== BOOK TICKET ==========");

        // Show selection
        List<Show> shows =
                showService.getAllShows();

        if (shows.isEmpty()) {
            logger.info("No shows available.");
            return;
        }

        List<Movie> movies =
                movieService.getAllMovies();

        logger.info("");
        logger.info("Available Shows:");

        for (Show show : shows) {

            String movieTitle = "Unknown";

            for (Movie movie : movies) {

                if (movie.getMovieId() == show.getMovieId()) {
                    movieTitle = movie.getTitle();
                    break;
                }
            }

            logger.info(
                    show.getShowId()
                            + ". "
                            + movieTitle
                            + " | Theatre ID: "
                            + show.getTheatreId()
                            + " | "
                            + show.getShowDate()
                            + " | "
                            + show.getStartTime()
                            + " - "
                            + show.getEndTime()
            );
        }

        logger.info("Enter show ID:");

        int showId = scanner.nextInt();
        scanner.nextLine();

        Show selectedShow =
                showService.getShowById(showId);

        if (selectedShow == null) {

            logger.info("Show not found.");
            return;
        }

        // Display available seats
        List<Seat> allSeats =
                seatService.getAllSeats();

        List<Integer> bookedSeatIds =
                bookedSeatService.getBookedSeatIdsByShowId(showId);

        Set<Integer> bookedSeatSet =
                new HashSet<>(bookedSeatIds);

        List<Seat> availableSeats =
                new ArrayList<>();

        logger.info("");
        logger.info("========== AVAILABLE SEATS ==========");

        for (Seat seat : allSeats) {

            if (seat.getTheatreId() != selectedShow.getTheatreId()) {
                continue;
            }

            if (bookedSeatSet.contains(seat.getSeatId())) {
                continue;
            }

            availableSeats.add(seat);

            logger.info(
                    "Seat ID: "
                            + seat.getSeatId()
                            + " | "
                            + seat.getSeatNumber()
                            + " | "
                            + seat.getSeatType()
                            + " | ₹"
                            + seat.getPrice()
            );
        }

        if (availableSeats.isEmpty()) {

            logger.info("No seats available for this show.");
            return;
        }

        // Seat selection
        logger.info(
                "Enter seat IDs separated by comma (maximum 10):"
        );

        String seatInput = scanner.nextLine();

        String[] seatValues =
                seatInput.split(",");

        if (seatValues.length > 10) {

            logger.warning(
                    "You can select a maximum of 10 seats."
            );

            return;
        }

        List<Integer> selectedSeatIds =
                new ArrayList<>();

        try {

            for (String value : seatValues) {

                int seatId =
                        Integer.parseInt(value.trim());

                selectedSeatIds.add(seatId);
            }

        } catch (NumberFormatException e) {

            logger.warning(
                    "Invalid seat ID. Please enter valid numbers."
            );

            return;
        }

        // Prevent duplicate selection before creating booking
        Set<Integer> uniqueSeatIds =
                new HashSet<>(selectedSeatIds);

        if (uniqueSeatIds.size() != selectedSeatIds.size()) {

            logger.warning(
                    "The same seat cannot be selected more than once."
            );

            return;
        }

        // Calculate and display total
        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (Integer seatId : selectedSeatIds) {

            Seat selectedSeat = null;

            for (Seat seat : availableSeats) {

                if (seat.getSeatId() == seatId) {
                    selectedSeat = seat;
                    break;
                }
            }

            if (selectedSeat == null) {

                logger.warning(
                        "Seat ID "
                                + seatId
                                + " is not available for this show."
                );

                return;
            }

            totalAmount =
                    totalAmount.add(
                            selectedSeat.getPrice()
                    );
        }

        logger.info("");
        logger.info("Selected Seats: " + selectedSeatIds);
        logger.info("Total Amount  : ₹" + totalAmount);

        logger.info("Confirm booking? (Y/N):");

        String confirmation =
                scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("Y")) {

            logger.info("Booking cancelled.");
            return;
        }

        // Customer user ID
        logger.info("Enter your user ID:");

        int userId =
                scanner.nextInt();

        scanner.nextLine();

        // Payment method
        logger.info("");
        logger.info("========== PAYMENT ==========");
        logger.info("1. UPI");
        logger.info("2. CARD");

        logger.info("Select payment method:");

        int paymentChoice =
                scanner.nextInt();

        scanner.nextLine();

        String paymentMethod;

        if (paymentChoice == 1) {

            paymentMethod = "UPI";

        } else if (paymentChoice == 2) {

            paymentMethod = "CARD";

        } else {

            logger.warning(
                    "Invalid payment method."
            );

            return;
        }

        // Create Booking object
        Booking booking =
                new Booking();

        booking.setShowId(showId);
        booking.setUserId(userId);
        booking.setTotalAmount(totalAmount);
        booking.setBookingStatus("PENDING");

        // Create Payment object
        Payment payment =
                new Payment();

        payment.setPaymentMethod(paymentMethod);

        // Complete booking
        boolean bookingSuccessful =
                bookingService.completeBooking(
                        booking,
                        selectedSeatIds,
                        payment
                );

        if (!bookingSuccessful) {

            logger.warning(
                    "Booking could not be completed."
            );

            return;
        }

        logger.info("");
        logger.info("========================================");
        logger.info("         BOOKING CONFIRMED");
        logger.info("========================================");
        logger.info("Booking ID     : " + booking.getBookingId());
        logger.info("Show ID        : " + showId);
        logger.info("User ID        : " + userId);
        logger.info("Seats          : " + selectedSeatIds);
        logger.info("Total Amount   : ₹" + booking.getTotalAmount());
        logger.info("Payment Method : " + payment.getPaymentMethod());
        logger.info("Payment Status : " + payment.getPaymentStatus());
        logger.info("Booking Status : " + booking.getBookingStatus());
        logger.info("========================================");
        logger.info("Thank you for booking!");
    }

    private void viewMyBookings() throws SQLException {

        logger.info("");
        logger.info("========== MY BOOKINGS ==========");

        logger.info("Enter your user ID:");

        int userId =
                scanner.nextInt();

        scanner.nextLine();

        List<Booking> bookings =
                bookingService.getAllBookings();

        boolean found = false;

        for (Booking booking : bookings) {

            if (booking.getUserId() == userId) {

                logger.info("----------------------------------------");
                logger.info("Booking ID   : " + booking.getBookingId());
                logger.info("Show ID      : " + booking.getShowId());
                logger.info("Booking Date : " + booking.getBookingDate());
                logger.info("Total Amount : ₹" + booking.getTotalAmount());
                logger.info("Status       : " + booking.getBookingStatus());

                found = true;
            }
        }

        if (!found) {
            logger.info("No bookings found for this user.");
        }

        logger.info("----------------------------------------");
    }
}