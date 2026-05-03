package com.callamechanic.login.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.callamechanic.exception.ApiException;
import com.callamechanic.login.dto.LoginResponseDTO;
import com.callamechanic.user.model.User;
import com.callamechanic.user.repository.UserRepository;

@Service
public class LoginService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService      jwtService;

    public LoginService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService      = jwtService;
    }

    public LoginResponseDTO login(String identifier, String password) {
        String loginIdentifier = identifier.trim();

        User user = userRepository
                .findFirstByEmailIgnoreCaseOrMechanicIdIgnoreCaseOrAdminIdIgnoreCase(
                        loginIdentifier,
                        loginIdentifier,
                        loginIdentifier
                )
                .orElseThrow(() -> new ApiException("AUTH-001",
                        "Invalid credentials",
                        "The identifier or password you entered is incorrect."));

        // Check password
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ApiException("AUTH-001",
                    "Invalid credentials",
                    "The identifier or password you entered is incorrect.");
        }

        // Check if account is active
        if (!user.isActive()) {
            throw new ApiException("AUTH-003",
                    "Account disabled",
                    "Your account has been deactivated. Please contact the admin.");
        }

        String token = jwtService.generateToken(user);

        LoginResponseDTO.UserInfo userInfo = new LoginResponseDTO.UserInfo(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getMechanicId(),
                user.getAdminId()
        );

        return new LoginResponseDTO(token, userInfo);
    }
}
