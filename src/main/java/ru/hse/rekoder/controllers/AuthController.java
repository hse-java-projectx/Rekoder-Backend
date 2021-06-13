package ru.hse.rekoder.controllers;

import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rekoder.jwt.JwtConfig;
import ru.hse.rekoder.requests.ChangePasswordRequest;
import ru.hse.rekoder.requests.UsernamePasswordAuthenticationRequest;
import ru.hse.rekoder.responses.ErrorsResponse;
import ru.hse.rekoder.responses.SingleErrorResponse;
import ru.hse.rekoder.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtConfig jwtConfig) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UsernamePasswordAuthenticationRequest authenticationRequest,
                                   HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getId(),
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

            return ResponseEntity.noContent()
                    .header(HttpHeaders.AUTHORIZATION, jwtConfig.getTokenPrefix() + token)
                    .build();
        } catch (BadCredentialsException e) {
            SingleErrorResponse error = new SingleErrorResponse(
                    "login-fail",
                    "The id or the password is invalid"
            );
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorsResponse(List.of(error)));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                            Authentication authentication) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authentication.getName(),
                changePasswordRequest.getOldPassword()
        );
        try {
            authenticationManager.authenticate(authenticationToken);
            userService.changePassword(authentication.getName(), changePasswordRequest.getNewPassword());
            return ResponseEntity.noContent().build();
        } catch (BadCredentialsException e) {
            SingleErrorResponse error = new SingleErrorResponse(
                    "change-password-fail",
                    "The old password is invalid"
            );
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorsResponse(List.of(error)));
        }
    }
}
