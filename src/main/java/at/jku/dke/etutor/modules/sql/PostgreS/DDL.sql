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
   
   UPDATE connections
	SET conn_user = 'sql';

	UPDATE connections
	SET conn_pwd = 'sql';

	UPDATE connections
	SET conn_string = 'jdbc:postgresql://localhost:5432/sql';
	
	
   COPY exercises
   FROM 'C:\Users\Public\sql_exercises.csv'
   DELIMITER ',' CSV HEADER;
   
  
   
/* SQL_TRIAL_BEGIN Tables from referenced oracle database */
 DROP TABLE vermietet CASCADE;
 DROP TABLE wohnung CASCADE;
 DROP TABLE person CASCADE;
 DROP TABLE aenderungs_protokoll CASCADE;
 DROP TABLE students CASCADE;
 DROP TABLE bestellposition CASCADE;
 DROP TABLE bauprodukt CASCADE;
 DROP TABLE belegung CASCADE;
 DROP TABLE kurs CASCADE;
 DROP TABLE student CASCADE;
 DROP TABLE bestellung CASCADE;
 DROP TABLE bookcopies CASCADE;
 DROP TABLE bookloan CASCADE;
 DROP TABLE book CASCADE;
 DROP TABLE borrower CASCADE;
 DROP TABLE branch CASCADE;
 DROP TABLE booking CASCADE;
 DROP TABLE room CASCADE;
 DROP TABLE hotel CASCADE;
 DROP TABLE guest CASCADE;
 DROP TABLE buchung CASCADE;
 DROP TABLE city CASCADE;
 DROP TABLE course CASCADE;
 DROP TABLE staff CASCADE;
 DROP TABLE distribute CASCADE;
 DROP TABLE record CASCADE;
 DROP TABLE genre CASCADE;
 DROP TABLE artist CASCADE;
 DROP TABLE dept CASCADE;
 DROP TABLE deptlocation CASCADE;
DROP TABLE enrollment CASCADE;
DROP TABLE node CASCADE;
DROP TABLE exit CASCADE;
DROP TABLE fakultaet CASCADE;
DROP TABLE filiale CASCADE;
DROP TABLE highway CASCADE;
DROP TABLE highwayexit CASCADE;
DROP TABLE highwayintersection CASCADE;
DROP TABLE intersection CASCADE;
DROP TABLE human CASCADE;
DROP TABLE inhaber CASCADE;
DROP TABLE studienrichtung CASCADE;
DROP TABLE koje CASCADE;
DROP TABLE gewinne CASCADE;


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

CREATE TABLE artist
   (	
	   	NAME VARCHAR(50) NOT NULL, 
		NATIONALITY VARCHAR(10), 
	 	CONSTRAINT SYS_C002893 PRIMARY KEY (NAME)
   );
   
	COPY artist
  	FROM 'C:\Users\Public\sql_trial_begin_artist.csv'
   	DELIMITER ',' CSV HEADER; 
	
CREATE TABLE bauprodukt
   (	
	 PROD_NR numeric(4,0) NOT NULL, 
	 BEZEICHNUNG VARCHAR(50) NOT NULL, 
	 CONSTRAINT SYS_C002850 PRIMARY KEY (PROD_NR)
   );
	
	COPY bauprodukt
  	FROM 'C:\Users\Public\sql_trial_begin_bauprodukt.csv'
   	DELIMITER ',' CSV HEADER;
	
	
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
	
CREATE TABLE fakultaet
   (	
	KURZBEZ VARCHAR(5 ) NOT NULL , 
	BEZEICHNUNG VARCHAR(50 ) NOT NULL , 
	CONSTRAINT SYS_C002923 PRIMARY KEY (KURZBEZ)
   );
   COPY fakultaet
 	FROM 'C:\Users\Public\FAKULTAET_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;	


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




 CREATE TABLE inhaber
   (	
	 NAME CHAR(20), 
	GEBDAT DATE, 
	ADRESSE CHAR(20)
   );
   COPY inhaber
   FROM 'C:\Users\Public\INHABER_DATA_TABLE.csv'
    DELIMITER ',' CSV HEADER;

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

 

