//package com.example.bloggingbackend.service;
//
//import com.example.bloggingbackend.exception.ResourceNotFoundException;
//import com.example.bloggingbackend.model.Blog;
//import com.example.bloggingbackend.model.Comment;
//import com.example.bloggingbackend.model.User;
//import com.example.bloggingbackend.repository.BlogRepository;
//import com.example.bloggingbackend.repository.CommentRepository;
//import com.example.bloggingbackend.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private BlogRepository blogRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Transactional
//    public String addComment(Long blogId, Comment comment, String username) {
//        // Validate input
//        if (blogId == null) {
//            throw new IllegalArgumentException("Blog ID cannot be null");
//        }
//        if (username == null || username.trim().isEmpty()) {
//            throw new IllegalArgumentException("Username cannot be null or empty");
//        }
//        if (comment == null || comment.getContent() == null || comment.getContent().trim().isEmpty()) {
//            throw new IllegalArgumentException("Comment content cannot be null or empty");
//        }
//
//        // Fetch the blog and user
//        Blog blog = getBlogById(blogId);
//        User user = getUserByUsername(username);
//
//        // Set the author and blog for the comment
//        comment.setAuthor(user);
//        comment.setBlog(blog);
//
//        // Save the comment
//        commentRepository.save(comment);
//
//        return "Comment added successfully!";
//    }
//
//    // Helper method to fetch a blog by its ID
//    private Blog getBlogById(Long blogId) {
//        return blogRepository.findById(blogId)
//                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + blogId + " not found"));
//    }
//
//    // Helper method to fetch a user by username
//    private User getUserByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
//    }
//
//    public List<Comment> getCommentsForBlog(Long blogId) {
//        return commentRepository.findByBlogId(blogId); // Assuming the repository method is set up to find comments by blogId
//    }
//}


package com.example.bloggingbackend.service;

import com.example.bloggingbackend.exception.ResourceNotFoundException;
import com.example.bloggingbackend.model.Blog;
import com.example.bloggingbackend.model.Comment;
import com.example.bloggingbackend.model.User;
import com.example.bloggingbackend.repository.BlogRepository;
import com.example.bloggingbackend.repository.CommentRepository;
import com.example.bloggingbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String addComment(Long blogId, Comment comment, String username) {
        // Validate input
        if (blogId == null) {
            throw new IllegalArgumentException("Blog ID cannot be null");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (comment == null || comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be null or empty");
        }

        // Fetch the blog and user
        Blog blog = getBlogById(blogId);
        User user = getUserByUsername(username);

        // Set the author and blog for the comment
        comment.setAuthor(user);
        comment.setBlog(blog);

        // Save the comment
        commentRepository.save(comment);

        return "Comment added successfully!";
    }

    // Helper method to fetch a blog by its ID
    private Blog getBlogById(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + blogId + " not found"));
    }

    // Helper method to fetch a user by username
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
    }

    public List<Comment> getCommentsForBlog(Long blogId) {
        return commentRepository.findByBlogId(blogId); // Assuming the repository method is set up to find comments by blogId
    }
}
