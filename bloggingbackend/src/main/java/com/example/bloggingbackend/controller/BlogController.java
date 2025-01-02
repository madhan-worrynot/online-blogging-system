package com.example.bloggingbackend.controller;

import com.example.bloggingbackend.model.Blog;
import com.example.bloggingbackend.service.BlogService;
import com.example.bloggingbackend.util.JwtUtil;
import com.example.bloggingbackend.exception.UserNotFoundException;
import com.example.bloggingbackend.exception.BlogNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "http://127.0.0.1:5500")  // Allow CORS for the frontend
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private JwtUtil jwtUtil;  // Inject JwtUtil

    // Endpoint for creating a blog
    @PostMapping("/create")
    public ResponseEntity<String> createBlog(@RequestBody Blog blog, @RequestHeader("Authorization") String token) {
        try {
            String username = extractUsernameFromToken(token);
            String response = blogService.createBlog(blog, username);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // Return status 201
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found: " + e.getMessage(), HttpStatus.UNAUTHORIZED); // Return 401 if user not found
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint for publishing a blog
    @PostMapping("/publish/{id}")
    public ResponseEntity<String> publishBlog(@PathVariable Long id) {
        try {
            String response = blogService.publishBlog(id);
            return new ResponseEntity<>(response, HttpStatus.OK); // Return status 200
        } catch (BlogNotFoundException e) {
            return new ResponseEntity<>("Blog not found: " + e.getMessage(), HttpStatus.NOT_FOUND); // Return 404 if blog not found
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Extract the username from JWT token
    private String extractUsernameFromToken(String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return jwtUtil.extractUsername(jwtToken); // Extract username using JwtUtil
    }

    // Endpoint for searching blogs by keyword
    @GetMapping("/search")
    public List<Blog> searchBlogs(@RequestParam String keyword) {
        return blogService.searchBlogs(keyword);
    }

    // New endpoint to get all blogs
    @GetMapping("/")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlogs(); // Call service to get all blogs
        return new ResponseEntity<>(blogs, HttpStatus.OK); // Return blogs with status 200
    }
}
