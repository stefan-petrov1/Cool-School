package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.models.dto.auth.*;
import com.coolSchool.coolSchool.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request, HttpServletResponse servletResponse) {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        authenticationService.attachAuthCookies(authenticationResponse, servletResponse::addCookie);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse servletResponse) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        authenticationService.attachAuthCookies(authenticationResponse, servletResponse::addCookie);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenBodyDTO refreshTokenBody) throws IOException {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenBody));
    }

    @PostMapping("/me")
    public ResponseEntity<AuthenticationResponse> getMe(@RequestBody AccessTokenBodyDTO accessTokenBodyDTO) {
        return ResponseEntity.ok(authenticationService.me(accessTokenBodyDTO));
    }
}
