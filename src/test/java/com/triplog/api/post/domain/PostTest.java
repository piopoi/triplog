package com.triplog.api.post.domain;


import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.BaseTest;
import com.triplog.api.user.domain.Role;
import com.triplog.api.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest extends BaseTest {

    private final String title = "foo";
    private final String content = "bar";

    private Post post;

    @BeforeEach
    void setUp() {
        User user = createUser("test@test.com", "12345678", Role.USER);
        post = createPost(title, content, user);
    }

    @Test
    @DisplayName("게시글을 생성할 수 있다.")
    void createPost() {
        //then
        assertThat(post).isNotNull();
    }

    @Test
    @DisplayName("게시글의 제목과 본문을 수정할 수 있다.")
    void updatePost() {
        //given
        Post newPost = Post.of(title + "1", content + "1");

        //when
        post.update(newPost);

        //then
        assertThat(post.getTitle()).isEqualTo(newPost.getTitle());
        assertThat(post.getContent()).isEqualTo(newPost.getContent());
    }

    @Test
    @DisplayName("게시글의 제목만 수정할 수 있다.")
    void updatePost_onlyTitle() {
        //given
        Post newPost = Post.of(title + "1", null);

        //when
        post.update(newPost);

        //then
        assertThat(post.getTitle()).isEqualTo(newPost.getTitle());
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("게시글의 본문만 수정할 수 있다.")
    void updatePost_onlyContent() {
        //given
        Post newPost = Post.of(null, content + "1");

        //when
        post.update(newPost);

        //then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(newPost.getContent());
    }
}
