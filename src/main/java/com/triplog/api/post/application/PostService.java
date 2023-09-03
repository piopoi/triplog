package com.triplog.api.post.application;

import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostCreateRequest;
import com.triplog.api.post.dto.PostCreateResponse;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostCreateResponse createPost(PostCreateRequest postCreateRequest, User user) {
        Post post = Post.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);
        return PostCreateResponse.of(savedPost);
    }
}
