package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity()
@Table(name = "tokens")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Column(nullable = false)
    private String token;

    private String ip;

    private String device;

    private String browser;

    private String location;

    @Column(name = "user_agent")
    private String userAgent;

    @Builder.Default
    private boolean revoked = false;
}
