package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.enums.ERole;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findByName(ERole name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Error: Role " + name + " not found"));
    }
}
