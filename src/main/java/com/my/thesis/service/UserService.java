package com.my.thesis.service;

import com.my.thesis.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    User update(User user);

    List<User> getAll();

    User findByUsername(String name);

    User findById(Long id);

    void delete(Long id);

    void updateResetPasswordToken(String token, String email);

    User getResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);
}