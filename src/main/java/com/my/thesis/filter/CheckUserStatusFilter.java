//package com.my.thesis.filter;
//
//import com.my.thesis.model.Status;
//import com.my.thesis.model.User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@Component
//@Slf4j
//public class CheckUserStatusFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("userToken");
//        if (user == null) {
//            filterChain.doFilter(request, response);
//            System.out.println("doFilterInternal null");
//        } else {
//
//            if (!user.getStatus().name().equals(Status.BANNED.name())) {
//                System.out.println("doFilterInternal Active");
//                filterChain.doFilter(request, response);
//            }
//
//            System.out.println("doFilterInternal logout");
//            RequestDispatcher rd = request.getRequestDispatcher("/shop/account/logout");
//            rd.forward(request, response);
//        }
//    }
//}
