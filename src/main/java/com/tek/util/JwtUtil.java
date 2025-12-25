package com.tek.util;

import com.tek.entity.Role;
import com.tek.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "b2df428b9929d3ace7c598bbf4e496b2b2df428b9929d3ace7c598bbf4e496b2".getBytes()
    );

    public String generateToken(String userEmail, Role role) {
        return Jwts.builder()
                .claim("role", "ROLE_"+role.name())
                .setSubject(userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10)) // 10 hours
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateToken(String doctorEmail) {
        return Jwts.builder()
                .claim("role", "ROLE_DOCTOR")
                .setSubject(doctorEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaims(token)
                .getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String userEmail = extractUserName(token);
        return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
