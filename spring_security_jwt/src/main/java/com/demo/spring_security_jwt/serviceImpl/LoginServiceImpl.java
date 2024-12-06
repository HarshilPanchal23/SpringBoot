package com.demo.spring_security_jwt.serviceImpl;

import com.demo.spring_security_jwt.dto.JwtResponseDto;
import com.demo.spring_security_jwt.dto.LoginRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenResponse;
import com.demo.spring_security_jwt.entity.RoleEntity;
import com.demo.spring_security_jwt.entity.UserEntity;
import com.demo.spring_security_jwt.entity.UserRoleEntity;
import com.demo.spring_security_jwt.enums.ExceptionEnum;
import com.demo.spring_security_jwt.exception.CustomException;
import com.demo.spring_security_jwt.repository.RoleRepository;
import com.demo.spring_security_jwt.repository.UserRepository;
import com.demo.spring_security_jwt.repository.UserRoleRepository;
import com.demo.spring_security_jwt.service.LoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {


    private final JwtService jwtService;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private static final String SYSTEM_ROLE = "role";

    private static final String USER_ID = "userId";
    private final RoleRepository roleRepository;
    private final String apiKey = "e3LHqZMEtP7pc8HTJeU7cnslny6OQhNgaHa3Jdbjd0jK4AJUBz3xoRLS6AtuWgv4g0pu2L7OIpZvMOtGhGIZLSQ0P0oiQcY1OrvM";


    @Override
    public JwtResponseDto verify(LoginRequestDto loginRequestDto) {

        UserEntity userEntity = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
        List<String> rolesList = userRoleRepository.findByUserId(userEntity.getId());
        return jwtService.generateToken(userEntity.getEmail(), userEntity.getId(), rolesList);
    }

    @Override
    public RefreshTokenResponse generateRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        String bearerToken = refreshTokenRequestDto.getRefreshToken();

        // Extract username and user ID from the token
        String username = jwtService.extractUsername(bearerToken);
        Long userId = jwtService.getUserIdToken(bearerToken);

        // Fetch user role and system role
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user id  not found: " + userId));

        List<UserRoleEntity> userRoleEntityList = userRoleRepository.findByUserId(userEntity);
        List<String> roles = userRoleEntityList.stream().map(userRoleEntity -> {

            RoleEntity roleEntity = roleRepository.findByRole(userRoleEntity.getRoleId().getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + userRoleEntity.getRoleId().getRole()));
            return roleEntity.getRole();
        }).toList();

        // Set claims for the new access token
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, userId);
        claims.put(SYSTEM_ROLE, roles); // Add the list of roles to the claims

        // Generate access token
        Date now = new Date();
        Date validity = new Date(now.getTime() + 30 * 60 * 1000); // 30 minutes
        String accessToken = Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        return new RefreshTokenResponse(accessToken);
    }


}
