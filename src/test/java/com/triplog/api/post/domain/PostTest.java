package com.triplog.api.post.domain;


import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.BaseTest;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.dto.PostUpdateRequestDTO;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PostTest extends BaseTest {

    private final String title = "foo";
    private final String content = "bar";

    private Post post;
    private PostCreateRequestDTO postCreateRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of("test@test.com", "12345678");
        user = User.from(userCreateRequestDTO);
        ReflectionTestUtils.setField(user, "id", 1L);

        postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        post = Post.of(postCreateRequestDTO, user);
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
        String updatedTitle = title + "1";
        String updatedContent = content + "1";
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.of(updatedTitle, updatedContent);

        //when
        post.update(postUpdateRequestDTO);

        //then
        assertThat(post.getTitle()).isEqualTo(updatedTitle);
        assertThat(post.getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("게시글의 제목만 수정할 수 있다.")
    void updatePost_onlyTitle() {
        //given
        String updatedTitle = title + "1";
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.of(updatedTitle, "");

        //when
        post.update(postUpdateRequestDTO);

        //then
        assertThat(post.getTitle()).isEqualTo(updatedTitle);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("게시글의 본문만 수정할 수 있다.")
    void updatePost_onlyContent() {
        //given
        String updatedContent = content + "1";
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.of("", updatedContent);

        //when
        post.update(postUpdateRequestDTO);

        //then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(updatedContent);
    }
}
