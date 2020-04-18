package com.hachicore.sellbook.controller;

import com.hachicore.sellbook.common.BaseControllerMockTest;
import com.hachicore.sellbook.config.security.jwt.JwtUtil;
import com.hachicore.sellbook.controller.form.SignUpRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
class AccountControllerSignUpTest extends BaseControllerMockTest {

    @Autowired
    AccountRepository accountRepository;

    static String email    = "test@test.com";
    static String nickname = "test";
    static String password = "12345678";

    static class SignUpRequestAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(argumentsAccessor.getString(0));
            signUpRequest.setNickname(argumentsAccessor.getString(1));
            signUpRequest.setPassword(argumentsAccessor.getString(2));
            return signUpRequest;
        }
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 - 성공")
    void signUp_Correct() throws Exception {
        SignUpRequest req = new SignUpRequest();
        req.setEmail(email);
        req.setNickname(nickname);
        req.setPassword(password);

        mockMvc.perform(post("/api/auth/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists(JwtUtil.COOKIE_NAME))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(req.getEmail()))
                .andExpect(jsonPath("nickname").value(req.getNickname()))
        ;
    }

    private static Stream<Arguments> emptyInput() {
        return Stream.of(
                Arguments.of("", "", "", "모두 빈칸"),
                Arguments.of("", nickname, password, "이메일 빈칸"),
                Arguments.of(email, "", password, "닉네임 빈칸"),
                Arguments.of(email, nickname, "",  "비밀번호 빈칸"),
                Arguments.of(email, "", "", "닉네임, 비밀번호 빈칸"),
                Arguments.of("", "", password, "이메일, 닉네임 빈칸"),
                Arguments.of("", nickname, "", "이메일, 비밀번호 빈칸")
        );
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("emptyInput")
    @DisplayName("회원 가입 - 빈 항목 존재시 에러")
    void signUp_Bad_Request_Empty_Input(@AggregateWith(SignUpRequestAggregator.class)
                                        SignUpRequest signUpRequest) throws Exception {

        mockMvc.perform(post("/api/auth/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist(JwtUtil.COOKIE_NAME))
        ;
    }

    private static Stream<Arguments> validationFail() {
        return Stream.of(
                Arguments.of(nickname, nickname, password, "이메일 형식 아님"),
                Arguments.of("test#test.com", nickname, password, "이메일 형식 아님 - 2"),
                Arguments.of(email, "짧음", password, "닉네임 길이 미만"),
                Arguments.of(email, "일이삼사오륙칠팔구십일이삼사오륙칠팔구십초과", password, "닉네임 길이 초과"),
                Arguments.of(email, "(@*%()@)_", password, "닉네임 특수문자 포함"),
                Arguments.of(email, nickname, "12345", "비밀번호 길이 미만"),
                Arguments.of(email, nickname, "123456789012345678901234567890123456789012345678901", "비밀번호 길이 초과")
        );
    }
    
    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("validationFail")
    @DisplayName("회원 가입 - 유효성 통과 실패")
    void signUp_Bad_Request_Validation_Fail(@AggregateWith(SignUpRequestAggregator.class)
                                            SignUpRequest signUpRequest) throws Exception {

        mockMvc.perform(post("/api/auth/account")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist(JwtUtil.COOKIE_NAME))
        ;
    }

    private static Stream<Arguments> duplicateFail() {
        return Stream.of(
                Arguments.of(email, "test1", password, "이메일 중복"),
                Arguments.of("test1@test.com", nickname,  password, "닉네임 중복")
        );
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("duplicateFail")
    @DisplayName("회원 가입 - 중복 실패")
    void signUp_Bad_Request_Duplicate_Fail(@AggregateWith(SignUpRequestAggregator.class)
                                            SignUpRequest signUpRequest) throws Exception {

        Account account = Account.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();

        accountRepository.save(account);

        mockMvc.perform(post("/api/auth/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist(JwtUtil.COOKIE_NAME))
        ;
    }

}