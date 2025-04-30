-- Insertion d'exemples de données dans la table User
INSERT INTO User (username, email, password)
VALUES ('john_doe', 'john@example.com', 'password123'),
       ('jane_smith', 'jane@example.com', 'password456');

-- Insertion d'exemples de données dans la table User_relations
-- Insérer une relation entre user1 et user2
INSERT INTO relations (user_id_1, user_id_2, status)
VALUES (1, 2, 'ACCEPTEE');


-- Insertion d'exemples de données dans la table Transactions
INSERT INTO Transactions (user_id_sender, user_id_receiver, description, amount)
VALUES (1, 2, 'Paiement pour le repas', 20.50),
       (2, 1, 'Remboursement pour le déjeuner', 15.00);
