package com.triplog.api.user.domain;

import com.triplog.api.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class User extends BaseEntity {

    public static final int USER_PASSWORD_LENGTH_MIN = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private Role role;

    protected User() {
    }

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User of(String email, String password, String roleName, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(password);
        Role role = Role.valueOf(roleName);
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .role(role)
                .build();
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean isAdmin() {
        return Objects.equals(this.role, Role.ADMIN);
    }

    public boolean isSameUser(Long userId) {
        return Objects.equals(this.id, userId);
    }
}
