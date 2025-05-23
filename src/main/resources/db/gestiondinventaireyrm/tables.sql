/**
 * PROJECT DATABASE SCHEMA
 * 
 * This schema defines the complete database structure for an inventory management system
 * with supplier tracking, product categorization, and user authentication.
 * 
 * Key Features:
 * 1. Multi-tenant database architecture
 * 2. Comprehensive inventory tracking with expiration dates
 * 3. Supplier management with legal identifiers
 * 4. Location-based asset managementfournisseur
 * 5. Role-based user access control
 * 
 * Schema Design Principles:
 * - All tables use VARCHAR(10) primary keys for consistency
 * - ENUM types enforce domain-specific constraints
 * - CHECK constraints validate business rules
 * - ON DELETE/UPDATE cascades deliberately omitted for data integrity
 * Created: 2025-04-25
*/

CREATE DATABASE IF NOT EXISTS project;
USE project;

/**
 * FOURNISSEUR (Supplier) Table
 * Stores legal entity information for product suppliers
 * Includes all required Algerian business identifiers:
 * - NIF (Numéro d'Identification Fiscale)
 * - NIS (Numéro d'Identification Statistique)
 * - IA (Identification Algérie)
 * - RC (Registre du Commerce)
 */

CREATE TABLE IF NOT EXISTS Fournisseur (
	id_f VARCHAR(10) PRIMARY KEY,
	adresse VARCHAR(255),
	numero_tlph VARCHAR(11),
	nom VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL,
	NIF VARCHAR(50)	UNIQUE NOT NULL,
	NIS VARCHAR(50)	UNIQUE NOT NULL,
	RC VARCHAR(50)  UNIQUE NOT NULL
);

/**
 * EMPLACEMENT (Location) Table
 * Tracks physical storage locations and office assignments
 * Supports mixed storage types (warehouse/office)
 */
CREATE TABLE IF NOT EXISTS Emplacement (
	id_emplacement VARCHAR(10) PRIMARY KEY,
	type_salle VARCHAR(50),
	superficie DECIMAL(10,2),
	bureau INT,
	-- TODO: nom_serivce should be enumiration
	nom_service ENUM('VICE DOYENS' , 'SECRETARIAT' , 'CHEF DE DEPARTEMENT' , 'BIBLIOTHEQUE')
);

/**
 * BON (Inventory Document) Table
 * Master table for all inventory transactions
 * Uses a boolean flag to bon_type between:
 * - TRUE: Bon de réception (incoming goods)
 * - FALSE: Bon de sortie (outgoing goods)
 */
CREATE TABLE IF NOT EXISTS Bon (
	id_bon varchar(10) PRIMARY KEY,
	bon_date DATE NOT NULL,
	bon_type BOOLEAN ,
	id_emplacement VARCHAR(10),
	id_f VARCHAR(10),
	FOREIGN KEY (id_emplacement) REFERENCES Emplacement(id_emplacement),
	FOREIGN KEY (id_f) REFERENCES Fournisseur(id_f)
);

/**
 * RATTACHER (Supplier-Document Link) Table
 * Junction table implementing many-to-many relationship
 * between suppliers and inventory documents
 */
CREATE TABLE IF NOT EXISTS rattacher (
	id_f varchar(10),
	id_bon varchar(10),
	PRIMARY KEY (id_f, id_bon),
	FOREIGN KEY (id_f) REFERENCES Fournisseur(id_f),
	FOREIGN KEY (id_bon) REFERENCES Bon(id_bon)
);

/**
 * PRODUITMODELE (Product Model) Table
 * Defines product types and categories
 * Uses ENUM for strict category control
 */
CREATE TABLE IF NOT EXISTS ProduitModele (
	id_modele VARCHAR(10) PRIMARY KEY,
	type_produit BOOLEAN DEFAULT FALSE,
	designation VARCHAR(255) NOT NULL ,
	categorie ENUM('INFORMATIQUE' , 'BUREAUTIQUE' , 'RESEAU' , 'TELECOMMUNICATION' , 'STOCKAGE' , 'AUTRE') NOT NULL ,
	id_bon VARCHAR(10) NOT NULL,
	FOREIGN KEY (id_bon) REFERENCES Bon(id_bon)
);

/**
 * PRODUITARTICLE (Product Item) Table
 * Tracks individual product instances with:
 * - Expiration dates (for perishable inventory)
 * - Purchase dates
 * - Quantity tracking
 */
CREATE TABLE IF NOT EXISTS ProduitArticle (
	id_article VARCHAR(10) PRIMARY KEY,
	date_peremption DATE,
	nom_article VARCHAR(100) NOT NULL,
	quantite_global INT NOT NULL DEFAULT 0,
	date_dachat DATE NOT NULL,
	id_modele VARCHAR(10) NOT NULL,
	FOREIGN KEY (id_modele) REFERENCES ProduitModele(id_modele),
	CONSTRAINT chk_quantite CHECK (quantite_global >= 0)
);

/**
 * INVENTORIER (Inventory Tracking) Table
 * Quantifies products referenced in inventory documents
 * Includes positive quantity validation
 */
CREATE TABLE IF NOT EXISTS Inventorier (
	id_modele VARCHAR(10) NOT NULL,
	id_bon VARCHAR(10) NOT NULL,
	quantite INT NOT NULL,
	PRIMARY KEY (id_modele, id_bon),
	FOREIGN KEY (id_modele) REFERENCES ProduitModele(id_modele),
	FOREIGN KEY (id_bon) REFERENCES Bon(id_bon),
	CONSTRAINT chk_quantite_positive CHECK (quantite > 0)
);

/**
 * COMPTE (User Account) Table
 * Secure user authentication system with:
 * - Minimum password length enforcement
 * - Role-based access control
 */
CREATE TABLE IF NOT EXISTS Compte (
	id_c varchar(10) PRIMARY KEY,
	nom_utilisateur VARCHAR(50) NOT NULL UNIQUE,
	mot_de_passe VARCHAR(255) NOT NULL,
	-- check enum later
	role ENUM('ADMINISTRATEUR' , 'MAGASINIER' , 'SECRETAIRE' , 'CLIENT') NOT NULL,
	CONSTRAINT chk_password_length CHECK (LENGTH(mot_de_passe) >= 8)
);

/**
 * PERSONNE (Person) Table
 * Master data for all human entities in the system
 * Links to both physical locations and user accounts
 */
CREATE TABLE IF NOT EXISTS Personne (
	id_p varchar(10) PRIMARY KEY,
	nom VARCHAR(100) NOT NULL,
	prenom VARCHAR(100) NOT NULL,
	date_naissance DATE NOT NULL,
	email VARCHAR(100) NOT NULL,
	adresse VARCHAR(255),
	numero_tlph VARCHAR(20),
	avoir_compte BOOLEAN DEFAULT FALSE,
	id_emplacement varchar(10),
	id_c varchar(10),
	FOREIGN KEY (id_emplacement) REFERENCES Emplacement(id_emplacement),
	FOREIGN KEY (id_c) REFERENCES Compte(id_c)
);
