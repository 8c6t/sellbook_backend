package com.hachicore.sellbook.controller.form;

import com.hachicore.sellbook.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpRequestValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpRequest.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpRequest signUpRequest = (SignUpRequest) object;
        if (accountRepository.existsByEmail(signUpRequest.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[] { signUpRequest.getEmail() }, "이미 사용중인 이메일입니다");
        }

        if (accountRepository.existsByNickname(signUpRequest.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[] { signUpRequest.getNickname() }, "이미 사용중인 닉네임입니다");
        }
    }

}
