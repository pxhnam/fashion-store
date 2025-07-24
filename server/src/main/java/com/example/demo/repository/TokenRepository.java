package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByToken(String token);

    void deleteByToken(String token);
}
