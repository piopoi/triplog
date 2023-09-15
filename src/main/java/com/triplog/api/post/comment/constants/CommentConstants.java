package com.triplog.api.post.comment.constants;

import static com.triplog.api.post.comment.domain.Comment.COMMENT_CONTENT_LENGTH_MAX;

public class CommentConstants {

    public static final String MESSAGE_COMMENT_CONTENT_EMPTY = "댓글을 입력해주세요.";
    public static final String MESSAGE_COMMENT_CONTENT_LENGTH = "댓글은 " + COMMENT_CONTENT_LENGTH_MAX + "자를 초과할 수 없습니다.";
    public static final String MESSAGE_COMMENT_NOT_EXISTS = "존재하지 않는 댓글입니다.";
}
