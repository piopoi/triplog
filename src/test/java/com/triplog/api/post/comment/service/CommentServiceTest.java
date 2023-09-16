package com.triplog.api.post.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.triplog.api.BaseTest;
import com.triplog.api.post.comment.domain.Comment;
import com.triplog.api.post.domain.Post;
import com.triplog.api.post.comment.dto.CommentCreateRequestDTO;
import com.triplog.api.post.comment.repository.CommentRepository;
import com.triplog.api.user.domain.Role;
import com.triplog.api.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

class CommentServiceTest extends BaseTest {

    private final String content = "bar";

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        user = createUser("test@test.com", "12345678", Role.ADMIN);
        post = createPost("foo", "bar", user);
    }

    @Test
    @DisplayName("댓글을 작성할 수 있다.")
    @Transactional
    void createComment() {
        //given
        CommentCreateRequestDTO commentCreateRequestDTO = new CommentCreateRequestDTO(content);

        //when
        Long commentId = commentService.createComment(commentCreateRequestDTO, post.getId(), user);

        //then
        Comment actualComment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getContent()).isEqualTo(content);
        assertThat(actualComment.getPost().getId()).isEqualTo(post.getId());
        assertThat(actualComment.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("없는 게시글의 댓글을 작성할 수 없다.")
    void createComment_postNotExists() {
        //given
        CommentCreateRequestDTO commentCreateRequestDTO = new CommentCreateRequestDTO(content);

        //when then
        assertThatThrownBy(() -> commentService.createComment(commentCreateRequestDTO, 99L, user))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("max length를 초과하는 댓글을 작성할 수 없다.")
    void createComment_contentMaxLength() {
        //given
        CommentCreateRequestDTO commentCreateRequestDTO = new CommentCreateRequestDTO("1".repeat(101));

        //when then
        assertThatThrownBy(() -> commentService.createComment(commentCreateRequestDTO, post.getId(), user))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
