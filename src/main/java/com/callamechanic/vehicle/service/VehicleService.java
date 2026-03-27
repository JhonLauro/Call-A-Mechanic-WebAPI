package com.callamechanic.vehicle.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.callamechanic.exception.ApiException;
import com.callamechanic.vehicle.dto.VehicleRequestDTO;
import com.callamechanic.vehicle.dto.VehicleResponseDTO;
import com.callamechanic.vehicle.model.Vehicle;
import com.callamechanic.vehicle.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Get all vehicles for authenticated user
     */
    public List<VehicleResponseDTO> getAllVehiclesByUser(Long userId) {
        List<Vehicle> vehicles = vehicleRepository.findByUserId(userId);
        return vehicles.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get specific vehicle by ID with ownership validation
     */
    public VehicleResponseDTO getVehicleById(Long vehicleId, Long userId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiException("VEH-001",
                        "Vehicle not found",
                        "The vehicle with the provided ID does not exist."));

        // Validate ownership
        if (!vehicle.getUserId().equals(userId)) {
            throw new ApiException("VEH-002",
                    "Access denied",
                    "You do not have permission to access this vehicle.");
        }

        return toResponseDTO(vehicle);
    }

    /**
     * Create new vehicle
     */
    public VehicleResponseDTO createVehicle(VehicleRequestDTO dto, Long userId) {
        // Check for duplicate plate number
        Optional<Vehicle> existingVehicle = vehicleRepository.findByPlateNumber(dto.getPlateNumber());
        if (existingVehicle.isPresent()) {
            throw new ApiException("VEH-003",
                    "Duplicate plate number",
                    "A vehicle with this plate number already exists in the system.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(userId);
        vehicle.setMake(dto.getMake());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setColor(dto.getColor());
        vehicle.setType(dto.getType());
        vehicle.setNotes(dto.getNotes());

        Vehicle saved = vehicleRepository.save(vehicle);
        return toResponseDTO(saved);
    }

    /**
     * Update vehicle with ownership validation
     */
    public VehicleResponseDTO updateVehicle(Long vehicleId, VehicleRequestDTO dto, Long userId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiException("VEH-001",
                        "Vehicle not found",
                        "The vehicle with the provided ID does not exist."));

        // Validate ownership
        if (!vehicle.getUserId().equals(userId)) {
            throw new ApiException("VEH-002",
                    "Access denied",
                    "You do not have permission to modify this vehicle.");
        }

        // Check for duplicate plate number (excluding current vehicle)
        Optional<Vehicle> existingVehicle = vehicleRepository.findByPlateNumber(dto.getPlateNumber());
        if (existingVehicle.isPresent() && !existingVehicle.get().getId().equals(vehicleId)) {
            throw new ApiException("VEH-003",
                    "Duplicate plate number",
                    "A vehicle with this plate number already exists in the system.");
        }

        vehicle.setMake(dto.getMake());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setColor(dto.getColor());
        vehicle.setType(dto.getType());
        vehicle.setNotes(dto.getNotes());

        Vehicle updated = vehicleRepository.save(vehicle);
        return toResponseDTO(updated);
    }

    /**
     * Delete vehicle with ownership validation
     */
    public void deleteVehicle(Long vehicleId, Long userId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ApiException("VEH-001",
                        "Vehicle not found",
                        "The vehicle with the provided ID does not exist."));

        // Validate ownership
        if (!vehicle.getUserId().equals(userId)) {
            throw new ApiException("VEH-002",
                    "Access denied",
                    "You do not have permission to delete this vehicle.");
        }

        vehicleRepository.delete(vehicle);
    }

    /**
     * Convert Vehicle entity to VehicleResponseDTO
     */
    private VehicleResponseDTO toResponseDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getUserId(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getPlateNumber(),
                vehicle.getColor(),
                vehicle.getType(),
                vehicle.getNotes(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt()
        );
    }
}
