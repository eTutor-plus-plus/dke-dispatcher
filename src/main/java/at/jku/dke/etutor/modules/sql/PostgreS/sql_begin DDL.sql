-- Database: sql

--DROP DATABASE sql;
/*
CREATE DATABASE sql
    WITH 
    OWNER = sql
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
-- SCHEME OWNED BY SQL_TRIAL_BEGIN (ORACLE)
---------------------------------------------------
 DROP TABLE if exists wohnung CASCADE;
 DROP TABLE if exists person CASCADE;
 DROP TABLE if exists aenderungs_protokoll CASCADE;
 DROP TABLE if exists students CASCADE;
 DROP TABLE if exists bestellposition CASCADE;
 DROP TABLE if exists bauprodukt CASCADE;
 DROP TABLE if exists belegung CASCADE;
 DROP TABLE if exists kurs CASCADE;
 DROP TABLE if exists student CASCADE;
 DROP TABLE if exists bestellung CASCADE;
 DROP TABLE if exists bookcopies CASCADE;
 DROP TABLE if exists bookloan CASCADE;
 DROP TABLE if exists book CASCADE;
 DROP TABLE if exists borrower CASCADE;
 DROP TABLE if exists branch CASCADE;
 DROP TABLE if exists booking CASCADE;
 DROP TABLE if exists room CASCADE;
 DROP TABLE if exists hotel CASCADE;
 DROP TABLE if exists guest CASCADE;
 DROP TABLE if exists buchung CASCADE;
 DROP TABLE if exists city CASCADE;
 DROP TABLE if exists course CASCADE;
 DROP TABLE if exists staff CASCADE;
 DROP TABLE if exists distribute CASCADE;
 DROP TABLE if exists record CASCADE;
 DROP TABLE if exists genre CASCADE;
 DROP TABLE if exists artist CASCADE;
 DROP TABLE if exists dept CASCADE;
 DROP TABLE if exists deptlocation CASCADE;
DROP TABLE if exists enrollment CASCADE;
DROP TABLE if exists node CASCADE;
DROP TABLE if exists exit CASCADE;
DROP TABLE if exists fakultaet CASCADE;
DROP TABLE if exists filiale CASCADE;
DROP TABLE if exists highway CASCADE;
DROP TABLE if exists highwayexit CASCADE;
DROP TABLE if exists highwayintersection CASCADE;
DROP TABLE if exists intersection CASCADE;
DROP TABLE if exists human CASCADE;
DROP TABLE if exists inhaber CASCADE;
DROP TABLE if exists studienrichtung CASCADE;
DROP TABLE if exists koje CASCADE;
DROP TABLE if exists gewinne CASCADE;
-----------------------------------------
DROP TABLE if exists konto CASCADE;
DROP TABLE if exists kunde CASCADE;
DROP TABLE if exists lieferposition CASCADE;
DROP TABLE if exists lieferschein CASCADE;
DROP TABLE if exists liegtanstrasse CASCADE;
DROP TABLE if exists strasse CASCADE;
DROP TABLE if exists ort CASCADE;
DROP TABLE if exists location CASCADE;
DROP TABLE if exists mietet CASCADE;
DROP TABLE if exists orteverbindung CASCADE;
DROP TABLE if exists parent CASCADE;
DROP TABLE if exists product CASCADE;
DROP TABLE if exists produkt CASCADE;
DROP TABLE if exists project CASCADE;
DROP TABLE if exists time CASCADE;
DROP TABLE if exists purchase CASCADE;
DROP TABLE if exists rechnung CASCADE;
-----------------------------------------
DROP TABLE if exists rechnungpos CASCADE;
DROP TABLE if exists sales CASCADE;
DROP TABLE if exists segment CASCADE;
DROP TABLE if exists sortiment CASCADE;
DROP TABLE if exists sortiment_aenderungen CASCADE;
DROP TABLE if exists staffhotel CASCADE;
DROP TABLE if exists test CASCADE;
DROP TABLE if exists strassenart CASCADE;
DROP TABLE if exists track CASCADE;
DROP TABLE if exists workson CASCADE;
-----------------------------------------
DROP TABLE if exists benutzer CASCADE;
DROP TABLE if exists buch CASCADE;
DROP TABLE if exists entlehng CASCADE;
DROP TABLE if exists reserviert CASCADE;
DROP TABLE if exists studenten CASCADE;
DROP TABLE if exists wartung CASCADE;
DROP TABLE if exists terminal CASCADE;
---------------------------------------------------
--Tables--
---------------------------------------------------
  CREATE TABLE person
   (	PERSNR integer, 
	NAME VARCHAR(10), 
	STAND VARCHAR(15), 
	BERUF VARCHAR(15), 
	 PRIMARY KEY (PERSNR)
   ); 
 
   COPY person
   FROM   'C:\Users\Public\PERSON_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;

---------------------------------------------------
CREATE TABLE wohnung 
   (	
	 WOHNNR integer,
	 EIGENTUEMER integer,
	BEZIRK integer, 
	GROSS integer, 
	 PRIMARY KEY (WOHNNR)
   ); 
   

   COPY wohnung
   FROM   'C:\Users\Public\WOHNUNG_DATA_TABLE.csv'
   DELIMITER ',' CSV HEADER;
   
  
---------------------------------------------------

CREATE TABLE aenderungs_protokoll
   (	
	FILNR integer, 
	EAN CHAR(15), 
	VKPREIS numeric(7,2), 
	PREISRED numeric(7,2), 
	BESTAND numeric(3,0), 
	ZEIT DATE
   );

	COPY aenderungs_protokoll
  	FROM 'C:\Users\Public\sql_trial_begin_aenderungs_protokoll.csv'
   	DELIMITER ',' CSV HEADER;

---------------------------------------------------
CREATE TABLE artist
   (	
	   	NAME VARCHAR(50) NOT NULL, 
		NATIONALITY VARCHAR(10), 
	 	CONSTRAINT SYS_C002893 PRIMARY KEY (NAME)
   );
   
	COPY artist
  	FROM 'C:\Users\Public\sql_trial_begin_artist.csv'
   	DELIMITER ',' CSV HEADER; 

---------------------------------------------------	
CREATE TABLE bauprodukt
   (	
	 PROD_NR numeric(4,0) NOT NULL, 
	 BEZEICHNUNG VARCHAR(50) NOT NULL, 
	 CONSTRAINT SYS_C002850 PRIMARY KEY (PROD_NR)
   );
	
	COPY bauprodukt
  	FROM 'C:\Users\Public\sql_trial_begin_bauprodukt.csv'
   	DELIMITER ',' CSV HEADER;
	
	
---------------------------------------------------
CREATE TABLE students
   (	
	STUDENTID VARCHAR(7), 
	NAME VARCHAR(20), 
	COUNTRY VARCHAR(20), 
	PRIMARY KEY (STUDENTID)
   );
   COPY students
 	FROM 'C:\Users\Public\sql_trial_begin_students.csv'
   DELIMITER ',' CSV HEADER; 
	
	
	
---------------------------------------------------
CREATE TABLE student
   (	
	MATRIKELNR VARCHAR(7) NOT NULL, 
	NAME VARCHAR(20), 
	LAND VARCHAR(20), 
	CONSTRAINT SYS_C005003 PRIMARY KEY (MATRIKELNR)
   );
	COPY student
 	FROM 'C:\Users\Public\sql_trial_begin_student.csv'
    DELIMITER ',' CSV HEADER; 
	
	
	
---------------------------------------------------
CREATE TABLE kurs
   (	
	KURSNR numeric(2,0) NOT NULL, 
	NAME VARCHAR(40), 
	LEKTOR VARCHAR(20), 
	CONSTRAINT SYS_C005004 PRIMARY KEY (KURSNR)
   );
   	COPY kurs
 	FROM 'C:\Users\Public\sql_trial_begin_kurs.csv'
    DELIMITER ',' CSV HEADER;
   
 
---------------------------------------------------  
CREATE TABLE belegung
   (	
	 MATRIKELNR VARCHAR(7) NOT NULL, 
	KURSNR numeric(2,0) NOT NULL, 
	NOTE numeric(1,0), 
	 CONSTRAINT SYS_C005005 PRIMARY KEY (MATRIKELNR, KURSNR),
  
	 CONSTRAINT SYS_C005006 FOREIGN KEY (MATRIKELNR)
	  REFERENCES student (MATRIKELNR), 
	 CONSTRAINT SYS_C005007 FOREIGN KEY (KURSNR)
	  REFERENCES KURS (KURSNR) 
   ); 
   	COPY belegung
 	FROM 'C:\Users\Public\sql_trial_begin_belegung.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------   
CREATE TABLE bestellung
   (	
	 BESTELL_NR numeric(10,0) NOT NULL, 
	DATUM DATE NOT NULL, 
	ANSCHRIFT VARCHAR(100 ) NOT NULL , 
	 CONSTRAINT SYS_C002853 PRIMARY KEY (BESTELL_NR)
   );
   COPY bestellung
 	FROM 'C:\Users\Public\sql_trial_begin_bestellung.csv'
    DELIMITER ',' CSV HEADER;
	
---------------------------------------------------
CREATE TABLE bestellposition
   (	
	 BESTELL_NR numeric(10,0) NOT NULL, 
	POS_NR numeric(4,0) NOT NULL, 
	PROD_NR numeric(4,0) NOT NULL, 
	MENGE numeric(4,0) NOT NULL, 
	 CONSTRAINT SYS_C002856 PRIMARY KEY (BESTELL_NR, POS_NR),
 
	 CONSTRAINT SYS_C002857 FOREIGN KEY (PROD_NR)
	  REFERENCES bauprodukt (PROD_NR)
   );
   COPY bestellposition
 	FROM 'C:\Users\Public\sql_trial_begin_bestellposition.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------	
CREATE TABLE book
   (	
	BOOKID numeric(4,0), 
	TITLE VARCHAR(30), 
	AUTHOR VARCHAR(20), 
	PRIMARY KEY (BOOKID)
   );
    COPY book
 	FROM 'C:\Users\Public\sql_trial_begin_book.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------	
CREATE TABLE branch
   (	
	BRANCHID numeric(4,0), 
	BRANCHNAME VARCHAR(30 ), 
	BRANCHADDRESS VARCHAR(30 ), 
	PRIMARY KEY (BRANCHID)
   );
	COPY branch
 	FROM 'C:\Users\Public\sql_trial_begin_branch.csv'
    DELIMITER ',' CSV HEADER;



---------------------------------------------------
CREATE TABLE bookcopies
   (	
	 BOOKID numeric(4,0), 
	BRANCHID numeric(4,0), 
	NCOPIES numeric(4,0), 
	 PRIMARY KEY (BOOKID, BRANCHID),
  
	 FOREIGN KEY (BOOKID)
	  REFERENCES book (BOOKID), 
	 FOREIGN KEY (BRANCHID)
	  REFERENCES branch (BRANCHID) 
   );
	COPY bookcopies
 	FROM 'C:\Users\Public\sql_trial_begin_bookcopies.csv'
    DELIMITER ',' CSV HEADER;
	

---------------------------------------------------
CREATE TABLE guest
   (	
	GNO CHAR(4 ) NOT NULL , 
	NAME VARCHAR(20) NOT NULL , 
	ADDRESS VARCHAR(50 ), 
	CONSTRAINT PK_GUEST PRIMARY KEY (GNO)
   );
	COPY guest
 	FROM 'C:\Users\Public\sql_trial_begin_guest.csv'
    DELIMITER ',' CSV HEADER;


---------------------------------------------------
CREATE TABLE hotel
   (	
	HNO CHAR(4 ) NOT NULL , 
	NAME VARCHAR(20 ) NOT NULL , 
	ADDRESS VARCHAR(50 ), 
	CONSTRAINT PK_HOTEL PRIMARY KEY (HNO)
   );
	COPY hotel
 	FROM 'C:\Users\Public\sql_trial_begin_hotel.csv'
    DELIMITER ',' CSV HEADER;


---------------------------------------------------
CREATE TABLE room
   (	
	HNO CHAR(4) NOT NULL , 
	RNO CHAR(4 ) NOT NULL , 
	TYPE VARCHAR(10), 
	PRICE numeric(6,2), 
	 CONSTRAINT PK_ROOM PRIMARY KEY (HNO, RNO),
	 CONSTRAINT FK_ROOM_HNO FOREIGN KEY (HNO)
	  REFERENCES hotel (HNO)
   );
   COPY room
 	FROM 'C:\Users\Public\sql_trial_begin_room.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------
CREATE TABLE booking
   (	
	HNO CHAR(4) NOT NULL, 
	GNO CHAR(4) NOT NULL, 
	DATEFROM DATE NOT NULL, 
	DATETO DATE, 
	RNO CHAR(4), 
	 CONSTRAINT PK_BOOKING PRIMARY KEY (HNO, GNO, DATEFROM),
  
	 CONSTRAINT FK_BOOKING_GNO FOREIGN KEY (GNO)
	  REFERENCES guest (GNO), 
	 CONSTRAINT FK_BOOKING_HNO_RNO FOREIGN KEY (HNO, RNO)
	  REFERENCES room (HNO, RNO)
   );
	COPY booking
 	FROM 'C:\Users\Public\sql_trial_begin_booking.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------	
CREATE TABLE borrower
   (	
	CARDNO numeric(4,0), 
	NAME VARCHAR(20 ), 
	SEX VARCHAR(1 ), 
	ADDRESS VARCHAR(30 ), 
	BORROWERBRANCH numeric(4,0), 
	 PRIMARY KEY (CARDNO),
	 FOREIGN KEY (BORROWERBRANCH)
	  REFERENCES branch (BRANCHID) 
   );
	COPY borrower
 	FROM 'C:\Users\Public\sql_trial_begin_borrower.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------
CREATE TABLE bookloan
   (	
	BOOKID numeric(4,0), 
	BRANCHID numeric(4,0), 
	CARDNO numeric(4,0), 
	DATEOUT DATE, 
	DUEDATE DATE, 
	PRIMARY KEY (BOOKID, BRANCHID, CARDNO),
	 FOREIGN KEY (BOOKID)
	  REFERENCES book (BOOKID) , 
	 FOREIGN KEY (BRANCHID)
	  REFERENCES branch (BRANCHID) , 
	 FOREIGN KEY (CARDNO)
	  REFERENCES borrower (CARDNO) 
   );
	COPY bookloan
 	FROM 'C:\Users\Public\sql_trial_begin_bookloan.csv'
    DELIMITER ',' CSV HEADER;
	

---------------------------------------------------	
CREATE TABLE buchung
   (	
	 BUCHNGNR numeric, 
	VONKONTO numeric, 
	AUFKONTO numeric, 
	BETRAG numeric(10,2), 
	DATUM DATE
   );	
	COPY buchung
 	FROM 'C:\Users\Public\BUCHUNG_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
	
---------------------------------------------------
CREATE TABLE city
   (	
	 ZIP CHAR(4 ) NOT NULL , 
	NAME VARCHAR(30 ) NOT NULL , 
	 CONSTRAINT SYS_C003475 PRIMARY KEY (ZIP),
	 CONSTRAINT SYS_C003476 UNIQUE (NAME)
   );
   COPY city
 	FROM 'C:\Users\Public\CITY_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------	
CREATE TABLE course
   (	
	COURSECODE numeric(2,0), 
	NAME VARCHAR(40), 
	LECTURER VARCHAR(20 ), 
	PRIMARY KEY (COURSECODE)
   );
	 COPY course
 	FROM 'C:\Users\Public\COURSE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
	

---------------------------------------------------
CREATE TABLE dept
   (	
	 DNUM CHAR(4) NOT NULL , 
	DNAME VARCHAR(20 ) NOT NULL, 
	MANAGER CHAR(4 ), 
	MGRSTARTDATE DATE, 
	 PRIMARY KEY (DNUM)
   );
CREATE TABLE staff
   (	
	 SNUM CHAR(4 ) NOT NULL , 
	NAME VARCHAR(20 ) NOT NULL , 
	DOB DATE, 
	ADDRESS VARCHAR(30 ), 
	CITY VARCHAR(20 ), 
	GENDER CHAR(1 ), 
	SALARY numeric(9,2), 
	SUPERVISOR CHAR(4 ), 
	DNUM CHAR(4), 
	PRIMARY KEY (SNUM), 
	 /*FOREIGN KEY (SUPERVISOR)
	  REFERENCES staff (SNUM) , */
	 FOREIGN KEY (DNUM)
	  REFERENCES dept (DNUM) 
   );
   
	
	
	
	ALTER TABLE dept ADD CONSTRAINT
 	 dept_fk_staff FOREIGN KEY (MANAGER)
	  REFERENCES staff (SNUM) DEFERRABLE INITIALLY DEFERRED;
	  
	COPY dept
 	FROM 'C:\Users\Public\DEPT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER; 
	COPY staff
 	FROM 'C:\Users\Public\STAFF_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
	

