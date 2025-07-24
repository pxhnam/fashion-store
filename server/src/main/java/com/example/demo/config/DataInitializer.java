package com.example.demo.config;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Role;
import com.example.demo.enums.ERole;
import com.example.demo.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void initializeData() {
        initializeRoles();
    }

    private void initializeRoles() {
        for (ERole role : ERole.values()) {
            if (!roleRepository.existsByName(role)) {
                Role newRole = Role.builder()
                        .name(role)
                        .build();
                roleRepository.save(newRole);
            }
        }
    }
}
