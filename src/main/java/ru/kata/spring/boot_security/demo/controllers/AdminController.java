package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

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

    @GetMapping("")
    public String getAllUsers(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.findUserByUsername(username);
        model.addAttribute("usersList", userService.findAll());
        model.addAttribute("currentUser", currentUser);

        return "admin";
    }

    @PostMapping("/create")
    public String addUser(@ModelAttribute(value = "user") User user) {
        userService.save(user);

        return "redirect:/admin";
    }

    @GetMapping("/create")
    public String create(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("role", roleService.getAllUser());
        return "create";
    }

//@GetMapping("/create")
//public String create(Model model, Principal principal) {
//    String currentUserEmail = principal.getName();
//    User currentUser = userService.findUserByUsername(currentUserEmail);
//
//    model.addAttribute("currentUser", currentUser);
//    model.addAttribute("user", new User());
//    model.addAttribute("role", roleService.getAllUser());
//
//    return "create";
//}
//
//
//    @PostMapping("/create")
//    public String addUser(@ModelAttribute("userForm") User userForm) {
//        userService.save(userForm);
//
//        return "redirect:/admin";
//    }

    @GetMapping("/edit")
    public String edit(@RequestParam(value = "id", required = false) Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("role", roleService.getAllUser());

        return "edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("userForm") User userForm) {
        userService.update(userForm);

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(value = "id") Long id) {
        userService.deleteById(id);

        return "redirect:/admin";
    }

}