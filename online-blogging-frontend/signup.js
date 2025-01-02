document.getElementById('signupForm').addEventListener('submit', function (event) {
    event.preventDefault();

    // Get the username, email, and password from the input fields
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Get the signup button element to apply the animation
    const signupButton = document.getElementById('signupButton'); // Assuming your button has this ID

    // Add the rubberBand animation
    signupButton.classList.add('animate__animated', 'animate__rubberBand');

    // Remove the animation after it finishes
    signupButton.addEventListener('animationend', function() {
        signupButton.classList.remove('animate__rubberBand');
    });

    const signupData = {
        username: username,
        email: email,
        password: password
    };

    fetch('http://localhost:8080/api/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(signupData)
    })
    .then(response => {
        // Handle plain text and JSON responses
        return response.text().then(text => {
            try {
                return JSON.parse(text); // Try parsing the response as JSON
            } catch (error) {
                return { message: text }; // If not valid JSON, return text as a message
            }
        });
    })
    .then(data => {
        if (data.message) {
            alert(data.message); // Show success or error message
            if (data.message === "User registered successfully!") {
                window.location.href = 'login.html'; // Redirect to login on success
            }
        } else {
            alert('Unexpected response from the server.');
        }
    })
    .catch(error => {
        console.error('Error during signup:', error);
        alert('An error occurred. Please try again.');
    });
});
