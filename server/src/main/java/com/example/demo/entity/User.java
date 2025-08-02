package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.demo.enums.AuthProvider;
import com.example.demo.enums.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity()
public class User extends BaseEntity {
    private String avatar;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    private String password;

    private String providerId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.LOCAL;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.PENDING;

    private LocalDateTime verifiedAt;

    private String verifyToken;

    private LocalDateTime tokenExp;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens = new ArrayList<>();

    public void setRoles(Role... roles) {
        this.roles = new HashSet<>(Arrays.asList(roles));
    }

    public void addRoles(Role... roles) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.addAll(Arrays.asList(roles));
    }

    public void addAddress(Address addr) {
        addresses.add(addr);
        addr.setUser(this);
    }

    public void removeAddress(Address addr) {
        addresses.remove(addr);
        addr.setUser(null);
    }
}
