-- Database: xquery

-- DROP DATABASE xquery;

CREATE DATABASE xquery
    WITH 
    OWNER = etutor
    ENCODING = 'UTF8'
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
	
	DROP TABLE BU_EXERCISE_URLS;
	
	CREATE TABLE BU_EXERCISE_URLS
   (	URL VARCHAR(100) NOT NULL, 
	HIDDEN_URL VARCHAR(100) NOT NULL, 
	EXERCISE integer NOT NULL
   );
   
   COPY BU_EXERCISE_URLS 
   FROM 'C:\Users\Public\Documents\xquery\BU_EXERCISE_URLS_DATA_TABLE.csv' 
   DELIMITER ',' 
   CSV HEADER;
   
   DROP TABLE ERROR_CATEGORIES;
   
   CREATE TABLE ERROR_CATEGORIES(
   	NAME VARCHAR(25) NOT NULL, 
	ID integer NOT NULL, 
	 CONSTRAINT CAT_ID PRIMARY KEY (ID)
   );
   
   COPY ERROR_CATEGORIES 
   FROM 'C:\Users\Public\Documents\xquery\ERROR_CATEGORIES_DATA_TABLE.csv' 
   DELIMITER ',' 
   CSV HEADER;
   
   DROP TABLE ERROR_GRADING_GROU;
   
   CREATE TABLE ERROR_GRADING_GROUP(
   	ID integer NOT NULL
   );
   
   
   COPY ERROR_GRADING_GROUP
   FROM 'C:\Users\Public\Documents\xquery\ERROR_GRADING_GROUP_DATA_TABLE.csv' 
   DELIMITER ',' 
   CSV HEADER;
   
   
  DROP TABLE ERROR_GRADINGS;
  
  CREATE TABLE ERROR_GRADINGS(
	  GRADING_GROUP integer NOT NULL, 
	 GRADING_LEVEL  integer     NOT NULL       , 
	 GRADING_CATEGORY  integer    NOT NULL       , 
	 MINUS_POINTS            numeric not null, 
	 CONSTRAINT  GRAD_UNIQUE  UNIQUE ( GRADING_GROUP ,  GRADING_CATEGORY )
  );
  
   COPY ERROR_GRADINGS       
   FROM 'C:\Users\Public\Documents\xquery\ERROR_GRADINGS_DATA_TABLE.csv' 
   DELIMITER ',' 
   CSV HEADER;
   
   DROP TABLE EXERCISE;
   
   CREATE TABLE EXERCISE(
    ID    integer  primary key     , 
	 QUERY  VARCHAR (1000     ) NOT NULL       , 
	 GRADINGS    integer  , 
	 POINTS   numeric   
   );
   
   COPY EXERCISE
   FROM 'C:\Users\Public\Documents\xquery\EXERCISE_DATA_TABLE.csv'
   DELIMITER ',' 
   CSV HEADER;
   
   DROP TABLE EXERCISE_URLS;
   
   CREATE TABLE EXERCISE_URLS(
   URL VARCHAR (100     ) NOT NULL       , 
	 HIDDEN_URL  VARCHAR (100     ) NOT NULL       , 
	 EXERCISE     integer   NOT NULL         
   );
   
   COPY EXERCISE_URLS
   FROM 'C:\Users\Public\Documents\xquery\EXERCISE_URLS_DATA_TABLE.csv'
   DELIMITER ',' 
   CSV HEADER;
   
   DROP TABLE SORTINGS;
   
   CREATE TABLE SORTINGS(
    XPATH  VARCHAR (50     ) NOT NULL       , 
	 EXERCISE  integer      NOT NULL       
   );
   
   COPY SORTINGS
   FROM 'C:\Users\Public\Documents\xquery\SORTINGS_DATA_TABLE.csv'
   DELIMITER ',' 
   CSV HEADER;
   
   DROP TABLE XMLDOCS;
   
   CREATE TABLE XMLDOCS(
    ID  integer      primary key    , 
	DOC XML       , 
	 FILENAME  VARCHAR (20     )       
   );
   
   COPY XMLDOCS 
   FROM 'C:\Users\Public\Documents\xquery\XMLDOCS_DATA_TABLE.csv'
   DELIMITER ',' 
   CSV HEADER;
   
 
   
  