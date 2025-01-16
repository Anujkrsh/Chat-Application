package com.olive.chatapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET = "ow6r/t+vw90/V19/b3/t8P7/v/P/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/v/9/";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
