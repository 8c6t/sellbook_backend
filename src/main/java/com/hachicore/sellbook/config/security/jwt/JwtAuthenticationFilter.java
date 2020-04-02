package com.hachicore.sellbook.config.security.jwt;

import com.hachicore.sellbook.config.security.account.UserAccount;
import com.hachicore.sellbook.domain.Account;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = getAuthentication(request, response);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.getToken(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            return null;
        }

        // TODO: 2020.04.02. 별도의 authentication token 객체 생성(AbstractAuthenticationToken)
        Account account = jwtUtil.getAccount(token);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                "",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        if (jwtUtil.needTokenRefresh(token)) {
            jwtUtil.refreshToken(account.getId(), response);
        }

        return authentication;
    }

}
