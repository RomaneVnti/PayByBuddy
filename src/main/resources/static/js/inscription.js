// Soumission du formulaire d'inscription
document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault();  // Empêche le rechargement de la page

    const formData = new FormData(this);
    const data = new URLSearchParams();

    formData.forEach((value, key) => {
        data.append(key, value);
    });

    fetch("/user/create", {
        method: "POST",
        body: data,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorResponse => {
                const errorContainer = document.getElementById("errorContainer");
                errorContainer.textContent = errorResponse.message; // Affiche le message d'erreur retourné par le serveur
            });
        }
        window.location.href = "/connexion"; // Redirige vers la page de connexion après inscription réussie
    })
    .catch(error => {
        const errorContainer = document.getElementById("errorContainer");
        errorContainer.textContent = "Une erreur est survenue. Veuillez réessayer.";
    });
});
