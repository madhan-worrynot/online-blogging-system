// document.getElementById('loginForm').addEventListener('submit', function(e) {
//     e.preventDefault(); // Prevent the default form submission

//     // Get the username and password from the input fields
//     const username = document.getElementById('username').value;
//     const password = document.getElementById('password').value;

//     // Send the login request to the backend
//     fetch('http://localhost:8080/api/auth/login', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({ username, password })
//     })
//     .then(response => {
//         if (!response.ok) {
//             throw new Error('Login failed');
//         }
//         return response.text();  // Change from response.json() to response.text()
//     })
//     .then(data => {
//         if (data) {
//             // Save the JWT token in localStorage
//             localStorage.setItem('jwtToken', data);  // Store the token directly (data is the token string)
//             alert('Login successful!');
//             window.location.href = 'blog.html';  // Redirect to the blog page after login
//         } else {
//             showErrorMessage('Login failed: No token received');
//         }
//     })
//     .catch(error => {
//         showErrorMessage('Error during login: ' + error.message);
//     });
// });

// // Function to display error messages
// function showErrorMessage(message) {
//     const errorMessage = document.getElementById('errorMessage');
//     errorMessage.style.display = 'block';
//     errorMessage.textContent = message;
// }

document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent the default form submission

    // Get the username and password from the input fields
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Get the login button element to apply the animation
    const loginButton = document.getElementById('loginButton'); // Assuming your button has this ID

    // Add the rubberBand animation
    loginButton.classList.add('animate__animated', 'animate__rubberBand');

    // Remove the animation after it finishes
    loginButton.addEventListener('animationend', function() {
        loginButton.classList.remove('animate__rubberBand');
    });

    // Send the login request to the backend
    fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Login failed');
        }
        return response.text();  // Change from response.json() to response.text()
    })
    .then(data => {
        if (data) {
            // Save the JWT token in localStorage
            localStorage.setItem('jwtToken', data);  // Store the token directly (data is the token string)
            alert('Login successful!');
            window.location.href = 'blog.html';  // Redirect to the blog page after login
        } else {
            showErrorMessage('Login failed: No token received');
        }
    })
    .catch(error => {
        showErrorMessage('Error during login: ' + error.message);
    });
});

// Function to display error messages
function showErrorMessage(message) {
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.style.display = 'block';
    errorMessage.textContent = message;
}
