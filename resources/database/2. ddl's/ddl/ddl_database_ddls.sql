-- Database: postgres

-- DROP DATABASE postgres;

CREATE USER ddl WITH PASSWORD 'ddl' SUPERUSER;
----------------------------------------
--	Databases for SQL module 		---
----------------------------------------
DROP DATABASE if exists ddl;
----------------------------------------
CREATE DATABASE ddl
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    TABLESPACE = pg_default 	
    CONNECTION LIMIT = -1;

----------------------------------------

----------------------------------------
-- Reference tables with exercises and connections
----------------------------------------
DROP TABLE if exists exercises;
DROP TABLE if exists connections;
CREATE TABLE exercises
(
  ID integer PRIMARY KEY;
  SCHEMA_NAME VARCHAR(50) NOT NULL UNIQUE,
  SOLUTION VARCHAR(5000) NOT NULL,
  INSERT_STATEMENTS VARCHAR(5000),
  MAX_POINTS integer NOT NULL,
  TABLE_POINTS integer NOT NULL,
  COLUMN_POINTS integer NOT NULL,
  PRIMARYKEY_POINTS integer NOT NULL,
  FOREIGNKEY_POINTS integer NOT NULL,
  CONSTRAINT_POINTS integer NOT NULL
);

CREATE TABLE connections
(
  ID integer PRIMARY KEY,
  SCHEMA_NAME VARCHAR(20) NOT NULL UNIQUE,
  CONN_USER VARCHAR(100) NOT NULL,
  CONN_PWD VARCHAR(100) NOT NULL
);
----------------------------------------
-- Create users and schemas
----------------------------------------
CREATE USER ddl1 WITH PASSWORD 'ddl1';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl1;

CREATE USER ddl2 WITH PASSWORD 'ddl2';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl2;

CREATE USER ddl3 WITH PASSWORD 'ddl3';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl3;

CREATE USER ddl4 WITH PASSWORD 'ddl4';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl4;

CREATE USER ddl5 WITH PASSWORD 'ddl5';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl5;

CREATE USER ddl6 WITH PASSWORD 'ddl6';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl6;

CREATE USER ddl7 WITH PASSWORD 'ddl7';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl7;

CREATE USER ddl8 WITH PASSWORD 'ddl8';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl8;

CREATE USER ddl9 WITH PASSWORD 'ddl9';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl9;

CREATE USER ddl10 WITH PASSWORD 'ddl10';
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION ddl10;
----------------------------------------
-- Insert users in connections table
----------------------------------------
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl1', 'ddl1', 'ddl1');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl2', 'ddl2', 'ddl2');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl3', 'ddl3', 'ddl3');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl4', 'ddl4', 'ddl4');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl5', 'ddl5', 'ddl5');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl6', 'ddl6', 'ddl6');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl7', 'ddl7', 'ddl7');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl8', 'ddl8', 'ddl8');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl9', 'ddl9', 'ddl9');
INSERT INTO connections (ID, SCHEMA_NAME, CONN_USER, CONN_PWD) VALUES (1, 'ddl10', 'ddl10', 'ddl10');