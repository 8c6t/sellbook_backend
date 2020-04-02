package com.hachicore.sellbook.controller;

import com.hachicore.sellbook.config.security.jwt.JwtUtil;
import com.hachicore.sellbook.controller.form.LoginRequest;
import com.hachicore.sellbook.controller.form.SignUpRequest;
import com.hachicore.sellbook.controller.form.SignUpRequestValidator;
import com.hachicore.sellbook.domain.Account;
import com.hachicore.sellbook.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AccountController {

    private final AccountService accountService;

    private final JwtUtil jwtUtil;
    private final SignUpRequestValidator signUpRequestValidator;

    @InitBinder("signUpRequest")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpRequestValidator);
    }

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

        accountService.saveNewAccount(signUpRequest);
        return ResponseEntity.ok().build();
    }

}
