package com.compass.demo_park_api.controllers;

import com.compass.demo_park_api.dtos.UserCreateDTO;
import com.compass.demo_park_api.dtos.UserPasswordDTO;
import com.compass.demo_park_api.dtos.UserResponseDTO;
import com.compass.demo_park_api.dtos.mapper.UserMapper;
import com.compass.demo_park_api.entities.User;
import com.compass.demo_park_api.errors.ErrorMessage;
import com.compass.demo_park_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Usuários", description = "Contém todas as operações de CRUD de usuários.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Criar um novo usuário", responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso criado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nome do usuário ja está cadastrado no sistema.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Entrada de dados inválidos.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO createDTO) {
        User createdUser = userService.save(UserMapper.toUser(createDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDTO(createdUser));
    }

    @Operation(summary = "Buscar um usuário pelo ID", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Recurso retornado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário com o ID especificado não foi encontrado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long userId) {
        User user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toResponseDTO(user));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UserPasswordDTO userPasswordDTO
    ) {
        userService.updatePassword(
                userId,
                userPasswordDTO.getPassword(),
                userPasswordDTO.getNewPassword(),
                userPasswordDTO.getConfirmNewPassword()
        );

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getMany() {
        List<User> users = userService.findAll();

        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();

        for (User user : users) {
            userResponseDTOS.add(UserMapper.toResponseDTO(user));
        }

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTOS);
    }
}
