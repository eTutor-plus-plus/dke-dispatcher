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
-- Name: aenderungs_protokoll; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aenderungs_protokoll (
    filnr integer,
    ean character(15),
    vkpreis numeric(7,2),
    preisred numeric(7,2),
    bestand numeric(3,0),
    zeit date
);


ALTER TABLE public.aenderungs_protokoll OWNER TO postgres;

--
-- Name: artist; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artist (
    name character varying(50) NOT NULL,
    nationality character varying(10)
);


ALTER TABLE public.artist OWNER TO postgres;

--
-- Name: bauprodukt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bauprodukt (
    prod_nr numeric(4,0) NOT NULL,
    bezeichnung character varying(50) NOT NULL
);


ALTER TABLE public.bauprodukt OWNER TO postgres;

--
-- Name: belegung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.belegung (
    matrikelnr character varying(7) NOT NULL,
    kursnr numeric(2,0) NOT NULL,
    note numeric(1,0)
);


ALTER TABLE public.belegung OWNER TO postgres;

--
-- Name: benutzer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.benutzer (
    bennr numeric NOT NULL,
    name character varying(20),
    gebdat date,
    adresse character varying(20)
);


ALTER TABLE public.benutzer OWNER TO postgres;

--
-- Name: bestellposition; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bestellposition (
    bestell_nr numeric(10,0) NOT NULL,
    pos_nr numeric(4,0) NOT NULL,
    prod_nr numeric(4,0) NOT NULL,
    menge numeric(4,0) NOT NULL
);


ALTER TABLE public.bestellposition OWNER TO postgres;

--
-- Name: bestellung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bestellung (
    bestell_nr numeric(10,0) NOT NULL,
    datum date NOT NULL,
    anschrift character varying(100) NOT NULL
);


ALTER TABLE public.bestellung OWNER TO postgres;

--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    bookid numeric(4,0) NOT NULL,
    title character varying(30),
    author character varying(20)
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: bookcopies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookcopies (
    bookid numeric(4,0) NOT NULL,
    branchid numeric(4,0) NOT NULL,
    ncopies numeric(4,0)
);


ALTER TABLE public.bookcopies OWNER TO postgres;

--
-- Name: booking; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.booking (
    hno character(4) NOT NULL,
    gno character(4) NOT NULL,
    datefrom date NOT NULL,
    dateto date,
    rno character(4)
);


ALTER TABLE public.booking OWNER TO postgres;

--
-- Name: bookloan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookloan (
    bookid numeric(4,0) NOT NULL,
    branchid numeric(4,0) NOT NULL,
    cardno numeric(4,0) NOT NULL,
    dateout date,
    duedate date
);


ALTER TABLE public.bookloan OWNER TO postgres;

--
-- Name: borrower; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.borrower (
    cardno numeric(4,0) NOT NULL,
    name character varying(20),
    sex character varying(1),
    address character varying(30),
    borrowerbranch numeric(4,0)
);


ALTER TABLE public.borrower OWNER TO postgres;

--
-- Name: branch; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.branch (
    branchid numeric(4,0) NOT NULL,
    branchname character varying(30),
    branchaddress character varying(30)
);


ALTER TABLE public.branch OWNER TO postgres;

--
-- Name: buch; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.buch (
    buchnr numeric NOT NULL,
    titel character varying(30),
    autor character varying(20)
);


ALTER TABLE public.buch OWNER TO postgres;

--
-- Name: buchung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.buchung (
    buchngnr numeric,
    vonkonto numeric,
    aufkonto numeric,
    betrag numeric(10,2),
    datum date
);


ALTER TABLE public.buchung OWNER TO postgres;

--
-- Name: city; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.city (
    zip character(4) NOT NULL,
    name character varying(30) NOT NULL
);


ALTER TABLE public.city OWNER TO postgres;

--
-- Name: course; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.course (
    coursecode numeric(2,0) NOT NULL,
    name character varying(40),
    lecturer character varying(20)
);


ALTER TABLE public.course OWNER TO postgres;

--
-- Name: dept; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dept (
    dnum character(4) NOT NULL,
    dname character varying(20) NOT NULL,
    manager character(4),
    mgrstartdate date
);


ALTER TABLE public.dept OWNER TO postgres;

--
-- Name: deptlocation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deptlocation (
    dnum character(4) NOT NULL,
    dcity character varying(20) NOT NULL
);


ALTER TABLE public.deptlocation OWNER TO postgres;

--
-- Name: distribute; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.distribute (
    recordid numeric NOT NULL,
    media character varying(20) NOT NULL,
    price numeric
);


ALTER TABLE public.distribute OWNER TO postgres;

--
-- Name: enrollment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.enrollment (
    studentid character varying(7) NOT NULL,
    coursecode numeric(2,0) NOT NULL,
    grade numeric(1,0)
);


ALTER TABLE public.enrollment OWNER TO postgres;

--
-- Name: entlehng; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.entlehng (
    entlngnr integer NOT NULL,
    buch integer,
    benutzer integer,
    von date,
    bis date
);


ALTER TABLE public.entlehng OWNER TO postgres;

--
-- Name: exit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exit (
    nodeid character varying(5) NOT NULL,
    exitno character(5) NOT NULL,
    zip character(4) NOT NULL
);


ALTER TABLE public.exit OWNER TO postgres;

--
-- Name: fakultaet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.fakultaet (
    kurzbez character varying(5) NOT NULL,
    bezeichnung character varying(50) NOT NULL
);


ALTER TABLE public.fakultaet OWNER TO postgres;

--
-- Name: filiale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.filiale (
    filnr numeric(3,0) NOT NULL,
    inhname character varying(20) NOT NULL,
    strasse character varying(30) NOT NULL,
    plz numeric(4,0) NOT NULL
);


ALTER TABLE public.filiale OWNER TO postgres;

--
-- Name: genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.genre (
    genreid numeric NOT NULL,
    name character varying(30),
    anzahl numeric(3,0)
);


ALTER TABLE public.genre OWNER TO postgres;

--
-- Name: gewinne; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gewinne (
    firma character varying(100),
    branche character varying(100),
    bundesland character varying(100),
    jahr numeric(38,0),
    umsatz numeric(10,2)
);


ALTER TABLE public.gewinne OWNER TO postgres;

--
-- Name: guest; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.guest (
    gno character(4) NOT NULL,
    name character varying(20) NOT NULL,
    address character varying(50)
);


ALTER TABLE public.guest OWNER TO postgres;

--
-- Name: highway; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.highway (
    code character(5) NOT NULL,
    name character varying(30) NOT NULL,
    startnodeid character varying(5) NOT NULL,
    endnodeid character varying(5) NOT NULL,
    CONSTRAINT sys_c003462 CHECK ((NOT ((startnodeid)::text = (endnodeid)::text)))
);


ALTER TABLE public.highway OWNER TO postgres;

--
-- Name: highwayexit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.highwayexit (
    code character(5),
    nodeid character varying(5) NOT NULL,
    atkm numeric(5,2) NOT NULL
);


ALTER TABLE public.highwayexit OWNER TO postgres;

--
-- Name: highwayintersection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.highwayintersection (
    code character(5) NOT NULL,
    nodeid character varying(5) NOT NULL,
    atkm numeric(5,2) NOT NULL
);


ALTER TABLE public.highwayintersection OWNER TO postgres;

--
-- Name: hotel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hotel (
    hno character(4) NOT NULL,
    name character varying(20) NOT NULL,
    address character varying(50)
);


ALTER TABLE public.hotel OWNER TO postgres;

--
-- Name: human; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.human (
    name character varying(50) NOT NULL,
    gender character varying(1) NOT NULL,
    age numeric(3,0) NOT NULL
);


ALTER TABLE public.human OWNER TO postgres;

--
-- Name: inhaber; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inhaber (
    name character(20),
    gebdat date,
    adresse character(20)
);


ALTER TABLE public.inhaber OWNER TO postgres;

--
-- Name: intersection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.intersection (
    nodeid character varying(5) NOT NULL,
    name character varying(20) NOT NULL
);


ALTER TABLE public.intersection OWNER TO postgres;

--
-- Name: koje; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.koje (
    standnr character varying(5) NOT NULL,
    flaeche numeric NOT NULL,
    linkernachbar character varying(5),
    rechternachbar character varying(5),
    gemietet_von numeric,
    beschriftung character varying(50)
);


ALTER TABLE public.koje OWNER TO postgres;

--
-- Name: konto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.konto (
    kontonr numeric,
    filiale numeric,
    inhname character(20),
    gebdat date,
    saldo numeric(10,2)
);


ALTER TABLE public.konto OWNER TO postgres;

--
-- Name: kunde; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kunde (
    kundenr numeric(5,0) NOT NULL,
    name character varying(30) NOT NULL,
    bonstufe character(1) NOT NULL
);


ALTER TABLE public.kunde OWNER TO postgres;

--
-- Name: kurs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kurs (
    kursnr numeric(2,0) NOT NULL,
    name character varying(40),
    lektor character varying(20)
);


ALTER TABLE public.kurs OWNER TO postgres;

--
-- Name: lieferposition; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lieferposition (
    liefer_nr numeric(10,0) NOT NULL,
    lieferpos_nr numeric(4,0) NOT NULL,
    bestell_nr numeric(10,0) NOT NULL,
    bestellpos_nr numeric(4,0),
    menge numeric(4,0) NOT NULL
);


ALTER TABLE public.lieferposition OWNER TO postgres;

--
-- Name: lieferschein; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lieferschein (
    liefer_nr numeric(10,0) NOT NULL,
    datum date NOT NULL
);


ALTER TABLE public.lieferschein OWNER TO postgres;

--
-- Name: liegtanstrasse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.liegtanstrasse (
    strassenname character varying(10) NOT NULL,
    ort character varying(6) NOT NULL,
    beikm numeric(5,2)
);


ALTER TABLE public.liegtanstrasse OWNER TO postgres;

--
-- Name: location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location (
    locno numeric(10,0) NOT NULL,
    name character varying(10),
    city character varying(10),
    state character varying(5)
);


ALTER TABLE public.location OWNER TO postgres;

--
-- Name: mietet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mietet (
    mieternr numeric,
    wohnnr numeric,
    preis numeric,
    von date,
    bis date
);


ALTER TABLE public.mietet OWNER TO postgres;

--
-- Name: node; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.node (
    nodeid character varying(5) NOT NULL,
    longitude numeric(5,2) NOT NULL,
    latitude numeric(5,2) NOT NULL,
    type character(13) NOT NULL,
    CONSTRAINT sys_c003455 CHECK ((longitude > (0)::numeric)),
    CONSTRAINT sys_c003456 CHECK ((latitude > (0)::numeric)),
    CONSTRAINT sys_c003457 CHECK ((type = ANY (ARRAY['exit'::bpchar, 'intersection'::bpchar])))
);


ALTER TABLE public.node OWNER TO postgres;

--
-- Name: ort; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ort (
    plz character varying(6) NOT NULL,
    name character varying(20)
);


ALTER TABLE public.ort OWNER TO postgres;

--
-- Name: orteverbindung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orteverbindung (
    strassenname character varying(10) NOT NULL,
    ortvon character varying(6) NOT NULL,
    ortnach character varying(6) NOT NULL,
    distanz numeric(5,2)
);


ALTER TABLE public.orteverbindung OWNER TO postgres;

--
-- Name: parent; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parent (
    parentname character varying(50) NOT NULL,
    childname character varying(50) NOT NULL
);


ALTER TABLE public.parent OWNER TO postgres;

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
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    prodno numeric(20,0) NOT NULL,
    name character varying(15),
    type character varying(10),
    cat character varying(10)
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: produkt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produkt (
    ean character(15) NOT NULL,
    bezeichnung character varying(20) NOT NULL,
    kategorie character varying(10) NOT NULL,
    ekpreis numeric(7,2) NOT NULL,
    listpreis numeric(7,2) NOT NULL
);


ALTER TABLE public.produkt OWNER TO postgres;

