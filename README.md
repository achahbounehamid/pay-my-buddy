Description;
Pay My Buddy est une application web développée en Java (Spring Boot) 
permettant aux utilisateurs d'effectuer des transferts d'argent entre amis. 
Cette application suit une architecture MVC et inclut une gestion sécurisée des transactions.
Contenu du Repository

Ce repository contient :
Le modèle physique de données (ajouté ci-dessous).
Les scripts SQL de création et d'initialisation de la base de données.
La couche DAL (Data Access Layer) avec gestion des transactions (commit & rollback).
Une connexion sécurisée à la base de données.
Une interface web conforme aux maquettes, exploitant la couche DAL.

Lien vers le Repository:
https://github.com/achahbounehamid/pay-my-buddy/tree/master

Modèle Physique de Données sont disponibles dans le fichier: MPD

Scripts SQL
Tous les scripts SQL sont disponibles dans le fichier : database.sql

Couche DAL (Data Access Layer)
La couche DAL gère les interactions avec la base de données via JPA / Hibernate et inclut :
Requêtes SQL optimisées
Transactions sécurisées avec commit et rollback
Gestion des erreurs et exceptions
Code disponible dans src/main/java/com/paymybuddy/repository


