package com.hachicore.sellbook.controller;

import com.hachicore.sellbook.config.security.account.CurrentUser;
import com.hachicore.sellbook.config.security.jwt.JwtUtil;
import com.hachicore.sellbook.controller.form.LoginRequest;
import com.hachicore.sellbook.controller.form.SignUpRequest;
import com.hachicore.sellbook.controller.validator.SignUpRequestValidator;
import com.hachicore.sellbook.domain.Account;
import com.hachicore.sellbook.dto.AccountDto;
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

        Cookie cookie = jwtUtil.generateAccessTokenCookie(account);
        response.addCookie(cookie);

        return ResponseEntity.ok().body(new AccountDto(account));
    }

    @PostMapping("/account")
    public ResponseEntity signUp(@RequestBody @Valid SignUpRequest signUpRequest, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            // TODO 2020.04.15 별도 에러 객체로 json 형태 리턴할 수 있도록...
            return ResponseEntity.badRequest().body("올바른 값을 입력하세요");
        }

        Account account = accountService.saveNewAccount(signUpRequest);

        Cookie cookie = jwtUtil.generateAccessTokenCookie(account);
        response.addCookie(cookie);

        return ResponseEntity.ok().body(new AccountDto(account));
    }

    @GetMapping("/account")
    public ResponseEntity currentUserInfo(@CurrentUser Account account) {
        // 로그인 한 유저가 없다면 시큐리티 필터 선에서 걸러짐
        return ResponseEntity.ok().body(new AccountDto(account));
    }

}
