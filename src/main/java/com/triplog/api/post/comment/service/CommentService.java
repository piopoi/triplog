package com.triplog.api.post.comment.service;

import com.triplog.api.post.comment.domain.Comment;
import com.triplog.api.post.comment.dto.CommentCreateRequestDTO;
import com.triplog.api.post.comment.repository.CommentRepository;
import com.triplog.api.post.domain.Post;
import com.triplog.api.post.service.PostService;
import com.triplog.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public Long createComment(CommentCreateRequestDTO commentCreateRequestDTO, Long postId, User user) {
        Post post = postService.findPostById(postId);
        Comment comment = Comment.of(commentCreateRequestDTO.getContent(), post, user);
        return commentRepository.save(comment).getId();
    }
}
