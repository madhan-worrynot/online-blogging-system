window.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        alert('You are not logged in. Please log in first.');
        window.location.href = 'login.html';
        return;
    }

    // Fetch and display blog posts
    // fetch('http://localhost:8080/api/blogs/', {
    //     method: 'GET',
    //     headers: { 'Authorization': `Bearer ${token}` }
    // })
    //     .then(response => response.json())
    //     .then(data => {
    //         const blogList = document.getElementById('blog-list');
    //         data.forEach(post => {
    //             const blogPostElement = document.createElement('div');
    //             blogPostElement.classList.add('blog-post');
    //             blogPostElement.innerHTML = `
    //                 <h2>${post.title}</h2>
    //                 <p>${post.content}</p>
    //                 <div class="comments-section" data-blog-id="${post.id}">
    //                     <h3>Comments</h3>
    //                     <div class="comments-list"></div>
    //                     <textarea class="comment-input" placeholder="Write a comment..."></textarea>
    //                     <button class="submit-comment-btn">Add Comment</button>
    //                 </div>
    //             `;
    //             blogList.appendChild(blogPostElement);

    //             // Fetch and display comments for the blog post
    //             fetchComments(post.id, blogPostElement.querySelector('.comments-list'));
    //         });
    //     })
    //     .catch(error => console.error('Error fetching blog posts:', error));




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
    
                // Add event listener for the "Add Comment" button
                const submitButton = blogPostElement.querySelector('.submit-comment-btn');
                if (submitButton) {
                    submitButton.addEventListener('click', function () {
                        const commentInput = blogPostElement.querySelector('.comment-input');
                        const commentText = commentInput.value.trim();
    
                        if (commentText === "") {
                            alert("Comment cannot be empty!");
                            return;
                        }
    
                        // Get the blog ID from the dataset
                        const blogId = blogPostElement.querySelector('.comments-section')?.dataset.blogId;
                        if (blogId) {
                            // Assuming you have a function to handle the comment submission
                            addComment(blogId, commentText);
                        } else {
                            console.error("Blog ID not found!");
                        }
                    });
                }
            });
        })
        .catch(error => console.error('Error fetching blog posts:', error));
    
    function fetchComments(blogId, commentListElement) {
        // Your logic to fetch and display comments
    }
    
    function addComment(blogId, commentText) {
        // Your logic to send the comment to the server
        console.log(`Adding comment to blog ${blogId}: ${commentText}`);
    }
    
    









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
                        return response.text().then(text => { throw new Error(text); });
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

    


    function fetchComments(blogId, commentsListElement) {
        fetch(`http://localhost:8080/api/comments/get/${blogId}`, { method: 'GET' })
            .then(response => response.json())
            .then(comments => {
                commentsListElement.innerHTML = ''; // Clear existing comments
                comments.forEach(comment => {
                    const commentElement = document.createElement('div');
                    commentElement.classList.add('comment');
                    
                    // Log the comment to check its structure
                    console.log(comment);  // Check if username is present here
    
                    // Ensure comment.username is used correctly
                    commentElement.textContent = `${comment.username || 'Unknown'}: ${comment.content}`;
                    commentsListElement.appendChild(commentElement);
                });
            })
            .catch(error => {
                console.error(`Error fetching comments for blog ${blogId}:`, error);
                commentsListElement.innerHTML = '<p>Failed to load comments.</p>';
            });
    }
    
});




window.onload = function () {
    const blogList = document.getElementById('blog-list');
    blogList.classList.add('animate__fadeInUp');
};


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