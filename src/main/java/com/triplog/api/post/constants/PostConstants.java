package com.triplog.api.post.constants;

import static com.triplog.api.post.domain.Post.POST_TITLE_LENGTH_MAX;

public class PostConstants {

    public static final String MESSAGE_POST_CONTENT_EMPTY = "본문을 입력해주세요.";
    public static final String MESSAGE_POST_NOT_EXISTS = "존재하지 않는 글입니다.";
    public static final String MESSAGE_POST_TITLE_EMPTY = "제목을 입력해주세요.";
    public static final String MESSAGE_POST_TITLE_LENGTH = "제목은 " + POST_TITLE_LENGTH_MAX + "자를 초과할 수 없습니다.";
}
