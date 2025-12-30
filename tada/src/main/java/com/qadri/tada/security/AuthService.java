package com.qadri.tada.security;

import com.qadri.tada.dto.LoginRequestDto;
import com.qadri.tada.dto.LoginResponseDto;
import com.qadri.tada.dto.SignupRequestDto;
import com.qadri.tada.dto.SignupResponseDto;
import com.qadri.tada.entity.User;
import com.qadri.tada.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(user.getId(), token);
    }

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        User user = userRepository.findByUsername(signupRequestDto.getEmail()).orElse(null);

        if (user != null) throw new IllegalArgumentException("User already exists");

        user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build();

        user = userRepository.save(user);
        String token = authUtil.generateAccessToken(user);

        return new SignupResponseDto(user.getId(), token);
    }
}