---------------------------------------------------	  
CREATE TABLE deptlocation
   (	
	 DNUM CHAR(4 ) NOT NULL , 
	DCITY VARCHAR(20 ) NOT NULL , 
	 PRIMARY KEY (DNUM, DCITY), 
	 FOREIGN KEY (DNUM)
	  REFERENCES dept (DNUM)
   );	
	COPY deptlocation
 	FROM 'C:\Users\Public\DEPTLOCATION_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;


---------------------------------------------------
CREATE TABLE genre
   (	
	GENREID numeric NOT NULL, 
	NAME VARCHAR(30), 
	ANZAHL numeric(3,0), 
	CONSTRAINT SYS_C002894 PRIMARY KEY (GENREID)
   );
   COPY genre
 	FROM 'C:\Users\Public\GENRE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;


---------------------------------------------------
CREATE TABLE record 
   (	
	 RECORDID numeric NOT NULL, 
	ARTISTNAME VARCHAR(50 ), 
	TITLE VARCHAR(50 ), 
	RELEASEDATE DATE, 
	TYPE VARCHAR(20 ), 
	GENREID numeric, 
	 CONSTRAINT SYS_C002895 PRIMARY KEY (RECORDID), 
	 CONSTRAINT SYS_C002896 FOREIGN KEY (ARTISTNAME)
	  REFERENCES artist (NAME) , 
	 CONSTRAINT SYS_C002897 FOREIGN KEY (GENREID)
	  REFERENCES genre (GENREID)
   );
	COPY record
 	FROM 'C:\Users\Public\RECORD_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;


