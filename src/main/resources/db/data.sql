-- Insertion d'exemples de données dans la table User
INSERT INTO User (username, email, password)
VALUES ('john_doe', 'john@example.com', 'password123'),
       ('jane_smith', 'jane@example.com', 'password456');

-- Insertion d'exemples de données dans la table User_relations
INSERT INTO User_relations (user_id, friend_id)
VALUES (1, 2),  -- John est ami avec Jane
       (2, 1);  -- Jane est amie avec John

-- Insertion d'exemples de données dans la table Transactions
INSERT INTO Transactions (user_id_sender, user_id_receiver, description, amount)
VALUES (1, 2, 'Paiement pour le repas', 20.50),
       (2, 1, 'Remboursement pour le déjeuner', 15.00);
