package com.triplog.api.user.constants;

import static com.triplog.api.user.domain.User.*;

public class UserConstants {

    public static final String MESSAGE_USER_EMAIL_EMPTY = "이메일을 입력해주세요.";
    public static final String MESSAGE_USER_EMAIL_INVALID = "이메일 주소 형식에 맞게 입력해주세요.";
    public static final String MESSAGE_USER_PASSWORD_EMPTY = "비밀번호를 입력해주세요.";
    public static final String MESSAGE_USER_PASSWORD_LENGTH_MIN = USER_PASSWORD_LENGTH_MIN + "자 이상의 비밀번호를 사용해주세요.";
    public static final String MESSAGE_USER_ROLE_EMPTY = "사용자 권한이 없습니다.";
    public static final String MESSAGE_USER_NOT_EXISTS = "존재하지 않는 사용자입니다.";
}
