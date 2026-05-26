package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {

        return entityManager.createQuery("SELECT r FROM Role r", Role.class)
                .getResultList();
    }
    @Override
    public Optional<Role> getRoleById(Long id) {
        return Optional.ofNullable(entityManager.find(Role.class, id));
    }
    @Override
    public Set<Role> getRolesByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptySet();
        }

        return entityManager.createQuery(
                        "SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", ids)
                .getResultList()
                .stream()
                .collect(Collectors.toSet());
    }


    @Override
    public void saveRole(Role role) {
        entityManager.persist(role);
    }
}