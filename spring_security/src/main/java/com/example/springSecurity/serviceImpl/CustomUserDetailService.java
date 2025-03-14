package com.example.springSecurity.serviceImpl;

import com.example.springSecurity.entity.UserEntity;
import com.example.springSecurity.enums.ExceptionEnum;
import com.example.springSecurity.exception.CustomException;
import com.example.springSecurity.repository.UserRepository;
import com.example.springSecurity.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByFirstName(username)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_WITH_ID_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND));
        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User not found");
        }
        List<String> roles = userRoleRepository.findByUserId(user.getId());
        System.out.println("byUserId = " + roles.toString());

        // Convert roles to authorities
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .toList();

        // Create and return UserDetails
        return new User(user.getFirstName(), user.getPassword(), authorities);


    }
}
