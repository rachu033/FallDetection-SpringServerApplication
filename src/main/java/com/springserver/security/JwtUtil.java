package com.springserver.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final byte[] secretBytes;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretBytes = secret.getBytes();
    }

    public String generateToken(String email) {
        long expirationMillis = 1000 * 60 * 60 * 24;
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretBytes)
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration != null && expiration.after(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretBytes)
                .parseClaimsJws(token)
                .getBody();
    }
}
