package com.movieticket.controller;

import com.movieticket.exception.MovieTicketException;
import com.movieticket.model.BookedSeat;
import com.movieticket.model.Booking;
import com.movieticket.model.Movie;
import com.movieticket.model.Payment;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import com.movieticket.model.Theatre;
import com.movieticket.model.User;
import com.movieticket.service.BookedSeatService;
import com.movieticket.service.BookingService;
import com.movieticket.service.MovieService;
import com.movieticket.service.SeatService;
import com.movieticket.service.ShowService;
import com.movieticket.service.TheatreService;
import com.movieticket.service.UserService;

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

    private final MovieService movieService = new MovieService();
    private final ShowService showService = new ShowService();
    private final SeatService seatService = new SeatService();
    private final TheatreService theatreService = new TheatreService();
    private final BookedSeatService bookedSeatService = new BookedSeatService();
    private final BookingService bookingService = new BookingService();
    private final UserService userService = new UserService();
    private final Scanner scanner = new Scanner(System.in);

    public void showCustomerMenu() {
        boolean running = true;

        while (running) {
            logger.info("\n========================================");
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
                    case 1 -> viewMovies();
                    case 2 -> viewShows();
                    case 3 -> viewAvailableSeats();
                    case 4 -> bookTicket();
                    case 5 -> viewMyBookings();
                    case 6 -> running = false;
                    default -> logger.warning(
                            "Invalid choice. Please try again.");
                }
            } catch (SQLException e) {
                logger.severe("Database error: " + e.getMessage());
            } catch (MovieTicketException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    private void viewMovies() throws SQLException {
        logger.info("\n========== AVAILABLE MOVIES ==========");

        List<Movie> movies = movieService.getAllMovies();

        if (movies.isEmpty()) {
            logger.info("No movies available.");
            return;
        }

        for (Movie movie : movies) {
            logger.info("----------------------------------------");
            logger.info("Movie      : " + movie.getTitle());
            logger.info("Language   : " + movie.getLanguage());
            logger.info("Genre      : " + movie.getGenre());
            logger.info("Duration   : " + movie.getDuration() + " minutes");
            logger.info("Release    : " + movie.getReleaseDate());
        }

        logger.info("----------------------------------------");
    }

    private void viewShows() throws SQLException {
        logger.info("\n========== AVAILABLE SHOWS ==========");

        List<Show> shows = showService.getAllShows();

        if (shows.isEmpty()) {
            logger.info("No shows available.");
            return;
        }

        List<Movie> movies = movieService.getAllMovies();

        for (Theatre theatre : theatreService.getAllTheatres()) {
            List<Show> theatreShows = new ArrayList<>();

            for (Show show : shows) {
                if (show.getTheatreId() == theatre.getTheatreId()) {
                    theatreShows.add(show);
                }
            }

            if (theatreShows.isEmpty()) {
                continue;
            }

            logger.info("\nTHEATRE: " + theatre.getName());
            logger.info("========================================");

            for (Show show : theatreShows) {
                Movie movie = findMovie(movies, show.getMovieId());

                logger.info("Movie       : "
                        + (movie == null ? "Unknown" : movie.getTitle()));
                logger.info("Date        : " + show.getShowDate());
                logger.info("Start Time  : " + show.getStartTime());
                logger.info("End Time    : " + show.getEndTime());
                logger.info("----------------------------------------");
            }
        }
    }

    private void viewAvailableSeats() throws SQLException {
        logger.info("\n========== AVAILABLE SEATS ==========");

        Theatre theatre = selectTheatre();

        if (theatre == null) {
            return;
        }

        List<Show> shows = new ArrayList<>();

        for (Show show : showService.getAllShows()) {
            if (show.getTheatreId() == theatre.getTheatreId()) {
                shows.add(show);
            }
        }

        if (shows.isEmpty()) {
            logger.info("No shows available at " + theatre.getName());
            return;
        }

        List<Movie> movies = movieService.getAllMovies();

        logger.info("Available Shows at " + theatre.getName() + ":");

        for (int i = 0; i < shows.size(); i++) {
            Show show = shows.get(i);
            Movie movie = findMovie(movies, show.getMovieId());

            logger.info((i + 1) + ". "
                    + (movie == null ? "Unknown" : movie.getTitle())
                    + " | " + show.getShowDate()
                    + " | " + show.getStartTime()
                    + " - " + show.getEndTime());
        }

        Show selectedShow = chooseShow(shows);

        if (selectedShow == null) {
            return;
        }

        Set<Integer> booked = new HashSet<>(
                bookedSeatService.getBookedSeatIdsByShowId(
                        selectedShow.getShowId()));

        boolean found = false;

        logger.info("\nAvailable Seats for " + theatre.getName());

        for (Seat seat : seatService.getAllSeats()) {
            if (seat.getTheatreId() != selectedShow.getTheatreId()
                    || booked.contains(seat.getSeatId())) {
                continue;
            }

            logger.info(seat.getSeatNumber()
                    + " - " + seat.getSeatType()
                    + " - ₹" + seat.getPrice());

            found = true;
        }

        if (!found) {
            logger.info("No available seats for this show.");
        }
    }

    private void bookTicket() throws SQLException {
        logger.info("\n========== BOOK TICKET ==========");

        List<Movie> movies = movieService.getAllMovies();

        if (movies.isEmpty()) {
            logger.info("No movies available.");
            return;
        }

        logger.info("Enter movie name:");
        String text = scanner.nextLine().trim();

        if (text.isEmpty()) {
            logger.warning("Movie name cannot be empty.");
            return;
        }

        List<Movie> matches = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase()
                    .contains(text.toLowerCase())) {
                matches.add(movie);
            }
        }

        if (matches.isEmpty()) {
            logger.warning("No matching movies found.");
            return;
        }

        Movie movie = chooseMovie(matches);

        if (movie == null) {
            return;
        }

        List<Show> movieShows = new ArrayList<>();

        for (Show show : showService.getAllShows()) {
            if (show.getMovieId() == movie.getMovieId()) {
                movieShows.add(show);
            }
        }

        if (movieShows.isEmpty()) {
            logger.info("No theatres are showing this movie.");
            return;
        }

        List<Theatre> theatres = theatreService.getAllTheatres();
        List<Theatre> availableTheatres = new ArrayList<>();

        for (Show show : movieShows) {
            Theatre theatre =
                    findTheatre(theatres, show.getTheatreId());

            if (theatre != null
                    && findTheatre(
                    availableTheatres,
                    theatre.getTheatreId()) == null) {
                availableTheatres.add(theatre);
            }
        }

        if (availableTheatres.isEmpty()) {
            logger.info("No theatres are available for this movie.");
            return;
        }

        logger.info("\nAvailable Theatres for "
                + movie.getTitle() + ":");

        for (int i = 0; i < availableTheatres.size(); i++) {
            Theatre theatre = availableTheatres.get(i);

            logger.info((i + 1) + ". "
                    + theatre.getName()
                    + " - " + theatre.getCity());
        }

        Theatre theatre = chooseTheatre(availableTheatres);

        if (theatre == null) {
            return;
        }

        List<Show> theatreShows = new ArrayList<>();

        for (Show show : movieShows) {
            if (show.getTheatreId() == theatre.getTheatreId()) {
                theatreShows.add(show);
            }
        }

        if (theatreShows.isEmpty()) {
            logger.info("No shows available at this theatre.");
            return;
        }

        logger.info("\nAvailable Shows:");

        for (int i = 0; i < theatreShows.size(); i++) {
            Show show = theatreShows.get(i);

            logger.info((i + 1) + ". "
                    + show.getShowDate()
                    + " | " + show.getStartTime()
                    + " - " + show.getEndTime());
        }

        Show show = chooseShow(theatreShows);

        if (show == null) {
            return;
        }

        List<Seat> availableSeats = getAvailableSeats(show);

        if (availableSeats.isEmpty()) {
            logger.info("No seats available for this show.");
            return;
        }

        logger.info("\n========== AVAILABLE SEATS ==========");

        for (Seat seat : availableSeats) {
            logger.info(seat.getSeatNumber()
                    + " | " + seat.getSeatType()
                    + " | ₹" + seat.getPrice());
        }

        logger.info("Enter seat numbers separated by comma:");

        String input = scanner.nextLine();

        List<Integer> seatIds = new ArrayList<>();
        List<String> seatNumbers = new ArrayList<>();

        for (String value : input.split(",")) {
            String number = value.trim();
            Seat seat = findSeatByNumber(availableSeats, number);

            if (seat == null) {
                logger.warning(
                        "Seat " + number + " is not available.");
                return;
            }

            if (seatIds.contains(seat.getSeatId())) {
                logger.warning(
                        "The same seat cannot be selected more than once.");
                return;
            }

            seatIds.add(seat.getSeatId());
            seatNumbers.add(seat.getSeatNumber());
        }

        if (seatIds.isEmpty()) {
            logger.warning("At least one seat must be selected.");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Seat seat : availableSeats) {
            if (seatIds.contains(seat.getSeatId())) {
                total = total.add(seat.getPrice());
            }
        }

        logger.info("\nMovie         : " + movie.getTitle());
        logger.info("Theatre       : " + theatre.getName());
        logger.info("Date          : " + show.getShowDate());
        logger.info("Show Time     : "
                + show.getStartTime()
                + " - " + show.getEndTime());
        logger.info("Selected Seats: "
                + String.join(", ", seatNumbers));
        logger.info("Total Amount  : ₹" + total);

        logger.info("Confirm booking? (Y/N):");

        if (!scanner.nextLine().equalsIgnoreCase("Y")) {
            logger.info("Booking cancelled.");
            return;
        }

        logger.info("Enter your user ID:");

        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = userService.getUserById(userId);

        if (user == null) {
            logger.warning("User not found. Booking cancelled.");
            return;
        }

        String paymentMethod = choosePaymentMethod();

        if (paymentMethod == null) {
            return;
        }

        Booking booking = new Booking();
        booking.setShowId(show.getShowId());
        booking.setUserId(userId);
        booking.setTotalAmount(total);
        booking.setBookingStatus("PENDING");

        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);

        try {
            boolean successful = bookingService.completeBooking(
                    booking,
                    seatIds,
                    payment);

            if (!successful) {
                logger.warning("Booking could not be completed.");
                return;
            }

        } catch (MovieTicketException e) {
            logger.warning("Booking failed: " + e.getMessage());
            return;
        }

        printConfirmation(
                booking,
                user,
                movie,
                theatre,
                show,
                seatNumbers,
                payment);
    }

    private void viewMyBookings() throws SQLException {
        logger.info("\n========== MY BOOKINGS ==========");

        logger.info("Enter your user ID:");

        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = userService.getUserById(userId);

        if (user == null) {
            logger.warning("User not found.");
            return;
        }

        List<Booking> bookings =
                bookingService.getAllBookings();

        List<Show> shows =
                showService.getAllShows();

        List<Movie> movies =
                movieService.getAllMovies();

        List<Theatre> theatres =
                theatreService.getAllTheatres();

        List<Seat> seats =
                seatService.getAllSeats();

        List<BookedSeat> bookedSeats =
                bookedSeatService.getAllBookedSeats();

        boolean found = false;

        logger.info("Customer: " + user.getName());

        for (Booking booking : bookings) {
            if (booking.getUserId() != userId) {
                continue;
            }

            found = true;

            Show show =
                    findShow(shows, booking.getShowId());

            Movie movie = show == null
                    ? null
                    : findMovie(movies, show.getMovieId());

            Theatre theatre = show == null
                    ? null
                    : findTheatre(
                    theatres,
                    show.getTheatreId());

            List<String> seatNumbers = new ArrayList<>();

            /*
             * Get only the seats belonging to this booking.
             * booked_seats contains booking_id + seat_id.
             */
            for (BookedSeat bookedSeat : bookedSeats) {
                if (bookedSeat.getBookingId()
                        != booking.getBookingId()) {
                    continue;
                }

                Seat seat =
                        findSeat(
                                seats,
                                bookedSeat.getSeatId());

                if (seat != null) {
                    seatNumbers.add(
                            seat.getSeatNumber());
                }
            }

            logger.info("\n----------------------------------------");
            logger.info("Booking ID     : "
                    + booking.getBookingId());
            logger.info("Customer Name  : "
                    + user.getName());
            logger.info("Customer Email : "
                    + user.getEmail());
            logger.info("Movie          : "
                    + (movie == null
                    ? "Unknown"
                    : movie.getTitle()));
            logger.info("Theatre        : "
                    + (theatre == null
                    ? "Unknown"
                    : theatre.getName()));

            if (show != null) {
                logger.info("Date           : "
                        + show.getShowDate());

                logger.info("Show Time      : "
                        + show.getStartTime()
                        + " - "
                        + show.getEndTime());
            }

            logger.info("Seat Numbers   : "
                    + String.join(", ", seatNumbers));

            logger.info("Booking Date   : "
                    + booking.getBookingDate());

            logger.info("Total Amount   : ₹"
                    + booking.getTotalAmount());

            logger.info("Status         : "
                    + booking.getBookingStatus());
        }

        if (!found) {
            logger.info(
                    "No bookings found for this user.");
        }

        logger.info(
                "----------------------------------------");
    }

    private List<Seat> getAvailableSeats(Show show)
            throws SQLException {

        Set<Integer> booked =
                new HashSet<>(
                        bookedSeatService
                                .getBookedSeatIdsByShowId(
                                        show.getShowId()));

        List<Seat> result = new ArrayList<>();

        for (Seat seat : seatService.getAllSeats()) {
            if (seat.getTheatreId()
                    == show.getTheatreId()
                    && !booked.contains(
                    seat.getSeatId())) {

                result.add(seat);
            }
        }

        return result;
    }

    private String choosePaymentMethod() {
        logger.info("\n========== PAYMENT ==========");
        logger.info("1. UPI");
        logger.info("2. CARD");
        logger.info("3. CASH");
        logger.info("Select payment method:");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            return "UPI";
        }

        if (choice == 2) {
            return "CARD";
        }

        if (choice == 3) {
            return "CASH";
        }

        logger.warning("Invalid payment method.");
        return null;
    }

    private void printConfirmation(
            Booking booking,
            User user,
            Movie movie,
            Theatre theatre,
            Show show,
            List<String> seats,
            Payment payment) {

        logger.info("\n========================================");
        logger.info("         BOOKING CONFIRMED");
        logger.info("========================================");

        logger.info("Booking ID     : "
                + booking.getBookingId());
        logger.info("Customer Name  : "
                + user.getName());
        logger.info("Customer Email : "
                + user.getEmail());
        logger.info("Movie          : "
                + movie.getTitle());
        logger.info("Theatre        : "
                + theatre.getName());
        logger.info("Date           : "
                + show.getShowDate());
        logger.info("Show Time      : "
                + show.getStartTime()
                + " - "
                + show.getEndTime());
        logger.info("Seat Numbers   : "
                + String.join(", ", seats));
        logger.info("Total Amount   : ₹"
                + booking.getTotalAmount());
        logger.info("Payment Method : "
                + payment.getPaymentMethod());
        logger.info("Payment Status : "
                + payment.getPaymentStatus());
        logger.info("Booking Status : "
                + booking.getBookingStatus());

        logger.info("========================================");
        logger.info("Thank you for booking!");
    }

    private Movie chooseMovie(List<Movie> movies) {
        logger.info("\nMatching Movies:");

        for (int i = 0; i < movies.size(); i++) {
            logger.info((i + 1)
                    + ". "
                    + movies.get(i).getTitle());
        }

        logger.info("Select movie:");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > movies.size()) {
            logger.warning(
                    "Invalid movie selection.");
            return null;
        }

        return movies.get(choice - 1);
    }

    private Theatre selectTheatre()
            throws SQLException {

        logger.info("Enter theatre name:");

        String text =
                scanner.nextLine().trim();

        if (text.isEmpty()) {
            logger.warning(
                    "Theatre name cannot be empty.");
            return null;
        }

        List<Theatre> matches =
                new ArrayList<>();

        for (Theatre theatre :
                theatreService.getAllTheatres()) {

            if (theatre.getName() != null
                    && theatre.getName()
                    .toLowerCase()
                    .contains(text.toLowerCase())) {

                matches.add(theatre);
            }
        }

        if (matches.isEmpty()) {
            logger.info(
                    "No matching theatres found.");
            return null;
        }

        return chooseTheatre(matches);
    }

    private Theatre chooseTheatre(
            List<Theatre> theatres) {

        logger.info("\nMatching Theatres:");

        for (int i = 0; i < theatres.size(); i++) {
            logger.info((i + 1)
                    + ". "
                    + theatres.get(i).getName());
        }

        logger.info("Select theatre:");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1
                || choice > theatres.size()) {

            logger.warning(
                    "Invalid theatre selection.");
            return null;
        }

        return theatres.get(choice - 1);
    }

    private Show chooseShow(List<Show> shows) {
        logger.info("Select show:");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1
                || choice > shows.size()) {

            logger.warning(
                    "Invalid show selection.");
            return null;
        }

        return shows.get(choice - 1);
    }

    private Movie findMovie(
            List<Movie> movies,
            int id) {

        for (Movie movie : movies) {
            if (movie.getMovieId() == id) {
                return movie;
            }
        }

        return null;
    }

    private Show findShow(
            List<Show> shows,
            int id) {

        for (Show show : shows) {
            if (show.getShowId() == id) {
                return show;
            }
        }

        return null;
    }

    private Theatre findTheatre(
            List<Theatre> theatres,
            int id) {

        for (Theatre theatre : theatres) {
            if (theatre.getTheatreId() == id) {
                return theatre;
            }
        }

        return null;
    }

    private Seat findSeat(
            List<Seat> seats,
            int id) {

        for (Seat seat : seats) {
            if (seat.getSeatId() == id) {
                return seat;
            }
        }

        return null;
    }

    private Seat findSeatByNumber(
            List<Seat> seats,
            String number) {

        for (Seat seat : seats) {
            if (seat.getSeatNumber()
                    .equalsIgnoreCase(number)) {
                return seat;
            }
        }

        return null;
    }
}