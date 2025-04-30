    // Fonction pour récupérer un cookie par son nom
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(";").shift();
        return null;
    }

    document.getElementById("updateProfileForm").addEventListener("submit", function(event) {
        event.preventDefault();  // Empêche la soumission traditionnelle du formulaire

        // Récupérer les données du formulaire
        const username = document.getElementById("username").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        // Vérifier les valeurs des champs
        console.log("Valeurs du formulaire:");
        console.log("Username:", username);
        console.log("Email:", email);
        console.log("Password:", password);

        // Construire l'objet UserDTO avec les données du formulaire
        const userDTO = {
            username: username,
            email: email,
            password: password
        };

        // Récupérer le token JWT depuis le cookie
        const token = getCookie("JWT");  // Utiliser la fonction pour récupérer le token depuis le cookie
        console.log("Token JWT récupéré:", token);

        if (!token) {
            console.log("Erreur: Aucun token JWT trouvé.");
            showError("Token JWT manquant.");
            return;
        }

        // Envoyer la requête PUT pour mettre à jour le profil
        console.log("Envoi de la requête PUT avec les données et le token...");
        fetch("/user/update", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`  // Inclure le token JWT dans l'en-tête
            },
            body: JSON.stringify(userDTO)  // Envoyer les données sous forme de JSON
        })
        .then(response => {
            console.log("Réponse de la requête:", response);
            if (response.ok) {
                return response.json();
            } else {
                console.error("Erreur dans la réponse:", response.statusText);
                throw new Error(`Erreur: ${response.statusText}`);
            }
        })
        .then(data => {
            console.log("Données reçues du serveur:", data);
            showError("");  // Effacer le message d'erreur
            alert("Profil mis à jour");
        })
        .catch(error => {
            console.error("Erreur lors de la mise à jour du profil:", error);
            showError("Erreur lors de la mise à jour du profil.");
        });
    });

    // Fonction pour afficher les erreurs
    function showError(message) {
        const errorContainer = document.getElementById("errorContainer");
        errorContainer.textContent = message;
    }
