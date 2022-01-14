--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.4

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

-- DATALOG database
CREATE DATABASE datalog WITH TEMPLATE = template0 ENCODING = 'UTF8';


ALTER DATABASE datalog OWNER TO etutor;

\connect datalog
--
-- Name: error_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.error_categories (
    name character varying(25) NOT NULL,
    id numeric NOT NULL
);


ALTER TABLE public.error_categories OWNER TO postgres;

--
-- Name: error_grading_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.error_grading_group (
    id numeric NOT NULL
);


ALTER TABLE public.error_grading_group OWNER TO postgres;

--
-- Name: error_gradings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.error_gradings (
    grading_group numeric NOT NULL,
    grading_level numeric NOT NULL,
    grading_category numeric NOT NULL,
    minus_points numeric(4,1) NOT NULL
);


ALTER TABLE public.error_gradings OWNER TO postgres;

--
-- Name: exercise; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exercise (
    id numeric NOT NULL,
    query character varying(1500) NOT NULL,
    facts numeric NOT NULL,
    gradings numeric,
    points numeric(4,1)
);


ALTER TABLE public.exercise OWNER TO postgres;

--
-- Name: facts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.facts (
    facts character varying(4000) NOT NULL,
    id numeric NOT NULL,
    name character varying(20) NOT NULL
);


ALTER TABLE public.facts OWNER TO postgres;

--
-- Name: predicates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.predicates (
    name character varying(35) NOT NULL,
    exercise numeric(10,0) NOT NULL
);


ALTER TABLE public.predicates OWNER TO postgres;

--
-- Name: unchecked_terms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.unchecked_terms (
    predicate character varying(35) NOT NULL,
    term character varying(35) NOT NULL,
    "position" numeric(10,0) NOT NULL,
    exercise numeric(10,0) NOT NULL
);


ALTER TABLE public.unchecked_terms OWNER TO postgres;

--
-- Data for Name: error_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.error_categories (name, id) FROM stdin;
missing-predicate	1
redundant-predicate	2
low-term-predicate	3
high-term-predicate	4
missing-fact	5
redundant-fact	6
negative-fact	7
positive-fact	8
\.


--
-- Data for Name: error_grading_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.error_grading_group (id) FROM stdin;
1
\.


--
-- Data for Name: error_gradings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.error_gradings (grading_group, grading_level, grading_category, minus_points) FROM stdin;
1	3	2	0.0
1	3	1	0.0
1	3	3	0.0
1	3	4	0.0
1	3	5	0.0
1	3	6	0.0
1	3	7	0.0
1	3	8	0.0
\.


