package ru.hse.rekoder.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.crypto.SecretKey;

@Configuration
@PropertySource("classpath:application.properties")
@Getter
public class JwtConfig {
    private final Integer tokenExpirationAfterDays;
    private final SecretKey secretKey;
    private final String tokenPrefix;

    public JwtConfig(@Value("${jwt.encode-secret-key}") String encodeSecretKey,
                     @Value("${jwt.token-expires-after-days}") Integer tokenExpirationAfterDays,
                     @Value("${jwt.token-prefix}") String tokenPrefix) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodeSecretKey));
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
        this.tokenPrefix = tokenPrefix;
    }
}
