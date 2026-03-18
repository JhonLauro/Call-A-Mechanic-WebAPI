package com.callamechanic.profile.service;

import com.callamechanic.exception.ApiException;
import com.callamechanic.profile.dto.EditPasswordRequestDTO;
import com.callamechanic.profile.dto.EditProfileRequestDTO;
import com.callamechanic.profile.dto.ProfileResponseDTO;
import com.callamechanic.profile.dto.UploadPhotoResponseDTO;
import com.callamechanic.user.model.User;
import com.callamechanic.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get user profile by user ID
     */
    public ProfileResponseDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        return new ProfileResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getMechanicId(),
                user.getAdminId(),
                user.isActive(),
                user.getPhoto() != null
        );
    }

    /**
     * Edit user profile (fullName and phoneNumber)
     */
    public ProfileResponseDTO editProfile(Long userId, EditProfileRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        User updatedUser = userRepository.save(user);

        return new ProfileResponseDTO(
                updatedUser.getId(),
                updatedUser.getFullName(),
                updatedUser.getEmail(),
                updatedUser.getPhoneNumber(),
                updatedUser.getRole(),
                updatedUser.getMechanicId(),
                updatedUser.getAdminId(),
                updatedUser.isActive(),
                updatedUser.getPhoto() != null
        );
    }

    /**
     * Change user password
     */
    public void changePassword(Long userId, EditPasswordRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new ApiException("PASS-001",
                    "Invalid current password",
                    "The current password you entered is incorrect.");
        }

        // Verify new password and confirmation match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ApiException("PASS-002",
                    "Password mismatch",
                    "The new password and confirmation password do not match.");
        }

        // Prevent using same password as current
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new ApiException("PASS-003",
                    "Invalid password change",
                    "The new password must be different from your current password.");
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Upload user photo
     */
    public UploadPhotoResponseDTO uploadPhoto(Long userId, byte[] photoData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        // Validate photo data size (max 5MB)
        if (photoData.length > 5 * 1024 * 1024) {
            throw new ApiException("FILE-001",
                    "File too large",
                    "The image file must be less than 5MB in size.");
        }

        user.setPhoto(photoData);
        userRepository.save(user);

        return new UploadPhotoResponseDTO(
                "Photo uploaded successfully",
                user.getId()
        );
    }
}
