package com.triplog.api.post.domain;

import com.triplog.api.BaseEntity;
import com.triplog.api.user.domain.User;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Post extends BaseEntity {

    public static final int POST_TITLE_LENGTH_MAX = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = POST_TITLE_LENGTH_MAX)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    protected Post() {
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static Post of(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    public static Post of(String title, String content, User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }

    public void update(Post post) {
        updateTitle(post.getTitle());
        updateContent(post.getContent());
    }

    public boolean isWriter(User user) {
        return Objects.equals(this.user, user);
    }

    private void updateTitle(String title) {
        if (!StringUtils.isBlank(title)) {
            this.title = title;
        }
    }

    private void updateContent(String content) {
        if (!StringUtils.isBlank(content)) {
            this.content = content;
        }
    }
}
