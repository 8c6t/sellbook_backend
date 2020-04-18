package com.hachicore.sellbook.controller;

import com.hachicore.sellbook.common.BaseControllerMockTest;
import com.hachicore.sellbook.config.security.jwt.JwtUtil;
import com.hachicore.sellbook.controller.form.LoginRequest;
import com.hachicore.sellbook.domain.Account;
import com.hachicore.sellbook.repository.AccountRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
public class AccountControllerLogInTest extends BaseControllerMockTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    static String email    = "test@test.com";
    static String nickname = "test";
    static String password = "12345678";

    static class LogInRequestAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(argumentsAccessor.getString(0));
            loginRequest.setPassword(argumentsAccessor.getString(1));
            return loginRequest;
        }
    }

    @BeforeEach
    void beforeEach() {
        Account account = Account.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .build();
        accountRepository.save(account);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    private static Stream<Arguments> success() {
        return Stream.of(
                Arguments.of(email, password, "이메일 로그인"),
                Arguments.of(nickname, password, "닉네임 로그인")
        );
    }

    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("success")
    @DisplayName("로그인 - 성공")
    void login_Success(@AggregateWith(LogInRequestAggregator.class)
                       LoginRequest loginRequest) throws Exception {

        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists(JwtUtil.COOKIE_NAME))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("nickname").value(nickname))
        ;
    }

    private static Stream<Arguments> fail() {
        return Stream.of(
                Arguments.of("test1@test.com", password, "회원 없음"),
                Arguments.of(email, "12345678910", "비밀번호 틀림")
        );
    }

    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("fail")
    @DisplayName("로그인 - 실패")
    void login_Fail(@AggregateWith(LogInRequestAggregator.class)
                    LoginRequest loginRequest) throws Exception {

        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist(JwtUtil.COOKIE_NAME))
        ;
    }

}