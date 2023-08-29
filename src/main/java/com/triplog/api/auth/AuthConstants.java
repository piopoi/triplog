package com.triplog.api.auth;

public class AuthConstants {

    public static final String AUTH_JWT_TOKEN_UNPRIVILEGED = "권한 정보가 없는 토큰입니다.";
    public static final String AUTH_JWT_TOKEN_INVALID = "Invalid JWT Token";
    public static final String AUTH_JWT_TOKEN_EXPIRED = "Expired JWT Token";
    public static final String AUTH_JWT_TOKEN_UNSUPPORTED = "Unsupported JWT Token";
    public static final String AUTH_JWT_CLAIMS_EMPTY = "JWT claims string is empty.";

    public static final String AUTH_EMAIL_EMPTY = "이메일을 입력해주세요.";
    public static final String AUTH_EMAIL_INVALID = "이메일 주소 형식에 맞게 입력해주세요.";
    public static final String AUTH_PASSWORD_EMPTY = "비밀번호를 입력해주세요.";
    public static final String AUTH_PASSWORD_LENGTH_MIN = "8자 이상의 비밀번호를 사용해주세요.";
    public static final String AUTH_USER_NOT_EXISTS = "존재하지 않는 사용자입니다.";
}
