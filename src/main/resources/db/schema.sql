-- Création de la base de données
CREATE DATABASE IF NOT EXISTS paymybuddy;
USE paymybuddy;

-- Table User
CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table User_relations
CREATE TABLE User_relations (
    relation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- Table Transactions
CREATE TABLE Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id_sender INT NOT NULL,
    user_id_receiver INT NOT NULL,
    description VARCHAR(255),
    amount DOUBLE NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id_sender) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id_receiver) REFERENCES User(user_id) ON DELETE CASCADE
);