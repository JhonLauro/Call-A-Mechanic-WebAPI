package com.callamechanic.appointment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.callamechanic.appointment.dto.AppointmentResponseDTO;
import com.callamechanic.appointment.dto.CreateAppointmentRequestDTO;
import com.callamechanic.appointment.model.Appointment;
import com.callamechanic.appointment.repository.AppointmentRepository;
import com.callamechanic.exception.ApiException;
import com.callamechanic.user.model.User;
import com.callamechanic.user.repository.UserRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get appointments filtered by user role
     * - CLIENT: sees own appointments
     * - MECHANIC: sees all appointments
     * - ADMIN: sees all appointments
     */
    public List<AppointmentResponseDTO> getAppointments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        List<Appointment> appointments;

        switch (user.getRole()) {
            case "CLIENT" -> appointments = appointmentRepository.findByClientId(userId);
            case "MECHANIC", "ADMIN" -> appointments = appointmentRepository.findAll();
            default -> throw new ApiException("AUTH-004",
                    "Invalid role",
                    "User role is not recognized.");
        }

        return appointments.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create new appointment (client only)
     */
    public AppointmentResponseDTO createAppointment(Long userId, CreateAppointmentRequestDTO request) {
        User client = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        if (!"CLIENT".equals(client.getRole())) {
            throw new ApiException("AUTH-005",
                    "Unauthorized action",
                    "Only clients can create appointments.");
        }

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setServiceType(request.getServiceType());
        appointment.setVehicleInfo(request.getVehicleInfo());
        appointment.setProblemDescription(request.getProblemDescription());
        appointment.setScheduledDate(request.getScheduledDate());
        appointment.setStatus("PENDING");

        Appointment saved = appointmentRepository.save(appointment);
        return toResponseDTO(saved);
    }

    /**
     * Update appointment status (mechanic or admin only)
     */
    public AppointmentResponseDTO updateStatus(Long userId, Long appointmentId, String newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        if (!"MECHANIC".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            throw new ApiException("AUTH-005",
                    "Unauthorized action",
                    "Only mechanics or admins can update appointment status.");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiException("APPT-001",
                        "Appointment not found",
                        "The appointment with the provided ID does not exist."));

        // Validate status value
        if (!List.of("PENDING", "IN_PROGRESS", "FINISHED").contains(newStatus)) {
            throw new ApiException("APPT-002",
                    "Invalid status",
                    "Status must be one of: PENDING, IN_PROGRESS, FINISHED.");
        }

        appointment.setStatus(newStatus);
        Appointment updated = appointmentRepository.save(appointment);
        return toResponseDTO(updated);
    }

    /**
     * Admin assigns mechanic to appointment
     */
    public AppointmentResponseDTO assignMechanicByAdmin(Long adminUserId, Long appointmentId, Long mechanicId) {
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
        Appointment updated = appointmentRepository.save(appointment);
        return toResponseDTO(updated);
    }

    /**
     * Mechanic claims appointment
     */
    public AppointmentResponseDTO claimAppointment(Long mechanicUserId, Long appointmentId) {
        User mechanic = userRepository.findById(mechanicUserId)
                .orElseThrow(() -> new ApiException("USER-001",
                        "User not found",
                        "The user with the provided ID does not exist."));

        if (!"MECHANIC".equals(mechanic.getRole())) {
            throw new ApiException("AUTH-005",
                    "Unauthorized action",
                    "Only mechanics can claim appointments.");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiException("APPT-001",
                        "Appointment not found",
                        "The appointment with the provided ID does not exist."));

        if (appointment.getMechanic() != null) {
            throw new ApiException("APPT-003",
                    "Appointment already assigned",
                    "This appointment has already been assigned to a mechanic.");
        }

        appointment.setMechanic(mechanic);
        Appointment updated = appointmentRepository.save(appointment);
        return toResponseDTO(updated);
    }
    
    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        AppointmentResponseDTO.ClientInfo clientInfo = new AppointmentResponseDTO.ClientInfo(
                appointment.getClient().getId(),
                appointment.getClient().getFullName(),
                appointment.getClient().getEmail(),
                appointment.getClient().getPhoneNumber()
        );

        AppointmentResponseDTO.MechanicInfo mechanicInfo = null;
        if (appointment.getMechanic() != null) {
            mechanicInfo = new AppointmentResponseDTO.MechanicInfo(
                    appointment.getMechanic().getId(),
                    appointment.getMechanic().getFullName(),
                    appointment.getMechanic().getMechanicId(),
                    appointment.getMechanic().getPhoneNumber()
            );
        }

        return new AppointmentResponseDTO(
                appointment.getId(),
                clientInfo,
                mechanicInfo,
                appointment.getServiceType(),
                appointment.getVehicleInfo(),
                appointment.getProblemDescription(),
                appointment.getScheduledDate(),
                appointment.getStatus(),
                appointment.getCreatedAt()
        );
    }
}
