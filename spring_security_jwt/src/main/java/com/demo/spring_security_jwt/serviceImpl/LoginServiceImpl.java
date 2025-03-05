package com.demo.spring_security_jwt.serviceImpl;

import com.demo.spring_security_jwt.dto.JwtResponseDto;
import com.demo.spring_security_jwt.dto.LoginRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenRequestDto;
import com.demo.spring_security_jwt.dto.RefreshTokenResponse;
import com.demo.spring_security_jwt.entity.PasswordReset;
import com.demo.spring_security_jwt.entity.RoleEntity;
import com.demo.spring_security_jwt.entity.UserEntity;
import com.demo.spring_security_jwt.entity.UserRoleEntity;
import com.demo.spring_security_jwt.enums.ExceptionEnum;
import com.demo.spring_security_jwt.exception.CustomException;
import com.demo.spring_security_jwt.repository.PasswordResetRepository;
import com.demo.spring_security_jwt.repository.RoleRepository;
import com.demo.spring_security_jwt.repository.UserRepository;
import com.demo.spring_security_jwt.repository.UserRoleRepository;
import com.demo.spring_security_jwt.service.LoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {


    private final JwtService jwtService;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private static final String SYSTEM_ROLE = "role";
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private static final String USER_ID = "userId";
    private final RoleRepository roleRepository;
    private final String apiKey = "e3LHqZMEtP7pc8HTJeU7cnslny6OQhNgaHa3Jdbjd0jK4AJUBz3xoRLS6AtuWgv4g0pu2L7OIpZvMOtGhGIZLSQ0P0oiQcY1OrvM";


    @Override
    public JwtResponseDto verify(LoginRequestDto loginRequestDto) {

        UserEntity userEntity = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
        if (this.passwordEncoder.matches(loginRequestDto.getPassword(), userEntity.getPassword())) {
            List<String> rolesList = userRoleRepository.findByUserId(userEntity.getId());
            return jwtService.generateToken(userEntity.getEmail(), userEntity.getId(), rolesList);
        } else {
            throw new CustomException("password does not match", HttpStatus.UNAUTHORIZED);
        }
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
        Date validity = new Date(now.getTime() + 15 * 60 * 1000); // 30 minutes
        String accessToken = Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(SignatureAlgorithm.HS256, apiKey)
                .compact();

        return new RefreshTokenResponse(accessToken);
    }

    @Override
    public void forgotPassword(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
        String token = UUID.randomUUID().toString();
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setToken(token);
        passwordReset.setUserEntity(userEntity);
        passwordReset.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        passwordResetRepository.save(passwordReset);
        String resetUrl = "http://192.168.10.33:8080/reset-password?token=" + token;
        this.sendPasswordResetEmail(userEntity.getEmail(), resetUrl);

    }


    @Override
    public ResponseEntity<String> resetPassword(String token, String newPassword) {
        PasswordReset resetToken = passwordResetRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
        UserEntity user = resetToken.getUserEntity();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        passwordResetRepository.delete(resetToken);
        return ResponseEntity.ok("Your Password has been reset successfully.");
    }


    public void sendPasswordResetEmail(String toEmail, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: " + resetUrl);
        message.setFrom("smtp.gmail.com");
        mailSender.send(message);
    }


}
