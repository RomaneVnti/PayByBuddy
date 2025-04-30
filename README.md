# 💸 PayMyBuddy

Application Java Spring Boot de gestion de transferts d'argent entre amis.  
Elle permet aux utilisateurs de créer un compte, ajouter des relations, et effectuer des virements.

---

## 📸 Aperçu de l'architecture

![UML de la base de données](src/main/resources/db/MPD_PayMyBuddy.png)

---

## 🔧 Technologies utilisées

- Java 21
- Spring Boot 3.4.2
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Token)
- Maven
- Log4j2
- Thymeleaf (pour les templates si activé)
- Lombok
- JUnit & Spring Test
- JaCoCo (couverture de tests)

---

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone https://github.com/ton-utilisateur/PayMyBuddy.git
cd PayMyBuddy
```

### 2. Créer le fichier application.properties

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

### 3. Installer les dépendances
```bash
mvn clean install
```

### 4. Lancer l'application
```bash
mvn spring-boot:run
```
---

## 🚀 Installation

### 🧪 Tests

```bash
mvn test
```
Un rapport JaCoCo sera généré dans target/site/jacoco/index.html pour la couverture des tests.

## 📝 Utilisation
Une fois l'application lancée, tu peux créer un compte en accédant à la route suivante dans ton navigateur :

Route principale pour la création de compte :
http://localhost:8080/inscription

