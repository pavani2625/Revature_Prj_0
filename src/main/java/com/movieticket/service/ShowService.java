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

    public ShowService(ShowDAO showDAO) {
        this.showDAO = showDAO;
    }

    public void addShow(Show show) throws SQLException {
        showDAO.addShow(show);
    }

    public List<Show> getAllShows() throws SQLException {
        return showDAO.getAllShows();
    }

    public Show getShowById(int showId) throws SQLException {
        return showDAO.getShowById(showId);
    }

    public void updateShow(Show show) throws SQLException {
        showDAO.updateShow(show);
    }

    public void deleteShow(int showId) throws SQLException {
        showDAO.deleteShow(showId);
    }
}