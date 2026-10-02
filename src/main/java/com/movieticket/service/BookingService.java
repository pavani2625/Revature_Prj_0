package com.movieticket.service;

import com.movieticket.dao.BookingDAO;
import com.movieticket.exception.MovieTicketException;
import com.movieticket.model.BookedSeat;
import com.movieticket.model.Booking;
import com.movieticket.model.Payment;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingService {

    private final BookingDAO bookingDAO;
    private final BookedSeatService bookedSeatService;
    private final PaymentService paymentService;
    private final SeatService seatService;
    private final ShowService showService;

    // Constructor used by the real console application
    public BookingService() {
        bookingDAO = new BookingDAO();
        bookedSeatService = new BookedSeatService();
        paymentService = new PaymentService();
        seatService = new SeatService();
        showService = new ShowService();
    }

    // Constructor used for the existing CRUD Mockito tests
    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
        this.bookedSeatService = null;
        this.paymentService = null;
        this.seatService = null;
        this.showService = null;
    }

    // Constructor used for booking-flow unit testing
    public BookingService(
            BookingDAO bookingDAO,
            BookedSeatService bookedSeatService,
            PaymentService paymentService,
            SeatService seatService,
            ShowService showService) {

        this.bookingDAO = bookingDAO;
        this.bookedSeatService = bookedSeatService;
        this.paymentService = paymentService;
        this.seatService = seatService;
        this.showService = showService;
    }

    // CREATE
    public int addBooking(Booking booking) throws SQLException {
        return bookingDAO.addBooking(booking);
    }

    // READ ALL
    public List<Booking> getAllBookings() throws SQLException {
        return bookingDAO.getAllBookings();
    }

    // READ BY ID
    public Booking getBookingById(int bookingId) throws SQLException {
        return bookingDAO.getBookingById(bookingId);
    }

    // UPDATE
    public void updateBooking(Booking booking) throws SQLException {
        bookingDAO.updateBooking(booking);
    }

    // DELETE
    public void deleteBooking(int bookingId) throws SQLException {
        bookingDAO.deleteBooking(bookingId);
    }

    // COMPLETE CUSTOMER BOOKING FLOW
    public boolean completeBooking(
            Booking booking,
            List<Integer> seatIds,
            Payment payment) throws SQLException {

        if (seatIds == null || seatIds.isEmpty()) {
            throw new MovieTicketException(
                    "Please select at least one seat."
            );
        }

        if (payment == null) {
            throw new MovieTicketException(
                    "Payment information is required."
            );
        }

        // Prevent selecting the same seat more than once
        Set<Integer> uniqueSeatIds = new HashSet<>(seatIds);

        if (uniqueSeatIds.size() != seatIds.size()) {
            throw new MovieTicketException(
                    "Duplicate seats cannot be selected."
            );
        }

        // Find the selected show
        Show show = showService.getShowById(booking.getShowId());

        if (show == null) {
            throw new MovieTicketException(
                    "Selected show was not found."
            );
        }

        // Find seats already booked for this show
        List<Integer> bookedSeatIds =
                bookedSeatService.getBookedSeatIdsByShowId(
                        booking.getShowId()
                );

        // Calculate total amount and validate seats
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Integer seatId : seatIds) {

            if (seatId == null) {
                throw new MovieTicketException(
                        "Invalid seat selected."
                );
            }

            // Seat is already booked
            if (bookedSeatIds.contains(seatId)) {
                throw new MovieTicketException(
                        "Selected seat is already booked."
                );
            }

            Seat seat = seatService.getSeatById(seatId);

            // Seat does not exist
            if (seat == null) {
                throw new MovieTicketException(
                        "Selected seat was not found."
                );
            }

            // Seat must belong to the show's theatre
            if (seat.getTheatreId() != show.getTheatreId()) {
                throw new MovieTicketException(
                        "Selected seat does not belong to this theatre."
                );
            }

            totalAmount =
                    totalAmount.add(seat.getPrice());
        }

        // Set booking information
        booking.setTotalAmount(totalAmount);

        if (booking.getBookingDate() == null) {
            booking.setBookingDate(LocalDateTime.now());
        }

        // Booking starts as PENDING
        booking.setBookingStatus("PENDING");

        // Create booking
        int bookingId = bookingDAO.addBooking(booking);

        // IMPORTANT:
        // Store generated booking ID in the Booking object
        booking.setBookingId(bookingId);

        // Store selected seats
        for (Integer seatId : seatIds) {

            BookedSeat bookedSeat = new BookedSeat();

            bookedSeat.setSeatId(seatId);
            bookedSeat.setBookingId(bookingId);

            bookedSeatService.addBookedSeat(bookedSeat);
        }

        // Prepare payment
        payment.setBookingId(bookingId);
        payment.setAmount(totalAmount);

        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }

        // Process payment
        boolean paymentSuccessful =
                paymentService.processPayment(payment);

        if (!paymentSuccessful) {
            throw new MovieTicketException(
                    "Payment processing failed."
            );
        }

        // Payment successful → confirm booking
        booking.setBookingStatus("CONFIRMED");

        bookingDAO.updateBooking(booking);

        return true;
    }
}