--
-- Data for Name: exercise; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exercise (id, query, facts, gradings, points) FROM stdin;
14328	auftragRadprofi(X) :- \t\r\n\tauftrag(X,Y), \r\n\thatBezeichnung(Y,radprofi_gmbh).	3	\N	1.0
14329	kundeBez(X,Y) :- \t\r\n\tkundeInland(X), \r\n\thatBezeichnung(X,Y).\r\n\t\r\nkundeBez(X,Y) :-\t\r\n\tkundeExport(X),\r\n\thatBezeichnung(X,Y).	3	\N	1.0
14330	verbautesProdukt(X) :- \r\n\tprodukt(X), \r\n\tstueli(_,X).\r\n\r\nfertigprodukt(X) :- \r\n\tprodukt(X), \r\n\tnot verbautesProdukt(X).	3	\N	1.0
14332	verbautesProdukt(X) :- \r\n\tprodukt(X), \r\n\tstueli(_,X).\r\n\r\nfertigprodukt(X) :- \r\n\tprodukt(X), \r\n\tnot verbautesProdukt(X).\r\n\r\nprodukteEbene3(E3) :- \r\n\tfertigprodukt(E1), \r\n\tstueli(E1,E2), \r\n\tstueli(E2,E3). 	3	\N	1.0
14333	stueliAufl(X,Y) :- stueli(X,Y).\r\n\r\nstueliAufl(X,Y) :- \r\n\tstueli(X,Z), \r\n\tstueliAufl(Z,Y).\r\n\t\r\nverbautInMTB(Y) :- \r\n\tstueliAufl(X,Y), \r\n\thatBezeichnung(X,mtb_extreme_downhill).	3	\N	1.0
14334	stueliAufl(X,Y) :- stueli(X,Y).\r\n\r\nstueliAufl(X,Y) :- \r\n\tstueli(X,Z), \r\n\tstueliAufl(Z,Y).\r\n\r\nexportierteProdukte(X,Bez):- \t\r\n\tkundeExport(K), \r\n\tauftrag(A,K),  \r\n\tauftragpos(P,A,X,_),\r\n\thatBezeichnung(X,Bez). \r\n\t\r\nexportierteProdukte(X,Bez):- \t\r\n\tkundeExport(K), \r\n\tauftrag(A,K), \r\n\tauftragpos(P,A,FP,_),\r\n\tstueliAufl(FP,X),\r\n\thatBezeichnung(X,Bez).	3	\N	1.0
14335	verbautesProdukt(X) :- \r\n\tprodukt(X), \r\n\tstueli(_,X).\r\n\r\nfertigprodukt(X) :- \r\n\tprodukt(X), \r\n\tnot verbautesProdukt(X).\r\n\r\nkunde(K) :- kundeExport(K).\r\nkunde(K) :- kundeInland(K).\r\n\r\nproduktKunde(Pr,K) :- \t\r\n\tauftrag(A,K),\r\n\tauftragpos(Pos,A,Pr,_).\r\n\r\nproduktNichtAnAlleVerkauft(P) :- \r\n\tfertigprodukt(P), \r\n\tkunde(K), \r\n\tnot produktKunde(P,K).\r\n\t\t\t\t\t\t\t\r\nproduktAnAlleVerkauft(P) :- \t\r\n\tfertigprodukt(P), \r\n\tnot produktNichtAnAlleVerkauft(P).	3	\N	1.0
12001	alleSuperVerzeichnisse (V,S) :- verzeichnis(V,S), S <> root.\r\nalleSuperVerzeichnisse (V,S) :- verzeichnis(V,Z), alleSuperVerzeichnisse(Z,S), S <> root.\r\n\r\nkeinBlattVerzeichnis(S) :- verzeichnis(V,S).\r\nblattVerzeichnis(V) :- verzeichnis(V,S), not keinBlattVerzeichnis(V).\r\n\r\ngleichTiefeHierarchienTemp(V1,V2) :-  verzeichnis(V1,GS), verzeichnis(V2,GS).\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(V1,V2), V1 < V2.\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(S1,S2), verzeichnis(V1,S1), verzeichnis(V2,S2), V1 < V2.\r\n\r\ndtInVerzDatei(VID,T) :- datei(D,T,VID).\r\nverzMitFehlendenDateiTypen(VID) :- verzeichnis(VID,S), datei(DL,T,VL), not dtInVerzDatei(VID,T).\r\nenthaeltAlleDateiTypen (VID) :- verzeichnis(VID,S), not verzMitFehlendenDateiTypen(VID).\r\n\r\n\r\nhabenGemeinsameDateiTypen (VID1, VID2) :- dtInVerzDatei(VID1, T), dtInVerzDatei(VID2, T).\r\nhabenKeineGemeinsamenDateitypen (VID1, VID2) :- verzeichnis(VID1,S1), datei(D1,T1,VID1), verzeichnis(VID2,S2), datei(D2,T2,VID2), not habenGemeinsameDateiTypen(VID1,VID2), VID1 < VID2.\r\n\r\nenthaeltDatei(V,T) :- datei(D,T,V).\r\nenthaeltDatei(V,T) :- datei(D,T,V1), alleSuperVerzeichnisse(V1,V).\r\nenthaeltNichtTextDatei(V) :- enthaeltDatei(V,T), T <> text.\r\nhatNurTexte(V) :- enthaeltDatei(V,text), not enthaeltNichtTextDatei(V).	1	\N	1.0
12002	alleSuperVerzeichnisse (V,S) :- verzeichnis(V,S), S <> root.\r\nalleSuperVerzeichnisse (V,S) :- verzeichnis(V,Z), alleSuperVerzeichnisse(Z,S), S <> root.\r\n\r\nkeinBlattVerzeichnis(S) :- verzeichnis(V,S).\r\nblattVerzeichnis(V) :- verzeichnis(V,S), not keinBlattVerzeichnis(V).\r\n\r\ngleichTiefeHierarchienTemp(V1,V2) :-  verzeichnis(V1,GS), verzeichnis(V2,GS).\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(V1,V2), V1 < V2.\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(S1,S2), verzeichnis(V1,S1), verzeichnis(V2,S2), V1 < V2.\r\n\r\ndtInVerzDatei(VID,T) :- datei(D,T,VID).\r\nverzMitFehlendenDateiTypen(VID) :- verzeichnis(VID,S), datei(DL,T,VL), not dtInVerzDatei(VID,T).\r\nenthaeltAlleDateiTypen (VID) :- verzeichnis(VID,S), not verzMitFehlendenDateiTypen(VID).\r\n\r\n\r\nhabenGemeinsameDateiTypen (VID1, VID2) :- dtInVerzDatei(VID1, T), dtInVerzDatei(VID2, T).\r\nhabenKeineGemeinsamenDateitypen (VID1, VID2) :- verzeichnis(VID1,S1), datei(D1,T1,VID1), verzeichnis(VID2,S2), datei(D2,T2,VID2), not habenGemeinsameDateiTypen(VID1,VID2), VID1 < VID2.\r\n\r\nenthaeltDatei(V,T) :- datei(D,T,V).\r\nenthaeltDatei(V,T) :- datei(D,T,V1), alleSuperVerzeichnisse(V1,V).\r\nenthaeltNichtTextDatei(V) :- enthaeltDatei(V,T), T <> text.\r\nhatNurTexte(V) :- enthaeltDatei(V,text), not enthaeltNichtTextDatei(V).	1	\N	1.0
12003	alleSuperVerzeichnisse (V,S) :- verzeichnis(V,S), S <> root.\r\nalleSuperVerzeichnisse (V,S) :- verzeichnis(V,Z), alleSuperVerzeichnisse(Z,S), S <> root.\r\n\r\nkeinBlattVerzeichnis(S) :- verzeichnis(V,S).\r\nblattVerzeichnis(V) :- verzeichnis(V,S), not keinBlattVerzeichnis(V).\r\n\r\ngleichTiefeHierarchienTemp(V1,V2) :-  verzeichnis(V1,GS), verzeichnis(V2,GS).\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(V1,V2), V1 < V2.\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(S1,S2), verzeichnis(V1,S1), verzeichnis(V2,S2), V1 < V2.\r\n\r\ndtInVerzDatei(VID,T) :- datei(D,T,VID).\r\nverzMitFehlendenDateiTypen(VID) :- verzeichnis(VID,S), datei(DL,T,VL), not dtInVerzDatei(VID,T).\r\nenthaeltAlleDateiTypen (VID) :- verzeichnis(VID,S), not verzMitFehlendenDateiTypen(VID).\r\n\r\n\r\nhabenGemeinsameDateiTypen (VID1, VID2) :- dtInVerzDatei(VID1, T), dtInVerzDatei(VID2, T).\r\nhabenKeineGemeinsamenDateitypen (VID1, VID2) :- verzeichnis(VID1,S1), datei(D1,T1,VID1), verzeichnis(VID2,S2), datei(D2,T2,VID2), not habenGemeinsameDateiTypen(VID1,VID2), VID1 < VID2.\r\n\r\nenthaeltDatei(V,T) :- datei(D,T,V).\r\nenthaeltDatei(V,T) :- datei(D,T,V1), alleSuperVerzeichnisse(V1,V).\r\nenthaeltNichtTextDatei(V) :- enthaeltDatei(V,T), T <> text.\r\nhatNurTexte(V) :- enthaeltDatei(V,text), not enthaeltNichtTextDatei(V).	1	\N	1.0
12004	alleSuperVerzeichnisse (V,S) :- verzeichnis(V,S), S <> root.\r\nalleSuperVerzeichnisse (V,S) :- verzeichnis(V,Z), alleSuperVerzeichnisse(Z,S), S <> root.\r\n\r\nkeinBlattVerzeichnis(S) :- verzeichnis(V,S).\r\nblattVerzeichnis(V) :- verzeichnis(V,S), not keinBlattVerzeichnis(V).\r\n\r\ngleichTiefeHierarchienTemp(V1,V2) :-  verzeichnis(V1,GS), verzeichnis(V2,GS).\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(V1,V2), V1 < V2.\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(S1,S2), verzeichnis(V1,S1), verzeichnis(V2,S2), V1 < V2.\r\n\r\ndtInVerzDatei(VID,T) :- datei(D,T,VID).\r\nverzMitFehlendenDateiTypen(VID) :- verzeichnis(VID,S), datei(DL,T,VL), not dtInVerzDatei(VID,T).\r\nenthaeltAlleDateiTypen (VID) :- verzeichnis(VID,S), not verzMitFehlendenDateiTypen(VID).\r\n\r\n\r\nhabenGemeinsameDateiTypen (VID1, VID2) :- dtInVerzDatei(VID1, T), dtInVerzDatei(VID2, T).\r\nhabenKeineGemeinsamenDateitypen (VID1, VID2) :- verzeichnis(VID1,S1), datei(D1,T1,VID1), verzeichnis(VID2,S2), datei(D2,T2,VID2), not habenGemeinsameDateiTypen(VID1,VID2), VID1 < VID2.\r\n\r\nenthaeltDatei(V,T) :- datei(D,T,V).\r\nenthaeltDatei(V,T) :- datei(D,T,V1), alleSuperVerzeichnisse(V1,V).\r\nenthaeltNichtTextDatei(V) :- enthaeltDatei(V,T), T <> text.\r\nhatNurTexte(V) :- enthaeltDatei(V,text), not enthaeltNichtTextDatei(V).	1	\N	1.0
12005	alleSuperVerzeichnisse (V,S) :- verzeichnis(V,S), S <> root.\r\nalleSuperVerzeichnisse (V,S) :- verzeichnis(V,Z), alleSuperVerzeichnisse(Z,S), S <> root.\r\n\r\nkeinBlattVerzeichnis(S) :- verzeichnis(V,S).\r\nblattVerzeichnis(V) :- verzeichnis(V,S), not keinBlattVerzeichnis(V).\r\n\r\ngleichTiefeHierarchienTemp(V1,V2) :-  verzeichnis(V1,GS), verzeichnis(V2,GS).\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(V1,V2), V1 < V2.\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(S1,S2), verzeichnis(V1,S1), verzeichnis(V2,S2), V1 < V2.\r\n\r\ndtInVerzDatei(VID,T) :- datei(D,T,VID).\r\nverzMitFehlendenDateiTypen(VID) :- verzeichnis(VID,S), datei(DL,T,VL), not dtInVerzDatei(VID,T).\r\nenthaeltAlleDateiTypen (VID) :- verzeichnis(VID,S), not verzMitFehlendenDateiTypen(VID).\r\n\r\n\r\nhabenGemeinsameDateiTypen (VID1, VID2) :- dtInVerzDatei(VID1, T), dtInVerzDatei(VID2, T).\r\nhabenKeineGemeinsamenDateitypen (VID1, VID2) :- verzeichnis(VID1,S1), datei(D1,T1,VID1), verzeichnis(VID2,S2), datei(D2,T2,VID2), not habenGemeinsameDateiTypen(VID1,VID2), VID1 < VID2.\r\n\r\nenthaeltDatei(V,T) :- datei(D,T,V).\r\nenthaeltDatei(V,T) :- datei(D,T,V1), alleSuperVerzeichnisse(V1,V).\r\nenthaeltNichtTextDatei(V) :- enthaeltDatei(V,T), T <> text.\r\nhatNurTexte(V) :- enthaeltDatei(V,text), not enthaeltNichtTextDatei(V).	1	\N	1.0
14254	verkauft(A) :- artikel(A,_), verkauf(_,A).\r\nladenH(A) :- artikel(A,_), not verkauft(A).\r\n	2	\N	1.0
14256	verkauft(A) :- artikel(A,_), verkauf(_,A).\r\nladenH(A) :- artikel(A,_), not verkauft(A).\r\nlowPerformer(K) :- kategorie(K), artikel(Y,K), ladenH(Y).\r\nhighPerformer(X) :- kategorie(X), not lowPerformer(X).\r\n	2	\N	1.0
14255	verkauft(A) :- artikel(A,_), verkauf(_,A).\r\nladenH(A) :- artikel(A,_), not verkauft(A).\r\nlowPerformer(K) :- kategorie(K), artikel(Y,K), ladenH(Y).\r\n	2	\N	1.0
14257	katInTrans(K,T) :- verkauf(T,A), artikel(A,K).\r\nkatNotInTrans(K,T) :- kategorie(K), transaktion(T), not katInTrans(K,T).\r\nboth(T) :- katInTrans(_,T), katNotInTrans(_,T).\r\nalleKategorienTrans(T) :- katInTrans(_,T), not both(T).\r\n	2	\N	1.0
14251	brotOderMilch(T) :- verkauf(T,brot).\r\nbrotOderMilch(T) :- verkauf(T,milch).	2	\N	1.0
14252	anzProdJeTrans(T,N) :- transaktion(T), #count{A: verkauf(Y,A), Y=T}=N.	2	\N	1.0
14253	anzProdJeTrans(T,N) :- transaktion(T), #count{A: verkauf(Y,A), Y=T}=N.\r\ntMitMehrAlsZweiArt(T) :- anzProdJeTrans(T,N), N>2.	2	\N	1.0
14258	katTransOhneLebensmittel(T) :- verkauf(T,A), artikel(A,K), K<>lebensmittel.\r\nkatTransLebensmittel(T) :- verkauf(T,A), artikel(A,K), K=lebensmittel.\r\nallesLebensmittelTrans(T) :- not katTransOhneLebensmittel(T), katTransLebensmittel(T), transaktion(T).	2	\N	1.0
12000	alleSuperVerzeichnisse (V,S) :- verzeichnis(V,S), S <> root.\r\nalleSuperVerzeichnisse (V,S) :- verzeichnis(V,Z), alleSuperVerzeichnisse(Z,S), S <> root.\r\n\r\nkeinBlattVerzeichnis(S) :- verzeichnis(V,S).\r\nblattVerzeichnis(V) :- verzeichnis(V,S), not keinBlattVerzeichnis(V).\r\n\r\ngleichTiefeHierarchienTemp(V1,V2) :-  verzeichnis(V1,GS), verzeichnis(V2,GS).\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(V1,V2), V1 < V2.\r\ngleichTiefeHierarchien(V1,V2) :- gleichTiefeHierarchienTemp(S1,S2), verzeichnis(V1,S1), verzeichnis(V2,S2), V1 < V2.\r\n\r\ndtInVerzDatei(VID,T) :- datei(D,T,VID).\r\nverzMitFehlendenDateiTypen(VID) :- verzeichnis(VID,S), datei(DL,T,VL), not dtInVerzDatei(VID,T).\r\nenthaeltAlleDateiTypen (VID) :- verzeichnis(VID,S), not verzMitFehlendenDateiTypen(VID).\r\n\r\n\r\nhabenGemeinsameDateiTypen (VID1, VID2) :- dtInVerzDatei(VID1, T), dtInVerzDatei(VID2, T).\r\nhabenKeineGemeinsamenDateitypen (VID1, VID2) :- verzeichnis(VID1,S1), datei(D1,T1,VID1), verzeichnis(VID2,S2), datei(D2,T2,VID2), not habenGemeinsameDateiTypen(VID1,VID2), VID1 < VID2.\r\n\r\nenthaeltDatei(V,T) :- datei(D,T,V).\r\nenthaeltDatei(V,T) :- datei(D,T,V1), alleSuperVerzeichnisse(V1,V).\r\nenthaeltNichtTextDatei(V) :- enthaeltDatei(V,T), T <> text.\r\nhatNurTexte(V) :- enthaeltDatei(V,text), not enthaeltNichtTextDatei(V).	1	\N	1.0
14337	auftraege(X,Y) :- auftrag(X,Y).	3	\N	1.0
14336	auftraege(X,Y) :- auftrag(X,Y).	3	\N	1.0
\.


