package com.my.thesis.security.jwt;

import com.my.thesis.model.Status;
import com.my.thesis.model.User;
import com.my.thesis.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session;
        User user;
        Long userId;
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                session = request.getSession();
                userId = (Long) session.getAttribute("userId");
                user = userService.findById(userId);
                if (user.getStatus().name().equals(Status.BANNED.name())) {
                    session.setAttribute("formError", "Your account has been blocked. If you want to unblock, you can contact by email: gameshopcontactmail@gmail.com");
                    RequestDispatcher rd = request.getRequestDispatcher("/shop/account/logout");
                    rd.forward(request, response);
                } else
                    filterChain.doFilter(servletRequest, servletResponse);
            } else
                filterChain.doFilter(servletRequest, servletResponse);
        } catch (ExpiredJwtException e) {
            session = request.getSession();
            session.setAttribute("formError", "Your session has expired. Please log in");
            RequestDispatcher rd = request.getRequestDispatcher("/shop/account/logout");
            rd.forward(request, response);
        }
    }
}