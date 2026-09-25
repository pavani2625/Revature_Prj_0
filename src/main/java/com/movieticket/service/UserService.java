package com.movieticket.service;

import com.movieticket.dao.UserDAO;
import com.movieticket.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // CREATE
    public void addUser(User user) throws SQLException {
        userDAO.addUser(user);
    }

    // READ ALL
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    // READ BY ID
    public User getUserById(int userId) throws SQLException {
        return userDAO.getUserById(userId);
    }

    // UPDATE
    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    // DELETE
    public void deleteUser(int userId) throws SQLException {
        userDAO.deleteUser(userId);
    }
}