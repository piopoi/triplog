package com.triplog.api.post.dto;

import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_CONTENT_EMPTY;
import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_TITLE_EMPTY;
import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_TITLE_LENGTH;
import static com.triplog.api.post.domain.Post.POST_TITLE_LENGTH_MAX;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    @NotBlank(message = MESSAGE_POST_TITLE_EMPTY)
    @Size(max = POST_TITLE_LENGTH_MAX, message = MESSAGE_POST_TITLE_LENGTH)
    private final String title;

    @NotBlank(message = MESSAGE_POST_CONTENT_EMPTY)
    private final String content;

    @Builder
    public PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
