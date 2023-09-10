package com.triplog.api.user.service;

import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_NOT_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.BaseTest;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.dto.UserGetRequestDTO;
import com.triplog.api.user.dto.UserGetResponseDTO;
import com.triplog.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceTest extends BaseTest {

    private final String email = "test@test.com";
    private final String password = "12345678";

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of(email, password);
        Long userId = userService.createUser(userCreateRequestDTO);
        user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_USER_NOT_EXISTS));
    }

    @Test
    @DisplayName("사용자를 등록할 수 있다.")
    void createUser() {
        //then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("id로 사용자를 조회할 수 있다.")
    void getUserById() {
        //when
        UserGetResponseDTO userGetResponseDTO = userService.getUserById(user.getId());

        //then
        assertThat(userGetResponseDTO).isNotNull();
        assertThat(userGetResponseDTO.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("email로 사용자를 조회할 수 있다.")
    void getUserByEmail() {
        //given
        UserGetRequestDTO userGetRequestDTO = UserGetRequestDTO.from(email);

        //when
        UserGetResponseDTO userGetResponseDTO = userService.getUserByEmail(userGetRequestDTO);

        //then
        assertThat(userGetResponseDTO).isNotNull();
        assertThat(userGetResponseDTO.getId()).isEqualTo(user.getId());
    }
}