---------------------------------------------------
CREATE TABLE distribute
   (	
	 RECORDID numeric NOT NULL, 
	MEDIA VARCHAR(20 ) NOT NULL , 
	PRICE numeric, 
	 CONSTRAINT SYS_C002900 PRIMARY KEY (RECORDID, MEDIA), 
	 CONSTRAINT SYS_C002901 FOREIGN KEY (RECORDID)
	  REFERENCES record (RECORDID)
   );
   COPY distribute
 	FROM 'C:\Users\Public\DISTRIBUTE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------	
CREATE TABLE enrollment
   (	
	 STUDENTID VARCHAR(7 ), 
	COURSECODE numeric(2,0), 
	GRADE numeric(1,0), 
	 PRIMARY KEY (STUDENTID, COURSECODE), 
	 FOREIGN KEY (STUDENTID)
	  REFERENCES students (STUDENTID), 
	 FOREIGN KEY (COURSECODE)
	  REFERENCES course (COURSECODE)
   );
   COPY enrollment
 	FROM 'C:\Users\Public\ENROLLMENT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;	




---------------------------------------------------
CREATE TABLE node
   (	NODEID VARCHAR(5 ) NOT NULL , 
	LONGITUDE numeric(5,2) NOT NULL , 
	LATITUDE numeric(5,2) NOT NULL , 
	TYPE CHAR(13 ) NOT NULL , 
	 CONSTRAINT SYS_C003455 CHECK (longitude > 0) , 
	 CONSTRAINT SYS_C003456 CHECK (latitude > 0), 
	 CONSTRAINT SYS_C003457 CHECK (type IN ('exit',
    'intersection')), 
	 CONSTRAINT SYS_C003458 PRIMARY KEY (NODEID)
   ) ;
   COPY node
 	FROM 'C:\Users\Public\NODE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;	


