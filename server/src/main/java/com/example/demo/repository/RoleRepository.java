package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Role;
import com.example.demo.enums.ERole;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByName(ERole name);

    Optional<Role> findByName(ERole name);
}
