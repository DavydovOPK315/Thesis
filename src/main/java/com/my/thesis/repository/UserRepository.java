package com.my.thesis.repository;

import com.my.thesis.model.Role;
import com.my.thesis.model.Status;
import com.my.thesis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);

    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query("select u from User u where u.resetPasswordToken = ?1")
    User findByReset_password_token(String token);

    @Query("select u from User u where u.status = ?1")
    List<User> findAllByStatus(Status status);

    @Query("select u from User u where ?1 member of u.roles")
    List<User> findAllByRoles(Role role);
}
