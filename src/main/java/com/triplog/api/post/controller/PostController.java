package com.triplog.api.post.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.triplog.api.auth.domain.UserDetailsImpl;
import com.triplog.api.post.service.PostService;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.dto.PostGetResponseDTO;
import com.triplog.api.post.dto.PostUpdateRequestDTO;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        Long postId = postService.createPost(postCreateRequestDTO, userDetails.getUser());
        return ResponseEntity.created(URI.create("/api/post/" + postId)).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostGetResponseDTO> getPost(@PathVariable Long postId) {
        PostGetResponseDTO postGetResponseDTO = postService.getPost(postId);
        return ResponseEntity.ok(postGetResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<PostGetResponseDTO>> getAllPosts(@PageableDefault(size = 5, direction = DESC, sort = {"id"})
                                                                Pageable pageable) {
        List<PostGetResponseDTO> postGetResponseDTOs = postService.getAllPosts(pageable);
        return ResponseEntity.ok(postGetResponseDTOs);
    }

    @PatchMapping("/{postId}")
    @PreAuthorize("@postService.isPostAuthor(#postId, #userDetails.getUser())")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long postId,
                                           @RequestBody PostUpdateRequestDTO postUpdateRequestDTO) {
        postService.updatePost(postId, postUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("@postService.isPostAuthor(#postId, #userDetails.getUser())")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}
