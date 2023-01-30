--
-- PostgreSQL database dump
--

-- Dumped from database version ???
-- Dumped by pg_dump version ???

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: exerciseconfiguration; Type: TABLE; Schema: public; Owner: etutor
--

CREATE TABLE public.exerciseconfiguration (
    config_id integer NOT NULL,
    max_activity integer NOT NULL,
    min_activity integer NOT NULL,
    max_logsize integer NOT NULL,
    min_logsize integer NOT NULL,
    configuration_number character varying(200) NOT NULL,
    CONSTRAINT "exerciseConfiguration_pkey" PRIMARY KEY (config_id)
);

ALTER TABLE public.exerciseconfiguration OWNER TO postgres;

--
-- Name: logs; Type: TABLE; Schema: public; Owner: etutor
--

CREATE TABLE  public.logs (
    log_id integer NOT NULL,
    exercise_id integer NOT NULL,
    trace character varying(2000)[]  NOT NULL,
    CONSTRAINT logs_pkey PRIMARY KEY (log_id)
);

ALTER TABLE public.logs OWNER TO postgres;

--
-- Name: randomexercises; Type: TABLE; Schema: public; Owner: etutor
--

CREATE TABLE public.randomexercises (
    exercise_id integer NOT NULL,
    or_one character varying(5500) NOT NULL,
    or_two character varying(5500)  NOT NULL,
    or_three character varying(5500) NOT NULL,
    or_four character varying(10000)  NOT NULL,
    aa_one character varying(2000)  NOT NULL,
    aa_two character varying(2000)  NOT NULL,
    aa_three character varying(2000)  NOT NULL,
    aa_four character varying(10000)  NOT NULL,
    aa_five character varying(10000)  NOT NULL,
    aa_six character varying(10000)  NOT NULL,
    aa_seven character varying(10000)  NOT NULL,
    config_id integer NOT NULL,
    CONSTRAINT randomexercises_pkey PRIMARY KEY (exercise_id),
    CONSTRAINT randomexercise_configfk FOREIGN KEY (config_id)
    REFERENCES public.exerciseconfiguration (config_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

ALTER TABLE public.randomexercises OWNER TO postgres;

