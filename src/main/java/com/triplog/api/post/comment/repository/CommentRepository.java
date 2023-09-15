package com.triplog.api.post.comment.repository;

import com.triplog.api.post.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
