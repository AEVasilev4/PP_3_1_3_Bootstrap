package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery(
                        "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles", User.class)
                .getResultList();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        try {
            return Optional.ofNullable(entityManager.createQuery(
                            "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username",
                            User.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }


    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(User user) {
        entityManager.remove(user);
    }

    public User getUserByEmail(String email) {
        try {
            return entityManager.createQuery(
                            "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email",
                            User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }
}