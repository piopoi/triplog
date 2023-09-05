package com.triplog.api.post.application;

import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_NOT_EXISTS;

import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.dto.PostGetResponseDTO;
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

    public Long createPost(PostCreateRequestDTO postCreateRequestDTO, User user) {
        Post post = Post.builder()
                .title(postCreateRequestDTO.getTitle())
                .content(postCreateRequestDTO.getContent())
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    public PostGetResponseDTO getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_POST_NOT_EXISTS));
        return PostGetResponseDTO.from(post);
    }
}
