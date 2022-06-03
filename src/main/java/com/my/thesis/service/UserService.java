package com.my.thesis.service;

import com.my.thesis.model.Role;
import com.my.thesis.model.Status;
import com.my.thesis.model.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User register(User user);

    User registerByAdmin(User user);

    User update(User user);

    List<User> getAll();

    User findByUsername(String name);

    User findById(Long id);

    List<User> findAllByStatus(Status status);

    List<User> findAllByRoles(Role role);

    void delete(Long id);

    void updateResetPasswordToken(String token, String email);

    User getResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);
}