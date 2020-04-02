package com.hachicore.sellbook.controller;

import com.hachicore.sellbook.config.security.jwt.JwtUtil;
import com.hachicore.sellbook.controller.form.LoginRequest;
import com.hachicore.sellbook.controller.form.SignUpRequest;
import com.hachicore.sellbook.domain.Account;
import com.hachicore.sellbook.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AccountController {

    private final AccountService accountService;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Account account = accountService.login(loginRequest);

        Cookie cookie = jwtUtil.generateJwtCookie(account);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/account")
    public ResponseEntity signUp(@RequestBody @Valid SignUpRequest signUpRequest, Errors errors) {
        if (errors.hasErrors()) {
            ResponseEntity.badRequest().body("올바른 값을 입력하세요");
        }

        Account account = accountService.saveNewAccount(signUpRequest);
        return ResponseEntity.ok().body(account);
    }

}
