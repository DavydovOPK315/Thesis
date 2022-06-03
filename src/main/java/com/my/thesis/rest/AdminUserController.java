package com.my.thesis.rest;

import com.my.thesis.model.Role;
import com.my.thesis.model.Status;
import com.my.thesis.model.User;
import com.my.thesis.repository.RoleRepository;
import com.my.thesis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/shop/admin/users")
@Slf4j
public class AdminUserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminUserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public String index(Model model) {
        List<User> userList = userService.getAll();
        model.addAttribute("userList", userList);
        return "admin/user/index";
    }

    @GetMapping("/{id}")
    public String edit(@PathVariable("id") Long userId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("formError", session.getAttribute("formError"));
        session.setAttribute("formError", null);
        User user = userService.findById(userId);
        insertInModelStatusRoleUser(model, user);
        return "admin/user/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        try {
            if (bindingResult.hasErrors()) {
                log.info("IN edit - problem with user data");
                return "redirect:/shop/admin/users/" + user.getId();
            }
            User result = userService.findById(user.getId());
            result.setUsername(user.getUsername());
            result.setFirstname(user.getFirstname());
            result.setLastname(user.getLastname());
            result.setEmail(user.getEmail());
            result.setRoles(user.getRoles());
            result.setStatus(user.getStatus());
            userService.save(result);
            return "redirect:/shop/admin/users/" + user.getId();
        } catch (DataIntegrityViolationException e) {
            HttpSession session = request.getSession();
            session.setAttribute("formError", "A user with this login or email already exists in the system");
            return "redirect:/shop/admin/users/" + user.getId();
        }
    }

    @GetMapping(value = "/new")
    public String newUser(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("formError", session.getAttribute("formError"));
        session.setAttribute("formError", null);
        User user = new User();
        insertInModelStatusRoleUser(model, user);
        return "admin/user/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        try {
            if (bindingResult.hasErrors()) {
                log.info("IN save - problem with user data");
                return "redirect:/shop/admin/users/" + user.getId();
            }
            userService.registerByAdmin(user);
            return "redirect:/shop/admin/users";
        } catch (DataIntegrityViolationException e) {
            HttpSession session = request.getSession();
            session.setAttribute("formError", "A user with this login or email already exists in the system");
            return "redirect:/shop/admin/users/new";
        }
    }

    private void insertInModelStatusRoleUser(Model model, User user) {
        List<Status> statusList = Arrays.asList(Status.ACTIVE, Status.BANNED);
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("statusList", statusList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("user", user);
    }

    @GetMapping("/filter/username")
    public String showProductsByUsername(Model model, HttpServletRequest request) {
        String username = request.getParameter("key");
        List<User> userList = new ArrayList<>();
        if (username.isEmpty()) {
            model.addAttribute("userList", userList);
            return "admin/user/index";
        }
        User user = userService.findByUsername(username);
        if (user != null)
            userList.add(user);
        model.addAttribute("userList", userList);
        return "admin/user/index";
    }

    @GetMapping(value = "/filter/status")
    public String showUsersByStatus(Model model, HttpServletRequest request) {
        String status = request.getParameter("key").substring(1, request.getParameter("key").length() - 1);
        List<User> userList = userService.findAllByStatus(Status.valueOf(status));
        model.addAttribute("userList", userList);
        return "admin/user/index";
    }

    @GetMapping(value = "/filter/role")
    public String showUsersByRole(Model model, HttpServletRequest request) {
        String status = request.getParameter("key").substring(1, request.getParameter("key").length() - 1);
        List<User> userList = userService.findAllByRoles(roleRepository.findByName(status));
        model.addAttribute("userList", userList);
        return "admin/user/index";
    }
}