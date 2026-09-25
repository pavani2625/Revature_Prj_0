package com.movieticket.service;

import com.movieticket.dao.SeatDAO;
import com.movieticket.model.Seat;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SeatServiceTest {

    @Test
    void testAddSeat() throws SQLException {

        SeatDAO mockSeatDAO = mock(SeatDAO.class);

        SeatService seatService = new SeatService(mockSeatDAO);

        Seat seat = new Seat();
        seat.setTheatreId(1);
        seat.setSeatNumber("A5");
        seat.setSeatType("REGULAR");
        seat.setPrice(new BigDecimal("150.00"));

        seatService.addSeat(seat);

        verify(mockSeatDAO).addSeat(seat);
    }

    @Test
    void testGetAllSeats() throws SQLException {

        SeatDAO mockSeatDAO = mock(SeatDAO.class);

        SeatService seatService = new SeatService(mockSeatDAO);

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

        List<Seat> expectedSeats = Arrays.asList(seat1, seat2);

        when(mockSeatDAO.getAllSeats()).thenReturn(expectedSeats);

        List<Seat> actualSeats = seatService.getAllSeats();

        assertNotNull(actualSeats);
        assertEquals(2, actualSeats.size());
        assertEquals("A1", actualSeats.get(0).getSeatNumber());
        assertEquals("A2", actualSeats.get(1).getSeatNumber());

        verify(mockSeatDAO).getAllSeats();
    }

    @Test
    void testGetSeatById() throws SQLException {

        SeatDAO mockSeatDAO = mock(SeatDAO.class);

        SeatService seatService = new SeatService(mockSeatDAO);

        Seat expectedSeat = new Seat();
        expectedSeat.setSeatId(1);
        expectedSeat.setTheatreId(1);
        expectedSeat.setSeatNumber("A1");
        expectedSeat.setSeatType("REGULAR");
        expectedSeat.setPrice(new BigDecimal("150.00"));

        when(mockSeatDAO.getSeatById(1)).thenReturn(expectedSeat);

        Seat actualSeat = seatService.getSeatById(1);

        assertNotNull(actualSeat);
        assertEquals(1, actualSeat.getSeatId());
        assertEquals(1, actualSeat.getTheatreId());
        assertEquals("A1", actualSeat.getSeatNumber());
        assertEquals("REGULAR", actualSeat.getSeatType());
        assertEquals(new BigDecimal("150.00"), actualSeat.getPrice());

        verify(mockSeatDAO).getSeatById(1);
    }

    @Test
    void testUpdateSeat() throws SQLException {

        SeatDAO mockSeatDAO = mock(SeatDAO.class);

        SeatService seatService = new SeatService(mockSeatDAO);

        Seat seat = new Seat();
        seat.setSeatId(1);
        seat.setTheatreId(1);
        seat.setSeatNumber("A1");
        seat.setSeatType("PREMIUM");
        seat.setPrice(new BigDecimal("200.00"));

        seatService.updateSeat(seat);

        verify(mockSeatDAO).updateSeat(seat);
    }

    @Test
    void testDeleteSeat() throws SQLException {

        SeatDAO mockSeatDAO = mock(SeatDAO.class);

        SeatService seatService = new SeatService(mockSeatDAO);

        seatService.deleteSeat(1);

        verify(mockSeatDAO).deleteSeat(1);
    }
}