package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleDao {
    List<Role> getAllRoles();


    Optional<Role> getRoleById(Long id);


    Set<Role> getRolesByIds(Collection<Long> ids);

    void saveRole(Role role);
}