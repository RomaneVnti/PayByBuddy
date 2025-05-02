// Gérer la soumission du formulaire de connexion
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Empêche l'envoi classique du formulaire

    const formData = new FormData(this);
    const data = new URLSearchParams();

    formData.forEach((value, key) => {
        data.append(key, value);
    });

    fetch("/auth/login", {
        method: "POST",
        body: data,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorResponse => {
                // Afficher le message d'erreur retourné par le serveur
                const errorContainer = document.getElementById("errorContainer");
                errorContainer.textContent = errorResponse.message;
            });
        }
        // Rediriger l'utilisateur si la connexion est réussie
        window.location.href = "/home";
    })
    .catch(error => {
        // Afficher un message d'erreur générique en cas d'échec réseau
        const errorContainer = document.getElementById("errorContainer");
        errorContainer.textContent = "Une erreur est survenue. Veuillez réessayer.";
    });
});
