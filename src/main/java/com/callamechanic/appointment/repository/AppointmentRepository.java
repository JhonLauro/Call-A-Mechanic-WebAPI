package com.callamechanic.appointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.callamechanic.appointment.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClientId(Long clientId);
    List<Appointment> findByMechanicId(Long mechanicId);
}
