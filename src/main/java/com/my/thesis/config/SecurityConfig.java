package com.my.thesis.config;

import com.my.thesis.security.handler.CustomAccessDeniedHandler;
import com.my.thesis.security.handler.CustomAuthenticationEntryPoint;
import com.my.thesis.security.jwt.JwtConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String ADMIN_MANAGER_PRODUCTS_ENDPOINT = "/shop/admin/products/**";
    private static final String ADMIN_MANAGER_ORDERS_ENDPOINT = "/shop/admin/orders/**";
    private static final String ADMIN_MANAGER_USERS_ENDPOINT = "/shop/admin/users/**";

    private static final String ADMIN_PRODUCTS_NEW_ENDPOINT = "/shop/admin/products/new";
    private static final String ADMIN_PRODUCTS_UPDATE_ENDPOINT = "/shop/admin/products/update/**";
    private static final String ADMIN_USERS_NEW_ENDPOINT = "/shop/admin/users/new";
    private static final String ADMIN_USERS_EDIT_ENDPOINT = "/shop/admin/users/edit";

    private static final String ALL_LOGIN_REGISTER_ENDPOINT = "/shop/users/**";
    public static final String ALL_PRODUCTS_ENDPOINT = "/shop/products/**";
    public static final String ALL_SHOP_FILTERS_ENDPOINT = "/shop/filters/**";
    public static final String ALL_SHOP_ENDPOINT = "/shop";

    @Autowired
    public SecurityConfig(JwtConfigurer jwtConfigurer, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtConfigurer = jwtConfigurer;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/css2/**", "/fonts/**", "/js/**", "/scss/**", "/img/**", "/templates/**", "/access-denied/**").permitAll()
                .antMatchers(ALL_LOGIN_REGISTER_ENDPOINT).permitAll()
                .antMatchers(ALL_PRODUCTS_ENDPOINT).permitAll()
                .antMatchers(ALL_SHOP_ENDPOINT).permitAll()
                .antMatchers(ALL_SHOP_FILTERS_ENDPOINT).permitAll()
                .antMatchers(ADMIN_PRODUCTS_NEW_ENDPOINT).hasRole("ADMIN")
                .antMatchers(ADMIN_PRODUCTS_UPDATE_ENDPOINT).hasRole("ADMIN")
                .antMatchers(ADMIN_USERS_NEW_ENDPOINT).hasRole("ADMIN")
                .antMatchers(ADMIN_USERS_EDIT_ENDPOINT).hasRole("ADMIN")
                .antMatchers(ADMIN_MANAGER_PRODUCTS_ENDPOINT).hasAnyRole("ADMIN","MANAGER")
                .antMatchers(ADMIN_MANAGER_ORDERS_ENDPOINT).hasAnyRole("ADMIN","MANAGER")
                .antMatchers(ADMIN_MANAGER_USERS_ENDPOINT).hasAnyRole("ADMIN","MANAGER")
                .anyRequest().authenticated()
                .and()
                // first for auth users, second for not auth users
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .apply(jwtConfigurer);
    }
}