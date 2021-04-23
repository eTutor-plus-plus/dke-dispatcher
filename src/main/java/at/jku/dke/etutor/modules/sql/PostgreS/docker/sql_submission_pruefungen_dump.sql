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
-- Name: diplomprfg; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.diplomprfg (
    pnr character(5) NOT NULL,
    bezeichnung character varying(20) NOT NULL
);


ALTER TABLE public.diplomprfg OWNER TO postgres;

--
-- Name: lva; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lva (
    lvanr numeric(6,0) NOT NULL,
    bez character varying(20) NOT NULL
);


ALTER TABLE public.lva OWNER TO postgres;

--
-- Name: professor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.professor (
    profnr numeric NOT NULL,
    pname character varying(10) NOT NULL
);


ALTER TABLE public.professor OWNER TO postgres;

--
-- Name: student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student (
    matrnr numeric(7,0) NOT NULL,
    name character varying(10) NOT NULL,
    kennr numeric(3,0) NOT NULL
);


ALTER TABLE public.student OWNER TO postgres;

--
-- Name: voraussetzungen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voraussetzungen (
    lvanr numeric(6,0) NOT NULL,
    pnr character(5) NOT NULL
);


ALTER TABLE public.voraussetzungen OWNER TO postgres;

--
-- Name: zeugnis; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zeugnis (
    matrnr numeric(7,0) NOT NULL,
    lvanr numeric(6,0) NOT NULL,
    note numeric(1,0) NOT NULL,
    profnr numeric(1,0)
);


ALTER TABLE public.zeugnis OWNER TO postgres;

--
-- Data for Name: diplomprfg; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.diplomprfg (pnr, bezeichnung) FROM stdin;
PRF01	Grundlagen DBS
PRF02	Grundlagen INF
PRF03	Grundlagen BWL
PRF04	Grundlagen VWL
PRF05	Angewandte INF
\.


--
-- Data for Name: lva; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lva (lvanr, bez) FROM stdin;
100121	Expertensysteme
100122	Informatik A
100123	Informatik B
100124	EPROG
100125	Datenbanksysteme
100126	Rechnernetzwerke
100127	VWL
100128	Modellbildung
100200	BWL
100234	Datenschutz
\.


--
-- Data for Name: professor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.professor (profnr, pname) FROM stdin;
1	Meier
2	Schmidt
3	Frick
4	Huber
5	Wallner
\.


--
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student (matrnr, name, kennr) FROM stdin;
9526301	Meier	880
9525300	Huber	175
9526298	Bauer	176
9525301	Kaiser	176
9525303	Huber	175
9525650	Richter	880
9524300	Weiss	880
9525700	Traxler	177
9525701	Seyfried	175
9525702	Weikinger	880
9524790	Rechberger	880
9525791	Gangl	176
\.


--
-- Data for Name: voraussetzungen; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.voraussetzungen (lvanr, pnr) FROM stdin;
100121	PRF01
100122	PRF02
100123	PRF05
100124	PRF02
100125	PRF01
100126	PRF05
100127	PRF04
100128	PRF04
100200	PRF03
100234	PRF01
\.


--
-- Data for Name: zeugnis; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.zeugnis (matrnr, lvanr, note, profnr) FROM stdin;
9526301	100123	3	4
9525300	100123	2	5
9525301	100124	5	4
9525300	100234	3	1
9525650	100234	4	4
9525702	100123	1	4
9524300	100123	2	3
9525700	100122	3	4
9525701	100125	4	2
9525300	100126	2	2
9525701	100127	4	5
9524790	100128	5	5
\.


--
-- Name: diplomprfg diplomprfg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.diplomprfg
    ADD CONSTRAINT diplomprfg_pkey PRIMARY KEY (pnr);


--
-- Name: lva lva_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lva
    ADD CONSTRAINT lva_pkey PRIMARY KEY (lvanr);


--
-- Name: professor professor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professor
    ADD CONSTRAINT professor_pkey PRIMARY KEY (profnr);


--
-- Name: student student_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (matrnr);


--
-- Name: voraussetzungen voraussetzungen_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voraussetzungen
    ADD CONSTRAINT voraussetzungen_pkey PRIMARY KEY (lvanr, pnr);


--
-- Name: zeugnis zeugnis_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zeugnis
    ADD CONSTRAINT zeugnis_pkey PRIMARY KEY (matrnr, lvanr);


--
-- Name: voraussetzungen voraussetzungen_lvanr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voraussetzungen
    ADD CONSTRAINT voraussetzungen_lvanr_fkey FOREIGN KEY (lvanr) REFERENCES public.lva(lvanr);


--
-- Name: voraussetzungen voraussetzungen_pnr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voraussetzungen
    ADD CONSTRAINT voraussetzungen_pnr_fkey FOREIGN KEY (pnr) REFERENCES public.diplomprfg(pnr);


--
-- Name: zeugnis zeugnis_lvanr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zeugnis
    ADD CONSTRAINT zeugnis_lvanr_fkey FOREIGN KEY (lvanr) REFERENCES public.lva(lvanr);


--
-- Name: zeugnis zeugnis_matrnr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zeugnis
    ADD CONSTRAINT zeugnis_matrnr_fkey FOREIGN KEY (matrnr) REFERENCES public.student(matrnr);


--
-- Name: zeugnis zeugnis_profnr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zeugnis
    ADD CONSTRAINT zeugnis_profnr_fkey FOREIGN KEY (profnr) REFERENCES public.professor(profnr);


--
-- Name: TABLE diplomprfg; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.diplomprfg TO sql;


--
-- Name: TABLE lva; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.lva TO sql;


--
-- Name: TABLE professor; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.professor TO sql;


--
-- Name: TABLE student; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.student TO sql;


--
-- Name: TABLE voraussetzungen; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.voraussetzungen TO sql;


--
-- Name: TABLE zeugnis; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.zeugnis TO sql;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public REVOKE ALL ON TABLES  FROM postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT SELECT ON TABLES  TO sql;


--
-- PostgreSQL database dump complete
--

