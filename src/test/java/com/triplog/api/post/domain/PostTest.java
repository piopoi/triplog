package com.triplog.api.post.domain;


import static com.triplog.api.post.constants.PostConstants.*;
import static org.assertj.core.api.Assertions.*;

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

    @Test
    @DisplayName("제목 없이 게시글을 작성할 수 없다.")
    void createPost_emptyTitle() {
        assertThatThrownBy(() -> new Post("", content, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MESSAGE_POST_TITLE_EMPTY);
    }

    @Test
    @DisplayName("제목이 50자를 초과할 수 없다.")
    void createPost_invalidTitle() {
        assertThatThrownBy(() -> new Post("a".repeat(51), content, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MESSAGE_POST_TITLE_LENGTH);
    }

    @Test
    @DisplayName("본문 없이 게시글을 작성할 수 없다.")
    void createPost_emptyContent() {
        assertThatThrownBy(() -> new Post(title, "", user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MESSAGE_POST_CONTENT_EMPTY);
    }
}
