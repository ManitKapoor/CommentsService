package com.test.intuit.demo.controller;

import com.test.intuit.demo.pojo.User;
import com.test.intuit.demo.service.UserAuthDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserAuthDetailsService userAuthDetailsService;

    @GetMapping("/post/{postId}")
    public String viewMainPage(@PathVariable("postId") String postId,
                               Model model) {
        model.addAttribute("postId", postId);
        return "main";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        userAuthDetailsService.registerUser(user);
        return "register_success";
    }

}
