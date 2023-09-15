package com.triplog.api.post.comment.controller;

import com.triplog.api.auth.domain.UserAdapter;
import com.triplog.api.post.comment.dto.CommentCreateRequestDTO;
import com.triplog.api.post.comment.service.CommentService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal UserAdapter userAdapter,
                                              @PathVariable Long postId,
                                              @RequestBody @Valid CommentCreateRequestDTO commentCreateRequestDTO) {
        Long commentId = commentService.createComment(commentCreateRequestDTO, postId, userAdapter.getUser());
        URI uri = URI.create("/api/posts/" + postId + "/comments/" + commentId);
        return ResponseEntity.created(uri).build();
    }
}
