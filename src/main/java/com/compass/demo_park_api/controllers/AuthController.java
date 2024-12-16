package com.compass.demo_park_api.controllers;

import com.compass.demo_park_api.dtos.UserLoginDTO;
import com.compass.demo_park_api.errors.LoginError;
import com.compass.demo_park_api.jwt.JwtToken;
import com.compass.demo_park_api.jwt.JwtUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        log.info("Login request received");
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            authenticationManager.authenticate(authenticationToken);

            JwtToken jwtToken = jwtUserDetailsService.getTokenAuthenticated(userLoginDTO.getUsername());
            return ResponseEntity.ok(jwtToken);
        } catch (AuthenticationException e) {
            throw new LoginError();
        }
    }
}
