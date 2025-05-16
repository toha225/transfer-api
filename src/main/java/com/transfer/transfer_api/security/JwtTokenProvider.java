package com.transfer.transfer_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(Long userId) {
        Date now = new Date();
        long jwtExpirationInMs = 3600000;
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .claim("USER_ID", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.get("USER_ID").toString());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Неверная подпись JWT: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Неверная структура JWT: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("JWT токен просрочен: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Неподдерживаемый JWT токен: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT токен пустой или содержит невалидные данные: {}", ex.getMessage());
        }
        return false;
    }
}