--
-- Name: project; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project (
    pnum character(4) NOT NULL,
    pname character varying(20) NOT NULL,
    pcity character varying(20),
    dnum character(4)
);


ALTER TABLE public.project OWNER TO postgres;

--
-- Name: purchase; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase (
    dayno numeric(2,0) NOT NULL,
    prodno numeric(20,0) NOT NULL,
    qty numeric(6,0),
    price numeric(6,0)
);


ALTER TABLE public.purchase OWNER TO postgres;

--
-- Name: rechnung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rechnung (
    rechnungnr numeric(5,0) NOT NULL,
    datum date NOT NULL,
    bezahlt character(1) NOT NULL,
    kundenr numeric(5,0) NOT NULL,
    filnr numeric(3,0) NOT NULL
);


ALTER TABLE public.rechnung OWNER TO postgres;

--
-- Name: rechnungpos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rechnungpos (
    rechnungnr numeric(5,0) NOT NULL,
    datum date NOT NULL,
    ean character(15) NOT NULL,
    positionnr numeric(3,0) NOT NULL,
    einzelpreis numeric(7,2) NOT NULL,
    menge numeric(3,0) NOT NULL
);


ALTER TABLE public.rechnungpos OWNER TO postgres;

--
-- Name: record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.record (
    recordid numeric NOT NULL,
    artistname character varying(50),
    title character varying(50),
    releasedate date,
    type character varying(20),
    genreid numeric
);


ALTER TABLE public.record OWNER TO postgres;

--
-- Name: reserviert; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reserviert (
    tno numeric NOT NULL,
    tag numeric NOT NULL,
    stunde numeric NOT NULL,
    mano numeric NOT NULL
);


ALTER TABLE public.reserviert OWNER TO postgres;

--
-- Name: room; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.room (
    hno character(4) NOT NULL,
    rno character(4) NOT NULL,
    type character varying(10),
    price numeric(6,2)
);


ALTER TABLE public.room OWNER TO postgres;

--
-- Name: sales; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales (
    dayno numeric(3,0) NOT NULL,
    locno numeric(3,0) NOT NULL,
    prodno numeric(3,0) NOT NULL,
    qty numeric(3,0),
    price numeric(3,0)
);


ALTER TABLE public.sales OWNER TO postgres;

--
-- Name: segment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.segment (
    code character(5) NOT NULL,
    segid character(5) NOT NULL,
    fromkm numeric(5,2) NOT NULL,
    tokm numeric(5,2) NOT NULL
);


ALTER TABLE public.segment OWNER TO postgres;

--
-- Name: sortiment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sortiment (
    filnr numeric(3,0) NOT NULL,
    ean character(15) NOT NULL,
    vkpreis numeric(7,2) NOT NULL,
    preisred numeric(7,2) NOT NULL,
    bestand numeric(3,0) NOT NULL
);


ALTER TABLE public.sortiment OWNER TO postgres;

--
-- Name: sortiment_aenderungen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sortiment_aenderungen (
    filnr numeric(3,0),
    ean character(15),
    vkpreis numeric(7,2),
    preisred numeric(7,2),
    bestand numeric(3,0)
);


ALTER TABLE public.sortiment_aenderungen OWNER TO postgres;

--
-- Name: staff; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.staff (
    snum character(4) NOT NULL,
    name character varying(20) NOT NULL,
    dob date,
    address character varying(30),
    city character varying(20),
    gender character(1),
    salary numeric(9,2),
    supervisor character(4),
    dnum character(4)
);


ALTER TABLE public.staff OWNER TO postgres;

--
-- Name: staffhotel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.staffhotel (
    sno character(4) NOT NULL,
    name character varying(16) NOT NULL,
    address character varying(40),
    "position" character varying(16),
    salary numeric(8,2)
);


ALTER TABLE public.staffhotel OWNER TO postgres;

--
-- Name: strasse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.strasse (
    name character varying(10) NOT NULL,
    bezeichnung character varying(30),
    ortvon character varying(6),
    ortnach character varying(6),
    strassenart character varying(2),
    laenge numeric(5,2)
);


ALTER TABLE public.strasse OWNER TO postgres;

--
-- Name: strassenart; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.strassenart (
    art character varying(2) NOT NULL,
    bezeichnung character varying(30)
);


ALTER TABLE public.strassenart OWNER TO postgres;

--
-- Name: student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student (
    matrikelnr character varying(7) NOT NULL,
    name character varying(20),
    land character varying(20)
);


ALTER TABLE public.student OWNER TO postgres;

--
-- Name: studenten; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.studenten (
    mano numeric NOT NULL,
    name character varying(10) NOT NULL,
    rechner character varying(10) NOT NULL
);


ALTER TABLE public.studenten OWNER TO postgres;

--
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
    studentid character varying(7) NOT NULL,
    name character varying(20),
    country character varying(20)
);


ALTER TABLE public.students OWNER TO postgres;

--
-- Name: studienrichtung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.studienrichtung (
    kennzahl numeric NOT NULL,
    gehoert_zu character varying(5) NOT NULL,
    bezeichnung character varying(50) NOT NULL
);


ALTER TABLE public.studienrichtung OWNER TO postgres;

--
-- Name: terminal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.terminal (
    tno numeric NOT NULL,
    rechner character varying(10) NOT NULL
);


ALTER TABLE public.terminal OWNER TO postgres;

--
-- Name: test; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test (
    id numeric(10,0)
);


ALTER TABLE public.test OWNER TO postgres;

--
-- Name: time; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."time" (
    dayno numeric(3,0) NOT NULL,
    d character(10),
    m character(7),
    y numeric(4,0)
);


ALTER TABLE public."time" OWNER TO postgres;

--
-- Name: track; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.track (
    recordid numeric NOT NULL,
    tnumber numeric NOT NULL,
    title character varying(50),
    length numeric
);


ALTER TABLE public.track OWNER TO postgres;

--
-- Name: wartung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wartung (
    rechner character varying(10) NOT NULL,
    tag numeric NOT NULL,
    vonstunde numeric NOT NULL,
    bisstunde numeric
);


ALTER TABLE public.wartung OWNER TO postgres;

--
-- Name: wohnung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wohnung (
    wohnnr integer NOT NULL,
    eigentuemer integer,
    bezirk integer,
    gross integer
);


ALTER TABLE public.wohnung OWNER TO postgres;

--
-- Name: workson; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.workson (
    snum character(4) NOT NULL,
    pnum character(4) NOT NULL,
    hours numeric(2,0)
);


ALTER TABLE public.workson OWNER TO postgres;

--
-- Data for Name: aenderungs_protokoll; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.aenderungs_protokoll (filnr, ean, vkpreis, preisred, bestand, zeit) FROM stdin;
\.


--
-- Data for Name: artist; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artist (name, nationality) FROM stdin;
Sigi Saenger	AT
Trude Traeller	DE
Johann Jodler	AT
Carlo Cravallo	IT
\.


--
-- Data for Name: bauprodukt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bauprodukt (prod_nr, bezeichnung) FROM stdin;
1	Zementsack
2	Ziegel
3	Betonmischmaschine
4	Schalltafel
11	Ziegel grau
\.


--
-- Data for Name: belegung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.belegung (matrikelnr, kursnr, note) FROM stdin;
9978653	8	1
9978653	9	2
9978653	10	4
9978653	13	3
0034534	1	1
5489007	1	2
5489007	11	2
5489007	12	4
0299123	7	1
0299123	6	5
0178543	10	2
7813037	1	5
9954632	2	2
9954632	3	3
9954632	5	1
9755785	4	4
9755785	1	1
9856564	2	4
9876542	9	3
9876542	10	2
9800745	11	2
9800745	3	1
9900965	10	2
9900965	11	3
9900965	12	4
9955432	2	1
7732123	6	1
7732123	13	2
0023132	8	1
0023132	9	1
0023132	10	2
0199054	13	5
0203032	12	3
0203032	5	3
0278321	1	2
0278321	11	3
9722543	1	2
9677432	10	1
\.


--
-- Data for Name: benutzer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.benutzer (bennr, name, gebdat, adresse) FROM stdin;
1	Steiner Heinz	2042-10-10	Linz
2	Hafner Eleonore	2060-03-09	Salzburg
3	Wopfner Karin	1975-08-13	Innsbruck
4	Steindl Kurt	2036-04-24	Wien
5	Hofreiter Martin	1980-02-12	Graz
6	Gruber Martha	2050-07-29	Klagenfurt
\.


--
-- Data for Name: bestellposition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bestellposition (bestell_nr, pos_nr, prod_nr, menge) FROM stdin;
1001	1	2	200
1001	2	3	1
1001	3	4	10
1002	1	1	150
1002	2	2	120
1002	3	4	120
1003	1	1	10
1003	2	2	40
1004	1	1	200
1004	2	2	300
1005	1	2	250
1006	1	1	90
1007	1	1	90
1008	1	1	120
1009	1	1	100
1010	1	1	90
1011	1	1	190
1012	1	1	90
1013	1	1	90
1014	1	1	90
1006	2	11	1000
1007	2	11	500
1008	2	11	350
1009	2	11	200
1010	2	11	400
1011	2	11	300
1012	2	11	400
1013	2	11	100
1014	2	11	300
\.


--
-- Data for Name: bestellung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bestellung (bestell_nr, datum, anschrift) FROM stdin;
1001	2003-10-13	Lindenstrasse 11b
1002	2002-10-11	Uniweg 1
1003	2003-10-01	Lindenstrasse 11b
1004	2002-06-05	Uniweg 1
1005	2003-10-13	Uniweg 1
1006	2003-10-13	Lindenstrasse 11b
1007	2003-10-13	Lindenstrasse 11b
1008	2003-10-13	Lindenstrasse 11b
1009	2003-10-13	Lindenstrasse 11b
1010	2003-10-13	Lindenstrasse 11b
1011	2003-10-13	Lindenstrasse 11b
1012	2003-10-13	Lindenstrasse 11b
1013	2003-10-13	Lindenstrasse 11b
1014	2003-10-13	Lindenstrasse 11b
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (bookid, title, author) FROM stdin;
1	Misry	Stephen King
2	Wizard and Glass	Stephen King
3	The Drawing of the Three	Stephen King
4	What We're Reading	Leslie Yerkes
5	The Art of the Advantage	Kaihan Krippendorff
6	Harry Potter (1)	J. K. Rowling
7	Harry Potter (2)	J. K. Rowling
\.


--
-- Data for Name: bookcopies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bookcopies (bookid, branchid, ncopies) FROM stdin;
1	1	10
1	3	2
1	4	1
1	5	20
2	1	1
2	4	5
3	3	3
3	5	8
4	1	10
5	4	5
5	5	4
6	1	5
6	3	20
6	4	5
7	1	5
7	4	9
\.


--
-- Data for Name: booking; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.booking (hno, gno, datefrom, dateto, rno) FROM stdin;
H001	G001	1997-05-01	\N	R101
H002	G003	1997-03-25	1997-03-27	R203
H003	G005	1997-03-15	1997-03-20	R101
H004	G006	1997-04-01	\N	R102
H005	G004	1997-03-26	1997-04-04	R103
H001	G001	1997-03-12	1997-04-12	R202
H002	G002	1997-03-21	1997-04-05	R101
H003	G003	1997-03-31	1997-04-13	R102
H001	G004	1997-08-05	1997-08-10	R102
H002	G001	1997-07-26	1997-08-05	R203
H003	G002	1997-08-26	1997-09-03	R202
\.


--
-- Data for Name: bookloan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bookloan (bookid, branchid, cardno, dateout, duedate) FROM stdin;
1	1	1	2003-01-01	2003-03-02
2	1	1	2003-01-01	2003-03-02
1	3	3	2003-03-13	2003-05-14
2	5	7	2003-03-24	2003-05-25
2	5	1	2003-03-31	2003-06-01
3	4	3	2003-04-01	2003-06-02
4	1	6	2003-04-09	2003-06-10
4	3	4	2003-04-21	2003-06-02
4	3	3	2003-04-27	2003-06-28
6	1	2	2003-05-05	2003-07-06
7	5	4	2003-05-12	2003-07-13
\.


