package com.triplog.api.post.comment.service;

import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_NOT_EXISTS;

import com.triplog.api.post.comment.domain.Comment;
import com.triplog.api.post.domain.Post;
import com.triplog.api.post.comment.dto.CommentCreateRequestDTO;
import com.triplog.api.post.comment.repository.CommentRepository;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long createComment(CommentCreateRequestDTO requestDTO, Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_POST_NOT_EXISTS));
        Comment comment = Comment.of(requestDTO, post, user);
        return commentRepository.save(comment).getId();
    }
}
