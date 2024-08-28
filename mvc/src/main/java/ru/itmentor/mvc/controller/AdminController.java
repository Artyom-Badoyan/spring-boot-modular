package ru.itmentor.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.common.entity.User;
import ru.itmentor.mvc.service.RoleService;
import ru.itmentor.mvc.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/users")
    public String findAll(Model model) {
        model.addAttribute("users", userService.getListUsers());
        return "user-list";
    }

    @GetMapping("/user/add")
    public String createUserPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getListRoles());
        return "user-create";
    }

    @PostMapping("/user/add")
    public String createUser(@ModelAttribute User user, @RequestParam List<Long> roleIds) {
        user.setRoles(roleService.getRolesByIds(roleIds));
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/update/{id}")
    public String updateUserPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.getListRoles());
        return "user-update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@ModelAttribute User user, @RequestParam List<Long> roleIds) {
        user.setRoles(roleService.getRolesByIds(roleIds));
        userService.updateUser(user);
        return "redirect:/admin/users";
    }
}
