package ru.itmentor.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmentor.common.entity.User;
import ru.itmentor.mvc.service.UserService;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @SneakyThrows
    @GetMapping("login")
    public String loginPage(@AuthenticationPrincipal UserDetails userDetails,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        if (userDetails != null) {
            Set<String> roles = AuthorityUtils.authorityListToSet(userDetails.getAuthorities());

            if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/admin");
            } else if (roles.contains("ROLE_USER")) {
                response.sendRedirect("/profile");
            }
        }
        return "login";
    }

    @GetMapping("accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("profile")
    public String userProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("user", userService.findUserByName(userDetails.getUsername()));
        model.addAttribute("message", "Welcome to your profile!");
        return "user";
    }
}