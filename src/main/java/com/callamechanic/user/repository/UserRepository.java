package com.callamechanic.user.repository;

import com.callamechanic.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMechanicId(String mechanicId);
    Optional<User> findByAdminId(String adminId);
    boolean existsByEmail(String email);
}