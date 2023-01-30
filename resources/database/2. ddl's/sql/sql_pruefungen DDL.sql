-- Database: sql_trial_pruefungen

--DROP DATABASE sql_trial_pruefungen;

/*CREATE DATABASE sql_trial_pruefungen
    WITH 
    OWNER = postgres
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
--			DROP Tables							--
---------------------------------------------------
DROP TABLE if exists diplomprfg CASCADE;
DROP TABLE if exists lva CASCADE;
DROP TABLE if exists professor CASCADE;
DROP TABLE if exists student CASCADE;
DROP TABLE if exists voraussetzungen CASCADE;
DROP TABLE if exists zeugnis CASCADE;
---------------------------------------------------
--			CREATE Tables				 		--
---------------------------------------------------
CREATE TABLE diplomprfg 
   (	
	PNR CHAR(5), 
	BEZEICHNUNG VARCHAR(20 ) NOT NULL, 
	 PRIMARY KEY (PNR)
   );
---------------------------------------------------
CREATE TABLE lva 
   (	
	LVANR numeric(6,0), 
	BEZ VARCHAR(20 ) NOT NULL , 
	 PRIMARY KEY (LVANR)
   ); 
---------------------------------------------------
CREATE TABLE professor
   (	
	PROFNR numeric ,
	PNAME VARCHAR(10) NOT NULL, 
	 PRIMARY KEY (PROFNR)
   );
---------------------------------------------------
CREATE TABLE student
   (	
	MATRNR numeric(7,0), 
	NAME VARCHAR(10 ) NOT NULL , 
	KENNR numeric(3,0) NOT NULL , 
	 PRIMARY KEY (MATRNR)
   );
---------------------------------------------------   
  CREATE TABLE voraussetzungen
   (	
	LVANR numeric(6,0), 
	PNR CHAR(5), 
	PRIMARY KEY (LVANR, PNR), 
	 FOREIGN KEY (LVANR)
	  REFERENCES lva (LVANR) , 
	 FOREIGN KEY (PNR)
	  REFERENCES diplomprfg (PNR) 
   );
---------------------------------------------------    
   
CREATE TABLE zeugnis
   (	
	MATRNR numeric(7,0), 
	LVANR numeric(6,0), 
	NOTE numeric(1,0) NOT NULL , 
	PROFNR numeric(1,0), 
	 PRIMARY KEY (MATRNR, LVANR), 
	 FOREIGN KEY (MATRNR)
	  REFERENCES student (MATRNR), 
	 FOREIGN KEY (LVANR)
	  REFERENCES lva (LVANR) , 
	 FOREIGN KEY (PROFNR)
	  REFERENCES professor (PROFNR) 
   );
   
---------------------------------------------------
--			COPY Tables				     		--
---------------------------------------------------
COPY diplomprfg
   FROM   'C:\Users\Public\sql_pruefungen\diplomprfg_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;  
COPY lva
   FROM   'C:\Users\Public\sql_pruefungen\lva_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;
COPY professor
   FROM   'C:\Users\Public\sql_pruefungen\professor_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;
COPY student
   FROM   'C:\Users\Public\sql_pruefungen\student_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;
COPY voraussetzungen
   FROM   'C:\Users\Public\sql_pruefungen\voraussetzungen_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;
COPY zeugnis
   FROM   'C:\Users\Public\sql_pruefungen\zeugnis_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;
   

