package com.triplog.api.post.dto;

import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_TITLE_LENGTH;
import static com.triplog.api.post.domain.Post.POST_TITLE_LENGTH_MAX;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateRequestDTO {

    @Size(max = POST_TITLE_LENGTH_MAX, message = MESSAGE_POST_TITLE_LENGTH)
    private final String title;

    private final String content;

    @Builder(access = AccessLevel.PRIVATE)
    private PostUpdateRequestDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostUpdateRequestDTO of(String title, String content) {
        return PostUpdateRequestDTO.builder()
                .title(title)
                .content(content)
                .build();
    }
}