---------------------------------------------------
CREATE TABLE exit 
   (	
	 NODEID VARCHAR(5 ) NOT NULL, 
	EXITNO CHAR(5 ) NOT NULL , 
	ZIP CHAR(4 ) NOT NULL , 
	 CONSTRAINT SYS_C003479 PRIMARY KEY (NODEID),
	CONSTRAINT SYS_C003480 UNIQUE (EXITNO), 
	 CONSTRAINT SYS_C003481 FOREIGN KEY (NODEID)
	  REFERENCES node (NODEID) ON DELETE CASCADE, 
	 CONSTRAINT SYS_C003482 FOREIGN KEY (ZIP)
	  REFERENCES city (ZIP)
   );
	COPY exit
 	FROM 'C:\Users\Public\EXIT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;	

---------------------------------------------------	
CREATE TABLE fakultaet
   (	
	KURZBEZ VARCHAR(5 ) NOT NULL , 
	BEZEICHNUNG VARCHAR(50 ) NOT NULL , 
	CONSTRAINT SYS_C002923 PRIMARY KEY (KURZBEZ)
   );
   COPY fakultaet
 	FROM 'C:\Users\Public\FAKULTAET_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;	


---------------------------------------------------
CREATE TABLE filiale
   (	
	FILNR numeric(3,0) NOT NULL , 
	INHNAME VARCHAR(20 ) NOT NULL , 
	STRASSE VARCHAR(30 ) NOT NULL , 
	PLZ numeric(4,0) NOT NULL , 
	CONSTRAINT SYS_C002821 PRIMARY KEY (FILNR)
   );
   COPY filiale
 	FROM 'C:\Users\Public\FILIALE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;	



---------------------------------------------------
CREATE TABLE gewinne
   (	
	FIRMA VARCHAR(100), 
	BRANCHE VARCHAR(100 ), 
	BUNDESLAND VARCHAR(100 ), 
	JAHR numeric(38,0), 
	UMSATZ numeric(10,2)
   );
   COPY gewinne
 	FROM 'C:\Users\Public\GEWINNE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------
CREATE TABLE highway
   (	
	CODE CHAR(5) NOT NULL , 
	NAME VARCHAR(30 ) NOT NULL , 
	STARTNODEID VARCHAR(5 ) NOT NULL , 
	ENDNODEID VARCHAR(5) NOT NULL , 
	 CONSTRAINT SYS_C003462 CHECK (NOT (startNodeID =
    endNodeID)), 
	 CONSTRAINT SYS_C003463 PRIMARY KEY (CODE),
	 CONSTRAINT SYS_C003464 UNIQUE (NAME),
	 CONSTRAINT SYS_C003465 FOREIGN KEY (STARTNODEID)
	  REFERENCES node (NODEID) , 
	 CONSTRAINT SYS_C003466 FOREIGN KEY (ENDNODEID)
	  REFERENCES node (NODEID) 
   );
   COPY highway
 	FROM 'C:\Users\Public\HIGHWAY_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------	
CREATE TABLE highwayexit
   (	
	CODE CHAR(5 ), 
	NODEID VARCHAR(5) NOT NULL, 
	ATKM numeric(5,2) NOT NULL , 
	 CONSTRAINT SYS_C003484 PRIMARY KEY (NODEID),
	 CONSTRAINT SYS_C003485 FOREIGN KEY (CODE)
	  REFERENCES highway (CODE), 
	 CONSTRAINT SYS_C003486 FOREIGN KEY (NODEID)
	  REFERENCES exit (NODEID)
   );
	COPY highwayexit
 	FROM 'C:\Users\Public\HIGHWAYEXIT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------	
