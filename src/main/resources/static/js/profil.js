// Récupère un cookie par son nom
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
}

document.getElementById("updateProfileForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (password.length === 0) {
        showError("Le mot de passe ne peut pas être vide.");
        return;
    } else if (password.length < 8) {
        showError("Le mot de passe doit contenir plus de 8 caractères.");
        return;
    }

    const userDTO = {
        username: username,
        email: email,
        password: password
    };

    const token = getCookie("JWT");

    if (!token) {
        showError("Token JWT manquant.");
        return;
    }

    fetch("/user/update", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(userDTO)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.json().then(data => {
                throw new Error(data.message || "Erreur inconnue");
            });
        }
    })
    .then(data => {
        showError("");
        alert("Profil mis à jour");
    })
    .catch(error => {
        console.error("Erreur lors de la mise à jour du profil:", error);

        if (error.message && error.message.includes("PasswordTooShort")) {
            showError("Le mot de passe doit contenir plus de 8 caractères.");
        } else {
            showError("Erreur lors de la mise à jour du profil.");
        }
    });
});

// Affiche un message d'erreur dans le conteneur prévu
function showError(message) {
    const errorContainer = document.getElementById("errorContainer");
    errorContainer.textContent = message;
}
