package com.hachicore.sellbook.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfileControllerUnitTest {

    @Test
    @DisplayName("prod1 profile이 조회된다")
    void prodProfileCheck() {
        // given
        String expectedProfile = "prod1";

        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("api");
        env.addActiveProfile("auth");
        env.addActiveProfile("prod-db");

        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();
        System.out.println(profile);

        // then
        assertEquals(profile, expectedProfile);
    }

    @Test
    @DisplayName("prod profile이 없으면 첫 번째 프로파일이 조회된다")
    void firstProfileCheck() {
        // given
        String expectedProfile = "auth";

        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("api");
        env.addActiveProfile("prod-db");

        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();

        // then
        assertEquals(profile, expectedProfile);
    }

    @Test
    @DisplayName("active profile이 없으면 default가 조회된다")
    void defaultProfileCheck() {
        // given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        ProfileController controller = new ProfileController(env);

        // when
        String profile = controller.profile();

        // then
        assertEquals(profile, expectedProfile);
    }

}