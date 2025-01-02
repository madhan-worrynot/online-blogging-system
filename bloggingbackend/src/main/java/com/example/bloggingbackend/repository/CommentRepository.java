package com.example.bloggingbackend.repository;

import com.example.bloggingbackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Find all comments by user ID
    List<Comment> findByAuthorId(Long userId);

    // Delete all comments by blog ID
    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.blog.id = :blogId")
    void deleteByBlogId(Long blogId);

    // Find all comments by blog ID
    List<Comment> findByBlogId(Long blogId);
}
