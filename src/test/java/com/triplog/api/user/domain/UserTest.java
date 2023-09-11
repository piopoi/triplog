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

    private User user;

    @BeforeEach
    void setUp() {
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of(email, password);
        user = User.of(userCreateRequestDTO, passwordEncoder);
    }

    @Test
    @DisplayName("사용자를 생성할 수 있다.")
    void createUser() {
        //then
        assertThat(user).isNotNull();
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("사용자 비밀번호를 수정 할 수 있다.")
    void updatePassword() {
        //given
        String newPassword = password + "#";

        //when
        user.updatePassword(passwordEncoder, newPassword);

        //then
        boolean result = passwordEncoder.matches(newPassword, user.getPassword());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("관리자 여부를 알 수 있다.")
    void isAdmin() {
        //given
        UserCreateRequestDTO adminCreateRequestDTO = UserCreateRequestDTO.of(email, password, Role.ADMIN.name());
        User admin = User.of(adminCreateRequestDTO, passwordEncoder);

        //when
        boolean result = admin.isAdmin();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("같은 사용자인지 알 수 있다.")
    void isSameUser_true() {
        //when
        boolean result = user.isSameUser(user.getId());

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("같은 사용자인지 알 수 있다.")
    void isSameUser_false() {
        //when
        boolean result = user.isSameUser(99L);

        //then
        assertThat(result).isFalse();
    }
}