--
-- Data for Name: borrower; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.borrower (cardno, name, sex, address, borrowerbranch) FROM stdin;
1	ATZENI, John	M	16 Holhead, Aberdeen	2
2	BLAHA, Nick	M	6 Argyll St, London	1
3	BROWN, Barry	M	Lawrence St, Glasgow	3
4	CHEN, Mary	F	2 Manor St, Glasgow	3
5	BLACK, Dan	M	18 Dale St, Bristol	4
6	DAVIES, Kathy	F	9 Novar Dr, Bristol	4
7	ATZENI, John	M	50 Clover Dr, London	1
\.


--
-- Data for Name: branch; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.branch (branchid, branchname, branchaddress) FROM stdin;
1	Branch London	22 Deer Rd, London
2	Branch Aberdeen	Argyll St, Aberdeen
3	Branch Glasgow	163 Main St, Glasgow
4	Branch Bristol	32 Manse St, Bristol
5	Branch Vienna	56 Clover Dr, Vienna
\.


--
-- Data for Name: buch; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.buch (buchnr, titel, autor) FROM stdin;
1	Artificial Intelligence	Winston
2	The Art of Prolog	Sterling
3	Inside Smalltalk	Lalonde
4	Distributed Databases	Ceri
5	Advanced Database Systems	Gray
6	Computability and Logic	Boolos
7	Temporal Databases	Tansel
8	Multimedia Computing	Hodges
9	Concurrent Systems	Bacon
10	Introduction to SQL	Van der Lans
11	Object Databases	Loomis
12	Modern Operating Systems	Tanenbaum
\.


--
-- Data for Name: buchung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.buchung (buchngnr, vonkonto, aufkonto, betrag, datum) FROM stdin;
1	1	2	200.00	1995-08-01
2	12	6	1200.00	1995-08-05
3	4	7	340.00	1995-08-12
4	11	10	560.00	1995-08-13
5	3	8	1200.00	1995-08-20
6	4	12	780.00	1995-08-25
7	3	5	1230.00	1995-08-29
8	6	7	500.00	1995-09-01
9	8	3	1200.00	1995-09-07
10	10	9	100.00	1995-09-12
11	4	9	130.00	1995-09-15
12	7	1	230.00	1995-09-20
13	9	12	450.00	1995-09-27
14	7	4	1400.00	1995-09-30
15	10	3	230.00	1995-10-01
16	7	2	560.00	1995-10-02
17	2	11	340.00	1995-10-04
18	7	4	120.00	1995-10-08
19	5	1	250.00	1995-10-14
20	3	4	560.00	1995-10-18
21	7	6	240.00	1995-10-23
22	4	9	1100.00	1995-10-24
23	8	7	240.00	1995-10-26
24	8	1	1100.00	1995-10-30
25	10	9	10.00	1995-10-31
26	8	2	260.00	1995-11-01
27	5	8	390.00	1995-11-02
28	7	11	1300.00	1995-11-03
29	12	11	130.00	1995-11-04
30	7	12	410.00	1995-11-05
\.


--
-- Data for Name: city; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.city (zip, name) FROM stdin;
2001	Cinderella City
2002	Donald Duck City
2003	Mowgli City
2004	Lassie City
2005	Mickey Mouse City
2006	Goofy City
2007	Dumbo City
2008	Robin Hood City
2009	Flipper City
2010	Seven Dwarfs City
2011	Dagobert City
\.


--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.course (coursecode, name, lecturer) FROM stdin;
1	Database	Huber
2	Software Engineering	Miller
3	Information Engineering	Miller
4	Management	Amon
5	Communication Engineering	Zirm
6	C++ /C#	Huber
7	JAVA	Miller
8	Business Engineering	Glatz
9	Business Communication	Glatz
10	Accounting Systems	Glatz
11	Data Mining and Data Warehouse	Huber
12	Oracle	Huber
13	International Accounting	Glatz
\.


--
-- Data for Name: dept; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dept (dnum, dname, manager, mgrstartdate) FROM stdin;
D001	Administration	E001	1990-06-11
D002	Purchasing	E002	1995-07-22
D003	Marketing	E003	1997-08-06
D004	Development	E004	1997-09-12
\.


--
-- Data for Name: deptlocation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.deptlocation (dnum, dcity) FROM stdin;
D001	Springton
D002	Summerton
D003	Watervale
D004	Cookfield
D004	Springton
D004	Summerton
D004	Watervale
\.


--
-- Data for Name: distribute; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.distribute (recordid, media, price) FROM stdin;
1	CD	19
1	MC	20
2	CD	22
2	MC	23
2	DVD	30
2	MD	24
3	CD	8
4	CD	20
4	MD	26
5	CD	23
5	MC	25
6	CD	9
7	CD	8
8	MC	18
8	CD	18
9	CD	7
10	CD	8
11	CD	7
12	CD	9
13	CD	8
14	CD	7
15	CD	22
15	MC	24
15	MD	27
15	DVD	28
16	CD	21
17	CD	23
\.


--
-- Data for Name: enrollment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.enrollment (studentid, coursecode, grade) FROM stdin;
9978653	8	1
9978653	9	2
9978653	10	4
9978653	13	3
0034534	1	1
5489007	1	2
5489007	11	2
5489007	12	4
0299123	7	1
0299123	6	5
0178543	10	2
7813037	1	5
9954632	2	2
9954632	3	3
9954632	5	1
9755785	4	4
9755785	1	1
9856564	2	4
9876542	9	3
9876542	10	2
9800745	11	2
9800745	3	1
9900965	10	2
9900965	11	3
9900965	12	4
9955432	2	1
7732123	6	1
7732123	13	2
0023132	8	1
0023132	9	1
0023132	10	2
0199054	13	5
0203032	12	3
0203032	5	3
0278321	1	2
0278321	11	3
9722543	1	2
9677432	10	1
\.


--
-- Data for Name: entlehng; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.entlehng (entlngnr, buch, benutzer, von, bis) FROM stdin;
1	5	3	1993-09-09	1993-12-13
2	12	3	1993-09-09	1994-01-03
3	10	4	1993-10-14	1994-02-12
4	2	6	1993-10-28	1993-12-03
5	6	1	1993-11-03	1994-02-25
6	3	2	1993-12-18	1994-02-23
7	5	6	1993-12-20	1994-03-13
8	8	5	1994-01-02	1994-03-30
9	9	4	1994-02-05	1994-06-18
10	10	1	1994-02-18	1994-09-06
11	6	3	1994-03-12	1994-08-08
12	1	5	1994-05-03	1994-09-09
13	5	6	1994-06-10	1995-02-14
14	9	3	1994-07-03	1994-11-18
15	8	2	1994-08-30	1994-12-14
16	2	5	1994-09-10	1995-01-12
17	12	1	1994-11-11	1994-11-18
18	1	4	1994-12-03	1995-05-16
19	9	3	1995-01-02	1995-02-28
20	8	3	1995-01-02	1995-08-28
21	6	3	1995-01-02	1999-01-01
22	4	2	1995-01-04	1999-01-01
23	5	6	1995-02-20	1995-10-18
24	7	2	1995-03-01	1999-01-01
25	10	5	1995-03-13	1999-01-01
26	12	6	1995-04-10	1999-01-01
27	11	2	1995-05-06	1999-01-01
28	8	5	1995-06-12	1999-01-01
29	1	5	1995-06-12	1999-01-01
30	3	4	1995-10-13	1999-01-01
\.


--
-- Data for Name: exit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exit (nodeid, exitno, zip) FROM stdin;
N02	E01  	2001
N03	E02  	2002
N04	E03  	2003
N05	E04  	2004
N06	E05  	2005
N07	E06  	2006
N08	E07  	2007
N10	E08  	2008
N11	E09  	2009
N12	E10  	2010
\.


--
-- Data for Name: fakultaet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.fakultaet (kurzbez, bezeichnung) FROM stdin;
SoWi	Sozial- und Wirtschaftswissenschaftliche Fakultdt
TNF	Teschnisch-Naturwissenschaftliche Fakultdt
\.


--
-- Data for Name: filiale; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.filiale (filnr, inhname, strasse, plz) FROM stdin;
1	Leitner	Strubergasse 3	4040
2	Schraubner	Blattallee 7	4020
3	Rathgeb	Hauptstrasse 22	4744
4	Fiedler	Feldweg 8	6040
5	Hauser	Klessheimer Allee 6	5020
6	Huber	Ring 2	1010
\.


--
-- Data for Name: genre; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.genre (genreid, name, anzahl) FROM stdin;
1	Schoene Lieder	\N
2	Jodler	\N
3	Gegenden	\N
\.


--
-- Data for Name: gewinne; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.gewinne (firma, branche, bundesland, jahr, umsatz) FROM stdin;
Stahl und Co AG	Stahl	OÖ	2011	1433499.25
Voest Alpine AG	Stahl	OÖ	2011	4413100.05
Stahl und Co AG	Stahl	OÖ	2011	3242222.33
Stahlblech GmbH	Stahl	OÖ	2011	98238.76
Stahlzuschnitte GmbH und Co KG	Stahl	OÖ	2011	122442.33
Stahl und Co Regio	Stahl	OÖ	2011	1433499.25
VAI	Stahl	OÖ	2011	4413100.05
Stahl und Aluminium	Stahl	OÖ	2011	3242222.33
Stahl Experts	Stahl	OÖ	2011	983238.76
Edler Stahl	Stahl	OÖ	2011	198238.76
Diskont-Stahl-Händler	Stahl	OÖ	2011	982238.76
BMW Steyr	Auto	OÖ	2011	1334929.25
VA Auto	Auto	OÖ	2011	4411500.05
Solarauto	Auto	OÖ	2011	1242222.33
Pferdestark	Auto	OÖ	2011	82438.76
Austro-Ferrari	Auto	OÖ	2011	124492.33
Die Automanufaktur	Auto	OÖ	2011	1434949.25
Crash	Auto	OÖ	2011	4411200.05
Super-Vehikel	Auto	OÖ	2011	2342222.33
VW Linz	Auto	OÖ	2011	932378.76
Magna Steyr	Auto	OÖ	2011	198382.76
Pferdekutschen und Zubehör	Auto	OÖ	2011	198223.76
Flughafen Linz	Luftfahrt	OÖ	2011	147642.36
FACC	Luftfahrt	OÖ	2011	4317330.85
Luftschiff AG	Luftfahrt	OÖ	2011	1832231.33
\.


--
-- Data for Name: guest; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guest (gno, name, address) FROM stdin;
G001	Thomas, Thomas	2 North Street, Chelsea, London
G002	Wong, Veronica	7 South Street, Wimbledon, London
G003	Bianca, Bruce	452 West Terrace, Adelaide
G004	Imbrogno, Ignatius	Unit 3, 7 Smith Road, Detroit
G005	Hannagan, Sue	15 Earls Court, Edinburgh
G006	Boucher, Lesley	22 Seventh Avenue, Bristol
\.


--
-- Data for Name: highway; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.highway (code, name, startnodeid, endnodeid) FROM stdin;
H10  	Highway ARIELLE	N01	N10
H35  	Highway PETER PAN	N11	N01
H70  	Highway ALADIN	N02	N12
\.


--
-- Data for Name: highwayexit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.highwayexit (code, nodeid, atkm) FROM stdin;
H70  	N02	0.00
H70  	N06	123.00
H70  	N12	299.00
H35  	N11	0.00
H35  	N05	113.00
H35  	N08	250.00
H10  	N03	84.00
H10  	N04	173.00
H10  	N07	212.00
H10  	N10	357.00
\.


--
-- Data for Name: highwayintersection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.highwayintersection (code, nodeid, atkm) FROM stdin;
H10  	N01	0.00
H10  	N09	160.00
H35  	N01	307.00
H70  	N09	35.00
\.


