package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface RoleService {
    List<Role> getAllUser();

    void save(Role role);
    void addUserToRole(User user);

    Role showUserById(Long id);
}