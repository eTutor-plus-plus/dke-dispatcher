-- Database: etutor

-- DROP DATABASE etutor;

/*CREATE DATABASE etutor
    WITH 
    OWNER = etutor
    ENCODING = 'UTF8'
    LC_COLLATE = 'German_Germany.1252'
    LC_CTYPE = 'German_Germany.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
*/
CREATE TABLE sql_solution_problems(
	exercise_id integer primary key,
	solution varchar(2048),
	syntax_error varchar(2048)
	
);
	
INSERT INTO sql_solution_problems	
SELECT s.exercise_id, a.attribute_value AS solution, r.description AS syntax_error
FROM submission s JOIN submission_attribute_mapping a ON s.submission_id = a.submission
				JOIN grading g ON s.submission_id = g.submission_id
				JOIN report r ON s.submission_id = r.submission_id
WHERE a.attribute_key = 'submission';
				