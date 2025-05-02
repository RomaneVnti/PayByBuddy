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

/**
 * Service de gestion des relations entre utilisateurs.
 * Il permet de récupérer les relations existantes et d’en ajouter de nouvelles,
 * tout en assurant les validations nécessaires.
 */
@Service
public class RelationService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserRelationsDAO userRelationsDAO;

    /**
     * Récupère les adresses email des utilisateurs liés à un utilisateur donné.
     *
     * @param email l'adresse email de l'utilisateur
     * @return une liste d'adresses email correspondant aux relations de l'utilisateur
     * @throws EmailNotFoundException si l'utilisateur n'est pas trouvé
     */
    public List<String> getUserRelations(String email) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException("Utilisateur non trouvé.");
        }

        List<UserRelations> userRelationsList = userRelationsDAO.getUserRelations(user.getUserId());
        List<String> relationsEmails = new ArrayList<>();

        for (UserRelations relation : userRelationsList) {
            if (relation.getUser1().getUserId() == user.getUserId()) {
                relationsEmails.add(relation.getUser2().getEmail());
            } else {
                relationsEmails.add(relation.getUser1().getEmail());
            }
        }

        return relationsEmails;
    }

    /**
     * Ajoute une relation entre deux utilisateurs.
     *
     * @param userEmail      l'adresse email de l'utilisateur initiateur
     * @param relationEmail  l'adresse email de l'utilisateur à ajouter comme relation
     * @return true si la relation est ajoutée avec succès
     * @throws SelfRelationException si un utilisateur tente de se lier à lui-même
     * @throws EmailNotFoundException si l'un des deux emails n'existe pas
     * @throws RuntimeException si la relation existe déjà
     */
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
