package eci.ieti.safezone.service;

import eci.ieti.safezone.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey SECRET_KEY;
    private static final long EXPIRATION_TIME = 86400000; // 1 día

    public JwtService() {
        this.SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getName())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }

    public String getUsernameFromToken(String token) {
        return validateToken(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return validateToken(token).getBody().get("role", String.class);
    }
}