CREATE TABLE intersection
   (	
	 NODEID VARCHAR(5 ) NOT NULL , 
	NAME VARCHAR(20 ) NOT NULL , 
	 CONSTRAINT SYS_C003488 PRIMARY KEY (NODEID), 
	 CONSTRAINT SYS_C003489 UNIQUE (NAME),
	 CONSTRAINT SYS_C003490 FOREIGN KEY (NODEID)
	  REFERENCES node (NODEID) ON DELETE CASCADE
   );
	COPY intersection
 	FROM 'C:\Users\Public\INTERSECTION_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------
CREATE TABLE highwayintersection
   (	
	 CODE CHAR(5 ) NOT NULL , 
	NODEID VARCHAR(5 ) NOT NULL , 
	ATKM numeric(5,2) NOT NULL , 
	 CONSTRAINT SYS_C003492 PRIMARY KEY (CODE, NODEID),
	 CONSTRAINT SYS_C003493 FOREIGN KEY (CODE)
	  REFERENCES highway (CODE) , 
	 CONSTRAINT SYS_C003494 FOREIGN KEY (NODEID)
	  REFERENCES intersection (NODEID)
   );	
	COPY highwayintersection
 	FROM 'C:\Users\Public\HIGHWAYINTERSECTION_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------
CREATE TABLE human
   (	
	 NAME VARCHAR(50 ) NOT NULL , 
	GENDER VARCHAR(1 ) NOT NULL , 
	AGE numeric(3,0) NOT NULL , 
	 CONSTRAINT SYS_C002867 PRIMARY KEY (NAME)
   );
   COPY human
 	FROM 'C:\Users\Public\HUMAN_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;




---------------------------------------------------
 CREATE TABLE inhaber
   (	
	 NAME CHAR(20), 
	GEBDAT DATE, 
	ADRESSE CHAR(20)
   );
   COPY inhaber
   FROM 'C:\Users\Public\INHABER_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

---------------------------------------------------
CREATE TABLE studienrichtung
   (	
	 KENNZAHL numeric NOT NULL , 
	GEHOERT_ZU VARCHAR(5 ) NOT NULL , 
	BEZEICHNUNG VARCHAR(50 ) NOT NULL , 
	 CONSTRAINT SYS_C002926 PRIMARY KEY (KENNZAHL),
   CONSTRAINT SYS_C002927 FOREIGN KEY (GEHOERT_ZU)
	  REFERENCES fakultaet (KURZBEZ) 
   );
   COPY studienrichtung
   FROM 'C:\Users\Public\STUDIENRICHTUNG_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;


---------------------------------------------------
CREATE TABLE koje
   (	
	 STANDNR VARCHAR(5) NOT NULL , 
	FLAECHE numeric NOT NULL , 
	LINKERNACHBAR VARCHAR(5 ), 
	RECHTERNACHBAR VARCHAR(5 ), 
	GEMIETET_VON numeric, 
	BESCHRIFTUNG VARCHAR(50 ), 
	 CONSTRAINT SYS_C002929 PRIMARY KEY (STANDNR) , 
	 /* CONSTRAINT SYS_C002930 FOREIGN KEY (LINKERNACHBAR)
	  REFERENCES koje (STANDNR) DEFERRABLE INITIALLY DEFERRED , 
	 CONSTRAINT SYS_C002931 FOREIGN KEY (RECHTERNACHBAR)
	  REFERENCES koje (STANDNR) DEFERRABLE INITIALLY DEFERRED, */
	 CONSTRAINT SYS_C002932 FOREIGN KEY (GEMIETET_VON)
	  REFERENCES studienrichtung (KENNZAHL) 
   );
	COPY koje
   FROM 'C:\Users\Public\KOJE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

CREATE TABLE konto
   (	
	 KONTONR numeric, 
	FILIALE numeric, 
	INHNAME CHAR(20 ), 
	GEBDAT DATE, 
	SALDO numeric(10,2)
   );
   COPY konto
   FROM 'C:\Users\Public\konto_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
   
-----------------------------------------
CREATE TABLE kunde
   (	
	KUNDENR numeric(5,0) NOT NULL, 
	NAME VARCHAR(30 ) NOT NULL, 
	BONSTUFE CHAR(1 ) NOT NULL , 
	CONSTRAINT SYS_C002824 PRIMARY KEY (KUNDENR)
   );
   COPY kunde
   FROM 'C:\Users\Public\KUNDE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE lieferschein
   (	
	LIEFER_NR numeric(10,0) NOT NULL, 
	DATUM DATE NOT NULL , 
	 CONSTRAINT SYS_C002859 PRIMARY KEY (LIEFER_NR)
   );
   COPY lieferschein
   FROM 'C:\Users\Public\lieferschein_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE lieferposition
   (	
	 LIEFER_NR numeric(10,0) NOT NULL , 
	LIEFERPOS_NR numeric(4,0) NOT NULL , 
	BESTELL_NR numeric(10,0) NOT NULL , 
	BESTELLPOS_NR numeric(4,0), 
	MENGE numeric(4,0) NOT NULL , 
	 CONSTRAINT SYS_C002862 PRIMARY KEY (LIEFER_NR, LIEFERPOS_NR),
	 CONSTRAINT SYS_C002863 FOREIGN KEY (LIEFER_NR)
	  REFERENCES lieferschein (LIEFER_NR) DEFERRABLE INITIALLY DEFERRED, 
	 CONSTRAINT SYS_C002864 FOREIGN KEY (BESTELL_NR, BESTELLPOS_NR)
	  REFERENCES bestellposition (BESTELL_NR, POS_NR) DEFERRABLE INITIALLY DEFERRED
   );
   COPY lieferposition
   FROM 'C:\Users\Public\LIEFERPOSITION_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE strasse
   (	
	 NAME VARCHAR(10 ) NOT NULL , 
	BEZEICHNUNG VARCHAR(30 ), 
	ORTVON VARCHAR(6 ), 
	ORTNACH VARCHAR(6 ), 
	STRASSENART VARCHAR(2), 
	LAENGE numeric(5,2), 
	 CONSTRAINT SYS_C002766 PRIMARY KEY (NAME)
	 /*,CONSTRAINT "SYS_C002767" FOREIGN KEY ("ORTVON")
	   REFERENCES "SQL_TRIAL_BEGIN"."ORT" ("PLZ") ON DELETE CASCADE DISABLE, 
	 CONSTRAINT "SYS_C002768" FOREIGN KEY ("ORTNACH")
	  REFERENCES "SQL_TRIAL_BEGIN"."ORT" ("PLZ") ON DELETE CASCADE DISABLE, 
	 CONSTRAINT "SYS_C002769" FOREIGN KEY ("STRASSENART")
	  REFERENCES "SQL_TRIAL_BEGIN"."STRASSENART" ("ART") ON DELETE CASCADE DISABLE*/
   );
   COPY strasse
   FROM 'C:\Users\Public\STRASSE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE ort
   (	
	PLZ VARCHAR(6) NOT NULL, 
	NAME VARCHAR(20), 
	 CONSTRAINT SYS_C002764 PRIMARY KEY (PLZ)
   );
   COPY ort
   FROM 'C:\Users\Public\ORT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
   
