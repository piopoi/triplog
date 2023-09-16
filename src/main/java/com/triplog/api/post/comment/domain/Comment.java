package com.triplog.api.post.comment.domain;

import com.triplog.api.BaseEntity;
import com.triplog.api.post.domain.Post;
import com.triplog.api.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Slf4j
@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Comment extends BaseEntity {

    public static final int COMMENT_CONTENT_LENGTH_MAX = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = COMMENT_CONTENT_LENGTH_MAX)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    protected Comment() {
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        this.user = user;
    }

    public static Comment of(String content, Post post, User user) {
        return Comment.builder()
                .content(content)
                .post(post)
                .user(user)
                .build();
    }
}
