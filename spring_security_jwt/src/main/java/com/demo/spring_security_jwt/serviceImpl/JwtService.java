package com.demo.spring_security_jwt.serviceImpl;

import com.demo.spring_security_jwt.dto.JwtResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SYSTEM_ROLE = "roles";
    private static final String AUTHORIZATION = "Authorization";


    private final String apiKey = "e3LHqZMEtP7pc8HTJeU7cnslny6OQhNgaHa3Jdbjd0jK4AJUBz3xoRLS6AtuWgv4g0pu2L7OIpZvMOtGhGIZLSQ0P0oiQcY1OrvM";


    public JwtResponseDto generateToken(String email, List<String> roles) {

        Map<String, Object> accessTokenClaims = new HashMap<>();
        accessTokenClaims.put(SYSTEM_ROLE, roles);
        String accessToken = Jwts.builder()
                .claims()
                .add(accessTokenClaims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        Map<String, Object> refreshTokenClaims = new HashMap<>();
        refreshTokenClaims.put(SYSTEM_ROLE, roles);
        String refreshToken = Jwts.builder()
                .claims()
                .add(refreshTokenClaims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1800000))
                .and()
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        return new JwtResponseDto(accessToken, refreshToken);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(apiKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUsername(String token) {

        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> getSubject) {

        Claims claims = extractAllClaims(token);

        return getSubject.apply(claims);
    }


    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) throws JwtException, IllegalArgumentException {
        Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
        return true;
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);

    }

    public List<String> getSystemRoles(String token) {
        return (List<String>) extractAllClaims(token).get(SYSTEM_ROLE);
    }

    public Authentication getAuthentication(String token) {
        List<String> roles = getSystemRoles(token);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }

        UserDetails userDetails = new User(extractUsername(token), extractUsername(token), true, false, false, false, grantedAuthorities);
        System.out.println("userDetails is ::= " + userDetails);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getSystemRole(String token) {
        return (String) Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getBody().get(SYSTEM_ROLE);
    }


}
