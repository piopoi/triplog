package com.triplog.api.post.application;

import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.dto.PostCreateResponseDTO;
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

    public PostCreateResponseDTO createPost(PostCreateRequestDTO postCreateRequestDTO, User user) {
        Post post = Post.builder()
                .title(postCreateRequestDTO.getTitle())
                .content(postCreateRequestDTO.getContent())
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);
        return PostCreateResponseDTO.from(savedPost);
    }
}
