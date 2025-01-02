//package com.example.bloggingbackend.controller;
//
//import com.example.bloggingbackend.model.Comment;
//import com.example.bloggingbackend.model.User;
//import com.example.bloggingbackend.service.CommentService;
//import com.example.bloggingbackend.service.UserService;
//import com.example.bloggingbackend.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@CrossOrigin(origins = "http://127.0.0.1:5500")
//@RestController
//@RequestMapping("/api/comments") // Allowing specific origin; configure CORS globally if needed
//public class CommentController {
//
//    @Autowired
//    private CommentService commentService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    // Endpoint to add a comment to a blog
//    @PostMapping("/add/{blogId}")
//    public ResponseEntity<String> addComment(
//            @PathVariable Long blogId,
//            @RequestBody Comment comment,
//            @RequestHeader("Authorization") String token
//    ) {
//        try {
//            if (token == null || token.isBlank()) {
//                return new ResponseEntity<>("Authorization token is missing.", HttpStatus.UNAUTHORIZED);
//            }
//
//            // Extract the username from the JWT token
//            String username = extractUsernameFromToken(token);
//            if (username == null || username.isBlank()) {
//                return new ResponseEntity<>("Invalid or expired token.", HttpStatus.UNAUTHORIZED);
//            }
//
//            // Retrieve the user based on the username from the token
//            Optional<User> userOptional = userService.findByUsername(username);
//            if (userOptional.isEmpty()) {
//                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
//            }
//
//            User user = userOptional.get();
//
//            if (comment == null || comment.getContent() == null || comment.getContent().isBlank()) {
//                return new ResponseEntity<>("Comment content cannot be empty.", HttpStatus.BAD_REQUEST);
//            }
//
//            // Set the author of the comment
//            comment.setAuthor(user);
//
//            // Call the service to add the comment
//            String response = commentService.addComment(blogId, comment, username);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//
//        } catch (Exception e) {
//            return new ResponseEntity<>("Error adding comment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Helper method to extract the username from the JWT token
//    private String extractUsernameFromToken(String token) {
//        try {
//            // Remove "Bearer " prefix if present
//            if (token.startsWith("Bearer ")) {
//                token = token.substring(7); // Remove "Bearer " part
//            }
//            return jwtUtil.extractUsername(token); // Extract username from the JWT token
//        } catch (Exception e) {
//            return null; // Return null if the token is invalid or malformed
//        }
//    }
//
//    // Endpoint to get all comments for a blog
//    @GetMapping("/get/{blogId}")
//    public ResponseEntity<List<Comment>> getComments(@PathVariable Long blogId) {
//        try {
//            List<Comment> comments = commentService.getCommentsForBlog(blogId);
//            if (comments.isEmpty()) {
//                return ResponseEntity.noContent().build(); // Return 204 No Content if no comments found
//            }
//            return ResponseEntity.ok(comments); // Return 200 OK with the list of comments
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(null); // Return 500 Internal Server Error if there's an issue
//        }
//    }
//}


package com.example.bloggingbackend.controller;

import com.example.bloggingbackend.dto.CommentDTO;
import com.example.bloggingbackend.model.Comment;
import com.example.bloggingbackend.model.User;
import com.example.bloggingbackend.service.CommentService;
import com.example.bloggingbackend.service.UserService;
import com.example.bloggingbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/comments") // Allowing specific origin; configure CORS globally if needed
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint to add a comment to a blog
    @PostMapping("/add/{blogId}")
    public ResponseEntity<String> addComment(
            @PathVariable Long blogId,
            @RequestBody Comment comment,
            @RequestHeader("Authorization") String token
    ) {
        try {
            if (token == null || token.isBlank()) {
                return new ResponseEntity<>("Authorization token is missing.", HttpStatus.UNAUTHORIZED);
            }

            // Extract the username from the JWT token
            String username = extractUsernameFromToken(token);
            if (username == null || username.isBlank()) {
                return new ResponseEntity<>("Invalid or expired token.", HttpStatus.UNAUTHORIZED);
            }

            // Retrieve the user based on the username from the token
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }

            User user = userOptional.get();

            if (comment == null || comment.getContent() == null || comment.getContent().isBlank()) {
                return new ResponseEntity<>("Comment content cannot be empty.", HttpStatus.BAD_REQUEST);
            }

            // Set the author of the comment
            comment.setAuthor(user);

            // Call the service to add the comment
            String response = commentService.addComment(blogId, comment, username);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error adding comment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to extract the username from the JWT token
    private String extractUsernameFromToken(String token) {
        try {
            // Remove "Bearer " prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " part
            }
            return jwtUtil.extractUsername(token); // Extract username from the JWT token
        } catch (Exception e) {
            return null; // Return null if the token is invalid or malformed
        }
    }

    // Endpoint to get all comments for a blog
    @GetMapping("/get/{blogId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long blogId) {
        try {
            List<Comment> comments = commentService.getCommentsForBlog(blogId);
            if (comments.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 No Content if no comments found
            }

            // Convert Comment entities to CommentDTOs
            List<CommentDTO> commentDTOs = comments.stream()
                    .map(comment -> new CommentDTO(
                            comment.getId(),
                            comment.getContent(),
                            comment.getAuthor().getUsername(), // Extract username
                            comment.getCreatedAt()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(commentDTOs); // Return 200 OK with the list of DTOs
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Return 500 Internal Server Error if there's an issue
        }
    }
}
