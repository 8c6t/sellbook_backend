package com.hachicore.sellbook.repository;

import com.hachicore.sellbook.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AccountRepositoryTest {

    @Autowired AccountRepository accountRepository;

    static String email = "nkw8620@gmail.com";
    static String nickname = "hachicore";

    @BeforeEach
    void beforeEach() {
        accountRepository.save(Account.builder()
                .email(email)
                .nickname(nickname)
                .build());
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("이메일 중복 조회 테스트")
    @Test
    void existByEmailTest() {
        boolean isTrue = accountRepository.existsByEmail(email);
        assertTrue(isTrue);

        boolean isFalse = accountRepository.existsByEmail("hachicore@gmail.com");
        assertFalse(isFalse);
    }

    @DisplayName("닉네임 중복 조회 테스트")
    @Test
    void existByNicknameTest() {
        boolean isTrue = accountRepository.existsByNickname(nickname);
        assertTrue(isTrue);

        boolean isFalse = accountRepository.existsByNickname("malang");
        assertFalse(isFalse);
    }

}