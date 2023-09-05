package com.triplog.api.post.application;

import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_NOT_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.BaseTest;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.dto.PostCreateResponseDTO;
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

    private User user;
    private Long userId;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        userId = userService.createUser(new UserCreateRequestDTO("test@test.com", "12345678"));
        user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_USER_NOT_EXISTS));
    }

    @Test
    @DisplayName("게시글을 생성할 수 있다.")
    void createPost() {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.builder()
                .title(title)
                .content(content)
                .build();

        //when
        PostCreateResponseDTO postCreateResponseDTO = postService.createPost(postCreateRequestDTO, user);

        //then
        assertThat(postCreateResponseDTO).isNotNull();
        assertThat(postCreateResponseDTO.getTitle()).isEqualTo(title);
        assertThat(postCreateResponseDTO.getContent()).isEqualTo(content);
        assertThat(postCreateResponseDTO.getUserId()).isEqualTo(userId);
    }
}
