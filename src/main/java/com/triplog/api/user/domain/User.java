package com.triplog.api.user.domain;

import com.triplog.api.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Email
    private String email;

    @Column(nullable = false)
    @Size(min = USER_PASSWORD_LENGTH_MIN)
    private String password;

    @Column(nullable = false, length = 10)
    private String role;

    public User() {
    }

    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.USER.getValue();
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
