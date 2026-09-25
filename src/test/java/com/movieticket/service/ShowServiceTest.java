package com.movieticket.service;

import com.movieticket.dao.ShowDAO;
import com.movieticket.model.Show;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShowServiceTest {

    @Test
    void testAddShow() throws SQLException {

        ShowDAO mockShowDAO = mock(ShowDAO.class);

        ShowService showService = new ShowService(mockShowDAO);

        Show show = new Show();
        show.setTheatreId(1);
        show.setMovieId(1);
        show.setShowDate(LocalDate.of(2026, 9, 25));
        show.setStartTime(LocalTime.of(10, 0));
        show.setEndTime(LocalTime.of(13, 0));

        showService.addShow(show);

        verify(mockShowDAO).addShow(show);
    }

    @Test
    void testGetAllShows() throws SQLException {

        ShowDAO mockShowDAO = mock(ShowDAO.class);

        ShowService showService = new ShowService(mockShowDAO);

        Show show1 = new Show();
        show1.setShowId(1);
        show1.setTheatreId(1);
        show1.setMovieId(1);
        show1.setShowDate(LocalDate.of(2026, 9, 25));
        show1.setStartTime(LocalTime.of(10, 0));
        show1.setEndTime(LocalTime.of(13, 0));

        Show show2 = new Show();
        show2.setShowId(2);
        show2.setTheatreId(1);
        show2.setMovieId(2);
        show2.setShowDate(LocalDate.of(2026, 9, 25));
        show2.setStartTime(LocalTime.of(14, 0));
        show2.setEndTime(LocalTime.of(17, 0));

        List<Show> expectedShows = Arrays.asList(show1, show2);

        when(mockShowDAO.getAllShows()).thenReturn(expectedShows);

        List<Show> actualShows = showService.getAllShows();

        assertNotNull(actualShows);
        assertEquals(2, actualShows.size());
        assertEquals(1, actualShows.get(0).getShowId());
        assertEquals(2, actualShows.get(1).getShowId());

        verify(mockShowDAO).getAllShows();
    }

    @Test
    void testGetShowById() throws SQLException {

        ShowDAO mockShowDAO = mock(ShowDAO.class);

        ShowService showService = new ShowService(mockShowDAO);

        Show expectedShow = new Show();
        expectedShow.setShowId(1);
        expectedShow.setTheatreId(1);
        expectedShow.setMovieId(1);
        expectedShow.setShowDate(LocalDate.of(2026, 9, 25));
        expectedShow.setStartTime(LocalTime.of(10, 0));
        expectedShow.setEndTime(LocalTime.of(13, 0));

        when(mockShowDAO.getShowById(1)).thenReturn(expectedShow);

        Show actualShow = showService.getShowById(1);

        assertNotNull(actualShow);
        assertEquals(1, actualShow.getShowId());
        assertEquals(1, actualShow.getTheatreId());
        assertEquals(1, actualShow.getMovieId());
        assertEquals(LocalDate.of(2026, 9, 25), actualShow.getShowDate());
        assertEquals(LocalTime.of(10, 0), actualShow.getStartTime());
        assertEquals(LocalTime.of(13, 0), actualShow.getEndTime());

        verify(mockShowDAO).getShowById(1);
    }

    @Test
    void testUpdateShow() throws SQLException {

        ShowDAO mockShowDAO = mock(ShowDAO.class);

        ShowService showService = new ShowService(mockShowDAO);

        Show show = new Show();
        show.setShowId(1);
        show.setTheatreId(1);
        show.setMovieId(1);
        show.setShowDate(LocalDate.of(2026, 9, 25));
        show.setStartTime(LocalTime.of(11, 0));
        show.setEndTime(LocalTime.of(14, 0));

        showService.updateShow(show);

        verify(mockShowDAO).updateShow(show);
    }

    @Test
    void testDeleteShow() throws SQLException {

        ShowDAO mockShowDAO = mock(ShowDAO.class);

        ShowService showService = new ShowService(mockShowDAO);

        showService.deleteShow(1);

        verify(mockShowDAO).deleteShow(1);
    }
}