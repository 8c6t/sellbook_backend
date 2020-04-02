package com.hachicore.sellbook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AccountExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleUserNotFound() {
        return ResponseEntity.badRequest()
                .body("이메일, 닉네임 혹은 비밀번호가 일치하지 않습니다");
    }

}
