package com.movieticket.service;

import com.movieticket.dao.UserDAO;
import com.movieticket.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    void testAddUser() throws SQLException {

        UserDAO mockUserDAO = mock(UserDAO.class);

        UserService userService = new UserService(mockUserDAO);

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setPhone("9876543210");
        user.setPassword("test123");
        user.setRole("CUSTOMER");

        userService.addUser(user);

        verify(mockUserDAO).addUser(user);
    }


    @Test
    void testGetAllUsers() throws SQLException {

        UserDAO mockUserDAO = mock(UserDAO.class);

        UserService userService = new UserService(mockUserDAO);

        User user1 = new User();
        user1.setUserId(1);
        user1.setName("Admin User");

        User user2 = new User();
        user2.setUserId(2);
        user2.setName("Rahul Kumar");

        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(mockUserDAO.getAllUsers()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals("Admin User", actualUsers.get(0).getName());
        assertEquals("Rahul Kumar", actualUsers.get(1).getName());

        verify(mockUserDAO).getAllUsers();
    }


    @Test
    void testGetUserById() throws SQLException {

        UserDAO mockUserDAO = mock(UserDAO.class);

        UserService userService = new UserService(mockUserDAO);

        User expectedUser = new User();
        expectedUser.setUserId(2);
        expectedUser.setName("Rahul Kumar");
        expectedUser.setEmail("rahul@gmail.com");

        when(mockUserDAO.getUserById(2)).thenReturn(expectedUser);

        User actualUser = userService.getUserById(2);

        assertNotNull(actualUser);
        assertEquals(2, actualUser.getUserId());
        assertEquals("Rahul Kumar", actualUser.getName());
        assertEquals("rahul@gmail.com", actualUser.getEmail());

        verify(mockUserDAO).getUserById(2);
    }


    @Test
    void testUpdateUser() throws SQLException {

        UserDAO mockUserDAO = mock(UserDAO.class);

        UserService userService = new UserService(mockUserDAO);

        User user = new User();
        user.setUserId(2);
        user.setName("Rahul Updated");
        user.setEmail("rahul.updated@gmail.com");
        user.setPhone("9999999999");
        user.setPassword("updated123");
        user.setRole("CUSTOMER");

        userService.updateUser(user);

        verify(mockUserDAO).updateUser(user);
    }


    @Test
    void testDeleteUser() throws SQLException {

        UserDAO mockUserDAO = mock(UserDAO.class);

        UserService userService = new UserService(mockUserDAO);

        userService.deleteUser(2);

        verify(mockUserDAO).deleteUser(2);
    }
}