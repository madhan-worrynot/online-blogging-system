window.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    const createBlogLink = document.getElementById('createBlogLink');
  
    // Show or hide "Create Blog" link based on authentication status
    if (!token) {
      createBlogLink.style.display = 'none';
    } else {
      createBlogLink.style.display = 'inline';
    }
  
    // Fetch and display blogs
    fetchBlogs(token);
  });


  window.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    const loginLink = document.getElementById('loginLink');
    const signupLink = document.getElementById('signupLink');
    const createBlogLink = document.getElementById('createBlogLink');
    const logoutLink = document.getElementById('logoutLink');

    if (token) {
        // Hide Login and Sign Up links
        loginLink.style.display = 'none';
        signupLink.style.display = 'none';

        // Show Create Blog and Logout links
        createBlogLink.style.display = 'inline';
        logoutLink.style.display = 'inline';
    } else {
        // Show Login and Sign Up links
        loginLink.style.display = 'inline';
        signupLink.style.display = 'inline';

        // Hide Create Blog and Logout links
        createBlogLink.style.display = 'none';
        logoutLink.style.display = 'none';
    }
});

// Logout function
function logout() {
    // Clear the JWT token from localStorage
    localStorage.removeItem('jwtToken');

    // Alert user and redirect to the homepage
    alert('You have successfully logged out.');
    window.location.href = 'index.html';
}


  
  // function fetchBlogs(token) {
  //   fetch('http://localhost:8080/api/blogs/', {
  //     method: 'GET',
  //     headers: {
  //       Authorization: `Bearer ${token}`,
  //     },
  //   })
  //     .then((response) => response.json())
  //     .then((data) => {
  //       const blogList = document.getElementById('blog-list');
  //       if (data.length > 0) {
  //         data.forEach((post) => {
  //           const blogPostElement = document.createElement('div');
  //           blogPostElement.classList.add('blog-post');
  //           blogPostElement.innerHTML = `
  //             <h2>${post.title}</h2>
  //             <p>${post.content}</p>
  //             <button class="comment-btn">Comment</button>
  //           `;
  //           blogList.appendChild(blogPostElement);
  //         });
  //       } else {
  //         blogList.innerHTML = '<p>No blog posts available.</p>';
  //       }
  //     })
  //     .catch((error) => console.error('Error fetching blog posts:', error));
  // }
  
  window.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        alert('You are not logged in. Please log in first.');
        window.location.href = 'login.html';
        return;
    }

    // Fetch and display blog posts
    fetch('http://localhost:8080/api/blogs/', {
        method: 'GET',
        headers: { 'Authorization': `Bearer ${token}` }
    })
        .then(response => response.json())
        .then(data => {
            const blogList = document.getElementById('blog-list');
            data.forEach(post => {
                const blogPostElement = document.createElement('div');
                blogPostElement.classList.add('blog-post');
                blogPostElement.innerHTML = `
                    <h2>${post.title}</h2>
                    <p>${post.content}</p>
                    <div class="comments-section" data-blog-id="${post.id}">
                        <h3>Comments</h3>
                        <div class="comments-list"></div>
                        <textarea class="comment-input" placeholder="Write a comment..."></textarea>
                        <button class="submit-comment-btn">Add Comment</button>
                    </div>
                `;
                blogList.appendChild(blogPostElement);

                // Fetch and display comments for the blog post
                fetchComments(post.id, blogPostElement.querySelector('.comments-list'));
            });
        })
        .catch(error => console.error('Error fetching blog posts:', error));

    // Event delegation for adding comments
    document.addEventListener('click', function (e) {
        if (e.target && e.target.classList.contains('submit-comment-btn')) {
            const commentSection = e.target.closest('.comments-section');
            const blogId = commentSection.dataset.blogId;
            const commentContent = commentSection.querySelector('.comment-input').value;

            if (!commentContent.trim()) {
                alert('Comment content cannot be empty.');
                return;
            }

            // Add a new comment
            fetch(`http://localhost:8080/api/comments/add/${blogId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ content: commentContent }),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to add comment');
                    }
                    return response.text();
                })
                .then(() => {
                    alert('Comment added successfully!');
                    // Refresh comments
                    fetchComments(blogId, commentSection.querySelector('.comments-list'));
                    commentSection.querySelector('.comment-input').value = '';
                })
                .catch(error => {
                    console.error('Error adding comment:', error);
                    alert('Failed to add comment. Please try again.');
                });
        }
    });

    // Fetch comments for a blog post
    function fetchComments(blogId, commentsListElement) {
        fetch(`http://localhost:8080/api/comments/get/${blogId}`, { method: 'GET' })
            .then(response => response.json())
            .then(comments => {
                commentsListElement.innerHTML = '';
                comments.forEach(comment => {
                    const commentElement = document.createElement('div');
                    commentElement.classList.add('comment');
                    commentElement.textContent = `${comment.author.username}: ${comment.content}`;
                    commentsListElement.appendChild(commentElement);
                });
            })
            .catch(error => console.error(`Error fetching comments for blog ${blogId}:`, error));
    }
});


window.onload = function () {
    const blogList = document.getElementById('blog-list');
    blogList.classList.add('animate__fadeInUp');
};



// Get the search button and input elements
const searchButton = document.getElementById('searchButton');
const searchInput = document.getElementById('searchInput');

// Add event listener for the search button click
searchButton.addEventListener('click', function () {
    const keyword = searchInput.value.trim();

    if (keyword === "") {
        alert("Please enter a keyword to search.");
        return;
    }

    // Fetch blogs based on the search keyword
    fetch(`http://localhost:8080/api/blogs/search?keyword=${keyword}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data && data.length > 0) {
            displayBlogs(data); // Function to display the blogs
        } else {
            alert('No blogs found for this keyword.');
        }
    })
    .catch(error => {
        console.error('Error searching blogs:', error);
        alert('Error searching blogs. Please try again.');
    });
});

// // Function to display the blogs (search results)
// function displayBlogs(blogs) {
//     const blogList = document.getElementById('blog-list');
//     blogList.innerHTML = ''; // Clear the current blog list

//     blogs.forEach(post => {
//         const blogPostElement = document.createElement('div');
//         blogPostElement.classList.add('blog-post');
//         blogPostElement.innerHTML = `
//             <h2>${post.title}</h2>
//             <p>${post.content}</p>
//             <div class="comments-section" data-blog-id="${post.id}">
//                 <h3>Comments</h3>
//                 <div class="comments-list"></div>
//                 <textarea class="comment-input" placeholder="Write a comment..."></textarea>
//                 <button class="submit-comment-btn">Add Comment</button>
//             </div>
//         `;
//         blogList.appendChild(blogPostElement);

//         // Fetch and display comments for the blog post
//         fetchComments(post.id, blogPostElement.querySelector('.comments-list'));
//     });
// }

// Function to display the blogs (search results)
function displayBlogs(blogs) {
    const blogList = document.getElementById('blog-list');
    blogList.innerHTML = ''; // Clear the current blog list

    blogs.forEach(post => {
        const blogPostElement = document.createElement('div');
        blogPostElement.classList.add('blog-post');
        blogPostElement.innerHTML = `
            <button class="close-btn">&times;</button> <!-- Close button -->
            <h2>${post.title}</h2>
            <p>${post.content}</p>
            <div class="comments-section" data-blog-id="${post.id}">
                <h3>Comments</h3>
                <div class="comments-list"></div>
                <textarea class="comment-input" placeholder="Write a comment..."></textarea>
                <button class="submit-comment-btn">Add Comment</button>
            </div>
        `;
       
        blogList.appendChild(blogPostElement);

        

        // Add functionality to close the blog post
        const closeButton = blogPostElement.querySelector('.close-btn');
        closeButton.addEventListener('click', function () {
            blogPostElement.remove();
            location.reload();  // Remove the blog post element
        });
        

       

        // Fetch and display comments for the blog post
        fetchComments(post.id, blogPostElement.querySelector('.comments-list'));
    });
}




