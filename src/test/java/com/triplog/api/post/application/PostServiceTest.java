package com.triplog.api.post.application;

import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_NOT_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;

import com.triplog.api.post.dto.PostCreateRequest;
import com.triplog.api.post.dto.PostCreateResponse;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.application.UserService;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequest;
import com.triplog.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

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

        userId = userService.createUser(new UserCreateRequest("test@test.com", "12345678"));
        user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_USER_NOT_EXISTS));
    }

    @Test
    @DisplayName("게시글을 생성할 수 있다.")
    void createPost() {
        //given
        PostCreateRequest postCreateRequest = new PostCreateRequest(title, content);

        //when
        PostCreateResponse postCreateResponse = postService.createPost(postCreateRequest, user);

        //then
        assertThat(postCreateResponse).isNotNull();
        assertThat(postCreateResponse.getTitle()).isEqualTo(title);
        assertThat(postCreateResponse.getContent()).isEqualTo(content);
        assertThat(postCreateResponse.getUserId()).isEqualTo(userId);
    }
}
