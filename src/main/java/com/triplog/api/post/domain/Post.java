package com.triplog.api.post.domain;

import static com.triplog.api.post.constants.PostConstants.*;

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
import jakarta.validation.constraints.Size;
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

    @Column(nullable = false)
    @Size(max = POST_TITLE_LENGTH_MAX)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    public Post() {
    }

    @Builder
    public Post(String title, String content, User user) {
        validateTitle(title);
        validateContent(content);

        this.title = title;
        this.content = content;
        this.user = user;
    }

    private void validateTitle(String title) {
        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException(MESSAGE_POST_TITLE_EMPTY);
        }
        if (title.length() > POST_TITLE_LENGTH_MAX) {
            throw new IllegalArgumentException(MESSAGE_POST_TITLE_LENGTH);
        }
    }

    private void validateContent(String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException(MESSAGE_POST_CONTENT_EMPTY);
        }
    }
}
