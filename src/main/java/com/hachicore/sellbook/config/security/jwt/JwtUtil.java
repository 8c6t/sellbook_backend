package com.hachicore.sellbook.config.security.jwt;

import com.hachicore.sellbook.domain.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("#{${jwt.expiration} * 1000 * 60 * 60 * 24}")
    private Long expirationDay;

    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public String generateToken(Account account) {
        Date now = new Date();

        return Jwts.builder()
                .claim("id", account.getId())
                .claim("email", account.getEmail())
                .claim("nickname", account.getNickname())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationDay))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Cookie generateJwtCookie(Account account) {
        Cookie cookie = new Cookie(JwtUtil.HEADER_STRING, generateToken(account));
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getToken(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING);
        return token == null ? null : token.replaceFirst(TOKEN_PREFIX, "");
    }

    public Account getAccount(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Long id = body.get("id", Long.class);
        String email = body.get("email", String.class);
        String nickname = body.get("nickname", String.class);

        return Account.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .build();
    }

}