--
-- Data for Name: facts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.facts (facts, id, name) FROM stdin;
kundeInland(k1).\r\nkundeInland(k2).\r\n\r\nkundeExport(k3).\r\n\r\nhatBezeichnung(k1,radprofi_gmbh).\r\nhatBezeichnung(k2,bike_palast).\r\nhatBezeichnung(k3,velo_schwyz).\r\n\r\nprodukt(eb341234).\r\nprodukt(eb123123).\r\nprodukt(zb120000).\r\nprodukt(t1).\r\nprodukt(t2).\r\nprodukt(t3).\r\nprodukt(t4).\r\n\r\nhatBezeichnung(eb341234,mtb_extreme_downhill).\r\nhatBezeichnung(eb123123,cb_comfort).\r\nhatBezeichnung(zb120000,mtb_super_cross).\r\nhatBezeichnung(t1,vorderrad_12).\r\nhatBezeichnung(t2,speiche_12).\r\nhatBezeichnung(t3,nabe_12).\r\nhatBezeichnung(t4,kugellager_12).\r\n\r\nstueli(eb341234,t1).\r\nstueli(eb123123,t1).\r\nstueli(t1,t2).\r\nstueli(t1,t3).\r\nstueli(t2,t4).\r\n\r\nauftrag(ar123,k1).\r\nauftrag(ar125,k3).\r\nauftrag(ar128,k2).\r\n\r\nauftragpos(pos1,ar123,eb341234,3).\r\nauftragpos(pos2,ar123,eb123123,2).\r\nauftragpos(pos3,ar123,zb120000,4).\r\nauftragpos(pos4,ar125,eb341234,2).\r\nauftragpos(pos5,ar125,zb120000,1).\r\nauftragpos(pos6,ar128,eb341234,5).	3	Auftragsverwaltung
verzeichnis(kontinente, root).\nverzeichnis(europa, kontinente).\nverzeichnis(afrika, kontinente).\nverzeichnis(amerika, kontinente).\nverzeichnis(suedamerika, amerika).\nverzeichnis(nordamerika, amerika).\nverzeichnis(usa, nordamerika).\nverzeichnis(kanada, nordamerika).\nverzeichnis(oesterreich, europa).\nverzeichnis(frankreich, europa).\nverzeichnis(polen, europa).\nverzeichnis(ungarn, europa).\nverzeichnis(ozeane, root).\nverzeichnis(atlantik, ozeane).\nverzeichnis(pazifik, ozeane).\n\ndatei(landkarte11, bild, europa).\ndatei(landkarte12, bild, amerika).\ndatei(landkarte51, bild, europa).\ndatei(landkarte61, bild, oesterreich).\ndatei(landkarte62, bild, frankreich).\ndatei(landkarte63, bild, polen).\ndatei(landkarte70, bild, nordamerika).\ndatei(staedte100, text, oesterreich).\ndatei(staedte101, text, frankreich).\ndatei(staedte102, text, ungarn).\ndatei(staedte105, text, amerika).\ndatei(staedte110, text, nordamerika).\ndatei(staedte120, text, suedamerika).\ndatei(inseln1, text, atlantik).\ndatei(inseln2, text, pazifik).\ndatei(hymne500, musik, europa).\ndatei(hymne501, musik, oesterreich).\ndatei(hymne502, musik, frankreich).\ndatei(hymne505, musik, nordamerika).\ndatei(hymne510, musik, atlantik).\ndatei(film1, video, ungarn).\ndatei(film2, video, oesterreich).\ndatei(film4, video, nordamerika).	1	Verzeichnisse
kategorie(lebensmittel). \nkategorie(getraenke). \nkategorie(haushaltsartikel). \n\nartikel(chips, lebensmittel). \nartikel(brot, lebensmittel). \nartikel(milch, lebensmittel). \nartikel(fisch, lebensmittel). \nartikel(cola, getraenke). \nartikel(mineralwasser, getraenke). \nartikel(bier, getraenke). \nartikel(servietten, haushaltsartikel). \nartikel(messer, haushaltsartikel). \nartikel(teller, haushaltsartikel). \n\nverkauf(t1, brot). \nverkauf(t1, milch). \nverkauf(t2, brot). \nverkauf(t2, cola). \nverkauf(t2, servietten). \nverkauf(t3, chips). \nverkauf(t3, brot). \nverkauf(t3, fisch). \nverkauf(t3, milch). \nverkauf(t4, fisch). \nverkauf(t4, bier). \nverkauf(t4, teller). \nverkauf(t5, milch). \nverkauf(t5, teller). \n\ntransaktion(t1). \ntransaktion(t2). \ntransaktion(t3). \ntransaktion(t4). \ntransaktion(t5).	2	Verkaeufe
\.


