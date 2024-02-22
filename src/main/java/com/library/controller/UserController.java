package com.library.controller;

import com.library.domain.User;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register-user-information")
    public String insert(User user) {
        userService.insert(user);
        return "top";
    }

    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("/login-to-my-page")
    public String findByEmailAndPassword(String email, String password) {
        userService.findByEmailAndPassword(email, password);
        return "my-page";
    }

    @GetMapping("/to-registration-page")
    public String toRegisterPage() {
        return "registration";
    }

    @GetMapping("/top")
    public String toTopPage() {
        return "top";
    }

    @GetMapping("/to-login")
    public String toLoginPage() {
        return "login";
    }

}
