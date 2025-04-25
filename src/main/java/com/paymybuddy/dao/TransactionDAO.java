package com.paymybuddy.dao;

import com.paymybuddy.model.Transactions;
import com.paymybuddy.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DAO pour gérer les opérations liées aux transactions dans la base de données.
 * Ce repository utilise JPA pour interagir avec la table des transactions.
 */
@Repository
public class TransactionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Sauvegarde une nouvelle transaction dans la base de données.
     * Cette méthode persiste un objet Transactions via l'EntityManager.
     *
     * @param transaction L'objet transaction à sauvegarder.
     */
    @Transactional
    public void save(Transactions transaction) {
        // Sauvegarde de la transaction dans la base de données
        entityManager.persist(transaction);
    }

    /**
     * Récupère toutes les transactions où l'utilisateur est soit l'expéditeur, soit le destinataire.
     *
     * @param user L'utilisateur pour lequel les transactions sont recherchées.
     * @return La liste des transactions où l'utilisateur est impliqué (comme expéditeur ou destinataire).
     */
    public List<Transactions> findBySenderOrReceiver(User user) {
        String jpql = "SELECT t FROM Transactions t WHERE t.sender = :user OR t.receiver = :user";

        // Création de la requête JPQL pour récupérer les transactions de l'utilisateur
        return entityManager.createQuery(jpql, Transactions.class)
                .setParameter("user", user)  // Ajout du paramètre 'user' dans la requête
                .getResultList();  // Exécution de la requête et récupération des résultats
    }


}
