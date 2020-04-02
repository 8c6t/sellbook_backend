package com.hachicore.sellbook.service;

import com.hachicore.sellbook.controller.form.LoginRequest;
import com.hachicore.sellbook.controller.form.SignUpRequest;
import com.hachicore.sellbook.domain.Account;
import com.hachicore.sellbook.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account login(LoginRequest loginRequest) {
        String emailOrNickname = loginRequest.getUsername();

        Account account = accountRepository.findByEmail(emailOrNickname)
                .orElse(accountRepository.findByNickname(emailOrNickname)
                .orElseThrow(() -> new UsernameNotFoundException(emailOrNickname)));

        boolean validate = passwordEncoder.matches(loginRequest.getPassword(), account.getPassword());

        if (!validate) {
            // TODO: 2020.04.02. 별도의 exception 생성
            throw new RuntimeException("비밀번호 불일치");
        }

        return account;
    }

    @Transactional
    public Account saveNewAccount(SignUpRequest signUpRequest) {
        Account account = Account.builder()
                .email(signUpRequest.getEmail())
                .nickname(signUpRequest.getNickname())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        return accountRepository.save(account);
    }

}