--
-- Data for Name: predicates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.predicates (name, exercise) FROM stdin;
auftragRadprofi	14328
habenKeineGemeinsamenDateitypen	12004
produktAnAlleVerkauft	14335
brotOderMilch	14251
anzProdJeTrans	14252
tMitMehrAlsZweiArt	14253
ladenH	14254
lowPerformer	14255
highPerformer	14256
allesLebensmittelTrans	14258
kundeBez	14329
fertigprodukt	14330
produkteEbene3	14332
alleKategorienTrans	14257
verbautInMTB	14333
exportierteProdukte	14334
alleSuperVerzeichnisse	12000
blattVerzeichnis	12001
gleichTiefeHierarchien	12002
enthaeltAlleDateiTypen	12003
hatNurTexte	12005
\.


--
-- Data for Name: unchecked_terms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.unchecked_terms (predicate, term, "position", exercise) FROM stdin;
hatBezeichnung	radprofi_gmbh	2	14328
verzeichnis	root	2	12004
datei	text	2	12004
verkauf	milch	2	14251
verkauf	brot	2	14251
artikel	lebensmittel	2	14258
kategorie	lebensmittel	1	14258
hatBezeichnung	mtb_extreme_downhill	2	14333
datei	text	2	12000
verzeichnis	root	2	12000
verzeichnis	root	2	12001
datei	text	2	12001
verzeichnis	root	2	12002
datei	text	2	12002
verzeichnis	root	2	12003
datei	text	2	12003
verzeichnis	root	2	12005
datei	text	2	12005
\.


