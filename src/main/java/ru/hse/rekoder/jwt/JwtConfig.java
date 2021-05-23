package ru.hse.rekoder.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.crypto.SecretKey;

@Configuration
@PropertySource("classpath:application.properties")
public class JwtConfig {
    @Value("${jwt.encode-secret-key}")
    private String encodeSecretKey;
    @Value("${jwt.token-expires-after-days}")
    private Integer tokenExpirationAfterDays;
    private SecretKey secretKey;
    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    public SecretKey getSecretKey() {
        if (secretKey == null) {
            this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodeSecretKey));
        }
        return secretKey;
    }

    public int getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }
}
