package com.triplog.api.post.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.BaseTest;
import com.triplog.api.post.domain.Post;
import com.triplog.api.user.domain.Role;
import com.triplog.api.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest extends BaseTest {

    private final String content = "test";

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = createUser("test@test.com", "12345678", Role.USER);
        post = createPost("foo", "bar", user);
    }

    @Test
    @DisplayName("댓글을 생성할 수 있다.")
    void createComment() {
        //when
        Comment comment = Comment.of(content, post, user);

        //then
        assertThat(comment).isNotNull();
    }
}
