package com.paymybuddy.service;

import com.paymybuddy.dao.TransactionDAO;
import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.dao.UserRelationsDAO;
import com.paymybuddy.exception.RelationNotFoundException;
import com.paymybuddy.exception.SoldeInvalidException;
import com.paymybuddy.model.Transactions;
import com.paymybuddy.model.User;
import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.InvalidAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * Service de gestion des transactions entre utilisateurs.
 * Permet d’ajouter de nouvelles transactions et de récupérer
 * les transactions liées à un utilisateur donné.
 */
@Service
public class TransactionService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private UserRelationsDAO userRelationsDAO;

    /**
     * Ajoute une nouvelle transaction entre deux utilisateurs.
     *
     * @param senderEmail    l'email de l'expéditeur
     * @param receiverEmail  l'email du destinataire
     * @param description    la description de la transaction
     * @param amount         le montant de la transaction (doit être strictement positif)
     * @return l’objet {@link Transactions} créé et sauvegardé
     * @throws InvalidAmountException si le montant est inférieur ou égal à zéro
     * @throws EmailNotFoundException si l'expéditeur ou le destinataire n'existe pas
     */
    @Transactional
    public Transactions addTransaction(String senderEmail, String receiverEmail, String description, double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Le montant doit être supérieur à zéro.");
        }

        User sender = userDAO.findByEmail(senderEmail);
        if (sender == null) {
            throw new EmailNotFoundException("L'utilisateur expéditeur n'existe pas.");
        }

        User receiver = userDAO.findByEmail(receiverEmail);
        if (receiver == null) {
            throw new EmailNotFoundException("L'utilisateur destinataire n'existe pas.");
        }

        if (userRelationsDAO.findRelationByIds(sender.getUserId(), receiver.getUserId()) == null) {
            throw new RelationNotFoundException("Les utilisateurs ne sont pas en relation.");
        }

        // Vérifier le solde de l'expéditeur
        if (sender.getSolde() < amount) {
            throw new SoldeInvalidException("Solde insuffisant. Solde actuel = " + sender.getSolde() + ", montant à débiter = " + amount);
        }

        // Mettre à jour les soldes
        sender.setSolde(sender.getSolde() - amount);
        receiver.setSolde(receiver.getSolde() + amount);

        // Sauvegarder les mises à jour des utilisateurs
        userDAO.save(sender);
        userDAO.save(receiver);

        // Créer et sauvegarder la transaction
        Transactions transaction = new Transactions();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);

        transactionDAO.save(transaction);
        return transaction;
    }


    /**
     * Récupère toutes les transactions associées à un utilisateur donné,
     * en tant qu’expéditeur ou destinataire.
     *
     * @param userEmail l'adresse email de l'utilisateur concerné
     * @return une liste de transactions impliquant cet utilisateur
     * @throws EmailNotFoundException si l'utilisateur n'existe pas
     */
    public List<Transactions> getUserTransactions(String userEmail) {
        User user = userDAO.findByEmail(userEmail);
        if (user == null) {
            throw new EmailNotFoundException("L'utilisateur n'existe pas.");
        }

        return transactionDAO.findBySenderOrReceiver(user);
    }
}
