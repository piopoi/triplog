package com.triplog.api.post.domain;


import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PostTest {

    private final String title = "foo";
    private final String content = "bar";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@test.com", "12345678");
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    @DisplayName("게시글을 생성할 수 있다.")
    void createPost() {
        Post post = new Post(title, content, user);

        assertThat(post).isNotNull();
    }
}
