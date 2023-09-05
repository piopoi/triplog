package com.triplog.api.post.dto;

import com.triplog.api.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateResponseDTO {

    private final Long id;
    private final String title;
    private final String content;
    private final Long userId;

    @Builder
    public PostCreateResponseDTO(Long id, String title, String content, Long userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public static PostCreateResponseDTO from(Post post) {
        return PostCreateResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUser().getId())
                .build();
    }
}
