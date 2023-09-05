package com.triplog.api.post.ui;

import com.triplog.api.auth.domain.UserDetailsImpl;
import com.triplog.api.post.application.PostService;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.dto.PostCreateResponseDTO;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestBody @Valid PostCreateRequestDTO postCreateRequestDTO) {
        PostCreateResponseDTO postCreateResponseDTO = postService.createPost(postCreateRequestDTO, userDetails.getUser());
        return ResponseEntity.created(URI.create("/api//post/" + postCreateResponseDTO.getId())).build();
    }
}
