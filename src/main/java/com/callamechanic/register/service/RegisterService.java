package com.callamechanic.register.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.callamechanic.exception.ApiException;
import com.callamechanic.login.service.JwtService;
import com.callamechanic.register.dto.RegisterResponseDTO;
import com.callamechanic.user.model.User;
import com.callamechanic.user.repository.UserRepository;

@Service
public class RegisterService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService      jwtService;

    public RegisterService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService      = jwtService;
    }

    public RegisterResponseDTO register(String fullName, String email,
                                        String phoneNumber, String password) {

        // Check if email already exists
        if (userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            throw new ApiException("DB-002",
                    "Duplicate entry",
                    "An account with this email already exists..");
        }

        // Create and save new user
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email.toLowerCase());
        user.setPhoneNumber(phoneNumber);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole("CLIENT");
        user.setActive(true);

        userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(user);

        RegisterResponseDTO.UserInfo userInfo = new RegisterResponseDTO.UserInfo(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );

        return new RegisterResponseDTO(token, userInfo);
    }
}
