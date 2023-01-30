--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2
-- Dumped by pg_dump version 13.2

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
-- Name: person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person (
    persnr integer NOT NULL,
    name character varying(10),
    stand character varying(15),
    beruf character varying(15)
);


ALTER TABLE public.person OWNER TO postgres;

--
-- Name: vermietet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vermietet (
    vermieternr integer,
    mieternr integer,
    wohnnr integer,
    preis numeric
);


ALTER TABLE public.vermietet OWNER TO postgres;

--
-- Name: wohnung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wohnung (
    wohnnr integer NOT NULL,
    bezirk integer,
    gross integer
);


ALTER TABLE public.wohnung OWNER TO postgres;

--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person (persnr, name, stand, beruf) FROM stdin;
1	Decker	ledig	Student
2	Meier	verheiratet	Maurer
3	Huber	ledig	Schlosser
4	Bauer	verwitwet	Beamter
5	Kaiser	verheiratet	Beamter
6	Richter	ledig	Anwalt
7	Weiss	ledig	Maler
8	Traxler	verheiratet	Student
9	Seyfried	ledig	Maurer
10	Weikinger	ledig	Lehrer
11	Rechberger	verheiratet	Hausmeister
12	Gangl	ledig	Hausmeister
13	Wallner	verwitwet	Beamter
14	Reiber	ledig	Student
\.


--
-- Data for Name: vermietet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vermietet (vermieternr, mieternr, wohnnr, preis) FROM stdin;
11	1	2	10000
11	2	7	12000
12	6	9	20000
12	9	4	10000
12	5	3	15000
6	7	11	15000
6	8	1	13000
5	11	6	20000
8	12	13	30000
8	13	5	13000
5	14	12	17000
\.


--
-- Data for Name: wohnung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wohnung (wohnnr, bezirk, gross) FROM stdin;
1	4	62
2	1	100
3	1	60
4	2	80
5	5	40
6	3	100
7	4	100
8	5	40
9	5	100
10	3	30
11	3	95
12	3	50
13	4	120
\.


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (persnr);


--
-- Name: wohnung wohnung_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wohnung
    ADD CONSTRAINT wohnung_pkey PRIMARY KEY (wohnnr);


--
-- Name: vermietet vermietet_mieternr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietet
    ADD CONSTRAINT vermietet_mieternr_fkey FOREIGN KEY (mieternr) REFERENCES public.person(persnr);


--
-- Name: vermietet vermietet_vermieternr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietet
    ADD CONSTRAINT vermietet_vermieternr_fkey FOREIGN KEY (vermieternr) REFERENCES public.person(persnr);


--
-- Name: vermietet vermietet_wohnnr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietet
    ADD CONSTRAINT vermietet_wohnnr_fkey FOREIGN KEY (wohnnr) REFERENCES public.wohnung(wohnnr);


--
-- Name: TABLE person; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.person TO sql;


--
-- Name: TABLE vermietet; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.vermietet TO sql;


--
-- Name: TABLE wohnung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.wohnung TO sql;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public REVOKE ALL ON TABLES  FROM postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT SELECT ON TABLES  TO sql;


--
-- PostgreSQL database dump complete
--

