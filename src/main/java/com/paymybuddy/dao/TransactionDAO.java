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
 */
@Repository
public class TransactionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Sauvegarde une transaction.
     */
    @Transactional
    public void save(Transactions transaction) {
        entityManager.persist(transaction);
    }

    /**
     * Récupère les transactions où l'utilisateur est expéditeur ou destinataire.
     */
    public List<Transactions> findBySenderOrReceiver(User user) {
        String jpql = "SELECT t FROM Transactions t WHERE t.sender = :user OR t.receiver = :user";
        TypedQuery<Transactions> query = entityManager.createQuery(jpql, Transactions.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
}
