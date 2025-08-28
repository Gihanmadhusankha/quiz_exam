package com.quiz.quiz_exam.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor(
            "very-long-secret-key-change-me-please-1234567890".getBytes()
    );
    private final long expirationMs = 1000 * 60 * 60 * 12; // 12h

    //  generate token with custom claims
    public String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //  parse token
    public Jws<Claims> parse(String token) {
        return Jwts.parser()      // <-- use parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    //  extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()      // <-- use parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // extract userId from token
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("uid", Long.class);
    }

    // extract claims easily
    public Claims getClaims(String token) {
        return parse(token).getBody();
    }

    //  extract subject (email or username)
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }
}

