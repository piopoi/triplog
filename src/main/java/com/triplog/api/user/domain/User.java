package com.triplog.api.user.domain;

import com.triplog.api.BaseEntity;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(nullable = false, length = 10)
    private String role;

    protected User() {
    }

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.USER.getValue();
    }

    public static User from(UserCreateRequestDTO userCreateRequestDTO) {
        return User.builder()
                .email(userCreateRequestDTO.getEmail())
                .password(userCreateRequestDTO.getPassword())
                .build();
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
