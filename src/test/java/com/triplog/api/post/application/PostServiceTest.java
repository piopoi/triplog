package com.triplog.api.post.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.triplog.api.BaseTest;
import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostGetResponseDTO;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.application.UserService;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostServiceTest extends BaseTest {

    private final String title = "foo";
    private final String content = "bar";

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private Post post;
    private User user;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = userService.createUser(new UserCreateRequestDTO("test@test.com", "12345678"));
        user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        post = postRepository.save(new Post(title, content, user));
    }

    @Test
    @DisplayName("게시글을 생성할 수 있다.")
    void createPost() {
        //then
        Post findPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(title);
        assertThat(findPost.getContent()).isEqualTo(content);
        assertThat(findPost.getUser().getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("id로 게시글을 조회할 수 있다.")
    void getPost() {
        //when
        PostGetResponseDTO postGetResponseDTO = postService.getPost(post.getId());

        //then
        assertThat(postGetResponseDTO).isNotNull();
        assertThat(postGetResponseDTO.getId()).isEqualTo(post.getId());
        assertThat(postGetResponseDTO.getTitle()).isEqualTo(post.getTitle());
        assertThat(postGetResponseDTO.getContent()).isEqualTo(post.getContent());
        assertThat(postGetResponseDTO.getUserId()).isEqualTo(post.getUser().getId());
    }

    @Test
    @DisplayName("잘못된 id로 게시글을 조회할 수 없다.")
    void getPost_invalidId() {
        //when then
        assertThatThrownBy(() -> postService.getPost(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
