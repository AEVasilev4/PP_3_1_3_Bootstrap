package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;


public interface UserDao {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);;

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(User user);
    User getUserByEmail(String email);
}