-- Database: sql_trial_auftraege

-- DROP DATABASE sql_trial_auftraege;

/*CREATE DATABASE sql_trial_auftraege
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
DROP TABLE if exists artikel CASCADE;
DROP TABLE if exists auftragskopf CASCADE;
DROP TABLE if exists auftragszeile CASCADE;
DROP TABLE if exists kunde CASCADE;
DROP TABLE if exists vertreter CASCADE;
---------------------------------------------------
--			CREATE Tables				 		--
---------------------------------------------------
CREATE TABLE artikel
   (	
	 EAN VARCHAR(15 ), 
	BEZEICHNUNG VARCHAR(20 ), 
	KATEGORIE VARCHAR(10 ), 
	EKPREIS numeric
   );
---------------------------------------------------
 CREATE TABLE auftragskopf
   (	
	 NR numeric, 
	KUNDE numeric, 
	VERTRETER VARCHAR(3 ), 
	LIEFERDATUM DATE
   );
---------------------------------------------------
CREATE TABLE auftragszeile 
   (	
	NR numeric, 
	POSNR numeric, 
	ARTIKEL VARCHAR(15 ), 
	VKPREIS numeric, 
	MENGE numeric
   );
---------------------------------------------------
CREATE TABLE kunde
   (	
	NR numeric, 
	NAME VARCHAR(15 ), 
	GEBIET VARCHAR(5 )
   );
---------------------------------------------------
CREATE TABLE vertreter
   (	
	KURZZEICHEN VARCHAR(3 ), 
	NAME VARCHAR(20 ), 
	PROVISION numeric
   );
---------------------------------------------------
--			COPY Tables				 		--
--------------------------------------------------- 
COPY artikel
   FROM   'C:\Users\Public\sql_auftraege\artikel_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;  
COPY auftragskopf
   FROM   'C:\Users\Public\sql_auftraege\auftragskopf_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;  
COPY auftragszeile
   FROM   'C:\Users\Public\sql_auftraege\auftragszeile_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;  
COPY kunde
   FROM   'C:\Users\Public\sql_auftraege\kunde_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;  
COPY vertreter
   FROM   'C:\Users\Public\sql_auftraege\vertreter_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;  
