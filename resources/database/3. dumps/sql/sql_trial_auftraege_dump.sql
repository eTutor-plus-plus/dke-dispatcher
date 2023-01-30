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
-- Name: artikel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artikel (
    ean character varying(15),
    bezeichnung character varying(20),
    kategorie character varying(10),
    ekpreis numeric
);


ALTER TABLE public.artikel OWNER TO postgres;

--
-- Name: auftragskopf; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.auftragskopf (
    nr numeric,
    kunde numeric,
    vertreter character varying(3),
    lieferdatum date
);


ALTER TABLE public.auftragskopf OWNER TO postgres;

--
-- Name: auftragszeile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.auftragszeile (
    nr numeric,
    posnr numeric,
    artikel character varying(15),
    vkpreis numeric,
    menge numeric
);


ALTER TABLE public.auftragszeile OWNER TO postgres;

--
-- Name: kunde; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kunde (
    nr numeric,
    name character varying(15),
    gebiet character varying(5)
);


ALTER TABLE public.kunde OWNER TO postgres;

--
-- Name: vertreter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vertreter (
    kurzzeichen character varying(3),
    name character varying(20),
    provision numeric
);


ALTER TABLE public.vertreter OWNER TO postgres;

--
-- Data for Name: artikel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artikel (ean, bezeichnung, kategorie, ekpreis) FROM stdin;
0-666-4567-2-22	Autoschampoo	Pflege	35
0-777-4997-2-43	Glanzpolitur	Pflege	70
0-456-4887-3-22	Kaltwachs	Pflege	90
0-55-48567-16-2	Armaturenreiniger	Pflege	115
1-626-7767-2-99	Scheibenwischer	Ersatz	390
1-256-7700-2-00	Sportlenkrad	Ersatz	1300
1-333-7788-2-31	Gaspedal	Ersatz	800
2-446-7240-9-15	Rennsitz	Ersatz	18900
9-396-7510-9-00	Chromfelge	Ersatz	9000
3-211-1000-2-00	Sonnenschutz	Ersatz	160
7-2881-760-3-70	Schraubantenne	Audio	965
5-2671-955-5-55	Autoradio	Audio	5555
1-4444-652-8-88	CD-Wechsler	Audio	2345
3-1111-654-3-99	Lautsprecher	Audio	999
6-581-1766-3-45	Telefonhalter	Sonstiges	130
6-231-4777-3-15	Lenkradueberzug	Sonstiges	125
4-1161-730-3-88	Warndreieck	Sonstiges	350
0-4381-880-7-00	Verbandskasten	Sonstiges	965
5-6661-000-0-00	Abschleppseil	Sonstiges	225
\.


--
-- Data for Name: auftragskopf; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.auftragskopf (nr, kunde, vertreter, lieferdatum) FROM stdin;
1	11111	SL	1999-08-11
2	15882	SL	1999-09-12
3	24537	IL	1999-08-15
4	78436	GP	1999-09-10
5	95543	GP	1999-08-11
6	13451	GP	1999-09-20
7	11111	GP	1999-10-30
\.


--
-- Data for Name: auftragszeile; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.auftragszeile (nr, posnr, artikel, vkpreis, menge) FROM stdin;
1	1	0-666-4567-2-22	58.9	5
2	1	0-456-4887-3-22	229.9	1
2	2	2-446-7240-9-15	22500	1
3	1	5-2671-955-5-55	7000	1
4	1	6-581-1766-3-45	200	3
5	1	9-396-7510-9-00	12000	1
5	2	1-4444-652-8-88	4500	1
6	1	0-777-4997-2-43	125	3
1	1	4-1161-730-3-88	500	2
2	1	5-2671-955-5-55	6800	1
3	1	9-396-7510-9-00	12900	1
3	2	3-1111-654-3-99	1600	2
1	1	5-6661-000-0-00	530	3
1	2	7-2881-760-3-70	1300	1
1	3	0-4381-880-7-00	1350	1
2	1	0-55-48567-16-2	330	2
1	1	5-6661-000-0-00	500	3
2	1	6-231-4777-3-15	525	3
2	2	5-6661-000-0-00	490	5
2	3	9-396-7510-9-00	14000	1
1	1	3-1111-654-3-99	1300	2
2	1	1-256-7700-2-00	2200	1
3	1	5-6661-000-0-00	380	2
4	1	0-4381-880-7-00	1100	1
5	1	4-1161-730-3-88	510	3
6	1	5-6661-000-0-00	500	2
1	1	6-581-1766-3-45	150	1
2	1	5-2671-955-5-55	6900	1
3	1	6-231-4777-3-15	500	2
1	1	0-55-48567-16-2	390	5
2	1	5-6661-000-0-00	450	1
\.


--
-- Data for Name: kunde; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kunde (nr, name, gebiet) FROM stdin;
11111	Roller	OST
15882	Schieber	OST
78436	Flitzer	OST
98077	Sauser	OST
24537	Raser	WEST
13451	Stuerzer	WEST
22221	Bremser	WEST
99332	Kuppler	WEST
67891	Schleifer	WEST
95543	Kurver	NORD
55789	Schleuderer	NORD
87654	Unfaller	NORD
77777	Stenzer	NORD
\.


--
-- Data for Name: vertreter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vertreter (kurzzeichen, name, provision) FROM stdin;
GP	Ginzlinger-Philip	30
IL	Innaus-Lois	20
SL	Samsonig-Leopold	10
\.


--
-- Name: TABLE artikel; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.artikel TO sql;


--
-- Name: TABLE auftragskopf; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.auftragskopf TO sql;


--
-- Name: TABLE auftragszeile; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.auftragszeile TO sql;


--
-- Name: TABLE kunde; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.kunde TO sql;


--
-- Name: TABLE vertreter; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.vertreter TO sql;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public REVOKE ALL ON TABLES  FROM postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT SELECT ON TABLES  TO sql;


--
-- PostgreSQL database dump complete
--

