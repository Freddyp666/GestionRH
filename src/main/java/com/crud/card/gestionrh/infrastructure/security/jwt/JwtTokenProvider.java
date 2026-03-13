package com.crud.card.gestionrh.infrastructure.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.Column;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    //Clave secreta de jwt
    @Value("${jwt.secret}")
    private String jwtSecret;

    //expiracion de la clave
    @Value("${jwt.expiration}")
    private int jwtExpiration;


    //crea la clave criptográfica que se usará para firmar y verificar los tokens JWT.
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }


    //genera el JWT cuando el usuario inicia sesión correctamente.
    public String generarToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpiration);

        return Jwts
            .builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(now).setExpiration(expirationDate)
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }

    //método recibe un token JWT y extrae el nombre de usuario que está guardado dentro del token.
    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
          .setSigningKey(key())
          .build()
          .parseClaimsJws(token)
          .getBody();
        return claims.getSubject();
    }

    //
    public boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(key()).build().parse(token);
            return true;
        }catch (MalformedJwtException e){
            log.error("Token mal formado");
        }catch (ExpiredJwtException e) {
            log.error("Token expirado");
        }catch (UnsupportedJwtException e) {
            log.error("Token no soportado");

        }catch (IllegalArgumentException e){
            log.error("Token vacip");
        }

        return false;
    }
}
