package com.my.thesis.rest;

import com.my.thesis.dto.AuthenticationRequestDto;
import com.my.thesis.model.Status;
import com.my.thesis.model.User;
import com.my.thesis.security.jwt.JwtTokenProvider;
import com.my.thesis.service.UserService;
import com.my.thesis.utility.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/shop/users")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final JavaMailSender mailSender;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, JavaMailSender mailSender) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @GetMapping(value = "/login")
    public String index(@ModelAttribute("user") AuthenticationRequestDto user, HttpServletRequest request, Model model) {
       model.addAttribute("formError", request.getSession().getAttribute("formError"));
       request.getSession().setAttribute("formError", null);
        return "users/login";
    }

    @PostMapping(value = "/auth")
    public String login(@ModelAttribute("formError") String formError, @ModelAttribute("user") AuthenticationRequestDto requestDto, Model model, HttpServletRequest request) {
        String username = "";
        try {
            username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            String token = jwtTokenProvider.createToken(username, user.getRoles());
            model.addAttribute("token", token);
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userToken", user);
            session.setAttribute("token", token);
            return "redirect:/shop";
        } catch (AuthenticationException e) {
            if (StringUtils.isEmpty(username)) {
                model.addAttribute("formError", "Your username cannot be empty. Please try again.");
                return "users/login";
            }
            if (userService.findByUsername(username).getStatus().compareTo(Status.BANNED) == 0) {
                model.addAttribute("formError", "Your account has been blocked. If you want to unblock, you can contact by email: gameshopcontactmail@gmail.com");
            }else
                model.addAttribute("formError", "Your username and password didn't match. Please try again.");
            return "users/login";
        }
    }

    @GetMapping("/register")
    public String newRegister(@ModelAttribute("user") User user, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("formError", session.getAttribute("formError"));
        session.setAttribute("formError", null);
        return "users/register";
    }

    @PostMapping()
    public String register(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model, HttpServletRequest request) {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("formError", "Problem with data");
                return "users/register";
            }
            userService.register(user);
            return "redirect:/users/login";
        }catch (DataIntegrityViolationException e){
            HttpSession session = request.getSession();
            session.setAttribute("formError", "A user with this login or email already exists in the system");
            return "redirect:/shop/users/register";
        }
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("message", "Will be sending a reset password link to your email");
        return "users/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/shop/users/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "Have sent a reset password link to your email. Please check");
        } catch (Exception e) {
            model.addAttribute("error", "Error while sending email");
        }
        return "users/forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("contact@gameshop.com", "GameShop Support");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>" + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + link + "\">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, " + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getResetPasswordToken(token);
        model.addAttribute("token", token);
        if (user == null) {
            model.addAttribute("user", new AuthenticationRequestDto());
            model.addAttribute("formError", "Invalid token");
            return "users/login";
        }
        model.addAttribute("validLink", true);
        return "users/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(@ModelAttribute("user") AuthenticationRequestDto user, HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            model.addAttribute("validLink", false);
            return "users/reset_password_form";
        }
        String password = request.getParameter("password");
        User result = userService.getResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (result == null) {
            model.addAttribute("formError", "Invalid Token");
            return "redirect:/shop/users/login";
        } else {
            userService.updatePassword(result, password);
            model.addAttribute("formError", "You have successfully changed your password");
        }
        return "users/login";
    }
}