--
-- Data for Name: hotel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hotel (hno, name, address) FROM stdin;
H001	Grosvenor Hotel	Mayfair, London
H002	Hyatt Regency	99 Main Street, Edinburgh
H003	Earls Court Hotel	123 Earls Court Road, London
H004	Lakes Resort Hotel	Ambleside, Lake Windermere
H005	Eagle Cliffs Hotel	63 West Terrace, Aberdeen
H006	Caledonian Hotel	12 Bannockburn Street, Glasgow
\.


--
-- Data for Name: human; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.human (name, gender, age) FROM stdin;
Manfred Huber	m	65
Aloisia Huber	f	61
Alfons Meier	m	56
Gabriele M|ller	f	17
Hannes Meier	m	23
Leopold Meier	m	81
Anna Gruber	f	33
Kevin M|ller	m	1
Gerhard Burger	m	60
Franziska Burger	f	50
Christian Burger	m	25
Georg Oberer	m	34
Christine Oberer	f	28
Susanne Oberer	f	4
Daniel Steirer	m	33
Gabriele Steirer	f	26
Silvia Steirer	f	2
Stefan Wolkl	m	17
Lydia Wachter	f	19
Wilhelm Amsel	m	16
Rudolf Steirer	m	60
Patrick Steirer	m	4
Ferdinand Steirer	m	3
Peter Steinberger	m	1
Cindy Steinberger	f	22
\.


--
-- Data for Name: inhaber; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inhaber (name, gebdat, adresse) FROM stdin;
Steiner Heinz       	2042-10-10	Linz                
Hafner Eleonore     	2060-03-09	Salzburg            
Wopfner Karin       	1975-08-13	Innsbruck           
Steindl Kurt        	2036-04-24	Wien                
Hofreiter Martin    	1980-02-12	Graz                
Gruber Martha       	2050-07-29	Klagenfurt          
\.


--
-- Data for Name: intersection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.intersection (nodeid, name) FROM stdin;
N01	Voralpenkreuz
N09	Inntaldreieck
\.


--
-- Data for Name: koje; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.koje (standnr, flaeche, linkernachbar, rechternachbar, gemietet_von, beschriftung) FROM stdin;
D14	5	D13	RAND	175	Forschungsschwerpunkte am Institut fuer WIN
C11	5	RAND	C12	175	WIN
C12	4	C11	C13	175	Das Studium
C13	3	C12	RAND	880	Informatik: das bringt es
D11	3	RAND	D12	175	Berufsmoeglichkeiten nach dem Studium der WIN
D12	2	D11	D13	880	Berufsmoeglichkeiten fuer Informatiker
D13	4	D12	D14	880	Bio-Informatik: Eine Chance fuer die Zukunft?
\.


--
-- Data for Name: konto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.konto (kontonr, filiale, inhname, gebdat, saldo) FROM stdin;
13	3	Gruber Martha       	2050-07-29	1000.00
1	1	Steiner Heinz       	2042-10-10	12400.00
2	2	Wopfner Karin       	1975-08-13	500.00
3	3	Steiner Heinz       	2042-10-10	13500.00
4	3	Hafner Eleonore     	2060-03-09	8000.00
5	2	Wopfner Karin       	1975-08-13	1100.00
6	2	Steindl Kurt        	2036-04-24	6000.00
7	1	Gruber Martha       	2050-07-29	7500.00
8	2	Wopfner Karin       	1975-08-13	1900.00
9	1	Hofreiter Martin    	1980-02-12	10400.00
10	2	Hofreiter Martin    	1980-02-12	5100.00
11	1	Gruber Martha       	2050-07-29	8200.00
12	3	Gruber Martha       	2050-07-29	1200.00
\.


--
-- Data for Name: kunde; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kunde (kundenr, name, bonstufe) FROM stdin;
11111	Roller	C
15882	Schieber	B
78436	Flitzer	A
98077	Sauser	A
24537	Raser	B
13451	Stuerzer	C
22221	Bremser	B
99332	Kuppler	A
67891	Schleifer	C
95543	Kurver	B
55789	Schleuderer	A
87654	Unfaller	C
77777	Stenzer	A
\.


--
-- Data for Name: kurs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kurs (kursnr, name, lektor) FROM stdin;
1	Datenbanken	Huber
2	Software Engineering	Mueller
3	Information Engineering	Mueller
4	Management	Amon
5	Communication Engineering	Zirm
6	C++ /C#	Huber
7	JAVA	Mueller
8	Unternehmensführung	Glatz
9	Unternehmensbewertung und -analyse	Glatz
10	Konzernrechnungslegung	Glatz
11	Data Mining and Data Warehouse	Huber
12	Oracle	Huber
13	International Accounting	Glatz
\.


--
-- Data for Name: lieferposition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lieferposition (liefer_nr, lieferpos_nr, bestell_nr, bestellpos_nr, menge) FROM stdin;
901	1	1001	1	100
901	2	1001	2	1
902	1	1002	1	80
902	2	1004	1	40
903	1	1004	1	160
904	1	1003	2	40
904	2	1003	1	10
906	1	1004	2	300
906	2	1005	1	200
907	1	1005	1	40
908	1	1006	1	90
909	1	1008	1	100
\.


--
-- Data for Name: lieferschein; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lieferschein (liefer_nr, datum) FROM stdin;
901	2003-10-13
902	2003-10-13
903	2003-10-13
904	2003-10-13
906	2003-10-13
907	2003-10-14
908	2003-10-14
909	2003-10-14
\.


--
-- Data for Name: liegtanstrasse; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.liegtanstrasse (strassenname, ort, beikm) FROM stdin;
A1	1010	0.00
A10	5020	0.00
A8	4780	0.00
B148	4780	0.00
L345	4982	0.00
A1	5020	500.00
A10	7010	250.00
A8	4040	100.00
B148	5280	60.00
L345	4999	20.00
B148	4982	25.00
A8	4982	65.00
B148	4999	10.00
\.


--
-- Data for Name: location; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.location (locno, name, city, state) FROM stdin;
1	PassageXY	Linz	OOE
2	ABC-Markt	Linz	OOE
3	Plus City	Pasching	OOE
4	Airport C.	Salzburg	SBG-L
5	Mozart-M.	Salzburg	SBG-L
6	Hal-Markt	Hallein	SBG-L
\.


--
-- Data for Name: mietet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mietet (mieternr, wohnnr, preis, von, bis) FROM stdin;
1	2	500	1996-01-01	1999-12-31
8	2	900	1999-10-01	2001-12-31
6	9	1500	1994-04-01	1999-09-30
11	9	1600	1999-10-01	1999-12-31
9	4	700	1997-01-01	1999-02-28
11	4	650	1999-03-01	1999-12-31
9	11	1200	2002-01-01	2001-09-30
9	1	950	1996-01-01	1999-12-31
11	6	1400	1996-01-01	1999-12-31
1	13	2100	1990-01-01	1997-12-31
13	5	1000	1996-01-01	2001-08-31
14	12	1200	1996-01-01	2002-01-31
1	1	950	1990-01-01	1990-06-01
\.


--
-- Data for Name: node; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.node (nodeid, longitude, latitude, type) FROM stdin;
N01	21.00	19.00	intersection 
N02	21.00	19.00	exit         
N03	20.00	13.00	exit         
N04	18.00	16.00	exit         
N05	18.00	22.00	exit         
N06	17.00	26.00	exit         
N07	15.00	12.00	exit         
N08	15.00	17.00	exit         
N09	14.00	21.00	intersection 
N10	14.00	21.00	exit         
N11	12.00	15.00	exit         
N12	12.00	22.00	exit         
\.


--
-- Data for Name: ort; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ort (plz, name) FROM stdin;
4040	Linz
1010	Wien
5020	Salzburg
7010	Klagenfurt
4982	Obernberg
4780	Schaerding
5280	Braunau
4999	Hinterdupfing
\.


--
-- Data for Name: orteverbindung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orteverbindung (strassenname, ortvon, ortnach, distanz) FROM stdin;
A1	1010	5020	500.00
A10	5020	7010	250.00
A8	4780	4982	65.00
A8	4982	4040	35.00
B148	4780	4999	10.00
B148	4982	5280	35.00
B148	4999	4982	15.00
L345	4982	4999	20.00
\.


--
-- Data for Name: parent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parent (parentname, childname) FROM stdin;
Alfons Meier	Anna Gruber
Alfons Meier	Hannes Meier
Aloisia Huber	Anna Gruber
Christine Oberer	Susanne Oberer
Cindy Steinberger	Peter Steinberger
Daniel Steirer	Ferdinand Steirer
Daniel Steirer	Patrick Steirer
Daniel Steirer	Peter Steinberger
Daniel Steirer	Silvia Steirer
Franziska Burger	Christian Burger
Gabriele M|ller	Kevin M|ller
Gabriele Steirer	Ferdinand Steirer
Gabriele Steirer	Patrick Steirer
Gabriele Steirer	Silvia Steirer
Georg Oberer	Susanne Oberer
Gerhard Burger	Christian Burger
Leopold Meier	Alfons Meier
Rudolf Steirer	Gabriele Steirer
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person (persnr, name, stand, beruf) FROM stdin;
1	Decker    	ledig          	Student        
2	Meier     	verheiratet    	Maurer         
3	Huber     	ledig          	Schlosser      
4	Bauer     	verwitwet      	Beamter        
5	Kaiser    	verheiratet    	Beamter        
6	Richter   	ledig          	Anwalt         
7	Weiss     	ledig          	Maler          
8	Traxler   	verheiratet    	Student        
9	Seyfried  	ledig          	
10	Weikinger 	ledig          	Lehrer         
11	Rechberger	verheiratet    	Hausmeister    
13	Kofler    	ledig          	Autor          
14	Seberry   	ledig          	Autor          
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (prodno, name, type, cat) FROM stdin;
1	Jeans L501	Hose	Bekleidung
2	Jeans X318	Hose	Bekleidung
3	Replay ABC	Hose	Bekleidung
4	H+M 347	Hemd	Bekleidung
5	Esprit 128	Hemd	Bekleidung
6	Pullover XL	Pullover	Bekleidung
7	Scholle	Tiefkuehl	LM
8	Erbsen	Tiefkuehl	LM
9	Pizza C+M	Tiefkuehl	LM
10	Bohnensuppe	Konserven	LM
11	Gulaschsuppe	Konserven	LM
\.


--
-- Data for Name: produkt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.produkt (ean, bezeichnung, kategorie, ekpreis, listpreis) FROM stdin;
0-666-4567-2-22	Autoschampoo	Pflege	35.69	9.00
0-777-4997-2-43	Glanzpolitur	Pflege	70.00	119.90
0-456-4887-3-22	Kaltwachs	Pflege	90.00	199.90
0-55-48567-16-2	Armaturenreiniger	Pflege	115.00	333.00
1-626-7767-2-99	Scheibenwischer	Ersatz	390.00	400.00
1-256-7700-2-00	Sportlenkrad	Ersatz	1300.00	1999.00
1-333-7788-2-31	Gaspedal	Ersatz	800.00	960.00
2-446-7240-9-15	Rennsitz	Ersatz	18900.00	22999.00
9-396-7510-9-00	Chromfelge	Ersatz	9000.00	12980.00
3-211-1000-2-00	Sonnenschutz	Ersatz	160.00	250.00
7-2881-760-3-70	Schraubantenne	Audio	965.00	1300.00
5-2671-955-5-55	Autoradio	Audio	5555.00	6800.00
1-4444-652-8-88	CD-Wechsler	Audio	2345.00	3999.00
3-1111-654-3-99	Lautsprecher	Audio	999.00	1599.00
6-581-1766-3-45	Telefonhalter	Sonstiges	130.00	225.00
6-231-4777-3-15	Lenkradueberzug	Sonstiges	125.00	529.90
4-1161-730-3-88	Warndreieck	Sonstiges	350.00	499.00
0-4381-880-7-00	Verbandskasten DIN	Sonstiges	965.00	999.00
5-6661-000-0-00	Abschleppseil	Sonstiges	225.00	475.00
\.


