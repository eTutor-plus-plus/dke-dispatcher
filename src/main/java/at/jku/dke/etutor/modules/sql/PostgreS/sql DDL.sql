-- Database: sql

-- DROP DATABASE sql;
/*
CREATE DATABASE sql
    WITH 
    OWNER = etutor
    ENCODING = 'UTF8'
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	*/
	
---------------------------------------------------
--Granting Select Privileges To User "sql"--
---------------------------------------------------
REVOKE ALL
ON ALL TABLES IN SCHEMA public 
FROM PUBLIC;

GRANT SELECT
ON ALL TABLES IN SCHEMA public 
TO sql;

ALTER DEFAULT PRIVILEGES 
    FOR ROLE postgres   -- Alternatively "FOR USER"
    IN SCHEMA public
    GRANT SELECT ON TABLES TO sql;
---------------------------------------------------
-- reference tables with exercises and connections 
---------------------------------------------------
	
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
   
   UPDATE connections
	SET conn_user = 'sql';

	UPDATE connections
	SET conn_pwd = 'sql';

	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql_trial_begin';
	
	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql_submission_begin'
	WHERE id = 2 OR id = 4 OR id = 6;
	
	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql_trial_wohnungen'
	WHERE id = 7;
	
	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql_submission_wohnungen'
	WHERE id = 8;
	
	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql_trial_pruefungen'
	WHERE id = 11;
	
	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql_submission_pruefungen'
	WHERE id = 12;
	
	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql_trial_auftraege'
	WHERE id = 13;
	
	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql_submission_auftraege'
	WHERE id = 14;
	
	
   COPY exercises
   FROM 'C:\Users\Public\sql_exercises.csv'
   DELIMITER ',' CSV HEADER;
 
--------------------------------------------------- 
--Modifying exercises where oracle solution does not work in postgreSQL
--------------------------------------------------- 
UPDATE  exercises
SET solution = 'SELECT ean, bezeichnung, Spanne, kategorie FROM (select p1.ean, p1.bezeichnung,
((p1.listPreis - p1.ekPreis)/p1.ekPreis)*100 Spanne, p1.kategorie
from produkt p1
where ((p1.listPreis - p1.ekPreis)/p1.ekPreis) =
    (select MIN((p2.listPreis - p2.ekPreis)/p2.ekPreis)
     from produkt p2
     group by p2.kategorie
     having p2.kategorie = p1.kategorie) ) AS correctQuery ORDER BY ean, bezeichnung, Spanne, kategorie'
WHERE id = 13818;
   	