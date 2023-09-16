package com.triplog.api.post.dto;

import com.triplog.api.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostGetResponseDTO {

    private final Long id;
    private final String title;
    private final String content;
    private final Long userId;

    public static PostGetResponseDTO from(Post post) {
        return PostGetResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUser().getId())
                .build();
    }
}
