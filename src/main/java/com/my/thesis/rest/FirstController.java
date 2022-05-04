package com.my.thesis.rest;

import com.my.thesis.dto.AuthenticationRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/first")
public class FirstController {

    @GetMapping("/hello")
    public String sayHello(@ModelAttribute("user") AuthenticationRequestDto authenticationRequestDto) {
        return "login";
    }
}