--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project (pnum, pname, pcity, dnum) FROM stdin;
P001	Springton Hospital	Springton	D004
P002	Springton Fire Dept	Springton	D004
P003	Summerton Expo 2002	Summerton	D003
P004	Watervale Show	Watervale	D003
P005	Adelaide Festival	Adelaide	D003
P006	Ashes Tour Promo	Cookfield	D004
P007	Opera House	Cookfield	D004
P008	Cookfield High Gym	Cookfield	D004
P009	Summerton Hospital	Summerton	D004
P010	Watervale Ambulance	Watervale	D004
P011	Medici	Springton	D004
P012	Springton Show	Springton	D003
\.


--
-- Data for Name: purchase; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.purchase (dayno, prodno, qty, price) FROM stdin;
1	1	20	600
1	5	40	300
1	6	20	600
1	2	35	500
1	3	70	550
1	4	20	400
1	7	50	20
1	8	140	24
1	9	110	33
1	10	50	15
1	11	100	22
2	2	5	440
2	5	50	320
2	7	10	18
3	4	10	390
3	7	100	16
3	9	80	31
4	1	15	560
4	10	20	14
4	11	40	20
5	7	20	19
5	8	30	26
5	1	30	610
5	2	30	290
5	6	50	600
6	7	20	22
6	3	100	530
6	4	50	380
7	5	40	290
7	8	80	25
7	9	90	32
7	10	70	16
7	11	120	21
\.


--
-- Data for Name: rechnung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rechnung (rechnungnr, datum, bezahlt, kundenr, filnr) FROM stdin;
1	2000-08-11	Y	11111	1
2	2000-08-11	N	15882	1
3	2000-08-11	Y	24537	1
4	2000-08-11	Y	78436	1
5	2000-08-11	Y	95543	1
6	2000-08-11	N	13451	2
1	2000-09-15	N	11111	1
2	2000-09-15	Y	22221	3
3	2000-09-15	Y	87654	4
1	2000-10-03	Y	11111	6
2	2000-10-03	Y	95543	5
1	2000-10-10	N	77777	5
2	2000-10-10	Y	99332	2
1	2000-10-20	Y	67891	2
2	2000-10-20	N	15882	3
3	2000-10-20	Y	98077	3
4	2000-10-20	N	78436	3
5	2000-10-20	Y	95543	5
6	2000-10-20	N	13451	5
1	2000-10-21	Y	87654	4
2	2000-10-21	Y	22221	4
3	2000-10-21	Y	87654	5
1	2000-10-25	N	13451	2
2	2000-10-25	Y	99332	1
\.


--
-- Data for Name: rechnungpos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rechnungpos (rechnungnr, datum, ean, positionnr, einzelpreis, menge) FROM stdin;
1	2000-08-11	0-666-4567-2-22	1	58.90	5
2	2000-08-11	0-456-4887-3-22	1	229.90	1
2	2000-08-11	2-446-7240-9-15	2	22500.00	1
3	2000-08-11	5-2671-955-5-55	1	7000.00	1
4	2000-08-11	6-581-1766-3-45	1	200.00	3
5	2000-08-11	9-396-7510-9-00	1	12000.00	1
5	2000-08-11	1-4444-652-8-88	2	4500.00	1
6	2000-08-11	0-777-4997-2-43	1	125.00	3
1	2000-09-15	4-1161-730-3-88	1	500.00	2
2	2000-09-15	5-2671-955-5-55	1	6800.00	1
3	2000-09-15	9-396-7510-9-00	1	12900.00	1
3	2000-09-15	3-1111-654-3-99	2	1600.00	2
1	2000-10-03	5-6661-000-0-00	1	530.00	3
1	2000-10-03	7-2881-760-3-70	2	1300.00	1
1	2000-10-03	0-4381-880-7-00	3	1350.00	1
2	2000-10-03	0-55-48567-16-2	1	330.00	2
1	2000-10-10	5-6661-000-0-00	1	500.00	3
2	2000-10-10	6-231-4777-3-15	1	525.00	3
2	2000-10-10	5-6661-000-0-00	2	490.00	5
2	2000-10-10	9-396-7510-9-00	3	14000.00	1
1	2000-10-20	3-1111-654-3-99	1	1300.00	2
2	2000-10-20	1-256-7700-2-00	1	2200.00	1
3	2000-10-20	5-6661-000-0-00	1	380.00	2
4	2000-10-20	0-4381-880-7-00	1	1100.00	1
5	2000-10-20	4-1161-730-3-88	1	510.00	3
6	2000-10-20	5-6661-000-0-00	1	500.00	2
1	2000-10-21	6-581-1766-3-45	1	150.00	1
2	2000-10-21	5-2671-955-5-55	1	6900.00	1
3	2000-10-21	6-231-4777-3-15	1	500.00	2
1	2000-10-25	0-55-48567-16-2	1	390.00	5
2	2000-10-25	5-6661-000-0-00	1	450.00	1
\.


--
-- Data for Name: record; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.record (recordid, artistname, title, releasedate, type, genreid) FROM stdin;
1	Sigi Saenger	Meine schoensten Lieder	2000-11-12	Album	1
2	Sigi Saenger	Meine schoensten Lieder 2	2001-06-04	Album	1
3	Sigi Saenger	Noch ein schoenes Lied	2002-02-23	Single	1
4	Trude Traeller	Der Bach	1998-07-03	Album	3
5	Trude Traeller	Der Baum	1999-09-24	Album	3
6	Trude Traeller	Die Wiese	2000-01-03	Single	3
7	Trude Traeller	Das Feld	2000-10-22	Single	3
8	Trude Traeller	Der Acker	2001-05-17	Album	3
9	Johann Jodler	Steiermark-Jodler	1999-05-13	Single	2
10	Johann Jodler	Tirol-Jodler	2000-11-04	Single	2
11	Johann Jodler	Vorarlberg-Jodler	2000-12-06	Single	2
12	Johann Jodler	Wien-Jodler	2000-12-08	Single	2
13	Johann Jodler	Salzburg-Jodler	1997-04-27	Single	2
14	Johann Jodler	Kaernten-Jodler	1998-03-18	Single	2
15	Carlo Cravallo	Roma	1999-06-01	Album	3
16	Carlo Cravallo	Venezia	2002-07-02	Single	3
17	Carlo Cravallo	Milano	2003-08-03	Album	3
\.


--
-- Data for Name: reserviert; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reserviert (tno, tag, stunde, mano) FROM stdin;
1	10	10	9101
1	10	11	9102
1	10	12	9103
2	10	12	9101
1	10	14	9101
3	10	14	9104
1	11	10	9101
4	11	10	9101
4	11	11	9105
3	11	12	9105
2	11	13	9101
1	11	13	9102
\.


--
-- Data for Name: room; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room (hno, rno, type, price) FROM stdin;
H001	R101	single	22.00
H001	R102	double	39.00
H001	R103	double	39.00
H001	R201	family	85.00
H001	R202	double	44.00
H001	R203	family	49.00
H002	R101	double	41.00
H002	R102	single	25.00
H002	R103	family	49.00
H002	R201	family	49.00
H002	R202	double	41.00
H002	R203	double	48.00
H003	R101	double	44.00
H003	R102	family	55.00
H003	R103	family	55.00
H003	R201	double	44.00
H003	R202	single	28.00
H003	R203	double	44.00
H004	R101	double	41.00
H004	R102	family	49.00
H004	R103	family	49.00
H004	R201	single	90.00
H004	R202	single	24.00
H004	R203	double	39.00
H005	R101	single	23.00
H005	R102	double	35.00
H005	R103	double	35.00
H005	R201	family	39.00
H005	R202	single	23.00
H005	R203	single	20.00
H006	R101	double	33.00
H006	R102	double	37.00
H006	R103	family	45.00
H006	R201	family	49.00
H006	R202	family	39.00
H006	R203	family	39.00
\.


--
-- Data for Name: sales; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales (dayno, locno, prodno, qty, price) FROM stdin;
1	3	1	10	900
1	3	2	7	600
1	3	3	15	800
1	4	7	80	25
1	4	8	60	35
1	4	9	40	40
1	4	10	30	25
1	4	11	50	30
1	1	10	30	27
1	2	11	50	29
2	3	8	100	25
2	5	5	10	500
2	5	6	15	800
2	4	4	25	530
3	2	4	10	490
3	1	1	10	900
3	1	2	7	600
3	1	3	15	800
3	1	7	80	25
4	1	8	60	35
4	2	9	40	40
4	2	10	30	23
4	1	7	30	26
5	4	6	50	865
5	2	11	50	32
5	2	4	20	500
5	2	5	25	510
6	2	9	40	44
6	4	1	30	920
6	3	3	50	760
7	3	1	20	950
7	5	2	25	580
\.


--
-- Data for Name: segment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.segment (code, segid, fromkm, tokm) FROM stdin;
H10  	S01  	0.00	65.00
H10  	S02  	65.00	120.00
H10  	S03  	120.00	160.00
H10  	S04  	160.00	227.00
H10  	S05  	227.00	281.00
H10  	S06  	281.00	357.00
H35  	S01  	0.00	35.00
H35  	S02  	35.00	167.00
H35  	S03  	167.00	307.00
H70  	S01  	0.00	35.00
H70  	S02  	35.00	177.00
H70  	S03  	177.00	299.00
\.


--
-- Data for Name: sortiment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sortiment (filnr, ean, vkpreis, preisred, bestand) FROM stdin;
6	3-211-1000-2-00	20.00	0.00	20
1	0-666-4567-2-22	69.90	0.00	100
1	0-777-4997-2-43	120.00	30.00	150
1	0-456-4887-3-22	229.00	9.00	130
1	0-55-48567-16-2	300.00	0.00	100
1	1-626-7767-2-99	420.00	10.10	100
1	1-256-7700-2-00	1999.00	0.00	30
1	1-333-7788-2-31	999.00	0.00	30
1	2-446-7240-9-15	22500.00	0.00	7
1	9-396-7510-9-00	13000.00	1000.00	15
1	7-2881-760-3-70	1500.00	0.00	23
1	5-2671-955-5-55	7000.00	0.00	12
1	1-4444-652-8-88	4500.00	0.00	4
1	3-1111-654-3-99	1700.00	0.00	7
1	6-581-1766-3-45	200.00	0.00	40
1	6-231-4777-3-15	500.00	0.00	35
1	4-1161-730-3-88	500.00	0.00	25
1	0-4381-880-7-00	1250.00	250.00	85
1	5-6661-000-0-00	450.00	0.00	11
2	0-666-4567-2-22	69.00	9.00	90
2	0-777-4997-2-43	125.00	0.00	155
2	0-456-4887-3-22	200.00	9.00	120
2	0-55-48567-16-2	390.00	0.00	100
2	1-626-7767-2-99	400.00	0.00	110
2	1-256-7700-2-00	1900.00	0.00	40
2	2-446-7240-9-15	21999.00	0.00	0
2	9-396-7510-9-00	14000.00	0.00	25
2	7-2881-760-3-70	1300.00	0.00	9
2	5-2671-955-5-55	6800.00	250.00	9
2	1-4444-652-8-88	4000.00	0.00	3
2	3-1111-654-3-99	1500.00	125.00	1
2	6-581-1766-3-45	230.00	0.00	44
2	6-231-4777-3-15	530.00	0.00	0
2	4-1161-730-3-88	500.00	11.00	8
2	0-4381-880-7-00	1220.00	0.00	5
2	5-6661-000-0-00	400.00	0.00	1
3	0-666-4567-2-22	90.00	0.00	85
3	0-456-4887-3-22	199.00	0.00	80
3	0-55-48567-16-2	390.00	0.00	90
3	1-626-7767-2-99	400.00	0.00	50
3	1-256-7700-2-00	1900.00	0.00	10
3	9-396-7510-9-00	13800.00	0.00	5
3	7-2881-760-3-70	1300.00	0.00	0
3	5-2671-955-5-55	6800.00	0.00	0
3	1-4444-652-8-88	3999.00	0.00	0
3	6-581-1766-3-45	230.00	0.00	14
3	6-231-4777-3-15	530.00	0.00	4
3	0-4381-880-7-00	1211.00	0.00	4
3	5-6661-000-0-00	380.00	0.00	4
4	2-446-7240-9-15	22480.00	0.00	6
4	9-396-7510-9-00	12900.00	0.00	2
4	7-2881-760-3-70	1300.00	111.00	5
4	5-2671-955-5-55	6900.00	0.00	34
4	1-4444-652-8-88	4490.00	0.00	5
4	3-1111-654-3-99	1600.00	0.00	7
4	6-581-1766-3-45	200.00	20.00	9
5	0-666-4567-2-22	80.00	5.00	0
5	0-777-4997-2-43	160.00	0.00	150
5	0-456-4887-3-22	250.00	0.00	130
5	0-55-48567-16-2	330.00	0.00	108
5	6-231-4777-3-15	525.00	25.00	12
5	4-1161-730-3-88	510.00	0.00	66
5	0-4381-880-7-00	1425.00	125.00	2
5	5-6661-000-0-00	500.00	0.00	3
6	0-666-4567-2-22	70.00	11.00	100
6	0-777-4997-2-43	95.00	0.00	0
6	1-626-7767-2-99	380.00	25.00	100
6	1-256-7700-2-00	2109.00	0.00	0
6	1-333-7788-2-31	960.00	0.00	9
6	9-396-7510-9-00	11500.00	0.00	15
6	7-2881-760-3-70	1420.00	120.00	23
6	5-2671-955-5-55	6800.00	0.00	12
6	1-4444-652-8-88	4225.00	0.00	8
6	3-1111-654-3-99	1300.00	0.00	7
6	0-4381-880-7-00	1350.00	0.00	25
6	5-6661-000-0-00	553.00	23.00	11
\.


