package com.compass.demo_park_api;

import com.compass.demo_park_api.dtos.UserCreateDTO;
import com.compass.demo_park_api.dtos.UserResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIntegration {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUser_WithUsernameAndPassword_ReturnCreatedUserStatus201() {
        UserResponseDTO res = testClient
                .post()
                .uri("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("tody@email.com", "admin1"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getId()).isNotNull();
        Assertions.assertThat(res.getUsername()).isEqualTo("tody@email.com");
        Assertions.assertThat(res.getRole()).isEqualTo("USER");
    }
}