--
-- Name: error_categories error_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.error_categories
    ADD CONSTRAINT error_categories_pkey PRIMARY KEY (id);


--
-- Name: error_grading_group error_grading_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.error_grading_group
    ADD CONSTRAINT error_grading_group_pkey PRIMARY KEY (id);


--
-- Name: exercise exercise_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT exercise_pkey PRIMARY KEY (id);


--
-- Name: facts facts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.facts
    ADD CONSTRAINT facts_pkey PRIMARY KEY (id);


--
-- Name: error_gradings grading_grad_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.error_gradings
    ADD CONSTRAINT grading_grad_unique UNIQUE (grading_group, grading_category);


--
-- Name: exercise exercise_fact_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT exercise_fact_id FOREIGN KEY (facts) REFERENCES public.facts(id);


--
-- Name: exercise exercise_grad_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT exercise_grad_id FOREIGN KEY (gradings) REFERENCES public.error_grading_group(id);


--
-- Name: error_gradings grading_cat_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.error_gradings
    ADD CONSTRAINT grading_cat_id FOREIGN KEY (grading_category) REFERENCES public.error_categories(id);


--
-- Name: error_gradings grading_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.error_gradings
    ADD CONSTRAINT grading_id FOREIGN KEY (grading_group) REFERENCES public.error_grading_group(id) ON DELETE CASCADE;


--
-- Name: predicates predicates_fk_exercise; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predicates
    ADD CONSTRAINT predicates_fk_exercise FOREIGN KEY (exercise) REFERENCES public.exercise(id) ON DELETE CASCADE;


--
-- Name: unchecked_terms unchecked_exercise_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.unchecked_terms
    ADD CONSTRAINT unchecked_exercise_id FOREIGN KEY (exercise) REFERENCES public.exercise(id);


--
-- PostgreSQL database dump complete
--

