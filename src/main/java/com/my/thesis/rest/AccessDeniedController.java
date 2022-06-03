package com.my.thesis.rest;

import com.my.thesis.dto.AuthenticationRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// for error 403
@Controller
@RequestMapping("/access-denied")
public class AccessDeniedController {

    @GetMapping
    public String accessDeniedNotAuthUsers(Model model) {
        model.addAttribute("user", new AuthenticationRequestDto());
        model.addAttribute("formError", "Log in to do that action");
        return "users/login";
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String accessDeniedAuthUsers(Model model) {
        model.addAttribute("user", new AuthenticationRequestDto());
        model.addAttribute("formError", "Access is denied. Try logging in as admin");
        return "users/login";
    }
}