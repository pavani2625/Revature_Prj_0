package com.movieticket.service;

import com.movieticket.dao.TheatreDAO;
import com.movieticket.model.Theatre;

import java.sql.SQLException;
import java.util.List;

public class TheatreService {

    private final TheatreDAO theatreDAO;

    public TheatreService() {
        theatreDAO = new TheatreDAO();
    }
    // Constructor used for unit testing with Mockito
    public TheatreService(TheatreDAO theatreDAO) {
        this.theatreDAO = theatreDAO;
    }

    // CREATE
    public void addTheatre(Theatre theatre) throws SQLException {
        theatreDAO.addTheatre(theatre);
    }

    // READ ALL
    public List<Theatre> getAllTheatres() throws SQLException {
        return theatreDAO.getAllTheatres();
    }

    // READ BY ID
    public Theatre getTheatreById(int theatreId) throws SQLException {
        return theatreDAO.getTheatreById(theatreId);
    }

    // UPDATE
    public void updateTheatre(Theatre theatre) throws SQLException {
        theatreDAO.updateTheatre(theatre);
    }

    // DELETE
    public void deleteTheatre(int theatreId) throws SQLException {
        theatreDAO.deleteTheatre(theatreId);
    }
}