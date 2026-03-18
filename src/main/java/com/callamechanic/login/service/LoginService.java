package com.callamechanic.login.service;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.callamechanic.exception.ApiException;
import com.callamechanic.login.dto.LoginResponseDTO;
import com.callamechanic.user.model.User;
import com.callamechanic.user.repository.UserRepository;

@Service
public class LoginService {

    // Detects login type by identifier format
    private static final Pattern MECHANIC_PATTERN = Pattern.compile("^\\d{2}-\\d{3}$");
    private static final Pattern ADMIN_PATTERN    = Pattern.compile("^ADM-\\d{3}$", Pattern.CASE_INSENSITIVE);

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
        User user;

        if (ADMIN_PATTERN.matcher(identifier).matches()) {
            // ADM-xxx format → Admin login
            user = userRepository.findByAdminId(identifier)
                    .orElseThrow(() -> new ApiException("AUTH-001",
                            "Invalid credentials",
                            "The identifier or password you entered is incorrect."));

        } else if (MECHANIC_PATTERN.matcher(identifier).matches()) {
            // xx-xxx format → Mechanic login
            user = userRepository.findByMechanicId(identifier)
                    .orElseThrow(() -> new ApiException("AUTH-001",
                            "Invalid credentials",
                            "The identifier or password you entered is incorrect."));

        } else {
            // email format → Client login
            user = userRepository.findByEmail(identifier.toLowerCase())
                    .orElseThrow(() -> new ApiException("AUTH-001",
                            "Invalid credentials",
                            "The identifier or password you entered is incorrect."));
        }

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
