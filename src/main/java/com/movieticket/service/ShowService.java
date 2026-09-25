package com.movieticket.service;

import com.movieticket.dao.ShowDAO;
import com.movieticket.model.Show;

import java.sql.SQLException;
import java.util.List;

public class ShowService {

    private final ShowDAO showDAO;

    public ShowService() {
        showDAO = new ShowDAO();
    }
    // Constructor used for unit testing with Mockito
    public ShowService(ShowDAO showDAO) {
        this.showDAO = showDAO;
    }

    // CREATE
    public void addShow(Show show) throws SQLException {
        showDAO.addShow(show);
    }

    // READ ALL
    public List<Show> getAllShows() throws SQLException {
        return showDAO.getAllShows();
    }

    // READ BY ID
    public Show getShowById(int showId) throws SQLException {
        return showDAO.getShowById(showId);
    }

    // UPDATE
    public void updateShow(Show show) throws SQLException {
        showDAO.updateShow(show);
    }

    // DELETE
    public void deleteShow(int showId) throws SQLException {
        showDAO.deleteShow(showId);
    }
}