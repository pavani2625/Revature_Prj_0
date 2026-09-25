package com.movieticket.service;

import com.movieticket.dao.BookedSeatDAO;
import com.movieticket.model.BookedSeat;

import java.sql.SQLException;
import java.util.List;

public class BookedSeatService {

    private final BookedSeatDAO bookedSeatDAO;

    public BookedSeatService() {
        bookedSeatDAO = new BookedSeatDAO();
    }

    // Constructor used for unit testing with Mockito
    public BookedSeatService(BookedSeatDAO bookedSeatDAO) {
        this.bookedSeatDAO = bookedSeatDAO;
    }

    // CREATE
    public void addBookedSeat(BookedSeat bookedSeat)
            throws SQLException {

        bookedSeatDAO.addBookedSeat(bookedSeat);
    }

    // READ ALL
    public List<BookedSeat> getAllBookedSeats()
            throws SQLException {

        return bookedSeatDAO.getAllBookedSeats();
    }

    // READ BY ID
    public BookedSeat getBookedSeatById(int bookedSeatId)
            throws SQLException {

        return bookedSeatDAO.getBookedSeatById(bookedSeatId);
    }

    // READ BOOKED SEATS FOR A PARTICULAR SHOW
    public List<Integer> getBookedSeatIdsByShowId(int showId)
            throws SQLException {

        return bookedSeatDAO.getBookedSeatIdsByShowId(showId);
    }

    // UPDATE
    public void updateBookedSeat(BookedSeat bookedSeat)
            throws SQLException {

        bookedSeatDAO.updateBookedSeat(bookedSeat);
    }

    // DELETE
    public void deleteBookedSeat(int bookedSeatId)
            throws SQLException {

        bookedSeatDAO.deleteBookedSeat(bookedSeatId);
    }
}