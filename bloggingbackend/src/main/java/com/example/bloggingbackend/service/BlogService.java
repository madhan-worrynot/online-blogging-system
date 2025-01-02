package com.example.bloggingbackend.service;

import com.example.bloggingbackend.model.Blog;
import com.example.bloggingbackend.model.User;
import com.example.bloggingbackend.repository.BlogRepository;
import com.example.bloggingbackend.repository.UserRepository;
import com.example.bloggingbackend.exception.UserNotFoundException;
import com.example.bloggingbackend.exception.BlogNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new blog
    @Transactional
    public String createBlog(Blog blog, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with username: " + username);
        }

        blog.setAuthor(user.get());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setPublished(false);  // Initially set as not published
        blogRepository.save(blog);
        return "Blog created successfully!";
    }

    // Publish a blog
    @Transactional
    public String publishBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with ID: " + blogId));

        if (blog.isPublished()) {
            return "Blog is already published!";
        }

        blog.setPublished(true);
        blogRepository.save(blog);
        return "Blog published successfully!";
    }

    // Search blogs by title or content keyword
    public List<Blog> searchBlogs(String keyword) {
        return blogRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    // Get all blogs
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll(); // Fetch all blogs from the repository
    }
}
