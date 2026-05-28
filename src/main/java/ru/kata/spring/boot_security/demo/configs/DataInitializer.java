package ru.kata.spring.boot_security.demo.configs;

import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;





@Component
public class DataInitializer implements CommandLineRunner {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        if (roleDao.getAllRoles().isEmpty()) {
            roleDao.saveRole(new Role("ADMIN"));
            roleDao.saveRole(new Role("USER"));
        }


        if (userDao.getUserByUsername("admin").isEmpty()) {
            List<Role> roles = roleDao.getAllRoles();
            Role adminRole = roles.stream()
                    .filter(r -> "ADMIN".equals(r.getName()))
                    .findFirst()
                    .orElse(null);

            if (adminRole != null) {
                User admin = new User(
                        "admin",
                        passwordEncoder.encode("admin"),
                        "Admin",
                        "admin@test.com",
                        30
                );
                admin.setRoles(new HashSet<>(List.of(adminRole)));
                userDao.saveUser(admin);
            }
        }


        if (userDao.getUserByUsername("user").isEmpty()) {
            List<Role> roles = roleDao.getAllRoles();
            Role userRole = roles.stream()
                    .filter(r -> "USER".equals(r.getName()))
                    .findFirst()
                    .orElse(null);

            if (userRole != null) {
                User user = new User(
                        "user",
                        passwordEncoder.encode("user"),
                        "User",
                        "user@test.com",
                        25
                );
                user.setRoles(new HashSet<>(List.of(userRole)));
                userDao.saveUser(user);
            }
        }
    }
}