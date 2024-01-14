package com.kani.hotel.service;

import com.kani.hotel.model.User;

import java.util.List;

public interface IUserService {
    User registerUser(User user);

    List<User> getAllUsers();

    void deleteUserByEmail(String email);

    User getUserByEmail(String email);
}
