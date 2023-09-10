package com.triplog.api.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.BaseTest;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserTest extends BaseTest {

    private final String email = "test@test.com";
    private final String password = "12345678";

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserCreateRequestDTO userCreateRequestDTO;

    @BeforeEach
    void setUp() {
        userCreateRequestDTO = UserCreateRequestDTO.of(email, password);
    }

    @Test
    @DisplayName("사용자를 생성할 수 있다.")
    void createUser() {
        //when
        User user = User.from(userCreateRequestDTO);

        //then
        assertThat(user).isNotNull();
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("사용자 비밀번호를 암호화 할 수 있다.")
    void encodePassword() {
        //given
        User user = User.from(userCreateRequestDTO);

        //when
        user.encodePassword(passwordEncoder);

        //then
        boolean result = passwordEncoder.matches(password, user.getPassword());
        assertThat(result).isTrue();
    }
}
