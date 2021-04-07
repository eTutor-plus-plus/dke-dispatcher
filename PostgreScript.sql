-- Database: etutorgrading

-- DROP DATABASE etutorgrading;

CREATE DATABASE etutorgrading
    WITH 
    OWNER = "Kevin"
    ENCODING = 'UTF8'
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
DROP TABLE submission CASCADE;
DROP TABLE grading CASCADE;
CREATE TABLE submission(
	submissionId varchar(50) primary key,
	tasktype varchar(9),
	exerciseId integer,
	userid integer,
	action varchar (9),
	diagnoselevel integer,
	submission varchar,
	maxPoints integer
);
CREATE TABLE grading(
	submissionId varchar(50) primary key,
	maxPoints decimal,
	points decimal,
	
	constraint fk foreign key(submissionId) references submission(submissionId)

);



