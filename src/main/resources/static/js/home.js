// Récupère une valeur de cookie par son nom
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
}

function loadSolde() {
    const token = getCookie("JWT");
    if (!token) {
        showError("Token JWT manquant ou invalide.");
        return;
    }

    fetch("http://localhost:8080/user/user/profil", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération du solde");
        }
        return response.json();
    })
    .then(data => {
        const solde = data.solde || 0;
        document.getElementById("soldeValue").textContent = solde.toFixed(2);
    })
    .catch(error => {
        console.error("Erreur :", error);
        document.getElementById("soldeValue").textContent = "Erreur";
    });
}


document.addEventListener("DOMContentLoaded", function () {
    // Charger les relations de l'utilisateur connecté
    fetch("/user-relations", {
        method: "GET",
        credentials: "include",
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération des relations");
        }
        return response.json();
    })
    .then(data => {
        const select = document.getElementById("relationSelect");
        const relations = data.data || [];

        if (relations.length === 0) {
            console.log("Aucune relation disponible.");
            return;
        }

        relations.forEach(email => {
            const option = document.createElement("option");
            option.value = email;
            option.textContent = email;
            select.appendChild(option);
        });
    })
    .catch(error => {
        console.error("Erreur :", error);
        showError("Impossible de charger les relations.");
    });

    // Soumission d'une transaction
    const payButton = document.getElementById("payButton");

    payButton.addEventListener("click", function () {
        const senderEmail = "userEmail@example.com"; // À remplacer par l'email réel de l'utilisateur connecté
        const receiverEmail = document.getElementById("relationSelect").value;
        const description = document.getElementById("descriptionInput").value;
        const amount = parseFloat(document.getElementById("amountInput").value);

        if (!receiverEmail) {
            showError("Vous devez sélectionner une relation.");
            return;
        }

        if (!description) {
            showError("Vous devez renseigner une description.");
            return;
        }

        if (isNaN(amount) || amount <= 0) {
            showError("Le montant doit être supérieur à zéro.");
            return;
        }

        const transactionData = {
            senderEmail,
            receiverEmail,
            description,
            amount,
        };

        const token = getCookie("JWT");
        if (!token) {
            showError("Token JWT manquant ou invalide.");
            return;
        }

        fetch("http://localhost:8080/transaction", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(transactionData)
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Erreur lors de la création de la transaction");
            }
        })
        .then(data => {
            showError("");
            loadTransactions();
            loadSolde();
        })
        .catch(error => {
            console.error("Erreur :", error);
            showError("Une erreur est survenue lors de la création de la transaction.");
        });
    });

    // Affiche un message d'erreur à l'utilisateur
    function showError(message) {
        const errorContainer = document.getElementById("errorContainer");
        errorContainer.textContent = message;
    }

    // Charge les transactions et les affiche dans le tableau
    function loadTransactions() {
        const token = getCookie("JWT");
        if (!token) {
            showError("Token JWT manquant ou invalide.");
            return;
        }

        fetch("http://localhost:8080/transaction", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
        .then(response => response.json())
        .then(data => {
            if (!data || !data.data || !data.data.transactions) {
                console.error("La réponse ne contient pas les transactions sous 'data.transactions'.");
                return;
            }

            const transactionsTable = document.getElementById("transactionsTableBody");
            transactionsTable.innerHTML = "";

            const userTransactions = data.data.transactions.filter(
                transaction => transaction.sender.email !== "userEmail@example.com"
            );

            userTransactions.forEach(transaction => {
                const row = document.createElement("tr");

                const relationCell = document.createElement("td");
                relationCell.textContent = transaction.receiver.username || "N/A";

                const descriptionCell = document.createElement("td");
                descriptionCell.textContent = transaction.description || "Aucune description";

                const amountCell = document.createElement("td");
                amountCell.textContent = `${transaction.amount} €`;

                row.appendChild(relationCell);
                row.appendChild(descriptionCell);
                row.appendChild(amountCell);
                transactionsTable.appendChild(row);
            });
        })
        .catch(error => {
            console.error("Erreur lors du chargement des transactions:", error);
            showError("Impossible de charger les transactions.");
        });
    }
loadSolde();
    loadTransactions(); // Charger les transactions au démarrage
});