-----------------------------------------
CREATE TABLE liegtanstrasse
   (	
	STRASSENNAME VARCHAR(10 ) NOT NULL , 
	ORT VARCHAR(6 ) NOT NULL , 
	BEIKM numeric(5,2), 
	 CONSTRAINT SYS_C002770 PRIMARY KEY (STRASSENNAME, ORT),
	 CONSTRAINT SYS_C002771 FOREIGN KEY (STRASSENNAME)
	  REFERENCES strasse (NAME) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, 
	 CONSTRAINT SYS_C002772 FOREIGN KEY (ORT)
	  REFERENCES ort (PLZ) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED
   );
   COPY liegtanstrasse
   FROM 'C:\Users\Public\LIEGTANSTRASSE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE location
   (	
	LOCNO numeric(10,0), 
	NAME VARCHAR(10 ), 
	CITY VARCHAR(10 ), 
	STATE VARCHAR(5 ), 
	PRIMARY KEY (LOCNO)
   );
    COPY location
   FROM 'C:\Users\Public\LOCATION_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE mietet
   (	
	MIETERNR numeric, 
	WOHNNR numeric, 
	PREIS numeric, 
	VON DATE, 
	BIS DATE
   );
   COPY mietet
   FROM 'C:\Users\Public\MIETET_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE orteverbindung
   (	
	 STRASSENNAME VARCHAR(10 ) NOT NULL , 
	ORTVON VARCHAR(6 ) /*NOT NULL DISABLE*/, 
	ORTNACH VARCHAR(6 ) /*NOT NULL DISABLE*/, 
	DISTANZ numeric(5,2), 
	 CONSTRAINT SYS_C002933 PRIMARY KEY (STRASSENNAME, ORTVON, ORTNACH)
	  /* ,
	 CONSTRAINT "SYS_C002934" FOREIGN KEY ("STRASSENNAME")
	  REFERENCES "SQL_TRIAL_BEGIN"."STRASSE" ("NAME") ON DELETE CASCADE DISABLE, 
	 CONSTRAINT "SYS_C002935" FOREIGN KEY ("ORTVON")
	  REFERENCES "SQL_TRIAL_BEGIN"."ORT" ("PLZ") ON DELETE CASCADE DISABLE, 
	 CONSTRAINT "SYS_C002936" FOREIGN KEY ("ORTNACH")
	  REFERENCES "SQL_TRIAL_BEGIN"."ORT" ("PLZ") ON DELETE CASCADE DISABLE */
   );
   COPY orteverbindung
   FROM 'C:\Users\Public\ORTEVERBINDUNG_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE parent
   (	
	 PARENTNAME VARCHAR(50 ) NOT NULL , 
	CHILDNAME VARCHAR(50 ) NOT NULL , 
	 CONSTRAINT SYS_C002868 PRIMARY KEY (PARENTNAME, CHILDNAME)
	   /* ,CONSTRAINT "SYS_C002869" FOREIGN KEY ("PARENTNAME")
	  REFERENCES "SQL_TRIAL_BEGIN"."HUMAN" ("NAME") DISABLE, 
	 CONSTRAINT "SYS_C002870" FOREIGN KEY ("CHILDNAME")
	  REFERENCES "SQL_TRIAL_BEGIN"."HUMAN" ("NAME") DISABLE */
   );
   COPY parent
   FROM 'C:\Users\Public\parent_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

-----------------------------------------
CREATE TABLE product
   (	
	 PRODNO numeric(20,0), 
	NAME VARCHAR(15 ), 
	TYPE VARCHAR(10 ), 
	CAT VARCHAR(10 ), 
	 PRIMARY KEY (PRODNO)
   );
    COPY product
   FROM 'C:\Users\Public\PRODUCT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE produkt
   (	
	 EAN CHAR(15 ) NOT NULL , 
	BEZEICHNUNG VARCHAR(20 ) NOT NULL , 
	KATEGORIE VARCHAR(10 ) NOT NULL , 
	EKPREIS numeric(7,2) NOT NULL , 
	LISTPREIS numeric(7,2) NOT NULL , 
	 /* CONSTRAINT SYS_C002815 CHECK (ekPreis > 0) DISABLE, 
	 CONSTRAINT "SYS_C002816" CHECK (listPreis > 0) DISABLE, */
	 CONSTRAINT SYS_C002817 PRIMARY KEY (EAN), 
	 CONSTRAINT PRODUKT_UK41098377439043 UNIQUE (BEZEICHNUNG)
   );
    COPY produkt
   FROM 'C:\Users\Public\PRODUKT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
