package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    // Добавьте в AdminController.java или создайте новый ApiController

    @GetMapping
    public String showAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user-form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(name = "roles", required = false) Long[] roleIds) {
        Set<Role> roles = roleIds != null ? userService.getRolesByIds(Arrays.asList(roleIds)) : Set.of();
        userService.saveUser(user, roles);

        return "redirect:/admin";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(name = "roles", required = false) Long[] roleIds) {
        Set<Role> roles = roleIds != null ? userService.getRolesByIds(Arrays.asList(roleIds)) : Set.of();
        userService.updateUser(user, roles);

        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }


}