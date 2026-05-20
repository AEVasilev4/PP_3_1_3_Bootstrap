package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
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
    public Set<Role> getRolesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }

        return entityManager.createQuery(
                        "SELECT r FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", ids)
                .getResultList()
                .stream()
                .collect(Collectors.toSet());
    }

    @Override
    public Role getRoleById(Long id) {

        return entityManager.createQuery(
                        "SELECT r FROM Role r WHERE r.id = :id", Role.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveRole(Role role) {
        entityManager.persist(role);
    }
}