package com.demo.spring_security_jwt.serviceImpl;

import com.demo.spring_security_jwt.entity.UserEntity;
import com.demo.spring_security_jwt.enums.ExceptionEnum;
import com.demo.spring_security_jwt.exception.CustomException;
import com.demo.spring_security_jwt.repository.UserRepository;
import com.demo.spring_security_jwt.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_WITH_EMAIL_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }
        List<String> roles = userRoleRepository.findByUserId(user.getId());
        System.out.println("byUserId = " + roles.toString());

        // Convert roles to authorities
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .toList();

        return new User(user.getEmail(), encoder.encode(user.getPassword()), authorities);


    }
}
