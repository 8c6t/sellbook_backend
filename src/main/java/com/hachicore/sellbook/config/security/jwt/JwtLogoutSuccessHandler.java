package com.hachicore.sellbook.config.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie cookie = new Cookie(JwtUtil.HEADER_STRING, null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
