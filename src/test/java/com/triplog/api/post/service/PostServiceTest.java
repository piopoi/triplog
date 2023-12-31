package com.triplog.api.post.service;

import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_NOT_EXISTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.triplog.api.BaseTest;
import com.triplog.api.auth.domain.UserAdapter;
import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostGetResponseDTO;
import com.triplog.api.post.dto.PostUpdateRequestDTO;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.Role;
import com.triplog.api.user.domain.User;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

class PostServiceTest extends BaseTest {

    private final String title = "foo";
    private final String content = "bar";

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        user = createUser("test@test.com", "12345678", Role.USER);
        post = createPost(title, content, user);
    }

    @Test
    @DisplayName("게시글을 생성할 수 있다.")
    void createPost() {
        //then
        Post findPost = findPostById(post.getId());
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo(title);
        assertThat(findPost.getContent()).isEqualTo(content);
        assertThat(findPost.getUser().getId()).isEqualTo(user.getId());
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

    @Test
    @DisplayName("모든 게시글을 조회할 수 있다.")
    void getAllPosts() {
        //given
        Pageable pageable = Pageable.ofSize(5);
        IntStream.range(1, 10)
                .forEach(i -> createPost(title + i, content + i, user));

        //when
        List<PostGetResponseDTO> postGetResponseDTOs = postService.getAllPosts(pageable);

        //then
        assertThat(postGetResponseDTOs).hasSize(5);
    }

    @Test
    @DisplayName("게시글의 제목과 본문을 수정할 수 있다.")
    void updatePost() {
        //given
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.builder()
                .title(title + "1")
                .content(content + "1")
                .build();

        //when
        postService.updatePost(post.getId(), postUpdateRequestDTO);

        //then
        Post updatedPost = findPostById(post.getId());
        assertThat(updatedPost.getTitle()).isEqualTo(title + "1");
        assertThat(updatedPost.getContent()).isEqualTo(content + "1");
    }

    @Test
    @DisplayName("잘못된 id로 게시글을 수정할 수 없다.")
    void updatePost_invalidId() {
        //given
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.builder()
                .title(title + "1")
                .content(content + "1")
                .build();

        //when then
        assertThatThrownBy(() -> postService.updatePost(99L, postUpdateRequestDTO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게시글의 제목만 수정할 수 있다.")
    void updatePost_onlyTitle() {
        //given
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.builder()
                .title(title + "1")
                .build();

        //when
        postService.updatePost(post.getId(), postUpdateRequestDTO);

        //then
        Post updatedPost = findPostById(post.getId());
        assertThat(updatedPost.getTitle()).isEqualTo(title + "1");
        assertThat(updatedPost.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("게시글의 본문만 수정할 수 있다.")
    void updatePost_onlyContent() {
        //given
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.builder()
                .content(content + "1")
                .build();

        //when
        postService.updatePost(post.getId(), postUpdateRequestDTO);

        //then
        Post updatedPost = findPostById(post.getId());
        assertThat(updatedPost.getTitle()).isEqualTo(title);
        assertThat(updatedPost.getContent()).isEqualTo(content + "1");
    }

    @Test
    @DisplayName("글 작성자 여부를 확인할 수 있다.")
    void isPostAuthor() {
        //when
        boolean isAuthor = postService.hasAuthManagePost(UserAdapter.from(user), post.getId());

        //then
        assertThat(isAuthor).isTrue();
    }

    @Test
    @DisplayName("글 작성자가 아님을 확인할 수 있다.")
    void isPostAuthor_invalid() {
        //given
        User fakeUser = createUser("fake@test.com", "123123123", Role.USER);

        //when
        boolean isAuthor = postService.hasAuthManagePost(UserAdapter.from(fakeUser), post.getId());

        //then
        assertThat(isAuthor).isFalse();
    }

    @Test
    @DisplayName("잘못된 id로 글 작성자 여부를 확인할 수 없다.")
    void isPostAuthor_invalidId() {
        //when then
        assertThatThrownBy(() -> postService.hasAuthManagePost(UserAdapter.from(user), 99L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게시글을 삭제할 수 있다.")
    void deletePost() {
        //when
        postService.deletePost(post.getId());

        //then
        assertThat(postRepository.count()).isZero();
    }

    @Test
    @DisplayName("잘못된 id로 게시글을 삭제할 수 없다.")
    void deletePost_invalidId() {
        //when then
        assertThatThrownBy(() -> postService.deletePost(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_POST_NOT_EXISTS));
    }
}
