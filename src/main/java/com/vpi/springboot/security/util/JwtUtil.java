package com.vpi.springboot.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String SECRET_KEY = "secret";

    /**
     * 
     * @param token: 
     * @return nombre de ususario
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    
    /**
     * 
     * @param token
     * @return 
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    
    /**
     * Basicamente revisa si el token que llega es uno de los que mando a los usuarios en el login
     * @param <T>
     * @param token
     * @param claimsResolver
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 
     * @param userDetais: es el usuario que devuelve MyUserDetailsService
     * @return: un JWT basado en los detalles del usuario que llega por parametro
     */
    public String generateToken(MyDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("foto", userDetails.getUser().getFoto());
        return createToken(claims, userDetails.getUsername());
    }

    
    /**
     * ACA ESTA LA MAGIA
     * @param claims
     * @param subject
     * @return
     */
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    
    /**
     * obtiene el nombre de usuario a partir del token,
     * luego lo compara con el nombre del usuario y confirma que el token no ha expirado
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}