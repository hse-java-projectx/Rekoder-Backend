package ru.hse.rekoder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rekoder.exceptions.AccessDeniedException;
import ru.hse.rekoder.jwt.JwtConfig;
import ru.hse.rekoder.jwt.UsernamePasswordAuthenticationRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@RestController
public class AuthController {
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    public AuthController(ObjectMapper objectMapper,
                          AuthenticationManager authenticationManager,
                          JwtConfig jwtConfig) {
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request) {
        UsernamePasswordAuthenticationRequest authenticationRequest;
        try {
            authenticationRequest =
                    objectMapper.readValue(request.getInputStream(), UsernamePasswordAuthenticationRequest.class);
        } catch (IOException e) {
            throw new AccessDeniedException("Failed to parse request body", e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            String token = Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim("ip", request.getRemoteAddr())
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                    .signWith(jwtConfig.getSecretKey())
                    .compact();

            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtConfig.getTokenPrefix() + token)
                    .build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
