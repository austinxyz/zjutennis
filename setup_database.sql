-- Database setup script for zjutennis project

-- Create database
CREATE DATABASE IF NOT EXISTS zjualumni;

-- Use the database
USE zjualumni;

-- Create players table
CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    utr_rating DOUBLE,
    graduation_year INT,
    major VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    phone_number VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_graduation_year (graduation_year),
    INDEX idx_city (city),
    INDEX idx_utr_rating (utr_rating)
);

-- Verify table creation
SHOW TABLES;
DESCRIBE players;
