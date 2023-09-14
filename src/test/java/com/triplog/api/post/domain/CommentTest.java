package com.triplog.api.post.domain;

import com.triplog.api.BaseTest;
import com.triplog.api.post.dto.CommentCreateRequestDTO;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.repository.UserRepository;
import com.triplog.api.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentTest extends BaseTest {

    private final String content = "foo";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        Long userId = userService.createUser(UserCreateRequestDTO.of("test@test.com", "12345678"));
        user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);

        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of("title", "content");
        post = postRepository.save(Post.of(postCreateRequestDTO, user));
    }

    @Test
    @DisplayName("댓글을 생성할 수 있다.")
    void createComment() {
        //given
        CommentCreateRequestDTO commentCreateRequestDTO = new CommentCreateRequestDTO(content);

        //when
        Comment comment = Comment.of(commentCreateRequestDTO, post, user);

        //then
        Assertions.assertThat(comment).isNotNull();
    }
}
