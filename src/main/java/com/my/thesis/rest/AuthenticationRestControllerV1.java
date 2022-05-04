package com.my.thesis.rest;

import com.my.thesis.dto.AuthenticationRequestDto;
import com.my.thesis.model.User;
import com.my.thesis.security.jwt.JwtTokenProvider;
import com.my.thesis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

//@RestController
@Controller
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }


    @RequestMapping(value = "/login")
    public String login(Model model, AuthenticationRequestDto requestDto, HttpServletRequest request){
        try {

            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null){
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

//            HttpHeaders responseHeaders = new HttpHeaders();
//            responseHeaders.set("Authorization", "Bearer_" + token);
//
//
//            HttpServletResponse httpServletRequest = (HttpServletResponse) resp;
//            httpServletRequest.setHeader("Authorization", "Bearer_" + token);

//            HttpServletResponse.setHeader(
//                    "Authorization", "Bearer_" + token);
//

//            response.getHeaders().add("Authorization", "Bearer_" + token);

            model.addAttribute("token", token);

            HttpSession session = request.getSession();
            session.setAttribute("token", token);

//            Map<Object,Object> response = new HashMap<>();
//            response.put("username", username);
//            response.put("token", token);


//            ServerHttpResponse response = (ServerHttpResponse) resp;
//            response.getHeaders().add("Authorization", "Bearer_" + token);



//            HttpServletRequest httpServletRequest = (HttpServletRequest) resp;
//            httpServletRequest.authenticate(true);
//            httpServletRequest.setHeader(
//                "Authorization", "Bearer_" + token);



            return "result";
        }catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

//    @RequestMapping(value = "/login2")
//    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto){
//        try {
//
//            String username = requestDto.getUsername();
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
//            User user = userService.findByUsername(username);
//
//            if (user == null){
//                throw new UsernameNotFoundException("User with username: " + username + " not found");
//            }
//
//            String token = jwtTokenProvider.createToken(username, user.getRoles());
//
//            Map<Object,Object> response = new HashMap<>();
//            response.put("username", username);
//            response.put("token", token);
//
//            return ResponseEntity.ok(response);
//        }catch (AuthenticationException e){
//            throw new BadCredentialsException("Invalid username or password");
//        }
//    }
}
