package com.example.ProjectAlias.Security;


import com.example.ProjectAlias.Provider.CustomeAuthenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.persistence.Entity;

@Configuration
@EnableWebSecurity
public class CustomSecurity {

    @Autowired
    private CustomeAuthenProvider customeAuthenProvider;


    @Bean
    public PasswordEncoder passwordEncoder(HttpSecurity http){
        return  new BCryptPasswordEncoder();

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customeAuthenProvider)
                .build();

    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET,"login/**").permitAll()
                .antMatchers(HttpMethod.POST,"/product").hasRole("ADMIN")
//                .anyRequest().authenticated()
                .and().build();
    }
}
