package com.paymybuddy.service;

import com.paymybuddy.dao.TransactionDAO;
import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.model.Transactions;
import com.paymybuddy.model.User;
import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.InvalidAmountException;  // Ajouter une exception personnalisée pour l'invalidité du montant
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private UserDAO userDAO;  // Injection du UserDAO pour retrouver les utilisateurs

    @Autowired
    private TransactionDAO transactionDAO;  // Injection du TransactionDAO pour persister les transactions

    // Méthode pour ajouter une transaction
    public Transactions addTransaction(String senderEmail, String receiverEmail, String description, double amount) {
        // Vérification que le montant est supérieur à zéro
        if (amount <= 0) {
            throw new InvalidAmountException("Le montant doit être supérieur à zéro.");
        }

        // Recherche de l'utilisateur expéditeur via son email
        User sender = userDAO.findByEmail(senderEmail);
        if (sender == null) {
            throw new EmailNotFoundException("L'utilisateur expéditeur n'existe pas.");
        }

        // Recherche de l'utilisateur destinataire via son email
        User receiver = userDAO.findByEmail(receiverEmail);
        if (receiver == null) {
            throw new EmailNotFoundException("L'utilisateur destinataire n'existe pas.");
        }

        // Créer une nouvelle transaction
        Transactions transaction = new Transactions();
        transaction.setSender(sender);  // Assignation de l'objet User sender
        transaction.setReceiver(receiver);  // Assignation de l'objet User receiver
        transaction.setDescription(description);  // Description de la transaction
        transaction.setAmount(amount);  // Montant de la transaction

        // Enregistrer la transaction dans la base de données
        transactionDAO.save(transaction);  // Sauvegarde dans le DAO

        return transaction;
    }
}
