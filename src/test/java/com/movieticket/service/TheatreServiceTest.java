package com.movieticket.service;

import com.movieticket.dao.TheatreDAO;
import com.movieticket.model.Theatre;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TheatreServiceTest {

    @Test
    void testAddTheatre() throws SQLException {

        TheatreDAO mockTheatreDAO = mock(TheatreDAO.class);

        TheatreService theatreService = new TheatreService(mockTheatreDAO);

        Theatre theatre = new Theatre();
        theatre.setName("Test Theatre");
        theatre.setCity("Hyderabad");
        theatre.setAddress("Kondapur");
        theatre.setTotalSeats(100);

        theatreService.addTheatre(theatre);

        verify(mockTheatreDAO).addTheatre(theatre);
    }


    @Test
    void testGetAllTheatres() throws SQLException {

        TheatreDAO mockTheatreDAO = mock(TheatreDAO.class);

        TheatreService theatreService = new TheatreService(mockTheatreDAO);

        Theatre theatre1 = new Theatre();
        theatre1.setTheatreId(1);
        theatre1.setName("PVR Cinemas");

        Theatre theatre2 = new Theatre();
        theatre2.setTheatreId(2);
        theatre2.setName("INOX");

        List<Theatre> expectedTheatres =
                Arrays.asList(theatre1, theatre2);

        when(mockTheatreDAO.getAllTheatres())
                .thenReturn(expectedTheatres);

        List<Theatre> actualTheatres =
                theatreService.getAllTheatres();

        assertNotNull(actualTheatres);
        assertEquals(2, actualTheatres.size());
        assertEquals("PVR Cinemas", actualTheatres.get(0).getName());
        assertEquals("INOX", actualTheatres.get(1).getName());

        verify(mockTheatreDAO).getAllTheatres();
    }


    @Test
    void testGetTheatreById() throws SQLException {

        TheatreDAO mockTheatreDAO = mock(TheatreDAO.class);

        TheatreService theatreService =
                new TheatreService(mockTheatreDAO);

        Theatre expectedTheatre = new Theatre();
        expectedTheatre.setTheatreId(1);
        expectedTheatre.setName("PVR Cinemas");
        expectedTheatre.setCity("Hyderabad");
        expectedTheatre.setAddress("Banjara Hills");
        expectedTheatre.setTotalSeats(100);

        when(mockTheatreDAO.getTheatreById(1))
                .thenReturn(expectedTheatre);

        Theatre actualTheatre =
                theatreService.getTheatreById(1);

        assertNotNull(actualTheatre);
        assertEquals(1, actualTheatre.getTheatreId());
        assertEquals("PVR Cinemas", actualTheatre.getName());
        assertEquals("Hyderabad", actualTheatre.getCity());
        assertEquals("Banjara Hills", actualTheatre.getAddress());
        assertEquals(100, actualTheatre.getTotalSeats());

        verify(mockTheatreDAO).getTheatreById(1);
    }


    @Test
    void testUpdateTheatre() throws SQLException {

        TheatreDAO mockTheatreDAO = mock(TheatreDAO.class);

        TheatreService theatreService =
                new TheatreService(mockTheatreDAO);

        Theatre theatre = new Theatre();
        theatre.setTheatreId(1);
        theatre.setName("PVR Cinemas Updated");
        theatre.setCity("Hyderabad");
        theatre.setAddress("Banjara Hills");
        theatre.setTotalSeats(100);

        theatreService.updateTheatre(theatre);

        verify(mockTheatreDAO).updateTheatre(theatre);
    }


    @Test
    void testDeleteTheatre() throws SQLException {

        TheatreDAO mockTheatreDAO = mock(TheatreDAO.class);

        TheatreService theatreService =
                new TheatreService(mockTheatreDAO);

        theatreService.deleteTheatre(1);

        verify(mockTheatreDAO).deleteTheatre(1);
    }
}