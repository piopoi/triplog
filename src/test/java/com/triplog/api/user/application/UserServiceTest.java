package com.triplog.api.user.application;

import static org.assertj.core.api.Assertions.*;

import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserRequest;
import com.triplog.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

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
    @DisplayName("사용자 생성")
    void createUser() {
        //given
        UserRequest userRequest = UserRequest.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        //when
        User user = userService.createUser(userRequest);

        //then
        User findUser = userRepository.findAll().get(0);
        assertThat(findUser).isNotNull();
        assertThat(findUser.getId()).isEqualTo(user.getId());
    }
}