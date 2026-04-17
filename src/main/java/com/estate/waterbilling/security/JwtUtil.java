package com.estate.waterbilling.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // ⚠️ Change this secret to a long random string in production!
    private static final String SECRET = "NafakaMillimaniWaterBillingSecretKey2024!!";
    private static final long   EXPIRY  = 8 * 60 * 60 * 1000; // 8 hours in ms

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // ── Generate a token for the logged-in manager ──
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Extract username from token ──
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ── Validate token ──
    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
