    document.getElementById('addRelationForm').addEventListener('submit', function(event) {
        event.preventDefault(); // Empêcher la soumission du formulaire pour gérer l'erreur

        const emailInput = document.getElementById('emailInput').value;
        const errorContainer = document.getElementById('errorContainer');

        // Réinitialiser les erreurs précédentes
        errorContainer.style.display = 'none';
        errorContainer.innerHTML = '';

        // Exemple de logique d'erreur
        if (!emailInput) {
            errorContainer.innerHTML = 'L\'adresse email est requise.';
            errorContainer.style.display = 'block';
            return;
        }

        // Si l'email n'est pas valide
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(emailInput)) {
            errorContainer.innerHTML = 'Veuillez saisir une adresse email valide.';
            errorContainer.style.display = 'block';
            return;
        }

        // Si aucune erreur, soumettre le formulaire (ici on fait juste une soumission simulée)
        this.submit();
    });
