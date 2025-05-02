document.getElementById('addRelationForm').addEventListener('submit', function(event) {
    const emailInput = document.getElementById('emailInput').value;
    const errorContainer = document.getElementById('errorContainer');

    // Réinitialise l'affichage des erreurs
    errorContainer.style.display = 'none';
    errorContainer.innerHTML = '';

    if (!emailInput) {
        errorContainer.innerHTML = 'L\'adresse email est requise.';
        errorContainer.style.display = 'block';
        return;
    }

    this.submit();
});
