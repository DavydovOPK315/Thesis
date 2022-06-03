package com.my.thesis.config;

import com.my.thesis.security.handler.CustomAccessDeniedHandler;
import com.my.thesis.security.handler.CustomAuthenticationEntryPoint;
import com.my.thesis.security.jwt.JwtConfigurer;
import com.my.thesis.security.jwt.JwtTokenProvider;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String ADMIN_ENDPOINT = "/shop/admin/**";
    private static final String LOGIN_ENDPOINT = "/shop/users/**";
    public static final String PRODUCTS_ENDPOINT = "/shop/products/**";
    public static final String SHOP_ENDPOINT = "/shop/";
    public static final String BASKET_ENDPOINT = "/shop/basket/**";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtTokenProvider = jwtTokenProvider;
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
                .antMatchers(LOGIN_ENDPOINT).permitAll()
//                .antMatchers(SHOP_ENDPOINT).permitAll()
//                .antMatchers(PRODUCTS_ENDPOINT).permitAll()
//                .antMatchers(BASKET_ENDPOINT).hasRole("USER")
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
//                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer.authenticationEntryPoint(customAuthenticationEntryPoint).jwt())
                // first for auth users, second for not auth users
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}