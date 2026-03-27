package com.callamechanic.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.callamechanic.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMechanicId(String mechanicId);
    Optional<User> findByAdminId(String adminId);
    boolean existsByEmail(String email);
    List<User> findByRoleIn(List<String> roles);
}