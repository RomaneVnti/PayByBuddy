
    // Gérer la soumission du formulaire
    document.getElementById('loginForm').addEventListener('submit', function(event) {
        event.preventDefault();  // Empêcher l'envoi du formulaire par défaut

        const formData = new FormData(this);  // Récupérer les données du formulaire
        const data = new URLSearchParams();

        // Ajouter les données du formulaire au body de la requête
        formData.forEach((value, key) => {
            data.append(key, value);
        });

        // Envoyer la requête POST avec fetch
        fetch("/auth/login", {
            method: "POST",
            body: data,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        })
        .then(response => {
            if (!response.ok) {
                // Si la réponse n'est pas OK, on extrait l'erreur
                return response.json().then(errorResponse => {
                    const errorContainer = document.getElementById("errorContainer");
                    errorContainer.textContent = errorResponse.message; // Afficher le message d'erreur
                });
            }
            // Si la connexion est réussie, rediriger l'utilisateur
            window.location.href = "/home"; // ou rediriger vers la page d'accueil ou tableau de bord
        })
        .catch(error => {
            // En cas d'erreur de réseau ou autre, afficher une erreur générique
            const errorContainer = document.getElementById("errorContainer");
            errorContainer.textContent = "Une erreur est survenue. Veuillez réessayer.";
        });
    });
