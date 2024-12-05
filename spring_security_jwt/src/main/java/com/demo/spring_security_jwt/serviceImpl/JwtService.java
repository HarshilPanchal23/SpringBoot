package com.demo.spring_security_jwt.serviceImpl;

import com.demo.spring_security_jwt.dto.JwtResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    private String secretKey = "";

    public JwtService() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGenerator.generateKey();
        secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    }

    public JwtResponseDto generateToken(String email, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SYSTEM_ROLE, roles);
        String accessToken = Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(getSecretKey())
                .compact();
        return new JwtResponseDto(accessToken, null);
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
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
