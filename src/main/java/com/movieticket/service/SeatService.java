package com.movieticket.service;

import com.movieticket.dao.SeatDAO;
import com.movieticket.model.Seat;

import java.sql.SQLException;
import java.util.List;

public class SeatService {

    private final SeatDAO seatDAO;

    public SeatService() {
        seatDAO = new SeatDAO();
    }
    // Constructor used for unit testing with Mockito
    public SeatService(SeatDAO seatDAO) {
        this.seatDAO = seatDAO;
    }

    // CREATE
    public void addSeat(Seat seat) throws SQLException {
        seatDAO.addSeat(seat);
    }

    // READ ALL
    public List<Seat> getAllSeats() throws SQLException {
        return seatDAO.getAllSeats();
    }

    // READ BY ID
    public Seat getSeatById(int seatId) throws SQLException {
        return seatDAO.getSeatById(seatId);
    }

    // UPDATE
    public void updateSeat(Seat seat) throws SQLException {
        seatDAO.updateSeat(seat);
    }

    // DELETE
    public void deleteSeat(int seatId) throws SQLException {
        seatDAO.deleteSeat(seatId);
    }
}