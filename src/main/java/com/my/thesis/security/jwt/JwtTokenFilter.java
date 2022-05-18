package com.my.thesis.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

//        System.out.println("Filter request header ==> " + ((HttpServletRequest) servletRequest).getSession().getAttribute("token"));
//        System.out.println("Filter response header ==> " + ((HttpServletResponse) servletResponse).getHeader("Authorization"));

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException | ServletException | AuthenticationException e) {

//            /filterChain.doFilter(servletRequest.getRequestDispatcher("users/login").forward(););

            System.out.println(((HttpServletRequest) servletRequest).getRequestURI());
            System.out.println();
            e.printStackTrace();
        }


//        HttpServletRequest request = (HttpServletRequest) req;
//        String requestURI = request.getRequestURI();
//
//        request
//
//        if (requestURI.startsWith("/Check_License/Dir_My_App/")) {
//            String toReplace = requestURI.substring(requestURI.indexOf("/Dir_My_App"), requestURI.lastIndexOf("/") + 1);
//            String newURI = requestURI.replace(toReplace, "?Contact_Id=");
//            req.getRequestDispatcher(newURI).forward(req, res);
//        } else {
//            chain.doFilter(req, res);
//        }

    }
}
