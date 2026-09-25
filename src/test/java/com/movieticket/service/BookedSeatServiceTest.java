package com.movieticket.service;

import com.movieticket.dao.BookedSeatDAO;
import com.movieticket.model.BookedSeat;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookedSeatServiceTest {

    @Test
    void testAddBookedSeat() throws SQLException {

        BookedSeatDAO mockBookedSeatDAO =
                mock(BookedSeatDAO.class);

        BookedSeatService bookedSeatService =
                new BookedSeatService(mockBookedSeatDAO);

        BookedSeat bookedSeat = new BookedSeat();

        bookedSeat.setSeatId(1);
        bookedSeat.setBookingId(1);

        bookedSeatService.addBookedSeat(bookedSeat);

        verify(mockBookedSeatDAO)
                .addBookedSeat(bookedSeat);
    }

    @Test
    void testGetAllBookedSeats() throws SQLException {

        BookedSeatDAO mockBookedSeatDAO =
                mock(BookedSeatDAO.class);

        BookedSeatService bookedSeatService =
                new BookedSeatService(mockBookedSeatDAO);

        BookedSeat bookedSeat1 = new BookedSeat();
        bookedSeat1.setBookedSeatId(1);
        bookedSeat1.setSeatId(1);
        bookedSeat1.setBookingId(1);

        BookedSeat bookedSeat2 = new BookedSeat();
        bookedSeat2.setBookedSeatId(2);
        bookedSeat2.setSeatId(2);
        bookedSeat2.setBookingId(1);

        List<BookedSeat> expectedBookedSeats =
                Arrays.asList(bookedSeat1, bookedSeat2);

        when(mockBookedSeatDAO.getAllBookedSeats())
                .thenReturn(expectedBookedSeats);

        List<BookedSeat> actualBookedSeats =
                bookedSeatService.getAllBookedSeats();

        assertNotNull(actualBookedSeats);
        assertEquals(2, actualBookedSeats.size());
        assertEquals(
                1,
                actualBookedSeats.get(0).getBookedSeatId()
        );
        assertEquals(
                2,
                actualBookedSeats.get(1).getBookedSeatId()
        );

        verify(mockBookedSeatDAO)
                .getAllBookedSeats();
    }

    @Test
    void testGetBookedSeatById() throws SQLException {

        BookedSeatDAO mockBookedSeatDAO =
                mock(BookedSeatDAO.class);

        BookedSeatService bookedSeatService =
                new BookedSeatService(mockBookedSeatDAO);

        BookedSeat expectedBookedSeat =
                new BookedSeat();

        expectedBookedSeat.setBookedSeatId(1);
        expectedBookedSeat.setSeatId(1);
        expectedBookedSeat.setBookingId(1);

        when(mockBookedSeatDAO.getBookedSeatById(1))
                .thenReturn(expectedBookedSeat);

        BookedSeat actualBookedSeat =
                bookedSeatService.getBookedSeatById(1);

        assertNotNull(actualBookedSeat);
        assertEquals(
                1,
                actualBookedSeat.getBookedSeatId()
        );
        assertEquals(
                1,
                actualBookedSeat.getSeatId()
        );
        assertEquals(
                1,
                actualBookedSeat.getBookingId()
        );

        verify(mockBookedSeatDAO)
                .getBookedSeatById(1);
    }

    @Test
    void testGetBookedSeatIdsByShowId() throws SQLException {

        BookedSeatDAO mockBookedSeatDAO =
                mock(BookedSeatDAO.class);

        BookedSeatService bookedSeatService =
                new BookedSeatService(mockBookedSeatDAO);

        List<Integer> expectedSeatIds =
                Arrays.asList(1, 2);

        when(mockBookedSeatDAO.getBookedSeatIdsByShowId(1))
                .thenReturn(expectedSeatIds);

        List<Integer> actualSeatIds =
                bookedSeatService.getBookedSeatIdsByShowId(1);

        assertNotNull(actualSeatIds);
        assertEquals(2, actualSeatIds.size());
        assertEquals(1, actualSeatIds.get(0));
        assertEquals(2, actualSeatIds.get(1));

        verify(mockBookedSeatDAO)
                .getBookedSeatIdsByShowId(1);
    }

    @Test
    void testUpdateBookedSeat() throws SQLException {

        BookedSeatDAO mockBookedSeatDAO =
                mock(BookedSeatDAO.class);

        BookedSeatService bookedSeatService =
                new BookedSeatService(mockBookedSeatDAO);

        BookedSeat bookedSeat = new BookedSeat();

        bookedSeat.setBookedSeatId(1);
        bookedSeat.setSeatId(2);
        bookedSeat.setBookingId(1);

        bookedSeatService.updateBookedSeat(bookedSeat);

        verify(mockBookedSeatDAO)
                .updateBookedSeat(bookedSeat);
    }

    @Test
    void testDeleteBookedSeat() throws SQLException {

        BookedSeatDAO mockBookedSeatDAO =
                mock(BookedSeatDAO.class);

        BookedSeatService bookedSeatService =
                new BookedSeatService(mockBookedSeatDAO);

        bookedSeatService.deleteBookedSeat(1);

        verify(mockBookedSeatDAO)
                .deleteBookedSeat(1);
    }
}