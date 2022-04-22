package com.bootcamp.project.ecommerceapplication.config;

import com.bootcamp.project.ecommerceapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/", "/users/**")
//                .hasAnyRole("ADMIN", "CUSTOMER", "SELLER")
//                .mvcMatchers("/users/register","/customer/register", "/seller/register")
//                .permitAll()
//                .mvcMatchers("/customer/**")
//                .hasAnyRole("CUSTOMER")
//                .mvcMatchers("/seller/**")
//                .hasAnyRole("seller")
//                .mvcMatchers("/admin/**")
//                .hasAnyRole("ADMIN")
//                .anyRequest().permitAll()
//                .and().userDetailsService(userService)
//                .csrf().disable();

        http.authorizeRequests()
        .anyRequest().permitAll()
        .and().userDetailsService(userService)
        .csrf().disable();

    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
//http.authorizeRequests()
//        .anyRequest().permitAll()
//        .and().userDetailsService(userService)
//        .csrf().disable();