package com.callamechanic.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.callamechanic.admin.dto.CreateMechanicResponseDTO;
import com.callamechanic.admin.dto.DeleteUserResponseDTO;
import com.callamechanic.admin.dto.UserListResponseDTO;
import com.callamechanic.appointment.model.Appointment;
import com.callamechanic.appointment.repository.AppointmentRepository;
import com.callamechanic.exception.ApiException;
import com.callamechanic.user.model.User;
import com.callamechanic.user.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Get all clients and mechanics (admin only)
     */
    public UserListResponseDTO getAllUsers(Long adminUserId) {
        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        if (!"ADMIN".equals(admin.getRole())) {
            throw new ApiException("AUTH-005",
                    "Unauthorized action",
                    "Only admins can view all users.");
        }

        List<User> users = userRepository.findByRoleIn(List.of("CLIENT", "MECHANIC"));

        List<UserListResponseDTO.UserInfo> userInfoList = users.stream()
                .map(u -> new UserListResponseDTO.UserInfo(
                        u.getId(),
                        u.getFullName(),
                        u.getEmail(),
                        u.getPhoneNumber(),
                        u.getRole(),
                        u.getMechanicId(),
                        u.isActive()
                ))
                .collect(Collectors.toList());

        return new UserListResponseDTO(userInfoList);
    }

    /**
     * Create mechanic account (admin only)
     */
    public CreateMechanicResponseDTO createMechanic(Long adminUserId, String fullName, String email,
                                                    String phoneNumber, String password, String mechanicId) {
        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        if (!"ADMIN".equals(admin.getRole())) {
            throw new ApiException("AUTH-005",
                    "Unauthorized action",
                    "Only admins can create mechanic accounts.");
        }

        // Check if email already exists
        if (userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            throw new ApiException("DB-002",
                    "Duplicate entry",
                    "An account with this email already exists.");
        }

        // Check if mechanic ID already exists
        if (userRepository.findByMechanicId(mechanicId).isPresent()) {
            throw new ApiException("DB-002",
                    "Duplicate entry",
                    "This mechanic ID is already in use.");
        }

        User mechanic = new User();
        mechanic.setFullName(fullName);
        mechanic.setEmail(email.toLowerCase());
        mechanic.setPhoneNumber(phoneNumber);
        mechanic.setPasswordHash(passwordEncoder.encode(password));
        mechanic.setRole("MECHANIC");
        mechanic.setMechanicId(mechanicId);
        mechanic.setActive(true);

        User saved = userRepository.save(mechanic);

        CreateMechanicResponseDTO.MechanicInfo mechanicInfo = new CreateMechanicResponseDTO.MechanicInfo(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getMechanicId()
        );

        return new CreateMechanicResponseDTO("Mechanic account created successfully", mechanicInfo);
    }

    /**
     * Delete user account (admin only)
     */
    public DeleteUserResponseDTO deleteUser(Long adminUserId, Long userIdToDelete) {
        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        if (!"ADMIN".equals(admin.getRole())) {
            throw new ApiException("AUTH-005",
                    "Unauthorized action",
                    "Only admins can delete user accounts.");
        }

        User userToDelete = userRepository.findById(userIdToDelete)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        // Prevent admin from deleting other admins
        if ("ADMIN".equals(userToDelete.getRole())) {
            throw new ApiException("AUTH-006",
                    "Unauthorized action",
                    "Admins cannot delete other admin accounts.");
        }

        userRepository.delete(userToDelete);

        return new DeleteUserResponseDTO("User account deleted successfully");
    }

    /**
     * Assign mechanic to appointment (admin only)
     */
    public Appointment assignMechanic(Long adminUserId, Long appointmentId, Long mechanicId) {
        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        if (!"ADMIN".equals(admin.getRole())) {
            throw new ApiException("AUTH-005",
                    "Unauthorized action",
                    "Only admins can assign mechanics to appointments.");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiException("APPT-001",
                        "Appointment not found",
                        "The appointment with the provided ID does not exist."));

        User mechanic = userRepository.findById(mechanicId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The mechanic with the provided ID does not exist."));

        if (!"MECHANIC".equals(mechanic.getRole())) {
            throw new ApiException("AUTH-004",
                    "Invalid user role",
                    "The specified user is not a mechanic.");
        }

        appointment.setMechanic(mechanic);
        return appointmentRepository.save(appointment);
    }
}
