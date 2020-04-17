package com.hachicore.sellbook.config.security;

import com.hachicore.sellbook.config.security.jwt.JwtAuthenticationFilter;
import com.hachicore.sellbook.config.security.jwt.JwtLogoutSuccessHandler;
import com.hachicore.sellbook.config.security.jwt.JwtUtil;
import com.hachicore.sellbook.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;

import static java.util.Arrays.asList;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountRepository accountRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilter(jwtFilter())
            .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(jwtLogoutSuccessHandler())
        ;

        http.authorizeRequests()
            .mvcMatchers("/api/auth/login", "/api/v1/search/**", "/api/profile")
                .permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/auth/account")
                .permitAll()
            .anyRequest()
                .authenticated()
        ;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(asList("*"));
        configuration.setAllowedMethods(asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(asList("Authorization", "TOKEN_ID", "X-Requested-With", "Authorization", "Content-Type", "Content-Length", "Cache-Control"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(accountRepository);
    }

    private Filter jwtFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), jwtUtil());
    }

    private LogoutSuccessHandler jwtLogoutSuccessHandler() {
        return new JwtLogoutSuccessHandler();
    }

}
