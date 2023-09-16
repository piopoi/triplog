package com.triplog.api.post.dto;

import static com.triplog.api.post.constants.PostConstants.MESSAGE_POST_TITLE_LENGTH;
import static com.triplog.api.post.domain.Post.POST_TITLE_LENGTH_MAX;

import com.triplog.api.post.domain.Post;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateRequestDTO {

    @Size(max = POST_TITLE_LENGTH_MAX, message = MESSAGE_POST_TITLE_LENGTH)
    private final String title;

    private final String content;

    public Post toEntity() {
        return Post.of(title, content);
    }
}
