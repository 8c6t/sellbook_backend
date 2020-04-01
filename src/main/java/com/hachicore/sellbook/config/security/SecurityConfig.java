package com.hachicore.sellbook.config.security;

import com.hachicore.sellbook.config.security.jwt.JwtAuthenticationFilter;
import com.hachicore.sellbook.config.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.Filter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().disable()
            .csrf().disable()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilter(jwtFilter())
        ;

        http.authorizeRequests()
            .mvcMatchers("/api/auth/login", "/api/v1/search/**")
                .permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/auth/account")
                .permitAll()
            .anyRequest()
                .authenticated()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    private Filter jwtFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), jwtUtil());
    }

}