--
-- Data for Name: sortiment_aenderungen; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sortiment_aenderungen (filnr, ean, vkpreis, preisred, bestand) FROM stdin;
\.


--
-- Data for Name: staff; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.staff (snum, name, dob, address, city, gender, salary, supervisor, dnum) FROM stdin;
E001	ATZENI, John	2035-01-01	1 Spring Rd	Springton	M	90000.00	    	D001
E002	BLAHA, Nick	2040-02-02	2 Main St	Summerton	M	70000.00	E001	D002
E003	BROWN, Barry	2045-03-03	3 Summer Rd	Watervale	M	70000.00	E001	D003
E004	CHEN, Mary	2050-04-04	4 Smith St	Cookfield	F	70000.00	E001	D004
E005	BLACK, Dan	2055-05-05	5 High View Rd	Springton	M	60000.00	E001	D001
E006	DAVIES, Kathy	2060-06-06	6 Brown Rd	Summerton	F	50000.00	E002	D002
E007	SMITH, Andy	2065-07-07	7 Roberts Rd	Watervale	M	50000.00	E003	D003
E008	JONES, Frances	1970-08-08	8 Peters Rd	Cookfield	F	50000.00	E004	D004
E009	ROBERTS, Simon	1985-09-09	9 Mary St	Springton	M	40000.00	E005	D001
E010	GRAINGER, Bernard	1981-10-10	10 Adelaide Rd	Summerton	M	35000.00	E002	D002
E011	McBRIDE, David	1975-11-11	11 Laura Ave	Watervale	M	35000.00	E003	D003
E012	MARSHALL, John	1970-12-12	12 Apple Tree Lane	Cookfield	M	35000.00	E004	D004
E013	MATTHEWS, George	2065-01-13	13 May Ave	Springton	M	35000.00	E005	D001
E014	MORRISON, Joe	2060-02-14	14 Sussex St	Summerton	M	35000.00	E002	D002
E015	CERI, Rose	2055-03-15	15 Murray Rd	Watervale	F	35000.00	E003	D003
E016	PRICE, Patricia	2050-04-16	16 McGraw Hill	Cookfield	F	35000.00	E004	D004
E017	WHITE, John	2045-05-17	17 Nelson Rd	Springton	M	66000.00	E005	D004
E018	STURT, Michael	2040-06-18	18 Wills Rd	Summerton	M	60000.00	E002	D004
E019	SWAFFER, Jill	2035-07-19	19 Burke St	Watervale	F	67000.00	E003	D004
\.


--
-- Data for Name: staffhotel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.staffhotel (sno, name, address, "position", salary) FROM stdin;
S011	Moira, Samuel	49 School Road, Broxburn	Charge Nurse	37520.00
S098	Carol, Cummings	15 High Street, Edinburgh	Staff Nurse	28000.00
S123	Morgan, Russell	23A George Street, Broxburn	Nurse	18000.00
S167	Robin, Plevin	7 Glen Terrace, Edinburgh	Staff Nurse	28000.00
S234	Amy ,O'Donnell	234 Princes Street, Edinburgh	Nurse	20200.00
S344	Laurence, Burns	1 Apple Drive, Edinburgh	Consultant	56000.00
S321	Mohammed, Sharif		Charge Nurse	35110.00
S099	Mary,Keegan		Consultant	86000.00
S515	Mikhail,Kruschev		Nurse	24000.00
S143	Myron,Isaacs		Staff Nurse	30800.00
S323	M.,Morgenstern		Nurse	19710.00
\.


--
-- Data for Name: strasse; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.strasse (name, bezeichnung, ortvon, ortnach, strassenart, laenge) FROM stdin;
A1	Westautobahn	1010	5020	A	500.00
A10	Tauernautobahn	5020	7010	A	250.00
A8	Innkreisautobahn	4780	4040	A	100.00
B148	Innviertler Bundesstrasse	4780	5280	B	60.00
L345	Hinterdupfinger Feldweg	4982	4999	L	20.00
\.


--
-- Data for Name: strassenart; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.strassenart (art, bezeichnung) FROM stdin;
A	Autobahn
B	Bundesstrasse
L	Landesstrasse
\.


--
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student (matrikelnr, name, land) FROM stdin;
9978653	Maria Mayr	Austria
0034534	James Bond	England
5489007	Adam Smith	England
9988687	Anna Kurz	Schweiz
0299123	Maria Mayr	Schweiz
0178543	Barbara Eggersdorfer	Schweiz
7813037	Michael Lofter	Schweiz
9954632	Christian Just	Deutschland
0076802	Bernd Achleitner	Deutschland
9756432	Barbara Anegg	Deutschland
9755785	Margit Bauer	Deutschland
9784322	Martina Binder	Deutschland
9856564	Manfred Fiebiger	Austria
9876542	Gabriele Grabner	Austria
9800745	Alfred Grassegger	Austria
9900965	Herwig Huber	Austria
9921213	Manuela Jansa	Austria
9955432	Reinhard Kaiser	Austria
7732123	Heinz Kneifel	Austria
0076768	Helmut Leonhart	Austria
0076323	Gabriel Menzinger	Austria
0023132	Gabriela Oettl	Austria
0154321	Maria-Anna Pelzl	USA
0158732	Matthias Sandhofer	Deutschland
0199054	Arno Seeber	Deutschland
0203032	Ludwig Spoetl	Deutschland
0299789	Michaela Stangl	Deutschland
0278321	Michael Steyrer	Deutschland
9677432	Toegel Richard	USA
9722543	Natalie Zoechling	Schweiz
\.


--
-- Data for Name: studenten; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.studenten (mano, name, rechner) FROM stdin;
9101	atzel	andi
9102	asser	andi
9103	abelko	andi
9104	brunisch	bibi
9105	brabec	bibi
9106	berti	bibi
9107	fauler	bibi
9108	lazyguy	bibi
\.


--
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (studentid, name, country) FROM stdin;
9978653	Maria Mayr	Australia
0034534	James Bond	England
5489007	Adam Smith	England
9988687	Anna Kurz	Japan
0299123	Maria Mayr	Japan
0178543	Barbara Eggersdorfer	Japan
7813037	Michael Lofter	Japan
9954632	Christian Just	Thailand
0076802	Bernd Achleitner	Thailand
9756432	Barbara Anegg	Thailand
9755785	Margit Bauer	Thailand
9784322	Martina Binder	Thailand
9856564	Manfred Fiebiger	Australia
9876542	Gabriele Grabner	Australia
9800745	Alfred Grassegger	Australia
9900965	Herwig Huber	Australia
9921213	Manuela Jansa	Australia
9955432	Reinhard Kaiser	Australia
7732123	Heinz Kneifel	Australia
0076768	Helmut Leonhart	Australia
0076323	Gabriel Menzinger	Australia
0023132	Gabriela Oettl	Australia
0154321	Maria-Anna Pelzl	USA
0158732	Matthias Sandhofer	Thailand
0199054	Arno Seeber	Thailand
0203032	Ludwig Spoetl	Thailand
0299789	Michaela Stangl	Thailand
0278321	Michael Steyrer	Thailand
9677432	Toegel Richard	USA
9722543	Natalie Zoechling	Japan
\.


--
-- Data for Name: studienrichtung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.studienrichtung (kennzahl, gehoert_zu, bezeichnung) FROM stdin;
175	SoWi	Wirtschaftsinformatik
880	TNF	Informatik
750	TNF	Technische Physik
\.


--
-- Data for Name: terminal; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.terminal (tno, rechner) FROM stdin;
1	andi
2	andi
3	bibi
4	caesar
\.


--
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.test (id) FROM stdin;
\.


--
-- Data for Name: time; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."time" (dayno, d, m, y) FROM stdin;
1	1999/11/03	1999/11	1999
2	1999/11/04	1999/11	1999
3	1999/12/06	1999/12	1999
4	2000/04/13	2000/04	2000
5	2000/04/28	2000/04	2000
6	2000/05/05	2000/05	2000
7	2000/05/18	2000/05	2000
\.


--
-- Data for Name: track; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.track (recordid, tnumber, title, length) FROM stdin;
1	1	Mein 1.-schoenstes Lied	182
1	2	Mein 2.-schoenstes Lied	143
1	3	Mein 3.-schoenstes Lied	205
1	4	Mein 4.-schoenstes Lied	199
2	1	Mein 5.-schoenstes Lied	176
2	2	Mein 6.-schoenstes Lied	177
2	3	Mein 7.-schoenstes Lied	178
2	4	Mein 8.-schoenstes Lied	179
2	5	Mein 9.-schoenstes Lied	209
3	1	Noch ein schoenes Lied - Disco Version	180
3	2	Noch ein schoenes Lied - Extended Version	190
4	1	Der blaue Bach	192
4	2	Der gruene Bach	209
4	3	Der weisse Bach	170
4	4	Der andere blaue Bach	206
4	5	Der zweite gruene Bach	190
4	6	Der dritte gruene Bach	220
5	1	Die Buche	190
5	2	Die Eiche	200
5	3	Die Tanne	210
5	4	Die Fichte	220
5	5	Der Kirschbaum	230
5	6	Die Linde	240
5	7	Die Birke	220
5	8	Die Kiefer	208
6	1	Die Wiese - Vocal	270
6	2	Die Wiese - Instrumental	270
7	1	Das Feld - Vocal	170
7	2	Das Feld - Instrumental	173
8	1	Martins Ruebenacker	194
8	2	Kurts Maisacker	234
8	3	Lisas Getreideacker	210
8	4	Marias Gemuesefeld	195
9	1	Graz-Jodler	250
10	1	Innsbruck-Joder	184
10	2	Kitzbuehel-Joder	191
11	1	Bregenz-Jodler	301
12	1	Grinzing-Jodler	140
13	1	St.-Johann-Jodler	262
13	2	Zell-am-See-Jodler	213
14	1	Klagenfurt-Jodler	200
14	2	Villach-Jodler	211
15	1	Colosseo	240
15	2	Vaticano	253
15	3	Fontana di Trevi	187
15	4	Piazza di Spagna	210
16	1	Ponte di Rialto	182
16	2	San Marco	194
17	1	Luigi	240
17	2	Alessandra	253
17	3	Antonio	187
17	4	Gianna	199
17	5	Adriano	177
17	6	Nicoletta	310
\.


