package com.compass.demo_park_api;

import com.compass.demo_park_api.dtos.UserCreateDTO;
import com.compass.demo_park_api.dtos.UserPasswordDTO;
import com.compass.demo_park_api.dtos.UserResponseDTO;
import com.compass.demo_park_api.errors.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

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

    @Test
    public void createUser_WithInvalidUsername_ReturnErrorStatus422() {
        ErrorMessage res = testClient
                .post()
                .uri("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("", "admin1"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);

        res = testClient
                .post()
                .uri("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("tody@", "admin1"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_WithInvalidPassword_ReturnErrorStatus422() {
        ErrorMessage res = testClient
                .post()
                .uri("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("igor@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);

        res = testClient
                .post()
                .uri("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("igor@email.com", "admin1sadasdasdas"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);

        res = testClient
                .post()
                .uri("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("igor@email.com", "aaa"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_WithUniqueUsernameConflict_ReturnCreatedUserStatus422() {
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

        ErrorMessage resErr = testClient
                .post()
                .uri("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDTO("tody@email.com", "admin1"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(resErr).isNotNull();
        Assertions.assertThat(resErr.getStatus()).isEqualTo(400);
    }

    @Test
    public void getUserById_WithExistingId_ReturnUserStatus200() {
        UserResponseDTO res = testClient
                .get()
                .uri("/api/v1/user/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getId()).isNotNull();
        Assertions.assertThat(res.getUsername()).isEqualTo("ana@email.com");
        Assertions.assertThat(res.getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void getUserById_WithNonExistingId_ReturnUserStatus404() {
        ErrorMessage res = testClient
                .get()
                .uri("/api/v1/user/666")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(404);
    }

    @Test
    public void getManyUsers_WithNoParams_ReturnUserStatus200() {
        List<UserResponseDTO> res = testClient
                .get()
                .uri("/api/v1/user")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();

        Assertions.assertThat(res.get(0).getUsername()).isEqualTo("ana@email.com");
        Assertions.assertThat(res.get(1).getUsername()).isEqualTo("bia@email.com");
        Assertions.assertThat(res.get(2).getUsername()).isEqualTo("bob@email.com");
    }

    @Test
    public void updatePassword_WithValidData_ReturnStatus204() {
        testClient
                .patch()
                .uri("/api/v1/user/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "banana", "banana"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class)
                .returnResult().getResponseBody();
    }

    @Test
    public void updatePassword_WithInvalidData_ReturnStatus204() {
        ErrorMessage res = testClient
                .patch()
                .uri("/api/v1/user/666")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "banana", "banana"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(404);

        res = testClient
                .patch()
                .uri("/api/v1/user/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "banana", "banana2"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);

        res = testClient
                .patch()
                .uri("/api/v1/user/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "banan1", "banan2"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(400);

        res = testClient
                .patch()
                .uri("/api/v1/user/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123457", "banana", "banana"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(400);
    }
}
