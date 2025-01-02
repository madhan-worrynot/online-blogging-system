package com.example.bloggingbackend.repository;

import com.example.bloggingbackend.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}

