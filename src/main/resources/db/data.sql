-- Ces données sont à titre d'exemples, le mot de passe ne sera pas hashé car il ne passe pas par le processus de hashs

-- Insertion d'un utilisateur
INSERT INTO user (username, email, password)
VALUES ('alice', 'alice@example.com', 'password123');

-- Insertion d'un autre utilisateur
INSERT INTO user (username, email, password)
VALUES ('bob', 'bob@example.com', 'password456');


-- Insertion d'une relation entre Alice et Bob
INSERT INTO user_relations (user_id_1, user_id_2, relationship_status)
VALUES (1, 2, 'ACCEPTEE');

-- Insertion d'une relation entre Bob et Alice (relation bidirectionnelle)
INSERT INTO user_relations (user_id_1, user_id_2, relationship_status)
VALUES (2, 1, 'ACCEPTEE');


-- Insertion d'une transaction de Alice à Bob
INSERT INTO transactions (user_id_sender, user_id_receiver, description, amount)
VALUES (1, 2, 'Paiement pour dîner', 50.0);

-- Insertion d'une transaction de Bob à Alice
INSERT INTO transactions (user_id_sender, user_id_receiver, description, amount)
VALUES (2, 1, 'Remboursement pour dîner', 50.0);

