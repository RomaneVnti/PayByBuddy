package com.paymybuddy.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RelationService {

    // Simuler une base de données avec une map : email utilisateur -> liste de relations
    private final Map<String, List<String>> userRelations = new HashMap<>();

    public RelationService() {
        // Données simulées
        userRelations.put("user1", new ArrayList<>(List.of("john@example.com", "jane@example.com")));
        userRelations.put("user2", new ArrayList<>(List.of("alice@example.com", "bob@example.com")));
    }

    // Récupérer la liste des relations d'un utilisateur
    public List<String> getUserRelations(String username) {
        return userRelations.getOrDefault(username, new ArrayList<>());
    }

    // Ajouter une relation pour un utilisateur
    public boolean addRelation(String userEmail, String relationEmail) {
        // Vérifier que ce n’est pas le même utilisateur
        if (userEmail.equalsIgnoreCase(relationEmail)) {
            return false;
        }

        // Récupérer ou créer la liste de relations de l'utilisateur
        List<String> relations = userRelations.computeIfAbsent(userEmail, k -> new ArrayList<>());

        // Vérifier si la relation existe déjà
        if (relations.contains(relationEmail)) {
            return false; // Déjà en relation
        }

        // Ajouter la relation
        relations.add(relationEmail);
        return true;
    }
}
