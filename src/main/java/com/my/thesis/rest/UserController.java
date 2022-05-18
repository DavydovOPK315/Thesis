package com.my.thesis.rest;

import com.my.thesis.dto.AuthenticationRequestDto;
import com.my.thesis.model.User;
import com.my.thesis.security.jwt.JwtTokenProvider;
import com.my.thesis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String index(@ModelAttribute("user") AuthenticationRequestDto user) {
        return "users/login";
    }

    @PostMapping(value = "/auth")
    public String login(@ModelAttribute("formError") String formError, @ModelAttribute("user") AuthenticationRequestDto requestDto,Model model, HttpServletRequest request){
        try {

            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);


            // not needed, if incorrect authentication worked earlier
//            if (user == null){
//                model.addAttribute("formError", "User with username: " + username + " not found");
////                throw new UsernameNotFoundException("User with username: " + username + " not found");
//                return "users/login";
//            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            model.addAttribute("token", token);

            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("token", token);

            return "redirect:/shop";
        }catch (AuthenticationException e){
            model.addAttribute("formError", "Your username and password didn't match. Please try again.");
            return "users/login";
//            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @GetMapping("/register")
    public String newRegister(@ModelAttribute("user") User user) {
        return "users/register";
    }

    @PostMapping()
    public String register(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "users/register";
        }

        userService.register(user);
        return "redirect:/users/login";
    }
}