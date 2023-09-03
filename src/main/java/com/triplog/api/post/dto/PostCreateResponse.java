package com.triplog.api.post.dto;

import com.triplog.api.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Long userId;

    @Builder
    public PostCreateResponse(Long id, String title, String content, Long userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public static PostCreateResponse of(Post post) {
        return PostCreateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUser().getId())
                .build();
    }
}
