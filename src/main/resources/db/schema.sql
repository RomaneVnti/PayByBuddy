-- Création de la base de données
CREATE DATABASE IF NOT EXISTS paymybuddy;
USE paymybuddy;

-- Table User
CREATE TABLE IF NOT EXISTS user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table User_relations
CREATE TABLE IF NOT EXISTS user_relations (
    relation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id_1 INT NOT NULL,
    user_id_2 INT NOT NULL,
    relationship_status VARCHAR(255) NOT NULL DEFAULT 'ACCEPTEE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id_1, user_id_2),
    FOREIGN KEY (user_id_1) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id_2) REFERENCES user(user_id) ON DELETE CASCADE
);


-- Table Transactions
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id_sender INT NOT NULL,
    user_id_receiver INT NOT NULL,
    description VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id_sender) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id_receiver) REFERENCES user(user_id) ON DELETE CASCADE
);
