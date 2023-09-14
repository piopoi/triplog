package com.triplog.api.post.dto;

import static com.triplog.api.post.constants.PostConstants.*;

import com.triplog.api.post.domain.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDTO {

    @NotBlank(message = MESSAGE_COMMENT_CONTENT_EMPTY)
    @Size(max = Comment.COMMENT_CONTENT_LENGTH_MAX, message = MESSAGE_COMMENT_CONTENT_LENGTH)
    private String content;

    @Builder
    public CommentCreateRequestDTO(String content) {
        this.content = content;
    }
}