--
-- Data for Name: wartung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wartung (rechner, tag, vonstunde, bisstunde) FROM stdin;
andi	11	14	15
bibi	10	11	12
caesar	13	10	12
dora	14	14	18
andi	10	12	13
\.


--
-- Data for Name: wohnung; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.wohnung (wohnnr, eigentuemer, bezirk, gross) FROM stdin;
1	6	4	62
2	6	1	100
3	4	1	60
5	7	5	40
6	3	3	100
8	9	5	40
9	10	5	100
10	4	3	30
11	7	3	95
12	9	3	50
13	10	4	120
\.


--
-- Data for Name: workson; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.workson (snum, pnum, hours) FROM stdin;
E001	P005	8
E003	P003	16
E007	P004	16
E011	P005	16
E011	P012	16
E015	P005	16
E015	P012	16
E004	P001	8
E012	P001	16
E004	P007	8
E012	P007	16
E008	P006	8
E016	P006	8
E008	P002	8
E016	P002	8
E008	P008	8
E016	P008	16
E017	P011	32
E018	P009	32
E019	P010	32
\.


--
-- Name: benutzer benutzer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.benutzer
    ADD CONSTRAINT benutzer_pkey PRIMARY KEY (bennr);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (bookid);


--
-- Name: bookcopies bookcopies_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookcopies
    ADD CONSTRAINT bookcopies_pkey PRIMARY KEY (bookid, branchid);


--
-- Name: bookloan bookloan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookloan
    ADD CONSTRAINT bookloan_pkey PRIMARY KEY (bookid, branchid, cardno);


--
-- Name: borrower borrower_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.borrower
    ADD CONSTRAINT borrower_pkey PRIMARY KEY (cardno);


--
-- Name: branch branch_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.branch
    ADD CONSTRAINT branch_pkey PRIMARY KEY (branchid);


--
-- Name: buch buch_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.buch
    ADD CONSTRAINT buch_pkey PRIMARY KEY (buchnr);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (coursecode);


--
-- Name: dept dept_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dept
    ADD CONSTRAINT dept_pkey PRIMARY KEY (dnum);


--
-- Name: deptlocation deptlocation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deptlocation
    ADD CONSTRAINT deptlocation_pkey PRIMARY KEY (dnum, dcity);


--
-- Name: enrollment enrollment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_pkey PRIMARY KEY (studentid, coursecode);


--
-- Name: entlehng entlehng_buch_benutzer_von_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.entlehng
    ADD CONSTRAINT entlehng_buch_benutzer_von_key UNIQUE (buch, benutzer, von);


--
-- Name: entlehng entlehng_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.entlehng
    ADD CONSTRAINT entlehng_pkey PRIMARY KEY (entlngnr);


--
-- Name: location location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (locno);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (persnr);


--
-- Name: booking pk_booking; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT pk_booking PRIMARY KEY (hno, gno, datefrom);


--
-- Name: guest pk_guest; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guest
    ADD CONSTRAINT pk_guest PRIMARY KEY (gno);


--
-- Name: hotel pk_hotel; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hotel
    ADD CONSTRAINT pk_hotel PRIMARY KEY (hno);


--
-- Name: room pk_room; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT pk_room PRIMARY KEY (hno, rno);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (prodno);


--
-- Name: produkt produkt_uk41098377439043; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produkt
    ADD CONSTRAINT produkt_uk41098377439043 UNIQUE (bezeichnung);


--
-- Name: project project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (pnum);


--
-- Name: purchase purchase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_pkey PRIMARY KEY (dayno, prodno);


--
-- Name: rechnung rechnung_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rechnung
    ADD CONSTRAINT rechnung_pkey PRIMARY KEY (rechnungnr, datum);


--
-- Name: rechnungpos rechnungpos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rechnungpos
    ADD CONSTRAINT rechnungpos_pkey PRIMARY KEY (rechnungnr, datum, positionnr);


--
-- Name: reserviert reserviert_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserviert
    ADD CONSTRAINT reserviert_pkey PRIMARY KEY (tno, tag, stunde);


--
-- Name: sales sales_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_pkey PRIMARY KEY (dayno, locno, prodno);


--
-- Name: segment segment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.segment
    ADD CONSTRAINT segment_pkey PRIMARY KEY (code, segid);


--
-- Name: sortiment sortiment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sortiment
    ADD CONSTRAINT sortiment_pkey PRIMARY KEY (filnr, ean);


--
-- Name: staff staff_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.staff
    ADD CONSTRAINT staff_pkey PRIMARY KEY (snum);


--
-- Name: staffhotel staffhotel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.staffhotel
    ADD CONSTRAINT staffhotel_pkey PRIMARY KEY (sno);


--
-- Name: strassenart strassenart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.strassenart
    ADD CONSTRAINT strassenart_pkey PRIMARY KEY (art);


--
-- Name: studenten studenten_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studenten
    ADD CONSTRAINT studenten_pkey PRIMARY KEY (mano);


--
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (studentid);


--
-- Name: ort sys_c002764; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ort
    ADD CONSTRAINT sys_c002764 PRIMARY KEY (plz);


--
-- Name: strasse sys_c002766; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.strasse
    ADD CONSTRAINT sys_c002766 PRIMARY KEY (name);


--
-- Name: liegtanstrasse sys_c002770; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.liegtanstrasse
    ADD CONSTRAINT sys_c002770 PRIMARY KEY (strassenname, ort);


--
-- Name: produkt sys_c002817; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produkt
    ADD CONSTRAINT sys_c002817 PRIMARY KEY (ean);


--
-- Name: filiale sys_c002821; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.filiale
    ADD CONSTRAINT sys_c002821 PRIMARY KEY (filnr);


--
-- Name: kunde sys_c002824; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kunde
    ADD CONSTRAINT sys_c002824 PRIMARY KEY (kundenr);


--
-- Name: bauprodukt sys_c002850; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bauprodukt
    ADD CONSTRAINT sys_c002850 PRIMARY KEY (prod_nr);


--
-- Name: bestellung sys_c002853; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellung
    ADD CONSTRAINT sys_c002853 PRIMARY KEY (bestell_nr);


--
-- Name: bestellposition sys_c002856; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellposition
    ADD CONSTRAINT sys_c002856 PRIMARY KEY (bestell_nr, pos_nr);


--
-- Name: lieferschein sys_c002859; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lieferschein
    ADD CONSTRAINT sys_c002859 PRIMARY KEY (liefer_nr);


--
-- Name: lieferposition sys_c002862; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lieferposition
    ADD CONSTRAINT sys_c002862 PRIMARY KEY (liefer_nr, lieferpos_nr);


--
-- Name: human sys_c002867; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.human
    ADD CONSTRAINT sys_c002867 PRIMARY KEY (name);


--
-- Name: parent sys_c002868; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parent
    ADD CONSTRAINT sys_c002868 PRIMARY KEY (parentname, childname);


--
-- Name: artist sys_c002893; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist
    ADD CONSTRAINT sys_c002893 PRIMARY KEY (name);


--
-- Name: genre sys_c002894; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT sys_c002894 PRIMARY KEY (genreid);


--
-- Name: record sys_c002895; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record
    ADD CONSTRAINT sys_c002895 PRIMARY KEY (recordid);


--
-- Name: distribute sys_c002900; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.distribute
    ADD CONSTRAINT sys_c002900 PRIMARY KEY (recordid, media);


--
-- Name: fakultaet sys_c002923; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fakultaet
    ADD CONSTRAINT sys_c002923 PRIMARY KEY (kurzbez);


--
-- Name: studienrichtung sys_c002926; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studienrichtung
    ADD CONSTRAINT sys_c002926 PRIMARY KEY (kennzahl);


--
-- Name: koje sys_c002929; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.koje
    ADD CONSTRAINT sys_c002929 PRIMARY KEY (standnr);


--
-- Name: orteverbindung sys_c002933; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orteverbindung
    ADD CONSTRAINT sys_c002933 PRIMARY KEY (strassenname, ortvon, ortnach);


--
-- Name: node sys_c003458; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.node
    ADD CONSTRAINT sys_c003458 PRIMARY KEY (nodeid);


--
-- Name: highway sys_c003463; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highway
    ADD CONSTRAINT sys_c003463 PRIMARY KEY (code);


--
-- Name: highway sys_c003464; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highway
    ADD CONSTRAINT sys_c003464 UNIQUE (name);


--
-- Name: city sys_c003475; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT sys_c003475 PRIMARY KEY (zip);


--
-- Name: city sys_c003476; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT sys_c003476 UNIQUE (name);


--
-- Name: exit sys_c003479; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exit
    ADD CONSTRAINT sys_c003479 PRIMARY KEY (nodeid);


--
-- Name: exit sys_c003480; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exit
    ADD CONSTRAINT sys_c003480 UNIQUE (exitno);


--
-- Name: highwayexit sys_c003484; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highwayexit
    ADD CONSTRAINT sys_c003484 PRIMARY KEY (nodeid);


--
-- Name: intersection sys_c003488; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.intersection
    ADD CONSTRAINT sys_c003488 PRIMARY KEY (nodeid);


--
-- Name: intersection sys_c003489; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.intersection
    ADD CONSTRAINT sys_c003489 UNIQUE (name);


--
-- Name: highwayintersection sys_c003492; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highwayintersection
    ADD CONSTRAINT sys_c003492 PRIMARY KEY (code, nodeid);


--
-- Name: student sys_c005003; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT sys_c005003 PRIMARY KEY (matrikelnr);


--
-- Name: kurs sys_c005004; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kurs
    ADD CONSTRAINT sys_c005004 PRIMARY KEY (kursnr);


--
-- Name: belegung sys_c005005; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.belegung
    ADD CONSTRAINT sys_c005005 PRIMARY KEY (matrikelnr, kursnr);


--
-- Name: terminal terminal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.terminal
    ADD CONSTRAINT terminal_pkey PRIMARY KEY (tno);


--
-- Name: time time_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."time"
    ADD CONSTRAINT time_pkey PRIMARY KEY (dayno);


--
-- Name: track track_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.track
    ADD CONSTRAINT track_pkey PRIMARY KEY (recordid, tnumber);


--
-- Name: wartung wartung_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wartung
    ADD CONSTRAINT wartung_pkey PRIMARY KEY (rechner, tag, vonstunde);


--
-- Name: wohnung wohnung_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.wohnung
    ADD CONSTRAINT wohnung_pkey PRIMARY KEY (wohnnr);


--
-- Name: workson workson_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workson
    ADD CONSTRAINT workson_pkey PRIMARY KEY (snum, pnum);


--
-- Name: bookcopies bookcopies_bookid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookcopies
    ADD CONSTRAINT bookcopies_bookid_fkey FOREIGN KEY (bookid) REFERENCES public.book(bookid);


--
-- Name: bookcopies bookcopies_branchid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookcopies
    ADD CONSTRAINT bookcopies_branchid_fkey FOREIGN KEY (branchid) REFERENCES public.branch(branchid);


--
-- Name: bookloan bookloan_bookid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookloan
    ADD CONSTRAINT bookloan_bookid_fkey FOREIGN KEY (bookid) REFERENCES public.book(bookid);


--
-- Name: bookloan bookloan_branchid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookloan
    ADD CONSTRAINT bookloan_branchid_fkey FOREIGN KEY (branchid) REFERENCES public.branch(branchid);


--
-- Name: bookloan bookloan_cardno_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookloan
    ADD CONSTRAINT bookloan_cardno_fkey FOREIGN KEY (cardno) REFERENCES public.borrower(cardno);


--
-- Name: borrower borrower_borrowerbranch_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.borrower
    ADD CONSTRAINT borrower_borrowerbranch_fkey FOREIGN KEY (borrowerbranch) REFERENCES public.branch(branchid);


