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
-- Name: based_in; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.based_in (
    company_id integer NOT NULL,
    country_code character varying(3) NOT NULL
);


ALTER TABLE public.based_in OWNER TO postgres;

--
-- Name: company; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company (
    id integer NOT NULL,
    name character varying(64),
    year_established integer
);


ALTER TABLE public.company OWNER TO postgres;

--
-- Name: country; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.country (
    code character varying(3) NOT NULL,
    name character varying(64)
);


ALTER TABLE public.country OWNER TO postgres;

--
-- Name: listed_at; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.listed_at (
    company_id integer,
    stock_exchange_code character varying(10),
    date_valid date,
    share_price numeric(10,2),
    share_cnt integer
);


ALTER TABLE public.listed_at OWNER TO postgres;

--
-- Name: stock_exchange; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stock_exchange (
    code character varying(10) NOT NULL,
    name character varying(64),
    country_code character varying(3)
);


ALTER TABLE public.stock_exchange OWNER TO postgres;

--
-- Data for Name: based_in; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.based_in (company_id, country_code) FROM stdin;
3	AUT
3	CH
1	COL
1	GER
2	GER
3	GER
3	MEX
1	NL
1	USA
3	USA
4	USA
\.


--
-- Data for Name: company; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company (id, name, year_established) FROM stdin;
1	Bayer AG	1863
2	Fraport AG	1947
3	Oracle Corporation	1977
4	Facebook	2004
\.


--
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.country (code, name) FROM stdin;
AUT	Austria
CAN	Canada
CH	Swiss
COL	Columbia
FR	France
GER	Deutschland
HU	Ungary
ITA	Italia
MEX	Mexico
NL	Netherland
USA	United states of America
\.


--
-- Data for Name: listed_at; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.listed_at (company_id, stock_exchange_code, date_valid, share_price, share_cnt) FROM stdin;
1	BRX	2015-08-04	80.20	500
1	BRX	2015-12-04	90.00	400
1	BRX	2016-08-04	100.04	1000
1	FSE	2015-08-04	94.02	2000
1	FSE	2015-12-05	90.51	0
1	FSE	2016-08-04	94.02	2000
1	L&S	2015-08-04	120.00	2500
1	L&S	2015-12-04	110.00	2500
1	L&S	2016-08-04	93.60	2500
1	STU	2015-08-04	93.61	1520
1	STU	2015-12-04	93.61	1500
1	STU	2016-08-04	93.61	1500
1	XETRA	2015-08-04	94.00	2500
1	XETRA	2015-12-04	94.30	2700
1	XETRA	2016-08-04	94.25	2600
2	BRX	2015-12-04	46.50	855
2	BRX	2016-08-04	58.63	800
2	BRX	2016-08-05	60.21	900
2	L&S	2015-08-04	47.95	1200
2	L&S	2016-08-04	48.20	1000
2	L&S	2016-08-05	48.25	1000
3	BRX	2015-12-04	39.08	1200
3	BRX	2016-08-04	36.77	250
3	FSE	2016-08-04	36.59	1800
3	MXK	2016-08-04	775.00	100
3	NYSE	2016-08-04	40.90	1426
3	STU	2016-08-04	36.45	2000
3	XETRA	2016-08-04	36.80	455
4	L&S	2016-08-04	112.01	500
4	MXK	2015-08-04	2315.50	200
4	MXK	2015-12-04	2000.00	250
4	NASDAQ	2016-08-04	124.36	1500
4	STU	2016-08-04	112.17	1500
\.


--
-- Data for Name: stock_exchange; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stock_exchange (code, name, country_code) FROM stdin;
BRX	BX Swiss	CH
FSE	Frankfurter Börse	GER
L&S	Lang & Schwarz	AUT
MXK	Mexico Stock Exchange	MEX
NASDAQ	NASDAQ Stock Market	USA
NYSE	New York Stock Exchange	USA
STU	Stuttgarter Börse	GER
XETRA	XETRA	GER
\.


--
-- Name: based_in based_in_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.based_in
    ADD CONSTRAINT based_in_pkey PRIMARY KEY (company_id, country_code);


--
-- Name: company company_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company
    ADD CONSTRAINT company_pkey PRIMARY KEY (id);


--
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (code);


--
-- Name: stock_exchange stock_exchange_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_exchange
    ADD CONSTRAINT stock_exchange_pkey PRIMARY KEY (code);


--
-- Name: based_in based_in_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.based_in
    ADD CONSTRAINT based_in_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id);


--
-- Name: based_in based_in_country_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.based_in
    ADD CONSTRAINT based_in_country_code_fkey FOREIGN KEY (country_code) REFERENCES public.country(code);


--
-- Name: listed_at listed_at_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.listed_at
    ADD CONSTRAINT listed_at_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id);


--
-- Name: listed_at listed_at_stock_exchange_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.listed_at
    ADD CONSTRAINT listed_at_stock_exchange_code_fkey FOREIGN KEY (stock_exchange_code) REFERENCES public.stock_exchange(code);


--
-- Name: stock_exchange stock_exchange_country_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_exchange
    ADD CONSTRAINT stock_exchange_country_code_fkey FOREIGN KEY (country_code) REFERENCES public.country(code);


--
-- Name: TABLE based_in; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.based_in TO sql;


--
-- Name: TABLE company; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.company TO sql;


--
-- Name: TABLE country; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.country TO sql;


--
-- Name: TABLE listed_at; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.listed_at TO sql;


--
-- Name: TABLE stock_exchange; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.stock_exchange TO sql;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public REVOKE ALL ON TABLES  FROM postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT SELECT ON TABLES  TO sql;


--
-- PostgreSQL database dump complete
--

