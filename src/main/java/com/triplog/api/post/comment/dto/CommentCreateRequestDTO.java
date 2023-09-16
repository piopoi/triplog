package com.triplog.api.post.comment.dto;

import static com.triplog.api.post.comment.constants.CommentConstants.MESSAGE_COMMENT_CONTENT_EMPTY;
import static com.triplog.api.post.comment.constants.CommentConstants.MESSAGE_COMMENT_CONTENT_LENGTH;

import com.triplog.api.post.comment.domain.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentCreateRequestDTO {

    @NotBlank(message = MESSAGE_COMMENT_CONTENT_EMPTY)
    @Size(max = Comment.COMMENT_CONTENT_LENGTH_MAX, message = MESSAGE_COMMENT_CONTENT_LENGTH)
    private String content;
}
