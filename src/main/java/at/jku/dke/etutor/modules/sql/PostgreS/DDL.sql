-- Database: sql

-- DROP DATABASE sql;

CREATE DATABASE sql
    WITH 
    OWNER = sql
    ENCODING = 'UTF8'
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
/* oracle base database with exercises and connections */	
DROP TABLE exercises;
DROP TABLE connections;
CREATE TABLE CONNECTIONS 
   (	ID integer NOT NULL, 
	CONN_STRING VARCHAR(100) NOT NULL , 
	CONN_USER VARCHAR(100) NOT NULL , 
	CONN_PWD VARCHAR(100) NOT NULL , 
	 CONSTRAINT "CONNECTIONS_PK" PRIMARY KEY (ID)
   );

  CREATE TABLE exercises
   (	ID integer NOT NULL, 
	SUBMISSION_DB integer NOT NULL, 
	PRACTISE_DB integer NOT NULL, 
	SOLUTION VARCHAR(2048) NOT NULL , 
	 CONSTRAINT "EXERCISES_PK" PRIMARY KEY (ID),
	
	 CONSTRAINT "EXERCISES_FK1" FOREIGN KEY (PRACTISE_DB)
	  REFERENCES connections (ID) , 
	 CONSTRAINT "EXERCISES_FK2" FOREIGN KEY (SUBMISSION_DB)
	  REFERENCES connections (ID) 

   );
   
    COPY connections
   FROM   'C:\Users\Public\sql_connections.csv'
   DELIMITER ',' CSV HEADER;
   
   COPY exercises
   FROM 'C:\Users\Public\sql_exercises.csv'
   DELIMITER ',' CSV HEADER;
   
  
   
/* SQL_TRIAL_BEGIN Tables from referenced oracle database */
 DROP TABLE vermietet;
 DROP TABLE wohnung;
 DROP TABLE person;
 
  CREATE TABLE person
   (	PERSNR integer, 
	NAME VARCHAR(10), 
	STAND VARCHAR(15), 
	BERUF VARCHAR(15), 
	 PRIMARY KEY (PERSNR)
   ); 
 
   COPY person
   FROM   'C:\Users\Public\sql_trial_begin_person.csv'
   DELIMITER ',' CSV HEADER;

  CREATE TABLE wohnung 
   (	WOHNNR integer, 
	BEZIRK integer, 
	GROSS integer, 
	 PRIMARY KEY (WOHNNR)
   ); 
   
   COPY wohnung
   FROM   'C:\Users\Public\sql_trial_begin_wohnung.csv'
   DELIMITER ',' CSV HEADER;
   
  
  CREATE TABLE vermietet
   (	VERMIETERNR integer, 
	MIETERNR integer, 
	WOHNNR integer, 
	PREIS integer, 
	 FOREIGN KEY (VERMIETERNR)
	  REFERENCES person (PERSNR), 
	 FOREIGN KEY (MIETERNR)
	  REFERENCES person (PERSNR), 
	 FOREIGN KEY (WOHNNR)
	  REFERENCES wohnung (WOHNNR)
   );
  
  COPY vermietet
  FROM 'C:\Users\Public\sql_trial_begin_vermietet.csv'
   DELIMITER ',' CSV HEADER;
  
UPDATE connections
SET conn_user = 'sql';

UPDATE connections
SET conn_pwd = 'sql';

UPDATE connections
SET conn_string = 'jdbc:postgresql://localhost:5432/sql';