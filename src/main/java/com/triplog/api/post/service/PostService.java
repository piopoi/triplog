package com.triplog.api.post.service;

import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_NOT_EXISTS;

import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.dto.PostGetResponseDTO;
import com.triplog.api.post.dto.PostUpdateRequestDTO;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(PostCreateRequestDTO postCreateRequestDTO, User user) {
        Post post = Post.of(postCreateRequestDTO, user);
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Transactional(readOnly = true)
    public PostGetResponseDTO getPost(Long postId) {
        Post post = findPostById(postId);
        return PostGetResponseDTO.from(post);
    }

    @Transactional(readOnly = true)
    public List<PostGetResponseDTO> getAllPosts(Pageable pageable) {
        List<Post> posts = postRepository.findAll(pageable).getContent();
        return posts.stream()
                .map(PostGetResponseDTO::from)
                .toList();
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDTO postUpdateRequestDTO) {
        Post post = findPostById(postId);
        post.update(postUpdateRequestDTO);
    }

    @Transactional(readOnly = true)
    public boolean isPostAuthor(Long postId, User user) {
        Post post = findPostById(postId);
        return post.getUser().equals(user);
    }

    @Transactional
    public void deletePost(Long postId) {
        findPostById(postId);
        postRepository.deleteById(postId);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_POST_NOT_EXISTS));
    }
}