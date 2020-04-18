package com.hachicore.sellbook.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class ProfileControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void profileRequestWithoutAuth() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/profile", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}