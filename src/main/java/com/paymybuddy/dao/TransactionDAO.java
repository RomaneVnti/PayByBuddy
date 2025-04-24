package com.paymybuddy.dao;

import com.paymybuddy.model.Transactions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class TransactionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Sauvegarder une nouvelle transaction
    @Transactional
    public void save(Transactions transaction) {
        entityManager.persist(transaction);
    }
}
