package com.my.thesis.config;

import com.my.thesis.security.jwt.JwtConfigurer;
import com.my.thesis.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/admin/products/**";
    private static final String LOGIN_ENDPOINT = "/users/**";
    public static final String PRODUCTS_ENDPOINT = "/shop/products/**";
    public static final String SHOP_ENDPOINT = "/shop";
//    public static final String BASKET_ENDPOINT = "/shop/basket";
//    public static final String ORDER_ENDPOINT = "/shop/order";



    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/css2/**", "/fonts/**", "/js/**", "/scss/**", "/img/**").permitAll()
//                .antMatchers("/products/**").permitAll()
//                .antMatchers("/first/hello").permitAll()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(SHOP_ENDPOINT).permitAll()
                .antMatchers(PRODUCTS_ENDPOINT).permitAll()
//                .antMatchers(BASKET_ENDPOINT).hasRole("USER")
//                .antMatchers(ORDER_ENDPOINT).hasRole("USER")
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                ;
//                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }
}