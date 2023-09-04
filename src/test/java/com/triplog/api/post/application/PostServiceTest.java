package com.triplog.api.post.application;

import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_CONTENT_EMPTY;
import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_TITLE_EMPTY;
import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_TITLE_LENGTH;
import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_NOT_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .content(content)
                .build();

        //when
        PostCreateResponse postCreateResponse = postService.createPost(postCreateRequest, user);

        //then
        assertThat(postCreateResponse).isNotNull();
        assertThat(postCreateResponse.getTitle()).isEqualTo(title);
        assertThat(postCreateResponse.getContent()).isEqualTo(content);
        assertThat(postCreateResponse.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("제목 없이 게시글을 작성할 수 없다.")
    void createPost_emptyTitle() {
        //given
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title("")
                .content(content)
                .build();

        //when then
        assertThatThrownBy(() -> postService.createPost(postCreateRequest, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MESSAGE_POST_TITLE_EMPTY);
    }

    @Test
    @DisplayName("제목이 50자를 초과할 수 없다.")
    void createPost_invalidTitle() {
        //given
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title("a".repeat(51)) //length = 51
                .content(content)
                .build();

        //when then
        assertThatThrownBy(() -> postService.createPost(postCreateRequest, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MESSAGE_POST_TITLE_LENGTH);
    }

    @Test
    @DisplayName("본문 없이 게시글을 작성할 수 없다.")
    void createPost_emptyContent() {
        //given
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .content("")
                .build();

        //when then
        assertThatThrownBy(() -> postService.createPost(postCreateRequest, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MESSAGE_POST_CONTENT_EMPTY);
    }
}
