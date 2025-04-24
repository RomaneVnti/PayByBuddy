package com.paymybuddy.service;

import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.SelfRelationException;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserRelations;
import com.paymybuddy.dao.UserRelationsDAO;
import com.paymybuddy.dao.UserDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class RelationService {

    @Autowired
    private UserDAO userDAO;  // Injection du UserDAO

    @Autowired
    private UserRelationsDAO userRelationsDAO;  // Injection du UserRelationsDAO

    // Récupérer la liste des relations d'un utilisateur
    public List<String> getUserRelations(String email) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException("Utilisateur non trouvé.");
        }

        List<UserRelations> userRelationsList = userRelationsDAO.getUserRelations(user.getUserId());

        List<String> relationsEmails = new ArrayList<>();
        for (UserRelations relation : userRelationsList) {
            // Comparaison correcte pour des types int
            if (relation.getUser1().getUserId() == user.getUserId()) {
                relationsEmails.add(relation.getUser2().getEmail());
            } else {
                relationsEmails.add(relation.getUser1().getEmail());
            }
        }

        return relationsEmails;
    }


    // Ajouter une relation pour un utilisateur
    @Transactional
    public boolean addRelation(String userEmail, String relationEmail) {
        if (userEmail.equalsIgnoreCase(relationEmail)) {
            throw new SelfRelationException("Impossible d'ajouter votre propre adresse email.");
        }

        User relationUser = userDAO.findByEmail(relationEmail);
        if (relationUser == null) {
            throw new EmailNotFoundException("L'adresse email de la relation n'existe pas.");
        }

        User user = userDAO.findByEmail(userEmail);
        if (user == null) {
            throw new EmailNotFoundException("L'adresse email de l'utilisateur n'existe pas.");
        }

        // Vérification de l'existence de la relation avec des int
        if (userRelationsDAO.findRelationByIds(user.getUserId(), relationUser.getUserId()) != null) {
            throw new RuntimeException("Cette relation existe déjà.");
        }

        UserRelations userRelations = new UserRelations();
        userRelations.setUser1(user);
        userRelations.setUser2(relationUser);

        userRelationsDAO.save(userRelations);
        return true;
    }

}
