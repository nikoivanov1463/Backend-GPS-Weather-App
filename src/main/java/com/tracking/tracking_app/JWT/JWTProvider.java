package com.tracking.tracking_app.JWT;

import com.tracking.tracking_app.Entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class JWTProvider {
    private static final String issuer = "TrackerApp";

    private final long accessTokenExpirationMillis;

    private final long refreshTokenExpirationMillis;

    private final SecretKey accessTokenHashingKey;
    private final SecretKey refreshTokenHashingKey;

    public JWTProvider(@Value("${accessTokenExpirationMinutes}") int accessTokenExpirationMinutes, @Value("${refreshTokenExpirationDays}") int refreshTokenExpirationDays, @Value("${jwt.access.secret}") String accessTokenSecret, @Value("${jwt.refresh.secret}") String refreshTokenSecret
    ) {
        this.accessTokenHashingKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshTokenHashingKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMillis = accessTokenExpirationMinutes * 60 * 1000L;
        this.refreshTokenExpirationMillis = refreshTokenExpirationDays * 24 * 60 * 60 * 1000L;
    }

    public String createJWToken(User user) {
        return Jwts.builder().issuer(issuer)
                .subject(String.valueOf(user.getId())).
                issuedAt(new Date()).
                expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMillis))
                .claim("token_duration", accessTokenExpirationMillis)
                .signWith(accessTokenHashingKey)
                .compact();
    }

    public String createRefreshJWToken(User user, String tokenId) {
        return Jwts.builder().issuer(issuer)
                .subject(String.valueOf(user.getId()))
                .claim("token_id", tokenId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMillis))
                .claim("refresh_token_duration", refreshTokenExpirationMillis)
                .signWith(refreshTokenHashingKey)
                .compact();
    }

    private Optional<Jws<Claims>> decodeJWToken(String token) {
        try {
            return Optional.of(Jwts.parser().verifyWith(accessTokenHashingKey).build().parseSignedClaims(token));
        } catch (ExpiredJwtException e) {
            System.err.println("Expired JWT Token.");
        } catch (RuntimeException e) {
            System.err.println("Invalid session (JWT)");
        }
        return Optional.empty();
    }

    private Optional<Jws<Claims>> decodeRefreshJWToken(String token) {
        try {
            return Optional.of(Jwts.parser().verifyWith(refreshTokenHashingKey).build().parseSignedClaims(token));
        } catch (MalformedJwtException e) {
            System.err.println("Invalid refresh session");
        } catch (SignatureException e) {
            throw new SignatureException(e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Expired token please log in again.");
        }
        return Optional.empty();
    }

    public boolean validateJWToken(String token) {
        return decodeJWToken(token).isPresent();
    }

    public boolean validateRefreshJWToken(String token) {
        return decodeRefreshJWToken(token).isPresent();
    }

    public String getUserIdFromJWT(String token) {
        return decodeJWToken(token).get().getPayload().getSubject();
    }

    public String getUserIdFromRefreshJWT(String token) {
        return decodeRefreshJWToken(token).get().getPayload().getSubject();
    }

    public String getTokenIdFromRefreshJWT(String token) {
        Jws<Claims> refreshClaims = decodeRefreshJWToken(token).get();

        Object tokenIdObj = refreshClaims.getPayload().get("token_id");

        if (tokenIdObj == null) {
            throw new RuntimeException("Token ID is missing in the refresh token");
        }

        return tokenIdObj.toString();
    }
}
