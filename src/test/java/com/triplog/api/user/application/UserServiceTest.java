package com.triplog.api.user.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.BaseTest;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceTest extends BaseTest {

    private final String USER_EMAIL = "test@test.com";
    private final String USER_PASSWORD = "12345678";

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자를 등록할 수 있다.")
    void createUser() {
        //given
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        //when
        Long createdUserId = userService.createUser(userCreateRequestDTO);

        //then
        User findUser = userRepository.findAll().get(0);
        assertThat(findUser).isNotNull();
        assertThat(findUser.getId()).isEqualTo(createdUserId);
    }
}
