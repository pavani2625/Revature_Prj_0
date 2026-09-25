package com.movieticket.service;

import com.movieticket.dao.BookingDAO;
import com.movieticket.model.Booking;
import com.movieticket.model.Payment;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @Test
    void testAddBooking() throws SQLException {

        BookingDAO mockBookingDAO = mock(BookingDAO.class);

        BookingService bookingService =
                new BookingService(mockBookingDAO);

        Booking booking = new Booking();

        booking.setShowId(1);
        booking.setUserId(2);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalAmount(new BigDecimal("300.00"));
        booking.setBookingStatus("CONFIRMED");

        when(mockBookingDAO.addBooking(booking)).thenReturn(10);

        int bookingId =
                bookingService.addBooking(booking);

        assertEquals(10, bookingId);

        verify(mockBookingDAO).addBooking(booking);
    }

    @Test
    void testGetAllBookings() throws SQLException {

        BookingDAO mockBookingDAO = mock(BookingDAO.class);

        BookingService bookingService =
                new BookingService(mockBookingDAO);

        Booking booking1 = new Booking();

        booking1.setBookingId(1);
        booking1.setShowId(1);
        booking1.setUserId(2);
        booking1.setTotalAmount(
                new BigDecimal("300.00")
        );
        booking1.setBookingStatus("CONFIRMED");

        Booking booking2 = new Booking();

        booking2.setBookingId(2);
        booking2.setShowId(2);
        booking2.setUserId(3);
        booking2.setTotalAmount(
                new BigDecimal("400.00")
        );
        booking2.setBookingStatus("CONFIRMED");

        List<Booking> expectedBookings =
                Arrays.asList(booking1, booking2);

        when(mockBookingDAO.getAllBookings())
                .thenReturn(expectedBookings);

        List<Booking> actualBookings =
                bookingService.getAllBookings();

        assertNotNull(actualBookings);
        assertEquals(2, actualBookings.size());

        assertEquals(
                1,
                actualBookings.get(0).getBookingId()
        );

        assertEquals(
                2,
                actualBookings.get(1).getBookingId()
        );

        assertEquals(
                new BigDecimal("300.00"),
                actualBookings.get(0).getTotalAmount()
        );

        verify(mockBookingDAO).getAllBookings();
    }

    @Test
    void testGetBookingById() throws SQLException {

        BookingDAO mockBookingDAO = mock(BookingDAO.class);

        BookingService bookingService =
                new BookingService(mockBookingDAO);

        Booking expectedBooking = new Booking();

        expectedBooking.setBookingId(1);
        expectedBooking.setShowId(1);
        expectedBooking.setUserId(2);
        expectedBooking.setTotalAmount(
                new BigDecimal("300.00")
        );
        expectedBooking.setBookingStatus("CONFIRMED");

        when(mockBookingDAO.getBookingById(1))
                .thenReturn(expectedBooking);

        Booking actualBooking =
                bookingService.getBookingById(1);

        assertNotNull(actualBooking);

        assertEquals(
                1,
                actualBooking.getBookingId()
        );

        assertEquals(
                1,
                actualBooking.getShowId()
        );

        assertEquals(
                2,
                actualBooking.getUserId()
        );

        assertEquals(
                new BigDecimal("300.00"),
                actualBooking.getTotalAmount()
        );

        assertEquals(
                "CONFIRMED",
                actualBooking.getBookingStatus()
        );

        verify(mockBookingDAO)
                .getBookingById(1);
    }

    @Test
    void testUpdateBooking() throws SQLException {

        BookingDAO mockBookingDAO = mock(BookingDAO.class);

        BookingService bookingService =
                new BookingService(mockBookingDAO);

        Booking booking = new Booking();

        booking.setBookingId(1);
        booking.setShowId(1);
        booking.setUserId(2);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalAmount(
                new BigDecimal("300.00")
        );
        booking.setBookingStatus("CONFIRMED");

        bookingService.updateBooking(booking);

        verify(mockBookingDAO)
                .updateBooking(booking);
    }

    @Test
    void testDeleteBooking() throws SQLException {

        BookingDAO mockBookingDAO = mock(BookingDAO.class);

        BookingService bookingService =
                new BookingService(mockBookingDAO);

        bookingService.deleteBooking(1);

        verify(mockBookingDAO)
                .deleteBooking(1);
    }

    @Test
    void testCompleteBookingSuccessfully()
            throws SQLException {

        BookingDAO mockBookingDAO =
                mock(BookingDAO.class);

        BookedSeatService mockBookedSeatService =
                mock(BookedSeatService.class);

        PaymentService mockPaymentService =
                mock(PaymentService.class);

        SeatService mockSeatService =
                mock(SeatService.class);

        ShowService mockShowService =
                mock(ShowService.class);

        BookingService bookingService =
                new BookingService(
                        mockBookingDAO,
                        mockBookedSeatService,
                        mockPaymentService,
                        mockSeatService,
                        mockShowService
                );

        Show show = new Show();

        show.setShowId(1);
        show.setTheatreId(1);
        show.setMovieId(1);

        when(mockShowService.getShowById(1))
                .thenReturn(show);

        when(mockBookedSeatService
                .getBookedSeatIdsByShowId(1))
                .thenReturn(Collections.emptyList());

        Seat seat1 = new Seat();

        seat1.setSeatId(1);
        seat1.setTheatreId(1);
        seat1.setSeatNumber("A1");
        seat1.setSeatType("REGULAR");
        seat1.setPrice(new BigDecimal("150.00"));

        Seat seat2 = new Seat();

        seat2.setSeatId(2);
        seat2.setTheatreId(1);
        seat2.setSeatNumber("A2");
        seat2.setSeatType("REGULAR");
        seat2.setPrice(new BigDecimal("150.00"));

        when(mockSeatService.getSeatById(1))
                .thenReturn(seat1);

        when(mockSeatService.getSeatById(2))
                .thenReturn(seat2);

        when(mockBookingDAO.addBooking(any(Booking.class)))
                .thenReturn(10);

        when(mockPaymentService.processPayment(
                any(Payment.class)))
                .thenReturn(true);

        Booking booking = new Booking();

        booking.setShowId(1);
        booking.setUserId(2);

        Payment payment = new Payment();

        payment.setPaymentMethod("UPI");

        boolean result =
                bookingService.completeBooking(
                        booking,
                        Arrays.asList(1, 2),
                        payment
                );

        assertTrue(result);

        assertEquals(
                new BigDecimal("300.00"),
                booking.getTotalAmount()
        );

        assertEquals(
                "CONFIRMED",
                booking.getBookingStatus()
        );

        assertEquals(
                10,
                booking.getBookingId()
        );

        verify(mockBookedSeatService, times(2))
                .addBookedSeat(any());

        verify(mockPaymentService)
                .processPayment(payment);

        verify(mockBookingDAO)
                .updateBooking(booking);
    }

    @Test
    void testCompleteBookingFailsWhenSeatAlreadyBooked()
            throws SQLException {

        BookingDAO mockBookingDAO =
                mock(BookingDAO.class);

        BookedSeatService mockBookedSeatService =
                mock(BookedSeatService.class);

        PaymentService mockPaymentService =
                mock(PaymentService.class);

        SeatService mockSeatService =
                mock(SeatService.class);

        ShowService mockShowService =
                mock(ShowService.class);

        BookingService bookingService =
                new BookingService(
                        mockBookingDAO,
                        mockBookedSeatService,
                        mockPaymentService,
                        mockSeatService,
                        mockShowService
                );

        Show show = new Show();

        show.setShowId(1);
        show.setTheatreId(1);

        when(mockShowService.getShowById(1))
                .thenReturn(show);

        when(mockBookedSeatService
                .getBookedSeatIdsByShowId(1))
                .thenReturn(Arrays.asList(1));

        Booking booking = new Booking();

        booking.setShowId(1);
        booking.setUserId(2);

        Payment payment = new Payment();

        payment.setPaymentMethod("UPI");

        boolean result =
                bookingService.completeBooking(
                        booking,
                        Arrays.asList(1),
                        payment
                );

        assertFalse(result);

        verify(mockBookingDAO, never())
                .addBooking(any());

        verify(mockPaymentService, never())
                .processPayment(any());
    }

    @Test
    void testCompleteBookingFailsForDifferentTheatre()
            throws SQLException {

        BookingDAO mockBookingDAO =
                mock(BookingDAO.class);

        BookedSeatService mockBookedSeatService =
                mock(BookedSeatService.class);

        PaymentService mockPaymentService =
                mock(PaymentService.class);

        SeatService mockSeatService =
                mock(SeatService.class);

        ShowService mockShowService =
                mock(ShowService.class);

        BookingService bookingService =
                new BookingService(
                        mockBookingDAO,
                        mockBookedSeatService,
                        mockPaymentService,
                        mockSeatService,
                        mockShowService
                );

        Show show = new Show();

        show.setShowId(1);
        show.setTheatreId(1);

        when(mockShowService.getShowById(1))
                .thenReturn(show);

        when(mockBookedSeatService
                .getBookedSeatIdsByShowId(1))
                .thenReturn(Collections.emptyList());

        Seat seat = new Seat();

        seat.setSeatId(5);
        seat.setTheatreId(2);
        seat.setPrice(new BigDecimal("150.00"));

        when(mockSeatService.getSeatById(5))
                .thenReturn(seat);

        Booking booking = new Booking();

        booking.setShowId(1);
        booking.setUserId(2);

        Payment payment = new Payment();

        payment.setPaymentMethod("UPI");

        boolean result =
                bookingService.completeBooking(
                        booking,
                        Arrays.asList(5),
                        payment
                );

        assertFalse(result);

        verify(mockBookingDAO, never())
                .addBooking(any());

        verify(mockPaymentService, never())
                .processPayment(any());
    }
}