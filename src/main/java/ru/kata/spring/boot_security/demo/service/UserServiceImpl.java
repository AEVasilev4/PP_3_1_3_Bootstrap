package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public void saveUser(User user, Long[] roleIds) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));


        Set<Role> roles = (roleIds != null && roleIds.length > 0)
                ? roleService.getRolesByIds(Arrays.asList(roleIds))
                : new HashSet<>();

        user.setRoles(roles);
        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user, Long[] roleIds) {

        Optional<User> existingUserOpt = userDao.getUserById(user.getId());


        if (existingUserOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + user.getId());
        }

        User existingUser = existingUserOpt.get();
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAge(user.getAge());


        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Set<Role> roles = (roleIds != null && roleIds.length > 0)
                ? roleService.getRolesByIds(Arrays.asList(roleIds))
                : new HashSet<>();

        existingUser.setRoles(roles);
        userDao.updateUser(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        Optional<User> userOpt = userDao.getUserById(id);
        userOpt.ifPresent(userDao::deleteUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public User findByUsername(String username) {
        return userDao.getUserByUsername(username)
                .orElse(null);
    }
}






