-- Database: sql_trial_wohnungen

-- DROP DATABASE sql_trial_wohnungen;

/*CREATE DATABASE sql_trial_wohnungen
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
--    				DROP TABLE					--
---------------------------------------------------
DROP TABLE if exists person CASCADE;
DROP TABLE if exists vermietet CASCADE;
DROP TABLE if exists wohnung CASCADE;
---------------------------------------------------
--    				CREATE TABLE				--
---------------------------------------------------
CREATE TABLE person
   (	
	PERSNR integer, 
	NAME VARCHAR(10), 
	STAND VARCHAR(15), 
	BERUF VARCHAR(15), 
	 PRIMARY KEY (PERSNR)
   ); 
 CREATE TABLE wohnung 
   (	
	 WOHNNR integer,
	BEZIRK integer, 
	GROSS integer, 
	 PRIMARY KEY (WOHNNR)
   ); 
CREATE TABLE vermietet
   (	
	 VERMIETERNR integer, 
	MIETERNR integer, 
	WOHNNR integer, 
	PREIS numeric, 
	 FOREIGN KEY (VERMIETERNR)
	  REFERENCES person (PERSNR), 
	 FOREIGN KEY (MIETERNR)
	  REFERENCES person (PERSNR), 
	 FOREIGN KEY (WOHNNR)
	  REFERENCES wohnung (WOHNNR)
   );	
  
 ---------------------------------------------------
--    				COPY TABLE				--
---------------------------------------------------
   COPY person
   FROM   'C:\Users\Public\sql_wohnungen\PERSON_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;
   
   COPY wohnung
   FROM   'C:\Users\Public\sql_wohnungen\WOHNUNG_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;
   
   COPY vermietet
    FROM 'C:\Users\Public\sql_wohnungen\vermietet_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;