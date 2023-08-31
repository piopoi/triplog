package com.triplog.api.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserTest {

    private final String email = "test@test.com";
    private final String password = "12345678";
    private final String defaultRole = Role.USER.getValue();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("사용자를 생성할 수 있다.")
    void createUser() {
        //when
        User user = new User(email, password);

        //then
        assertThat(user).isNotNull();
        assertThat(user.getRole()).isEqualTo(defaultRole);
    }

    @Test
    @DisplayName("사용자 비밀번호를 암호화 할 수 있다.")
    void encodePassword() {
        //given
        User user = new User(email, password);

        //when
        user.encodePassword(passwordEncoder);

        //then
        boolean result = passwordEncoder.matches(password, user.getPassword());
        assertThat(result).isTrue();
    }
}
