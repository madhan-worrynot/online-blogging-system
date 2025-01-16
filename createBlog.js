document.getElementById('createBlogForm').addEventListener('submit', function (event) {
  event.preventDefault();

  const title = document.getElementById('title').value;
  const content = document.getElementById('content').value;
  const token = localStorage.getItem('jwtToken');

  if (!token) {
      alert('You are not logged in. Please log in first.');
      window.location.href = 'login.html';  // Redirect to login page if not logged in
      return;
  }

  fetch('http://localhost:8080/api/blogs/create', {
      method: 'POST',
      headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({ title, content }),
  })
  .then((response) => {
      if (!response.ok) {
          throw new Error('Failed to create blog');
      }
      return response.text();  // Read the response as text (instead of JSON directly)
  })
  .then((text) => {
      try {
          const data = JSON.parse(text);  // Try parsing the text as JSON
          alert(data.message || 'Blog created successfully!');  // Display success message
          window.location.href = 'blog.html';  // Redirect to the blog page after success
      } catch (error) {
          alert(text);  // If not valid JSON, show the raw text response as an alert
      }
  })
  .catch((error) => {
      console.error('Error creating blog:', error);
      alert('An error occurred while creating the blog.');
  });
});
