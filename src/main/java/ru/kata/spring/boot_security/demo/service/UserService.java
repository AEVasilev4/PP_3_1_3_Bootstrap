package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void saveUser(User user, Long[] roleIds);

    void updateUser(User user, Long[] roleIds);

    void deleteUser(Long id);

    User findByUsername(String username);



}