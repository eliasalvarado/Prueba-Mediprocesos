package com.mediprocesos.backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Proveedor JWT para generar, validar y extraer información de tokens.
 * Maneja la seguridad de los tokens JWT en la aplicación.
 * 
 * @author Mediprocesos
 * @version 1.0
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Genera un JWT token con rol.
     * 
     * @param username el nombre de usuario para el que se genera el token
     * @param rol el rol del usuario
     * @return el token JWT generado
     */
    public String generateToken(String username, String rol) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Obtiene el nombre de usuario del token.
     * 
     * @param token el token JWT
     * @return el nombre de usuario extraído del token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Obtiene el rol del token.
     * 
     * @param token el token JWT
     * @return el rol extraído del token
     */
    public String getRolFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("rol", String.class);
    }

    /**
     * Valida si un token es válido.
     * 
     * @param token el token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException e) {
            log.error("JWT signature doesn't match", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty", e);
        }
        return false;
    }

    /**
     * Obtiene todos los claims del token.
     * 
     * @param token el token JWT
     * @return los claims contenidos en el token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtiene la clave de firma para los tokens JWT.
     * Genera una SecretKey de 64 bytes (512 bits) requerida para HS512.
     * 
     * @return la SecretKey para firmar tokens
     */
    private SecretKey getSigningKey() {
        try {
            // Usar SHA-256 para derivar una clave de 64 bytes a partir del secreto
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(jwtSecret.getBytes(StandardCharsets.UTF_8));
            
            // Para HS512 necesitamos 64 bytes, SHA-256 nos da 32, duplicamos el hash
            byte[] key = new byte[64];
            System.arraycopy(hash, 0, key, 0, 32);
            System.arraycopy(hash, 0, key, 32, 32);
            
            return Keys.hmacShaKeyFor(key);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generando clave JWT", e);
            throw new RuntimeException("Error al generar la clave JWT", e);
        }
    }
}