CREATE TABLE project
   (	
	 PNUM CHAR(4 ) NOT NULL , 
	PNAME VARCHAR(20 ) NOT NULL, 
	PCITY VARCHAR(20 ), 
	DNUM CHAR(4 ), 
	 PRIMARY KEY (PNUM), 
	 FOREIGN KEY (DNUM)
	  REFERENCES dept (DNUM) 
   );
    COPY project
   FROM 'C:\Users\Public\PROJECT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
 -----------------------------------------
 CREATE TABLE time
   (	
	 DAYNO numeric(3,0), 
	D CHAR(10 ), 
	M CHAR(7 ), 
	Y numeric(4,0), 
	 PRIMARY KEY (DAYNO)
   );
   COPY time
   FROM 'C:\Users\Public\TIME_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
  -----------------------------------------
 CREATE TABLE purchase
   (	
	 DAYNO numeric(2,0), 
	PRODNO numeric(20,0), 
	QTY numeric(6,0), 
	PRICE numeric(6,0), 
	 PRIMARY KEY (DAYNO, PRODNO), 
	 FOREIGN KEY (DAYNO)
	  REFERENCES time (DAYNO) , 
	 FOREIGN KEY (PRODNO)
	  REFERENCES product (PRODNO) 
   );
   COPY purchase
   FROM 'C:\Users\Public\PURCHASE_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------

CREATE TABLE rechnung
   (	
	 RECHNUNGNR numeric(5,0) NOT NULL , 
	DATUM DATE NOT NULL , 
	BEZAHLT CHAR(1 ) NOT NULL , 
	KUNDENR numeric(5,0) NOT NULL , 
	FILNR numeric(3,0) NOT NULL,
	PRIMARY KEY (RECHNUNGNR, DATUM)
	 /* 
	   ,CONSTRAINT "SYS_C002837" CHECK (bezahlt IN ('Y', 'N')) DISABLE, 
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "EXERCISE"  ENABLE, 
	 CONSTRAINT "SYS_C002839" FOREIGN KEY ("KUNDENR")
	  REFERENCES "SQL_TRIAL_BEGIN"."KUNDE" ("KUNDENR") DISABLE, 
	 CONSTRAINT "SYS_C002840" FOREIGN KEY ("FILNR")
	  REFERENCES "SQL_TRIAL_BEGIN"."FILIALE" ("FILNR") DISABLE*/
   );
      COPY rechnung
   FROM 'C:\Users\Public\RECHNUNG_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;


CREATE TABLE rechnungpos
   (	
	 RECHNUNGNR numeric(5,0) NOT NULL , 
	DATUM DATE NOT NULL , 
	EAN CHAR(15 ) NOT NULL , 
	POSITIONNR numeric(3,0) NOT NULL , 
	EINZELPREIS numeric(7,2) NOT NULL , 
	MENGE numeric(3,0) NOT NULL, 
	 /* CONSTRAINT "SYS_C002844" CHECK (einzelPreis > 0) DISABLE, 
	 CONSTRAINT "SYS_C002845" CHECK (menge > 0) DISABLE, */
	 PRIMARY KEY (RECHNUNGNR, DATUM, POSITIONNR)
	   /*, 
	 CONSTRAINT "SYS_C002847" FOREIGN KEY ("EAN")
	  REFERENCES "SQL_TRIAL_BEGIN"."PRODUKT" ("EAN") DISABLE, 
	 CONSTRAINT "SYS_C002848" FOREIGN KEY ("RECHNUNGNR", "DATUM")
	  REFERENCES "SQL_TRIAL_BEGIN"."RECHNUNG" ("RECHNUNGNR", "DATUM") DISABLE
	   */
   );
    COPY rechnungpos
   FROM 'C:\Users\Public\RECHNUNGPOS_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

-----------------------------------------
CREATE TABLE sales 
   (	
	 DAYNO numeric(3,0), 
	LOCNO numeric(3,0), 
	PRODNO numeric(3,0), 
	QTY numeric(3,0), 
	PRICE numeric(3,0), 
	 PRIMARY KEY (DAYNO, LOCNO, PRODNO),
	 FOREIGN KEY (DAYNO)
	  REFERENCES time (DAYNO) , 
	 FOREIGN KEY (LOCNO)
	  REFERENCES location (LOCNO) , 
	 FOREIGN KEY (PRODNO)
	  REFERENCES product (PRODNO) 
   );
	COPY sales
   FROM 'C:\Users\Public\SALES_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

-----------------------------------------
CREATE TABLE segment
   (	
	CODE CHAR(5 ) NOT NULL , 
	SEGID CHAR(5) NOT NULL , 
	FROMKM numeric(5,2) NOT NULL , 
	TOKM numeric(5,2) NOT NULL , 
	 /*
	   CONSTRAINT "SYS_C003469" CHECK (fromKM >= 0) DISABLE, 
	 CONSTRAINT "SYS_C003470" CHECK (toKM > 0) DISABLE, 
	 CONSTRAINT "SYS_C003471" CHECK (fromKM < toKM) DISABLE, 
	   CONSTRAINT "SYS_C003473" FOREIGN KEY ("CODE")
	  REFERENCES "SQL_TRIAL_BEGIN"."HIGHWAY" ("CODE") ON DELETE CASCADE DISABLE,
	   */
	 PRIMARY KEY (CODE, SEGID)	 
   );
   COPY segment
   FROM 'C:\Users\Public\SEGMENT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
-----------------------------------------
 CREATE TABLE sortiment
   (	
	 FILNR numeric(3,0) NOT NULL, 
	EAN CHAR(15 ) NOT NULL, 
	VKPREIS numeric(7,2) NOT NULL, 
	PREISRED numeric(7,2) NOT NULL , 
	BESTAND numeric(3,0) NOT NULL , 
	/*
	 CONSTRAINT "SYS_C002828" CHECK (vkPreis > 0) DISABLE, 
	 CONSTRAINT "SYS_C002829" CHECK (preisRed BETWEEN 0
    AND vkPreis) DISABLE, 
	 CONSTRAINT "SYS_C002830" CHECK (bestand >=0) DISABLE, 
	  */
	PRIMARY KEY (FILNR, EAN)
	  /*
	 CONSTRAINT "SYS_C002832" FOREIGN KEY ("FILNR")
	  REFERENCES "SQL_TRIAL_BEGIN"."FILIALE" ("FILNR") DISABLE, 
	 CONSTRAINT "SYS_C002833" FOREIGN KEY ("EAN")
	  REFERENCES "SQL_TRIAL_BEGIN"."PRODUKT" ("EAN") DISABLE
	*/	   
   );
   COPY sortiment
   FROM 'C:\Users\Public\SORTIMENT_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

