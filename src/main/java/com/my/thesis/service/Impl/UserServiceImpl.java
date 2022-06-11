package com.my.thesis.service.Impl;

import com.my.thesis.model.Role;
import com.my.thesis.model.Status;
import com.my.thesis.model.User;
import com.my.thesis.repository.RoleRepository;
import com.my.thesis.repository.UserRepository;
import com.my.thesis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        User result = userRepository.save(user);
        log.info("IN save: user saved with username: {}", user.getUsername());
        return result;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        User registeredUser = userRepository.save(user);
        log.info("IN register - user {} successfully registered", registeredUser);
        return registeredUser;
    }

    @Override
    public User registerByAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        log.info("IN registerByAdmin - user {} successfully registered", registeredUser);
        return registeredUser;
    }

    @Override
    public User update(User user) {
        User result = userRepository.findById(user.getId()).orElse(null);
        if (result == null) return null;

        result.setUsername(user.getUsername());
        result.setEmail(user.getEmail());
        result.setFirstname(user.getFirstname());
        result.setLastname(user.getLastname());
        result.setPassword(passwordEncoder.encode(user.getPassword()));
        result = userRepository.save(result);
        log.info("IN register - user {} successfully registered", result.getUsername());
        return result;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getALL - {} users found", result.size());
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        if (result == null){
            log.warn("IN findByUsername - no user found with username: {}", username);
            return null;
        }
        log.info("IN findByUsername - user: {} successfully found", result.getUsername());
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);
        if (result == null){
            log.warn("IN findById - no user found {}", id);
            return null;
        }
        log.info("IN findById - user: {} successfully found by id", result.getUsername());
        return result;
    }

    @Override
    public List<User> findAllByStatus(Status status) {
        List<User> result = userRepository.findAllByStatus(status);
        if (result == null){
            log.warn("IN findAllByStatus - no user found with status {}", status.name());
            return new ArrayList<>();
        }
        log.info("IN findAllByStatus - {} users found", result.size());
        return result;
    }

    @Override
    public List<User> findAllByRoles(Role role) {
        List<User> result = userRepository.findAllByRoles(role);
        if (result == null){
            log.warn("IN findAllByStatus - no user found with role {}", role.getName());
            return new ArrayList<>();
        }
        log.info("IN findAllByRoles - {} users found", result.size());
        return result;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user successfully deleted by id: {}", id);
    }

    @Override
    public void updateResetPasswordToken(String token, String email){
        User user = userRepository.findByEmail(email);
        if (user != null){
            log.info("IN updateResetPasswordToken: token updated by email: {}", email);
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }else
        log.warn("IN updateResetPasswordToken: user not found by email: {}", email);
    }

    @Override
    public User getResetPasswordToken(String token){
        return userRepository.findByReset_password_token(token);
    }

    @Override
    public void updatePassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
        log.info("IN updatePassword: password updated by user: {}", user.getUsername());
    }
}