--
-- Name: dept dept_fk_staff; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dept
    ADD CONSTRAINT dept_fk_staff FOREIGN KEY (manager) REFERENCES public.staff(snum) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: deptlocation deptlocation_dnum_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deptlocation
    ADD CONSTRAINT deptlocation_dnum_fkey FOREIGN KEY (dnum) REFERENCES public.dept(dnum);


--
-- Name: enrollment enrollment_coursecode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_coursecode_fkey FOREIGN KEY (coursecode) REFERENCES public.course(coursecode);


--
-- Name: enrollment enrollment_studentid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_studentid_fkey FOREIGN KEY (studentid) REFERENCES public.students(studentid);


--
-- Name: entlehng entlehng_benutzer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.entlehng
    ADD CONSTRAINT entlehng_benutzer_fkey FOREIGN KEY (benutzer) REFERENCES public.benutzer(bennr);


--
-- Name: entlehng entlehng_buch_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.entlehng
    ADD CONSTRAINT entlehng_buch_fkey FOREIGN KEY (buch) REFERENCES public.buch(buchnr);


--
-- Name: booking fk_booking_gno; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT fk_booking_gno FOREIGN KEY (gno) REFERENCES public.guest(gno);


--
-- Name: booking fk_booking_hno_rno; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT fk_booking_hno_rno FOREIGN KEY (hno, rno) REFERENCES public.room(hno, rno);


--
-- Name: room fk_room_hno; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT fk_room_hno FOREIGN KEY (hno) REFERENCES public.hotel(hno);


--
-- Name: project project_dnum_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_dnum_fkey FOREIGN KEY (dnum) REFERENCES public.dept(dnum);


--
-- Name: purchase purchase_dayno_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_dayno_fkey FOREIGN KEY (dayno) REFERENCES public."time"(dayno);


--
-- Name: purchase purchase_prodno_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_prodno_fkey FOREIGN KEY (prodno) REFERENCES public.product(prodno);


--
-- Name: sales sales_dayno_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_dayno_fkey FOREIGN KEY (dayno) REFERENCES public."time"(dayno);


--
-- Name: sales sales_locno_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_locno_fkey FOREIGN KEY (locno) REFERENCES public.location(locno);


--
-- Name: sales sales_prodno_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_prodno_fkey FOREIGN KEY (prodno) REFERENCES public.product(prodno);


--
-- Name: staff staff_dnum_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.staff
    ADD CONSTRAINT staff_dnum_fkey FOREIGN KEY (dnum) REFERENCES public.dept(dnum);


--
-- Name: liegtanstrasse sys_c002771; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.liegtanstrasse
    ADD CONSTRAINT sys_c002771 FOREIGN KEY (strassenname) REFERENCES public.strasse(name) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;


--
-- Name: liegtanstrasse sys_c002772; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.liegtanstrasse
    ADD CONSTRAINT sys_c002772 FOREIGN KEY (ort) REFERENCES public.ort(plz) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;


--
-- Name: bestellposition sys_c002857; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellposition
    ADD CONSTRAINT sys_c002857 FOREIGN KEY (prod_nr) REFERENCES public.bauprodukt(prod_nr);


--
-- Name: lieferposition sys_c002863; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lieferposition
    ADD CONSTRAINT sys_c002863 FOREIGN KEY (liefer_nr) REFERENCES public.lieferschein(liefer_nr) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: lieferposition sys_c002864; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lieferposition
    ADD CONSTRAINT sys_c002864 FOREIGN KEY (bestell_nr, bestellpos_nr) REFERENCES public.bestellposition(bestell_nr, pos_nr) DEFERRABLE INITIALLY DEFERRED;


--
-- Name: record sys_c002896; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record
    ADD CONSTRAINT sys_c002896 FOREIGN KEY (artistname) REFERENCES public.artist(name);


--
-- Name: record sys_c002897; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record
    ADD CONSTRAINT sys_c002897 FOREIGN KEY (genreid) REFERENCES public.genre(genreid);


--
-- Name: distribute sys_c002901; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.distribute
    ADD CONSTRAINT sys_c002901 FOREIGN KEY (recordid) REFERENCES public.record(recordid);


--
-- Name: studienrichtung sys_c002927; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.studienrichtung
    ADD CONSTRAINT sys_c002927 FOREIGN KEY (gehoert_zu) REFERENCES public.fakultaet(kurzbez);


--
-- Name: koje sys_c002932; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.koje
    ADD CONSTRAINT sys_c002932 FOREIGN KEY (gemietet_von) REFERENCES public.studienrichtung(kennzahl);


--
-- Name: highway sys_c003465; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highway
    ADD CONSTRAINT sys_c003465 FOREIGN KEY (startnodeid) REFERENCES public.node(nodeid);


--
-- Name: highway sys_c003466; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highway
    ADD CONSTRAINT sys_c003466 FOREIGN KEY (endnodeid) REFERENCES public.node(nodeid);


--
-- Name: exit sys_c003481; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exit
    ADD CONSTRAINT sys_c003481 FOREIGN KEY (nodeid) REFERENCES public.node(nodeid) ON DELETE CASCADE;


--
-- Name: exit sys_c003482; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exit
    ADD CONSTRAINT sys_c003482 FOREIGN KEY (zip) REFERENCES public.city(zip);


--
-- Name: highwayexit sys_c003485; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highwayexit
    ADD CONSTRAINT sys_c003485 FOREIGN KEY (code) REFERENCES public.highway(code);


--
-- Name: highwayexit sys_c003486; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highwayexit
    ADD CONSTRAINT sys_c003486 FOREIGN KEY (nodeid) REFERENCES public.exit(nodeid);


--
-- Name: intersection sys_c003490; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.intersection
    ADD CONSTRAINT sys_c003490 FOREIGN KEY (nodeid) REFERENCES public.node(nodeid) ON DELETE CASCADE;


--
-- Name: highwayintersection sys_c003493; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highwayintersection
    ADD CONSTRAINT sys_c003493 FOREIGN KEY (code) REFERENCES public.highway(code);


--
-- Name: highwayintersection sys_c003494; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.highwayintersection
    ADD CONSTRAINT sys_c003494 FOREIGN KEY (nodeid) REFERENCES public.intersection(nodeid);


--
-- Name: belegung sys_c005006; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.belegung
    ADD CONSTRAINT sys_c005006 FOREIGN KEY (matrikelnr) REFERENCES public.student(matrikelnr);


--
-- Name: belegung sys_c005007; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.belegung
    ADD CONSTRAINT sys_c005007 FOREIGN KEY (kursnr) REFERENCES public.kurs(kursnr);


--
-- Name: workson workson_pnum_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workson
    ADD CONSTRAINT workson_pnum_fkey FOREIGN KEY (pnum) REFERENCES public.project(pnum);


--
-- Name: workson workson_snum_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workson
    ADD CONSTRAINT workson_snum_fkey FOREIGN KEY (snum) REFERENCES public.staff(snum);


--
-- Name: TABLE aenderungs_protokoll; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.aenderungs_protokoll TO sql;


--
-- Name: TABLE artist; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.artist TO sql;


--
-- Name: TABLE bauprodukt; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.bauprodukt TO sql;


--
-- Name: TABLE belegung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.belegung TO sql;


--
-- Name: TABLE benutzer; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.benutzer TO sql;


--
-- Name: TABLE bestellposition; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.bestellposition TO sql;


--
-- Name: TABLE bestellung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.bestellung TO sql;


--
-- Name: TABLE book; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.book TO sql;


--
-- Name: TABLE bookcopies; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.bookcopies TO sql;


--
-- Name: TABLE booking; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.booking TO sql;


--
-- Name: TABLE bookloan; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.bookloan TO sql;


--
-- Name: TABLE borrower; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.borrower TO sql;


--
-- Name: TABLE branch; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.branch TO sql;


--
-- Name: TABLE buch; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.buch TO sql;


--
-- Name: TABLE buchung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.buchung TO sql;


--
-- Name: TABLE city; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.city TO sql;


--
-- Name: TABLE course; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.course TO sql;


--
-- Name: TABLE dept; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.dept TO sql;


--
-- Name: TABLE deptlocation; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.deptlocation TO sql;


--
-- Name: TABLE distribute; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.distribute TO sql;


--
-- Name: TABLE enrollment; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.enrollment TO sql;


--
-- Name: TABLE entlehng; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.entlehng TO sql;


--
-- Name: TABLE exit; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.exit TO sql;


--
-- Name: TABLE fakultaet; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.fakultaet TO sql;


--
-- Name: TABLE filiale; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.filiale TO sql;


--
-- Name: TABLE genre; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.genre TO sql;


--
-- Name: TABLE gewinne; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.gewinne TO sql;


--
-- Name: TABLE guest; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.guest TO sql;


--
-- Name: TABLE highway; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.highway TO sql;


--
-- Name: TABLE highwayexit; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.highwayexit TO sql;


--
-- Name: TABLE highwayintersection; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.highwayintersection TO sql;


--
-- Name: TABLE hotel; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.hotel TO sql;


--
-- Name: TABLE human; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.human TO sql;


--
-- Name: TABLE inhaber; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.inhaber TO sql;


--
-- Name: TABLE intersection; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.intersection TO sql;


--
-- Name: TABLE koje; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.koje TO sql;


--
-- Name: TABLE konto; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.konto TO sql;


--
-- Name: TABLE kunde; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.kunde TO sql;


--
-- Name: TABLE kurs; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.kurs TO sql;


--
-- Name: TABLE lieferposition; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.lieferposition TO sql;


--
-- Name: TABLE lieferschein; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.lieferschein TO sql;


--
-- Name: TABLE liegtanstrasse; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.liegtanstrasse TO sql;


--
-- Name: TABLE location; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.location TO sql;


--
-- Name: TABLE mietet; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.mietet TO sql;


--
-- Name: TABLE node; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.node TO sql;


--
-- Name: TABLE ort; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.ort TO sql;


--
-- Name: TABLE orteverbindung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.orteverbindung TO sql;


--
-- Name: TABLE parent; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.parent TO sql;


--
-- Name: TABLE person; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.person TO sql;


--
-- Name: TABLE product; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.product TO sql;


--
-- Name: TABLE produkt; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.produkt TO sql;


--
-- Name: TABLE project; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.project TO sql;


--
-- Name: TABLE purchase; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.purchase TO sql;


--
-- Name: TABLE rechnung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.rechnung TO sql;


--
-- Name: TABLE rechnungpos; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.rechnungpos TO sql;


--
-- Name: TABLE record; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.record TO sql;


--
-- Name: TABLE reserviert; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.reserviert TO sql;


--
-- Name: TABLE room; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.room TO sql;


--
-- Name: TABLE sales; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.sales TO sql;


--
-- Name: TABLE segment; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.segment TO sql;


--
-- Name: TABLE sortiment; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.sortiment TO sql;


--
-- Name: TABLE sortiment_aenderungen; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.sortiment_aenderungen TO sql;


--
-- Name: TABLE staff; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.staff TO sql;


--
-- Name: TABLE staffhotel; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.staffhotel TO sql;


--
-- Name: TABLE strasse; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.strasse TO sql;


--
-- Name: TABLE strassenart; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.strassenart TO sql;


--
-- Name: TABLE student; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.student TO sql;


--
-- Name: TABLE studenten; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.studenten TO sql;


--
-- Name: TABLE students; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.students TO sql;


--
-- Name: TABLE studienrichtung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.studienrichtung TO sql;


--
-- Name: TABLE terminal; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.terminal TO sql;


--
-- Name: TABLE test; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.test TO sql;


--
-- Name: TABLE "time"; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public."time" TO sql;


--
-- Name: TABLE track; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.track TO sql;


--
-- Name: TABLE wartung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.wartung TO sql;


--
-- Name: TABLE wohnung; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.wohnung TO sql;


--
-- Name: TABLE workson; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.workson TO sql;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public REVOKE ALL ON TABLES  FROM postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT SELECT ON TABLES  TO sql;


--
-- PostgreSQL database dump complete
--