-----------------------------------------
CREATE TABLE sortiment_aenderungen
   (	
	 FILNR numeric(3,0), 
	EAN CHAR(15 ), 
	VKPREIS numeric(7,2), 
	PREISRED numeric(7,2), 
	BESTAND numeric(3,0)
	   /*
	   , 
	 CONSTRAINT "SYS_C002941" FOREIGN KEY ("FILNR")
	  REFERENCES "SQL_TRIAL_BEGIN"."FILIALE" ("FILNR") DISABLE, 
	 CONSTRAINT "SYS_C002942" FOREIGN KEY ("EAN")
	  REFERENCES "SQL_TRIAL_BEGIN"."PRODUKT" ("EAN") DISABLE
	*/   
   );
   COPY sortiment_aenderungen
   FROM 'C:\Users\Public\SORTIMENT_AENDERUNGEN_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
   
-----------------------------------------  
CREATE TABLE staffhotel
   (	
	 SNO CHAR(4 ) NOT NULL, 
	NAME VARCHAR(16 ) NOT NULL , 
	ADDRESS VARCHAR(40 ), 
	POSITION VARCHAR(16 ), 
	SALARY numeric(8,2), 
	 PRIMARY KEY (SNO)	   
   );
   COPY staffhotel
    FROM 'C:\Users\Public\STAFFHOTEL_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
	
----------------------------------------- 
CREATE TABLE strassenart
   (	
	ART VARCHAR(2 ) NOT NULL , 
	BEZEICHNUNG VARCHAR(30 ), 
	PRIMARY KEY (ART)
   );
      COPY strassenart
    FROM 'C:\Users\Public\STRASSENART_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
----------------------------------------- 
CREATE TABLE test
   (	
	   ID numeric(10,0)
   );
    COPY test
    FROM 'C:\Users\Public\TEST_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

----------------------------------------- 
CREATE TABLE track
   (	
	 RECORDID numeric NOT NULL , 
	TNUMBER numeric NOT NULL , 
	TITLE VARCHAR(50 ), 
	LENGTH numeric, 
	PRIMARY KEY (RECORDID, TNUMBER)
	   /*
	 CONSTRAINT "SYS_C002899" FOREIGN KEY ("RECORDID")
	  REFERENCES "SQL_TRIAL_BEGIN"."RECORD" ("RECORDID") DISABLE
 		*/  
   );
    COPY track
    FROM 'C:\Users\Public\track_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

-----------------------------------------
 CREATE TABLE workson
   (	
	 SNUM CHAR(4 ) NOT NULL, 
	PNUM CHAR(4 ) NOT NULL , 
	HOURS numeric(2,0), 
	 PRIMARY KEY (SNUM, PNUM), 
	 FOREIGN KEY (SNUM)
	  REFERENCES staff (SNUM), 
	 FOREIGN KEY (PNUM)
	  REFERENCES project (PNUM) 
   );
   COPY workson
    FROM 'C:\Users\Public\workson_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
	

CREATE TABLE benutzer 
   (	
	 BENNR numeric, 
	NAME VARCHAR(20), 
	GEBDAT DATE, 
	ADRESSE VARCHAR(20), 
	 PRIMARY KEY (BENNR)
   );
   COPY benutzer
    FROM 'C:\Users\Public\benutzer_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
	
-----------------------------------------
CREATE TABLE buch
   (	
	 BUCHNR numeric, 
	TITEL VARCHAR(30), 
	AUTOR VARCHAR(20 ), 
	 PRIMARY KEY (BUCHNR)
   );
   COPY buch
    FROM 'C:\Users\Public\buch_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

	
-----------------------------------------

CREATE TABLE entlehng
   (	
	ENTLNGNR integer, 
	BUCH integer, 
	BENUTZER integer, 
	VON DATE, 
	BIS DATE, 
	 PRIMARY KEY (ENTLNGNR), 
	 UNIQUE (BUCH, BENUTZER, VON), 
	 FOREIGN KEY (BUCH)
	  REFERENCES buch (BUCHNR), 
	 FOREIGN KEY (BENUTZER)
	  REFERENCES benutzer (BENNR) 
   );
   COPY entlehng
    FROM 'C:\Users\Public\entlehng_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
------------------------------------------------
CREATE TABLE reserviert
   (	
	 TNO numeric, 
	TAG numeric, 
	STUNDE numeric, 
	MANO numeric NOT NULL , 
	 PRIMARY KEY (TNO, TAG, STUNDE)
   );
      COPY reserviert
    FROM 'C:\Users\Public\reserviert_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
--------------------------------------------------
CREATE TABLE studenten
   (	
	 MANO numeric, 
	NAME VARCHAR(10) NOT NULL , 
	RECHNER VARCHAR(10 ) NOT NULL, 
	 PRIMARY KEY (MANO) 
   );
   COPY studenten
    FROM 'C:\Users\Public\studenten_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
--------------------------------------------------	
CREATE TABLE terminal
   (	
	 TNO numeric, 
	RECHNER VARCHAR(10 ) NOT NULL , 
	 PRIMARY KEY (TNO)
   );
    COPY terminal
    FROM 'C:\Users\Public\terminal_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
	
--------------------------------------------------
 CREATE TABLE wartung
   (	
	RECHNER VARCHAR(10 ), 
	TAG numeric, 
	VONSTUNDE numeric, 
	BISSTUNDE numeric, 
	 PRIMARY KEY (RECHNER, TAG, VONSTUNDE)
   );
   COPY wartung
    FROM 'C:\Users\Public\wartung_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;
	
--------------------------------------------------



