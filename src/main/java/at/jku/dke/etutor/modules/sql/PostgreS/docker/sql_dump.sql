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
-- Name: connections; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.connections (
    id integer NOT NULL,
    conn_string character varying(100) NOT NULL,
    conn_user character varying(100) NOT NULL,
    conn_pwd character varying(100) NOT NULL
);


ALTER TABLE public.connections OWNER TO postgres;

--
-- Name: exercises; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exercises (
    id integer NOT NULL,
    submission_db integer NOT NULL,
    practise_db integer NOT NULL,
    solution character varying(2048) NOT NULL
);


ALTER TABLE public.exercises OWNER TO postgres;

--
-- Data for Name: connections; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.connections (id, conn_string, conn_user, conn_pwd) FROM stdin;
1	jdbc:postgresql://localhost:5432/sql_trial_begin	sql	sql
3	jdbc:postgresql://localhost:5432/sql_trial_begin	sql	sql
5	jdbc:postgresql://localhost:5432/sql_trial_begin	sql	sql
9	jdbc:postgresql://localhost:5432/sql_trial_begin	sql	sql
10	jdbc:postgresql://localhost:5432/sql_trial_begin	sql	sql
16	jdbc:postgresql://localhost:5432/sql_trial_begin	sql	sql
15	jdbc:postgresql://localhost:5432/sql_trial_begin	sql	sql
2	jdbc:postgresql://localhost:5432/sql_submission_begin	sql	sql
4	jdbc:postgresql://localhost:5432/sql_submission_begin	sql	sql
6	jdbc:postgresql://localhost:5432/sql_submission_begin	sql	sql
7	jdbc:postgresql://localhost:5432/sql_trial_wohnungen	sql	sql
8	jdbc:postgresql://localhost:5432/sql_submission_wohnungen	sql	sql
11	jdbc:postgresql://localhost:5432/sql_trial_pruefungen	sql	sql
12	jdbc:postgresql://localhost:5432/sql_submission_pruefungen	sql	sql
13	jdbc:postgresql://localhost:5432/sql_trial_auftraege	sql	sql
14	jdbc:postgresql://localhost:5432/sql_submission_auftraege	sql	sql
\.


--
-- Data for Name: exercises; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exercises (id, submission_db, practise_db, solution) FROM stdin;
10015	1	1	SELECT u.prodno, s.Y,  SUM(u.qty) PQTY, SUM(s.qty) SQTY, SUM(s.qty)-SUM(u.qty) DQTY FROM  (SELECT t1.y, s1.prodno, SUM(s1.qty) qty FROM sales s1, TIME t1 WHERE s1.dayno=t1.dayno GROUP BY t1.y, s1.prodno) s, (SELECT t2.y, u1.prodno, SUM(u1.qty) qty FROM purchase u1, TIME t2 WHERE u1.dayno=t2.dayno GROUP BY t2.y, u1.prodno) u WHERE s.prodno=u.prodno AND u.y=s.y GROUP BY (u.prodno, s.y) ORDER BY u.prodno
10043	1	1	select distinct longitude, latitude from node;
10042	1	1	select * from segment where code = 'H10';
10044	1	1	select max(toKM - fromKM) MaxLen from segment;
10045	1	1	select code, max(toKM - fromKM) MaxLen from segment group by code;
10046	1	1	SELECT code, MAX(toKM - fromKM) MaxLen\r\nFROM segment\r\nGROUP BY code\r\nHAVING MAX(toKM - fromKM) > 50;
10047	1	1	SELECT name, longitude, latitude\r\nFROM highway, node\r\nWHERE startNodeID = nodeID;
10048	1	1	SELECT DISTINCT c.name, n1.longitude, n1.latitude\r\nFROM node n1, node n2, city c, exit e\r\nWHERE n1.type != n2.type AND\r\n      n1.longitude = n2.longitude AND\r\n      n1.latitude = n2.latitude AND\r\n      n1.nodeID = e.nodeID AND\r\n      e.ZIP = c.ZIP;
10051	1	1	select s1.code, s1.segID, s1.toKM - s1.fromKM Length from segment s1 where s1.toKM - s1.fromKM = (select max (s2.toKM - s2.fromKM) from segment s2 where s1.code = s2.code)
10052	1	1	(SELECT n1.nodeID, n1.longitude, n1.latitude FROM node n1, exit e, highwayExit he WHERE he.code = 'H10' AND       he.nodeID = e.nodeID AND       e.nodeID = n1.nodeID) UNION (SELECT n2.nodeID, n2.longitude, n2.latitude FROM node n2, intersection i, HighwayIntersection hi WHERE hi.code = 'H10' AND       hi.nodeID = i.nodeID AND       i.nodeID = n2.nodeID);\r\n
10050	1	1	SELECT s1.code, s1.segID, (s1.toKM - s1.fromKM) AS LENGTH\r\nFROM segment s1\r\nWHERE (s1.toKM - s1.fromKM) >\r\n      (SELECT (s2.toKM - s2.fromKM)\r\n       FROM segment s2\r\n       WHERE s2.segID = 'S01' AND s2.code = 'H10')
13169	4	3	select k.kontonr, k.inhname, max(betrag), min(betrag)\r\nfrom konto k, buchung b\r\nwhere k.kontonr = b.vonkonto\r\ngroup by k.kontonr, k.inhname;
13089	1	2	\t\r\nDROP TABLE ETUTOR_JESS.JessExercise;\r\n\r\nCREATE ETUTOR_JESS.TABLE JessExercise (\r\n\tid\tNUMBER,\r\n\tprivate_facts VARCHAR(4000),\r\n\tpublic_facts VARCHAR(4000),\r\n\treference_solution VARCHAR(4000),\r\n\ttransitivity NUMBER,\r\n\trecursion NUMBER,\r\n\tnegation NUMBER,\r\n\tsearchtree NUMBER,\r\n\texampletype NUMBER,\r\n\tmaximumpoints NUMBER,\r\n\tPRIMARY KEY(id)\r\n\t);\r\n
13170	4	3	select buchngnr, betrag\r\nfrom buchung\r\nwhere betrag >\r\n  (select avg(betrag) * 1.5 from Buchung);
13171	4	3	select buchngnr, betrag\r\nfrom buchung\r\nwhere vonkonto in\r\n  (select kontonr\r\n   from konto\r\n   where ADD_MONTHS(sysdate, -360)<gebdat)\r\nor aufkonto in \r\n  (select kontonr\r\n   from konto\r\n   where ADD_MONTHS(sysdate, -360)<gebdat)\r\norder by betrag desc;\r\n
13172	4	3	select betrag from buchung\r\nwhere vonkonto in\r\n  (select k.kontonr\r\n   from konto k, inhaber i\r\n   where k.inhname=i.name \r\n   and k.gebdat=i.gebdat\r\n   and adresse like '%z');
13173	4	3	select inhname, saldo\r\nfrom konto\r\nwhere saldo-80000 >\r\n  (select max(betrag)\r\n   from buchung\r\n   where datum between '01-OCT-95' and '31-OCT-95')\r\norder by inhname, saldo desc;
13174	4	3	select kontonr, saldo\r\nfrom konto k1\r\nwhere saldo <\r\n  (select sum(betrag)\r\n   from buchung b1\r\n   where k1.kontonr=b1.aufkonto);
13175	4	3	select i.name, i.gebdat, i.adresse, count(*)anzahl\r\nfrom inhaber i, konto k\r\nwhere i.name=k.inhname and i.gebdat=k.gebdat\r\ngroup by i.name, i.gebdat, i.adresse\r\nhaving count(*) >\r\n  (select avg(count(*))\r\n   from konto\r\n   group by inhname, gebdat);
13176	6	5	select count(*) entlehnungen\r\nfrom entlehng e\r\nwhere (e.bis = '01-JAN-2999');
13597	12	11	Select s.name, s.matrnr, s.kennr\r\nfrom student s, zeugnis z, professor p\r\nwhere p.pname = 'Huber'\r\nand s.matrnr = z.matrnr\r\nand z.profnr = p.profnr\r\nand z.note <>5\r\norder by s.name, s.matrnr;
13598	12	11	Select z1.lvanr, s1.matrnr, s1.name, s2.matrnr, s2.name\r\nfrom student s1, student s2, zeugnis z1, zeugnis z2\r\nwhere s1.matrnr = z1.matrnr\r\nand z1.note <> 5\r\nand s2.matrnr = z2.matrnr\r\nand z2.note <> 5\r\nand z1.lvanr = z2.lvanr\r\nand s1.matrnr > s2.matrnr\r\norder by z1.lvanr, s1.name, s2.name;
13599	12	11	select s.name, s.matrnr, s.kennr\r\nfrom student s\r\nwhere not exists\r\n(select * \r\n from \r\n zeugnis z\r\n where z.matrnr = s.matrnr\r\n and z.note < 5)\r\norder by s.kennr, s.name, s.matrnr;
13600	12	11	Select kennr, min(note), avg(note)\r\nfrom student, zeugnis\r\nwhere student.matrnr = zeugnis.matrnr\r\ngroup by kennr\r\nhaving (avg(note) <= (min(note) + 1))\r\norder by kennr;\r\n
13601	12	11	select pname, count(*), avg(note)\r\nfrom professor, zeugnis\r\nwhere professor.profnr = zeugnis.profnr\r\ngroup by pname\r\norder by count(*), pname;
13602	12	11	select p.pname, s.kennr\r\nfrom professor p, student s, zeugnis z\r\nwhere p.profnr = z.profnr\r\nand z.matrnr = s.matrnr\r\ngroup by p.pname, s.kennr \r\norder by p.pname, s.kennr;
13603	12	11	Select s.matrnr, s.name, s.kennr, l.bez, z.note\r\nfrom student s, zeugnis z, diplomprfg d, voraussetzungen v, lva l\r\nwhere s.matrnr = z.matrnr\r\nand z.lvanr = v.lvanr\r\nand v.pnr = d.pnr\r\nand l.lvanr = z.lvanr\r\nand z.note < 5\r\nand d.bezeichnung = 'Angewandte INF'\r\norder by s.matrnr, s.name, s.kennr;
13604	12	11	select s.matrnr, s.name, s.kennr\r\nfrom student s\r\nwhere not exists\r\n(Select * from diplomprfg d, voraussetzungen v\r\n where d.pnr = v.pnr\r\n and d.bezeichnung = 'Angewandte INF'\r\n and not exists (Select * from zeugnis z\r\n                 where z.matrnr = s.matrnr\r\n                 and z.lvanr = v.lvanr\r\n                 and z.note <> 5))\r\norder by s.matrnr, s.name, s.kennr;
13605	12	11	select s.matrnr, s.name, avg(z.note), count(*)\r\nfrom student s, zeugnis z\r\nwhere s.matrnr = z.matrnr\r\ngroup by s.matrnr, s.name\r\nhaving count(*) >= All \r\n(Select count(*) from zeugnis z2\r\n group by z2.matrnr);
13606	10	9	select distinct s.mano, s.name, t.tno\r\nfrom studenten s, reserviert r, terminal t\r\nwhere s.mano = r.mano\r\nand r.tno = t.tno\r\nand not (t.rechner = s.rechner)\r\norder by s.mano;
13607	10	9	select s1.mano, s1.name, r1.tno, r1.tag, r1.stunde, r2.mano as mano2\r\nfrom studenten s1, reserviert r1, reserviert r2\r\nwhere s1.mano = r1.mano \r\nand r1.tno = r2.tno\r\nand ((r1.tag=r2.tag and r1.stunde+1=r2.stunde) \r\n OR (r1.tag+1=r2.tag and r1.stunde=23 and r2.stunde=0))\r\norder by s1.mano, r1.tno, r1.tag, r1.stunde;\r\n
13608	10	9	select s.mano\r\nfrom studenten s\r\nwhere not exists\r\n(select * from reserviert r\r\nwhere s.mano = r.mano)\r\norder by s.mano;
13609	10	9	select t.rechner, r.tag, r.stunde, count(r.tno) terminals\r\nfrom terminal t, reserviert r\r\nwhere t.tno = r.tno\r\ngroup by t.rechner, r.tag, r.stunde\r\norder by r.tag, r.stunde;
13610	10	9	Select distinct s.mano, s.name\r\nfrom studenten s, reserviert r\r\nwhere s.mano = r.mano\r\nand r.stunde <=11\r\norder by s.mano;
13611	10	9	select distinct s.mano, s.name\r\nfrom studenten s, reserviert r\r\nwhere s.mano = r.mano\r\ngroup by s.mano, s.name\r\nhaving count(distinct r.tag) = 1\r\norder by s.mano;
2	2	1	SELECT r1.recordId, r1.artistName, r1.title\r\nFROM   record r1, track t1\r\nWHERE  r1.type='Album' AND r1.recordId=t1.recordId\r\nGROUP BY r1.recordId, r1.artistName, r1.title\r\nHAVING SUM(length) <\r\n  (SELECT AVG(SUM(t2.length))\r\n  FROM record r2, track t2\r\n  WHERE r2.type='Album' AND r2.recordId=t2.recordId\r\n  GROUP BY r2.recordId);
5	2	1	SELECT p.ean, p.bezeichnung, f.filnr, f.inhname, s.bestand\r\nFROM   produkt p, filiale f, sortiment s\r\nWHERE  p.ean=s.ean\r\nAND    f.filnr=s.filnr\r\nAND    s.bestand >= 50\r\nORDER BY p.ean DESC, f.filnr
6	2	1	SELECT DISTINCT b.bestell_nr\r\nFROM   Bestellung b, Bestellposition p\r\nWHERE  b.bestell_nr = p.bestell_nr\r\n       AND b.Datum = TO_DATE('13-10-03','DD-MM-YY')\r\n       AND p.Menge > 100;
7	2	1	SELECT bestell_nr, Datum\r\n    FROM Bestellung\r\n    WHERE bestell_nr NOT IN\r\n        (SELECT bestell_nr \r\n         FROM Lieferposition);
8	2	1	SELECT p.Bezeichnung\r\nFROM   Bestellposition bp, Bauprodukt p, Bestellung b\r\nWHERE  bp.prod_nr = p.prod_nr\r\n       AND bp.bestell_nr = b.bestell_nr\r\n       AND b.Datum = TO_DATE('13-10-03','DD-MM-YY')\r\nGROUP BY p.prod_nr, p.Bezeichnung\r\nHAVING COUNT(DISTINCT bp.bestell_nr) > 5;
9	2	1	SELECT ls.Datum, p.Bezeichnung, SUM(lp.Menge) Liefermenge\r\nFROM   Bauprodukt p, Bestellposition bp, Lieferposition lp, Lieferschein ls\r\nWHERE  p.prod_nr = bp.prod_nr\r\n       AND bp.bestell_nr = lp.bestell_nr\r\n       AND bp.pos_nr = lp.bestellpos_nr\r\n       AND lp.liefer_nr = ls.liefer_nr\r\nGROUP BY ls.Datum, p.prod_nr, p.Bezeichnung;
13	2	1	select sa.art, sa.bezeichnung, count (s.name) as ANZAHL, sum (s.laenge) as LAENGE\r\nfrom strassenart sa, strasse s\r\nwhere sa.art = s.strassenart\r\ngroup by sa.art, sa.bezeichnung\r\nhaving sum (s.laenge) >= ALL\r\n  (select sum (s2.laenge)\r\n   from strasse s2\r\n   group by s2.strassenart)
14	2	1	select *\r\nfrom ort o\r\nwhere o.plz in (\r\n  (select ortVon\r\n   from strasse s, strassenart sa\r\n   where s.strassenart = sa.art AND\r\n         sa.bezeichnung = 'Landesstrasse')\r\n  UNION\r\n  (select ortNach\r\n   from strasse s, strassenart sa\r\n   where s.strassenart = sa.art AND\r\n         sa.bezeichnung = 'Landesstrasse') )
15	2	1	select *\r\nfrom ort o\r\nwhere not exists\r\n  (select *\r\n   from strassenart sa\r\n   where not exists\r\n     (select *\r\n      from liegtAnStrasse las, strasse s\r\n      where las.strassenname = s.name AND\r\n            s.strassenart = sa.art AND\r\n            o.plz = las.ort))
17	2	1	SELECT s1.filnr, s1.ean as EAN_1, p1.bezeichnung as BEZ_1, s2.ean as EAN_2, p2.bezeichnung as BEZ_2,\r\n       (s1.vkpreis-s1.preisred) as preis\r\nFROM   sortiment s1, sortiment s2, produkt p1, produkt p2\r\nWHERE  s1.ean = p1.ean\r\nAND    s2.ean = p2.ean \r\nAND    s1.ean <> s2.ean\r\nAND    s1.filnr = s2.filnr\r\nAND    s1.vkpreis-s1.preisred = s2.vkpreis-s2.preisred
18	2	1	SELECT p.ean, p.bezeichnung\r\nFROM   produkt p\r\nWHERE  p.ean NOT IN (SELECT s.ean\r\n                     FROM   sortiment s, filiale f\r\n                     WHERE  s.filnr = f.filnr\r\n                     AND    f.plz BETWEEN 4010 AND 4049)
19	2	1	SELECT f.filnr, f.inhname\r\nFROM   filiale f\r\nWHERE  NOT EXISTS (SELECT *\r\n                   FROM   produkt p\r\n                   WHERE  p.kategorie NOT IN (SELECT p2.kategorie\r\n                                              FROM   sortiment s, produkt p2\r\n                                              WHERE  s.ean=p2.ean\r\n                                              AND    s.filnr=f.filnr))
20	2	1	SELECT COUNT(*) as AnzahlSortiment, COUNT(DISTINCT s.ean)As AnzahlEAN, SUM(s.bestand*p.ekpreis) as Bestandwert\r\nFROM   sortiment s, produkt p\r\nWHERE  s.ean=p.ean
21	2	1	SELECT f.filnr, f.plz, SUM(p.ekpreis * s.bestand) as Lagerwert\r\nFROM   filiale f, produkt p, sortiment s\r\nWHERE  f.filnr = s.filnr\r\nAND    p.ean = s.ean\r\nAND    p.kategorie = 'Ersatz'\r\nGROUP BY f.filnr, f.plz\r\nHAVING SUM(p.ekpreis * s.bestand) > 300000
22	2	1	SELECT rp.rechnungnr, rp.datum as Datum, p.kategorie, COUNT(*) as Anzahl, \r\n       SUM(rp.einzelpreis*rp.menge) AS rechnungsbetrag\r\nFROM   rechnungpos rp, produkt p\r\nWHERE  rp.ean=p.ean\r\nAND    NOT EXISTS (SELECT *\r\n                   FROM   rechnungpos rp2, produkt p2\r\n                   WHERE  rp2.ean=p2.ean\r\n                   AND    (rp2.rechnungnr=rp.rechnungnr AND rp2.datum=rp.datum)AND p2.kategorie<>p.kategorie)\r\nGROUP BY rp.rechnungnr, rp.datum, p.kategorie\r\n
23	2	1	(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Audio')\r\nINTERSECT\r\n(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Sonstiges')\r\nMINUS\r\n(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Pflege')\r\n
24	2	1	SELECT audio.kundenr, audio.umsatz As Umsatz_Audio, sonst.umsatz as Umsatz_Sonstiges\r\nFROM\r\n(SELECT k.kundenr, k.name, SUM(rp.menge*rp.einzelpreis) umsatz\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Audio'\r\nGROUP BY k.kundenr, k.name) audio,\r\n(SELECT k.kundenr, k.name, SUM(rp.menge*rp.einzelpreis) umsatz\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Sonstiges'\r\nGROUP BY k.kundenr, k.name) sonst\r\nWHERE  audio.kundenr=sonst.kundenr\r\n
25	2	1	SELECT MIN(TO_DATE(rp.datum)) as Tag\r\nFROM   rechnungpos rp\r\nWHERE  (SELECT SUM(rp2.menge*rp2.einzelpreis)\r\n        FROM   rechnungpos rp2\r\n        WHERE  rp2.datum<=rp.datum) > 0.5 *\r\n       (SELECT SUM(rp3.menge*rp3.einzelpreis)\r\n        FROM   rechnungpos rp3)\r\n
26	2	1	SELECT f.filnr, f.plz, SUM(p.ekpreis * s.bestand) as Lagerwert\r\nFROM   filiale f, produkt p, sortiment s\r\nWHERE  f.filnr = s.filnr\r\nAND    p.ean = s.ean\r\nAND    p.kategorie = 'Ersatz'\r\nGROUP BY f.filnr, f.plz\r\nHAVING SUM(p.ekpreis * s.bestand) > \r\n                 (SELECT AVG(SUM(p1.ekpreis * s1.bestand))\r\n                  FROM   produkt p1, sortiment s1\r\n                  WHERE  p1.ean = s1.ean\r\n                  AND    p1.kategorie = 'Ersatz'\r\n                  GROUP BY s1.filnr)
27	2	1	SELECT f.filnr, p.kategorie, SUM(rp.menge*rp.einzelpreis) Umsatz, d.avg_umsatz\r\nFROM   filiale f, produkt p, rechnung r, rechnungpos rp,\r\n       (SELECT p1.kategorie, \r\n               SUM(rp1.menge*rp1.einzelpreis)/COUNT(DISTINCT r1.filnr) \r\n               AS avg_umsatz\r\n        FROM   rechnung r1, rechnungpos rp1, produkt p1\r\n        WHERE  r1.rechnungnr = rp1.rechnungnr AND r1.datum = rp1.datum\r\n        AND    rp1.ean = p1.ean\r\n        GROUP BY p1.kategorie) d\r\nWHERE  f.filnr=r.filnr\r\nAND    rp.ean=p.ean\r\nAND    r.rechnungnr=rp.rechnungnr AND r.datum = rp.datum\r\nAND    p.kategorie=d.kategorie\r\nGROUP BY f.filnr, p.kategorie, d.avg_umsatz\r\n
28	2	1	SELECT (Bestell.BMenge - Liefer.LMenge) As Menge\r\n\tFROM\r\n\t(SELECT SUM(bp.Menge) AS BMenge \r\n\tFROM Bauprodukt p, Bestellposition bp, Bestellung b \tWHERE p.prod_nr = bp.prod_nr\r\n\tAND bp.bestell_nr = b.bestell_nr\r\n\tAND p.Bezeichnung = 'Zementsack'\r\n\tAND b.Anschrift = 'Uniweg 1')  Bestell,\r\n\t(SELECT SUM(lp.Menge) AS LMenge\r\n\tFROM Bauprodukt p, Bestellposition bp, Lieferposition lp, Bestellung b\r\n\tWHERE p.prod_nr = bp.prod_nr\r\n\tAND bp.bestell_nr = lp.bestell_nr\r\n\tAND bp.pos_nr = lp.bestellpos_nr\r\n\tAND bp.bestell_nr = b.bestell_nr\r\n\tAND p.Bezeichnung = 'Zementsack'\r\nAND b.Anschrift = 'Uniweg 1') Liefer;
29	2	1	SELECT AVG(COUNT (*)) AS ANZAHL FROM human p, parent par WHERE ParentName = name AND gender = 'f' GROUP BY name
30	2	1	SELECT name FROM human WHERE (age < 18) AND NOT EXISTS (SELECT * FROM parent WHERE name = ChildName)
31	2	1	SELECT p.ChildName As Name FROM parent p, parent geschwister WHERE p.ParentName = geschwister.ParentName \r\nGROUP BY p.ChildName HAVING COUNT (distinct geschwister.ChildName) >\r\n(SELECT AVG(COUNT(distinct g.ChildName)) FROM parent p2, parent g WHERE p2.ParentName = g.ParentName \r\nGROUP BY p2.ChildName)
32	2	1	SELECT count (*) As Anzahl FROM human vater, human mutter, parent vrel, parent mrel WHERE (vater.age - mutter.age >= 4) AND vater.name = vrel.ParentName AND mutter.name = mrel.ParentName AND vrel.ChildName = mrel.ChildName AND mutter.gender = 'f' AND vater.gender = 'm'
33	2	1	SELECT AVG(MIN(p.age - enkel.age)) AS DURCHSCHNITTSALTER\r\nFROM human p, human enkel, parent ist_kind, parent ist_enkel\r\nWHERE (p.name = ist_kind.ParentName)\r\nAND (ist_kind.ChildName = ist_enkel.ParentName)\r\nAND (ist_enkel.ChildName = enkel.name)\r\nAND (p.gender = 'm') GROUP BY p.name
34	2	1	select distinct p.persnr, p.name, p.beruf\r\nfrom   person p, mietet m, wohnung w\r\nwhere  p.persnr=m.mieternr\r\nand    m.wohnnr=w.wohnnr\r\nand    m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') and    m.preis <= 1100\r\nand    w.bezirk=4\r\norder by p.name
35	2	1	select distinct w.wohnnr, w.gross, p.name\r\nfrom   wohnung w, person p, mietet m, person p1\r\nwhere  w.eigentuemer=p.persnr\r\nand    w.gross > 50\r\nand    m.wohnnr=w.wohnnr \r\nand    p1.persnr=m.mieternr\r\nand    p1.stand='verheiratet'\r\norder by w.wohnnr
36	2	1	select count(*) Anzahl, sum(m.preis) Gesamtsumme, avg(m.preis/w.gross) Durchschnittsmiete\r\nfrom   mietet m, wohnung w\r\nwhere  m.wohnnr=w.wohnnr\r\nand    m.bis=TO_DATE('31-12-2099','DD-MM-YYYY');
37	2	1	select p.persnr, p.name, count(*) AnzahlWohnungen, sum(w.gross) Gesamtwohnflaeche\r\nfrom   person p, wohnung w\r\nwhere  p.persnr=w.eigentuemer\r\ngroup by p.persnr, p.name\r\norder by count(*) desc, p.persnr
38	2	1	select w.wohnnr, w.gross, count(*) AnzahlMietverh, avg(m.preis/w.gross) D_Miete, max(m.preis/w.gross) Hoechstmiete\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\ngroup by w.wohnnr, w.gross\r\nhaving avg(m.preis/w.gross) > 15\r\norder by w.wohnnr
39	2	1	select w.wohnnr, w.gross, count(*) Anzahlmietverh, avg(m.preis/w.gross) D_Miete, max(m.preis/w.gross) Hoechstmiete\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\ngroup by w.wohnnr, w.gross\r\nhaving max(m.preis/w.gross) > \r\n          (select avg(max(m1.preis/w1.gross))\r\n           from   wohnung w1, mietet m1\r\n           where  w1.wohnnr=m1.wohnnr\r\n           group by w1.wohnnr)\r\norder by w.wohnnr
40	2	1	select distinct w.bezirk\r\nfrom   wohnung w\r\nwhere  not exists\r\n       (select * \r\n        from   wohnung w1\r\n        where  w1.bezirk=w.bezirk\r\n        and    not exists\r\n               (select *\r\n                from   mietet m\r\n                where  m.bis=TO_DATE('31-12-2099','DD-MM-YYYY')                and    m.wohnnr=w1.wohnnr))\r\norder by w.bezirk
41	2	1	select w.bezirk, count(*) Anzahl_Wohnungen\r\nfrom   wohnung w\r\nwhere  not exists\r\n       (select *\r\n        from   mietet m, wohnung w1\r\n        where  m.wohnnr=w1.wohnnr\r\n        and    m.bis=TO_DATE('31-12-2099','DD-MM-YYYY')\r\n        and    w1.bezirk=w.bezirk)\r\ngroup by w.bezirk\r\norder by w.bezirk
42	2	1	select w.eigentuemer, COUNT(*) Anzahl_Mietverh, COUNT(DISTINCT w.wohnnr) Anzahl_Wohnungen, MAX(m.preis/w.gross) Hoechstmiete\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\ngroup by w.eigentuemer\r\norder by w.eigentuemer
43	2	1	select w.eigentuemer, m.preis\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\nand    not exists \r\n          (select *\r\n           from   mietet m1, wohnung w1\r\n           where  m1.wohnnr=w1.wohnnr\r\n           and    w1.eigentuemer=w.eigentuemer\r\n           and    m1.preis > m.preis)\r\nand    not exists\r\n          (select *\r\n           from   mietet m1, wohnung w1\r\n           where  m1.wohnnr=w1.wohnnr\r\n           and    w1.eigentuemer=w.eigentuemer\r\n           and    m1.von > m.von)   
44	2	1	select o1.plz as plz1, o1.name ort1, o2.plz plz2, o2.name ort2,\r\n       l2.beiKM - l1.beiKM as entfernung,\r\n       s.name, s.bezeichnung\r\nfrom ort o1, ort o2, liegtAnStrasse l1, liegtAnStrasse l2, strasse s\r\nwhere o1.plz = l1.ort AND\r\n      o2.plz = l2.ort AND\r\n      s.name = l1.strassenname AND\r\n      l1.strassenname = l2.strassenname AND\r\n      l2.beiKM > l1.beiKM AND\r\n      NOT exists (\r\n        select *\r\n        from liegtAnStrasse ldazw\r\n        where ldazw.strassenname = l1.strassenname AND\r\n              ldazw.beiKM > l1.beiKM AND\r\n              ldazw.beiKM < l2.beiKM)
51	2	1	SELECT DISTINCT p.persnr, p.name\r\nFROM   person p, wohnung w, mietet m\r\nWHERE  p.persnr=w.eigentuemer AND w.wohnnr=m.wohnnr\r\nAND    m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') AND p.stand='ledig'\r\nAND    m.preis/w.gross > 7\r\nORDER BY p.persnr\r\n
52	2	1	SELECT w.wohnnr, w.eigentuemer, p1.name as VName, m.mieternr, p2.name as MName\r\nFROM   wohnung w, person p1, person p2, mietet m\r\nWHERE  w.wohnnr=m.wohnnr AND w.eigentuemer=p1.persnr AND m.mieternr=p2.persnr\r\nAND    w.bezirk=1 AND m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') ORDER BY w.wohnnr\r\n
53	2	1	SELECT DISTINCT w.wohnnr, w.bezirk FROM   wohnung w, mietet m WHERE  w.wohnnr=m.wohnnr AND    w.wohnnr NOT IN (SELECT m2.wohnnr FROM   mietet m2 WHERE  m2.bis=TO_DATE('31-12-2099','DD-MM-YYYY'))ORDER BY w.bezirk, w.wohnnr
54	2	1	SELECT DISTINCT p.persnr, p.name\r\nFROM   person p, wohnung w\r\nWHERE  p.persnr=w.eigentuemer\r\nAND    NOT EXISTS (SELECT * \r\n                   FROM   wohnung w2\r\n                   WHERE  w2.eigentuemer=p.persnr\r\n                   AND NOT EXISTS (SELECT * \r\n                                   FROM   mietet m\r\n                                   WHERE  m.wohnnr=w2.wohnnr))\r\n
55	2	1	SELECT w.bezirk, SUM(w.gross) FLAECHE\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr AND m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') GROUP BY w.bezirk\r\nHAVING SUM(w.gross)> (SELECT AVG(SUM(w1.gross))\r\n                      FROM   wohnung w1, mietet m1\r\n                      WHERE  w1.wohnnr=m1.wohnnr AND m1.bis=TO_DATE('31-12-2099','DD-MM-YYYY')                      GROUP BY w1.bezirk)\r\n
56	2	1	SELECT w.wohnnr, w.bezirk, w.gross, (m.preis/w.gross) qm_miete, d.d_miete \r\nFROM   wohnung w, mietet m,\r\n       (SELECT w1.bezirk, AVG(m1.preis/w1.gross) d_miete\r\n        FROM   wohnung w1, mietet m1\r\n        WHERE  w1.wohnnr=m1.wohnnr AND m1.bis=TO_DATE('31-12-2099','DD-MM-YYYY')\r\n        GROUP BY w1.bezirk) d\r\nWHERE  w.wohnnr=m.wohnnr AND m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') AND    w.bezirk=d.bezirk\r\n
57	2	1	SELECT p.persnr, p.name, COUNT(*) as MIETVERHAELTNISSE\r\nFROM   person p, wohnung w, mietet m\r\nWHERE  p.persnr=w.eigentuemer AND w.wohnnr=m.wohnnr\r\nGROUP BY p.persnr, p.name\r\nHAVING COUNT(*)=COUNT(DISTINCT m.mieternr)\r\n
58	2	1	SELECT m1.mieternr, p.name, m1.wohnnr Wohnung1nr, m1.von Wohnung1von, m1.bis Wohnung1bis, m2.wohnnr Wohnung2nr, m2.von Wohnung2von, m2.bis Wohnung2bis\r\nFROM   mietet m1, person p, mietet m2, wohnung w1, wohnung w2\r\nWHERE  m1.mieternr=p.persnr AND m1.mieternr=m2.mieternr \r\nAND    m1.wohnnr=w1.wohnnr AND m2.wohnnr=w2.wohnnr\r\nAND    w1.bezirk=w2.bezirk\r\nAND    m1.wohnnr < m2.wohnnr\r\nAND    ((m1.von BETWEEN m2.von AND m2.bis) OR (m2.von BETWEEN m1.von AND m1.bis))\r\n
59	2	1	SELECT bezirk, SUM(erloes) as MIETEINNAHMEN FROM \r\n((SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(m.bis, m.von))*m.preis) erloes\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr\r\nAND    m.bis <> TO_DATE('31-12-2099','DD-MM-YYYY')\r\nGROUP BY w.bezirk)\r\nUNION\r\n(SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(SYSDATE, m.von))*m.preis) erloes\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr\r\nAND    m.bis = TO_DATE('31-12-2099','DD-MM-YYYY')\r\nGROUP BY w.bezirk))\r\nGROUP BY bezirk\r\nHAVING SUM(erloes)=(SELECT MAX(SUM(erloes)) \r\n                    FROM ((SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(m.bis, m.von))*m.preis) erloes\r\n                           FROM   wohnung w, mietet m\r\n                           WHERE  w.wohnnr=m.wohnnr\r\n                           AND    m.bis <> TO_DATE('31-12-2099','DD-MM-YYYY')\r\n                           GROUP BY w.bezirk)\r\n                          UNION\r\n                          (SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(SYSDATE, m.von))*m.preis) erloes\r\n                           FROM   wohnung w, mietet m\r\n                           WHERE  w.wohnnr=m.wohnnr\r\n                           AND    m.bis = TO_DATE('31-12-2099','DD-MM-YYYY')\r\n                           GROUP BY w.bezirk))\r\n                          GROUP BY bezirk)\r\n
60	2	1	SELECT a1.name, a1.nationality, r1.title, r1.releaseDate FROM artist a1, record r1, distribute d1 WHERE a1.name=r1.artistName AND r1.recordId=d1.recordId AND d1.media='MD' AND NOT EXISTS (SELECT * FROM record r2, distribute d2 WHERE r2.recordId=d2.recordId AND d2.media='MD' AND r2.releaseDate<r1.releaseDate)
61	2	1	SELECT r.recordid, r.artistName, r.title, r.releaseDate, r.type FROM record r, distribute d WHERE d.recordId=r.recordId GROUP BY r.recordId, r.artistName, r.title, r.releaseDate, r.type HAVING COUNT(*) = (SELECT COUNT(DISTINCT media) FROM distribute)
62	2	1	SELECT g.genreId, g.name\r\nFROM genre g\r\nWHERE NOT EXISTS (\r\nSELECT *\r\nFROM record r, distribute d\r\nWHERE r.genreId=g.genreId AND r.recordId=d.recordId AND d.media='DVD'\r\n);
63	2	1	SELECT r1.recordId, r1.artistName, r1.title FROM record r1 WHERE \r\nr1.genreId IN ( \r\n   SELECT r2.genreId \r\n   FROM record r2 \r\n   WHERE r2.type='Single' \r\n   GROUP BY r2.genreId \r\n   HAVING COUNT(*) >= ALL \r\n     (SELECT COUNT(*) FROM record r3 WHERE r3.type='Single' GROUP BY \r\n     r3.genreId)) \r\nORDER BY r1.artistName, r1.title;
64	2	1	SELECT r1.artistname, AVG(v.vkCD)*100/SUM(d1.price) AS CDAnteil \r\nFROM record r1, distribute d1, \r\n     (SELECT r2.artistname, SUM(d2.price) AS vkCD \r\n     FROM record r2, distribute d2 \r\n     WHERE r2.recordId=d2.recordId AND d2.media='CD' \r\n     GROUP BY r2.artistname \r\n     ) v\r\nWHERE r1.recordId=d1.recordId AND r1.artistname=v.artistname \r\nGROUP BY r1.artistname;
65	2	1	select a.name, a.nationality, r.title \r\nfrom artist a, record r, distribute d \r\nwhere a.name=r.artistname and r.recordid=d.recordid and d.media='MD' \r\norder by a.name, r.title;
66	2	1	select a.name, a.nationality, min(r.releasedate) as Datum \r\nfrom artist a, record r, distribute d \r\nwhere a.name=r.artistname and r.recordid=d.recordid and d.media='CD' \r\ngroup by a.name, a.nationality \r\norder by a.name, a.nationality;
67	2	1	select r.title, r.artistname, d.price \r\nfrom  record r, distribute d \r\nwhere r.recordid=d.recordid and d.media='CD' \r\nand not exists \r\n      (select * \r\n       from   record r1, distribute d1 \r\n       where  r1.recordid=d1.recordid \r\n       and    d1.media='CD' \r\n       and    d1.price>d.price) \r\norder by r.title; 
68	2	1	select r.recordid, r.title, count(*) Tracks \r\nfrom   record r, track t \r\nwhere  r.recordid=t.recordid \r\ngroup by r.recordid, r.title \r\nhaving count(*) = (select max(count(*)) \r\n                    from   record r1, track t1 \r\n                    where  r1.recordid=t1.recordid \r\n                    group by r1.recordid) order by r.title;
69	2	1	select s.kennzahl as STR_KZ, s.bezeichnung as STR_BEZ, f.kurzbez as FK_KBEZ, f.bezeichnung as FK_BEZ\r\nfrom studienrichtung s, fakultaet f\r\nwhere s.gehoert_zu = f.kurzbez AND\r\n  not exists(\r\n     select *\r\n     from koje k\r\n     where k.gemietet_von = s.kennzahl);
70	2	1	select s.kennzahl, s.bezeichnung, sum(flaeche) as Flaeche\r\nfrom studienrichtung s, koje k\r\nwhere k.gemietet_von = s.kennzahl\r\ngroup by s.kennzahl, s.bezeichnung;
71	2	1	select f.kurzbez, f.bezeichnung, count(*) Anzahl\r\nfrom fakultaet f, studienrichtung s, koje k\r\nwhere s.gehoert_zu = f.kurzbez AND\r\n      k.gemietet_von = s.kennzahl\r\ngroup by f.kurzbez, f.bezeichnung\r\nhaving count (standnr) = (\r\n select MAX (count(*))\r\n from studienrichtung s2, koje k2\r\n where k2.gemietet_von = s2.kennzahl\r\n group by s2.gehoert_zu);
72	2	1	select s1.kennzahl SKZ1, s1.bezeichnung BEZ1, k1.standnr SN1, k2.standnr SN2, s2.kennzahl SKZ2, s2.bezeichnung BEZ2\r\nfrom studienrichtung s1, koje k1, studienrichtung s2, koje k2\r\nwhere k1.gemietet_von = s1.kennzahl AND\r\n      k2.gemietet_von = s2.kennzahl AND\r\n      k1.linkernachbar = k2.standnr AND\r\n      k1.standnr > k2.standnr AND\r\n      k1.gemietet_von <> k2.gemietet_von;
73	2	1	select g.genreId, g.name, count(*) anzahl\r\nfrom genre g, record r, distribute d\r\nwhere g.genreid=r.genreid and r.recordid=d.recordid and d.media='CD' \r\ngroup by g.genreid, g.name;
74	1	1	SELECT name FROM  human WHERE  gender='m' AND    age > 30
115	2	1	select * from students where country='Thailand' or country='Japan' order by country, name;
116	2	1	select distinct (s.country) from students s, course c, enrollment e where e.studentId=s.studentId and e.courseCode=c.courseCode and c.name='Database';
117	2	1	select country, count(*) as numberOfStudents from students group by country;
118	2	1	select country, count(*) as numberOfStudents from students where country <> 'Australia' group by country;
119	2	1	select country, count(*) as numberOfStudents from students group by country having count(*)>=10;
120	2	1	select * from students where studentId not in(select studentId from enrollment);
121	2	1	SELECT * FROM hotel;
122	2	1	SELECT * FROM hotel WHERE address LIKE '%London%';
123	2	1	SELECT name, address FROM guest WHERE UPPER(address) LIKE '%LONDON%';
124	2	1	SELECT * FROM room WHERE type IN ('double','family') AND price < 40;
125	2	1	SELECT * FROM booking WHERE dateto IS NULL;
126	2	1	SELECT name, address FROM guest WHERE UPPER(address) LIKE '%LONDON%' ORDER BY name;
127	2	1	SELECT * FROM booking WHERE dateto IS NULL ORDER BY hno, datefrom;
128	2	1	SELECT rno, hno, price FROM room WHERE type = 'family' ORDER BY hno, price DESC;
129	2	1	SELECT r.price, r.type FROM room r, hotel h WHERE r.hno = h.hno AND h.name = 'Grosvenor Hotel';
130	2	1	SELECT b.datefrom, b.dateto FROM booking b, room r WHERE b.hno = r.hno AND b.rno = r.rno AND r.type = 'family';
131	2	1	SELECT DISTINCT g.name, g.address FROM guest g, booking b, hotel h WHERE b.gno = g.gno AND b.hno = h.hno AND h.name = 'Grosvenor Hotel';
132	2	1	SELECT g.name, g.address FROM guest g, booking b, room r WHERE g.gno = b.gno AND b.hno = r.hno AND b.rno = r.rno AND r.type = 'family';
133	2	1	SELECT COUNT(*) AS noOfHotels FROM hotel;
134	2	1	SELECT AVG(price) AS AVGFamily FROM room WHERE type = 'family';
135	2	1	SELECT SUM(price) AS revenue FROM room WHERE type = 'double' AND hno = 'H001';
136	2	1	SELECT COUNT(DISTINCT gno) AS guests FROM booking WHERE datefrom < TO_DATE('01-05-1997','dd-mm-yyyy') AND dateto > TO_DATE('31-03-1997','dd-mm-yyyy');
137	2	1	SELECT SUM(price) AS income FROM room r, booking b, hotel h WHERE r.hno = h.hno AND r.rno = b.rno AND r.hno = b.hno AND h.name = 'Grosvenor Hotel' AND b.datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND b.dateto > TO_DATE('26-03-1997','dd-mm-yyyy');
138	2	1	SELECT r.rno FROM room r, hotel h WHERE r.hno = h.hno AND h.name = 'Grosvenor Hotel' AND r.rno NOT IN (SELECT b.rno FROM booking b WHERE b.rno = r.rno AND b.hno = r.hno AND datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy'));
139	2	1	SELECT SUM(r.price) AS lost FROM room r, hotel h WHERE r.hno = h.hno AND h.name = 'Grosvenor Hotel' AND r.rno NOT IN (SELECT b.rno FROM booking b WHERE b.rno = r.rno AND b.hno = r.hno AND datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND dateto > TO_DATE('26-03-1997','dd-mm-yyyy'));
140	2	1	SELECT r.hno, r.rno, r.type, r.price, g.name\r\nFROM   hotel h JOIN room r ON h.hno = r.hno \r\n               LEFT OUTER JOIN (SELECT * \r\n                                FROM   booking b \r\n                                WHERE  b.datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND \r\n                                       b.dateto > TO_DATE('26-03-1997','dd-mm-yyyy')) b ON (r.rno = b.rno AND r.hno = b.hno)\r\n               LEFT OUTER JOIN guest g ON g.gno = b.gno\r\nWHERE  h.name='Grosvenor Hotel';
141	2	1	SELECT hno, COUNT(*) AS noOfRooms FROM room GROUP BY hno;
142	2	1	SELECT hno, COUNT(*) AS noOfRooms FROM room WHERE hno IN (SELECT hno FROM hotel WHERE UPPER(address) LIKE '%LONDON%') GROUP BY hno;
143	2	1	SELECT h.hno, h.name, COUNT(*) AS occRooms FROM hotel h, booking b WHERE h.hno = b.hno AND b.datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND b.dateto > TO_DATE('26-03-1997','dd-mm-yyyy') GROUP BY h.hno, h.name;
144	2	1	SELECT r1.type, COUNT (*) AS noBooked FROM room r1, booking b1 WHERE r1.hno = b1.hno AND r1.rno = b1.rno GROUP BY r1.type HAVING COUNT(*) >= ALL (SELECT COUNT(*) FROM room r2, booking b2 WHERE r2.hno = b2.hno AND r2.rno = b2.rno GROUP BY r2.type);
145	2	1	SELECT h.name, SUM(r.price) AS lost FROM room r, hotel h WHERE r.hno = h.hno AND (r.rno, r.hno) NOT IN (SELECT rno, hno FROM booking WHERE datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND dateto > TO_DATE('26-03-1997','dd-mm-yyyy')) GROUP BY h.name;
146	2	1	SELECT Name, Address, City FROM Staff WHERE Dnum = 'D001';
147	2	1	SELECT s.Name, d.dname FROM Staff s, Dept d WHERE s.Snum = d.Manager;
148	2	1	SELECT e.Name staff_member, s.Name supervisor FROM Staff s, Staff e WHERE e.Supervisor = s.Snum AND e.City <> s.City;
149	2	1	SELECT Name, Salary FROM Staff WHERE Salary > 65000 AND Snum NOT IN (SELECT DISTINCT Supervisor FROM Staff WHERE Supervisor IS NOT NULL);
150	2	1	SELECT p.Pnum, p.Pname, p.Pcity, p.Dnum FROM Project p WHERE p.Pcity NOT IN(SELECT dl.Dcity FROM DeptLocation dl WHERE dl.Dnum = p.Dnum);
151	2	1	SELECT p.Pname FROM Project p, WorksOn w WHERE p.Pnum = w.Pnum GROUP BY p.Pname HAVING COUNT(*) > 2;
152	2	1	SELECT s1.snum, s1.name, s1.dnum, count(*) AS nsupervised FROM staff s1, staff s2 WHERE s1.snum=s2.supervisor GROUP BY s1.snum, s1.name, s1.dnum;
153	2	1	SELECT   AVG(count(*)) AS avgsupervised\r\nFROM     staff s1, staff s2\r\nWHERE    s1.snum = s2.supervisor \r\nGROUP BY s1.snum;
10011	1	1	select pu.prodno, t.y year, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by rollup (pu.prodno, t.y, t.m)\r\norder by pu.prodno, t.y, t.m
10009	1	1	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by pu.prodno, t.m
10010	1	1	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by grouping sets ((pu.prodno, t.m), (pu.prodno))
10012	1	1	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value,\r\nSUM(SUM(pu.qty*pu.price)) OVER \r\n(PARTITION BY pu.prodno ORDER BY t.m ROWS UNBOUNDED PRECEDING) cumvalue\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by pu.prodno, t.m;\r\n
10013	1	1	select l.state, pr.type, sum(s.price*s.qty) turnover,\r\nrank() over (partition by l.state order by sum(s.price*s.qty) desc) R\r\nfrom   location l, product pr, sales s\r\nwhere  l.locno=s.locno and pr.prodno=s.prodno\r\ngroup by l.state, pr.type
10014	1	1	select s.prodno, l.state, SUM(s.qty*s.price) TURNOVER, \r\n(SUM(s.qty*s.price)*100/SUM(SUM(s.qty*s.price)) OVER \r\n(PARTITION BY s.prodno ORDER BY l.state ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING)) TShARE\r\nfrom   sales s, location l\r\nwhere  s.locno=l.locno\r\ngroup by s.prodno, l.state\r\norder by s.prodno, l.state
10049	1	1	SELECT s.code, s.segId, (s.toKM - s.fromKM) AS "Length" \r\nFROM   segment s WHERE  (s.toKM - s.fromKM) > \r\n       (SELECT (s1.toKM - s1.fromKM)\r\n        FROM   segment s1 \r\n        WHERE  s1.segId = 'S01' AND \r\n               s1.code = 'H10');
1	2	1	SELECT a.name, a.nationality\r\nFROM   artist a \r\nWHERE  NOT EXISTS\r\n        (SELECT r.recordId \r\n         FROM record r, distribute d\r\n         WHERE r.artistName = a.name AND \r\n               r.recordId = d.recordId AND \r\n               d.media = 'CD' AND \r\n               d.price < 20);
13612	10	9	select s.mano, s.name, count(*) stunden\r\nfrom studenten s, reserviert r\r\nwhere s.mano = r.mano\r\ngroup by s.mano, s.name\r\nhaving count(*) >= ALL\r\n (Select count(*) from reserviert r1\r\n  group by r1.mano);\r\n
13613	10	9	select s.mano, s.name, r.tag, r.stunde, r.tno\r\nfrom studenten s, reserviert r\r\nwhere s.mano = r.mano \r\nand NOT Exists\r\n (Select * from terminal t, wartung w\r\n  where r.tno = t.tno\r\n  and t.rechner = w.rechner\r\n  and r.tag = w.tag\r\n  and r.stunde between w.vonstunde and w.bisstunde)\r\norder by s.mano, r.tag, r.stunde;
13614	10	9	Select s.mano, s.name\r\nfrom studenten s\r\nwhere exists\r\n (select * from reserviert r\r\n  where s.mano = r.mano)\r\nAND NOT EXISTS\r\n (select * from reserviert r\r\n  Where s.mano = r.mano \r\n  AND NOT EXISTS\r\n   (Select * from Terminal t, wartung w\r\n    where r.tno = t.tno \r\n    and t.rechner = w.rechner\r\n    and r.tag = w.tag\r\n    and r.stunde between w.vonstunde and w.bisstunde));\r\n
13615	8	7	select p.name, p.beruf, w.gross, p.stand\r\nfrom person p, vermietet v, wohnung w\r\nwhere p.persnr = v.mieternr\r\nand v.wohnnr = w.wohnnr\r\nand w.gross > 80\r\nand p.stand = 'ledig'\r\norder by p.name, w.gross;\r\n
13616	8	7	select p1.name, p1.stand, v1.preis, v2.preis\r\nfrom person p1, vermietet v1, person p2, vermietet v2\r\nwhere p1.persnr = v1.mieternr\r\nand p2.persnr = v2.mieternr\r\nand v1.vermieternr = v2.mieternr\r\nand v1.preis < v2.preis\r\nand p1.stand = p2.stand\r\norder by p1.name, p1.stand;
13617	8	7	select p.name, p.stand, p.beruf\r\nfrom person p\r\nwhere p.persnr not in\r\n (Select v1.vermieternr from vermietet v1\r\n  union\r\n  select v2.mieternr from vermietet v2)\r\norder by p.name, p.stand, p.beruf;
13618	8	7	select w1.bezirk, min (v1.preis/w1.gross), max (v2.preis/w2.gross)\r\nfrom wohnung w1, wohnung w2, vermietet v1, vermietet v2\r\nwhere w1.wohnnr = v1.wohnnr \r\nand w2.wohnnr = v2.wohnnr\r\nand w1.bezirk = w2.bezirk\r\nand v1.preis/w1.gross * 1.5 < v2.preis/w2.gross\r\ngroup by w1.bezirk\r\norder by w1.bezirk;
13619	8	7	select w.bezirk, count (*)\r\nfrom wohnung w, vermietet v\r\nwhere w.wohnnr = v.wohnnr\r\ngroup by w.bezirk\r\norder by count (*);
13620	8	7	select p.name, avg(v.preis) Dpreis, avg(w.gross) DGroesse\r\nfrom person p, vermietet v, wohnung w\r\nwhere p.persnr = v.vermieternr\r\nand v.wohnnr = w.wohnnr\r\ngroup by p.name \r\norder by p.name;
13621	8	7	select w.bezirk, avg (v.preis/w.gross)\r\nfrom Wohnung w, vermietet v\r\nwhere w.wohnnr = v.wohnnr\r\ngroup by w.bezirk\r\nhaving avg(v.preis/w.gross) >= ALL\r\n  (select avg(v1.preis/w1.gross)\r\n   from wohnung w1, vermietet v1\r\n   where w1.wohnnr = v1.wohnnr\r\n   group by w1.bezirk);\r\n
13622	8	7	select p.name, w.bezirk, sum (v.preis)\r\nfrom person p, wohnung w, vermietet v\r\nwhere p.persnr = v.vermieternr\r\nand w.wohnnr = v.wohnnr\r\ngroup by p.name, w.bezirk\r\nhaving sum (v.preis) > ANY\r\n (Select sum (v1.preis) from vermietet v1, wohnung w1\r\n  where v1.wohnnr = w1.wohnnr\r\n  group by v1.vermieternr);\r\n
13623	8	7	select p1.persnr, p1.name, sum (v1.preis)\r\nfrom person p1, vermietet v1\r\nwhere p1.stand = 'verheiratet'\r\nand p1.persnr = v1.vermieternr\r\nand p1.beruf IN\r\n (select p2.beruf\r\n  from person p2\r\n  where p2.persnr <> p1.persnr)\r\ngroup by p1.persnr, p1.name;\r\n\r\n
13624	8	7	select w2.bezirk, count(*)\r\nfrom wohnung w2, vermietet v2\r\nwhere w2.wohnnr = v2.wohnnr\r\ngroup by w2.bezirk\r\nhaving count (*) > \r\n (select count(*) from person p, vermietet v, wohnung w\r\n  where p.persnr = v.mieternr\r\n  and v.wohnnr = w.wohnnr\r\n  and p.stand = 'ledig'\r\n  and w.bezirk = \r\n   (Select w1.bezirk\r\n    from wohnung w1\r\n    group by w1.bezirk\r\n    having count(*) <= ALL\r\n     (Select count(*) from wohnung w3\r\n      group by w3.bezirk)));\r\n
13645	2	1	select avg(tokm - fromkm) from segment
13646	2	1	select k.kontoNr, sum(b.betrag) as Gesamtsumme from konto k , buchung b where k.kontoNr=b.aufKonto group by kontoNr
13647	2	1	select i.name, i.gebdat, i.adresse, count(*) as Anzahl, sum(b.betrag) as Betragssumme from buchung b, konto k, inhaber i where i.name=k.inhname and i.gebdat=k.gebdat and k.kontoNr=b.aufKonto group by i.name, i.gebdat, i.adresse
100	2	1	SELECT k.kontonr, i.name, i.gebdat, i.adresse FROM   konto k, inhaber i WHERE  k.inhname=i.name AND k.gebdat=i.gebdat AND    i.adresse LIKE '%Linz%'
101	2	1	select k1.inhname, k1.gebdat, b.buchngnr from   konto k1, konto k2, buchung b where  k1.inhname = k2.inhname and    k1.gebdat = k2.gebdat and    k1.kontonr = b.vonkonto and    k2.kontonr = b.aufkonto order by k1.inhname, k1.gebdat, b.buchngnr desc
102	2	1	SELECT COUNT(*) AS Anzahl FROM konto
103	2	1	SELECT k.inhname, k.gebdat, COUNT(*) AS Anzahl FROM   konto k GROUP BY k.inhname, k.gebdat
104	2	1	SELECT AVG(COUNT(*)) AS AVG_Anzahl FROM   konto k GROUP BY k.inhname, k.gebdat
105	2	1	select count(distinct aufkonto) as Anzahl\r\nfrom buchung
106	2	1	select i1.name, i1.gebdat, i1.adresse, count(*) as Anzahl, sum(betrag) SUM_Betrag from   inhaber i1, konto k1, buchung b1\r\nwhere  i1.name = k1.inhname and i1.gebdat = k1.gebdat\r\nand    k1.kontonr = b1.aufkonto\r\ngroup by i1.name, i1.gebdat, i1.adresse\r\nhaving count(*)>4
107	2	1	select distinct inhname, gebdat from konto where saldo> 10000
108	2	1	select i1.name, i1.gebdat, i1.adresse, count(*) anzahl, sum(betrag) sum_betrag\r\nfrom   inhaber i1, konto k1, buchung b1\r\nwhere  i1.name = k1.inhname and i1.gebdat = k1.gebdat\r\nand    k1.kontonr = b1.aufkonto\r\ngroup by i1.name, i1.gebdat, i1.adresse\r\nhaving count(*) >\r\n       (select avg(count(*))\r\n        from   konto k2, buchung b2\r\n        where  k2.kontonr = b2.aufkonto\r\n        group by k2.inhname, k2.gebdat)
109	2	1	select i.name, i.gebdat\r\nfrom inhaber i\r\nwhere not exists (select *\r\n                  from   konto k\r\n                  where  k.inhname = i.name\r\n                  and    k.gebdat = i.gebdat\r\n                  and    not exists (select *\r\n                                     from   buchung b\r\n                                     where  k.kontonr = b.aufkonto))\r\norder by i.name, i.gebdat
110	2	1	select k.kontonr, k.filiale, sum(b.betrag) ksum, f.filsum\r\nfrom   konto k, buchung b,\r\n       (select k1.filiale,\r\n               sum(b1.betrag) filsum\r\n        from   konto k1, buchung b1\r\n        where  k1.kontonr = b1.aufkonto\r\n        or     k1.kontonr = b1.vonkonto\r\n        group by k1.filiale) f\r\n where  (k.kontonr=b.vonkonto\r\n or      k.kontonr=b.aufkonto)\r\n and    k.filiale = f.filiale\r\n group by k.kontonr, k.filiale, f.filsum
112	2	1	select k.inhname, k.gebdat, b.buchngnr, b.aufkonto\r\nfrom   konto k, buchung b\r\nwhere  k.kontonr = b.aufkonto\r\nand    not exists\r\n         (select *\r\n          from   konto k2, buchung b2\r\n          where k2.kontonr=b2.aufkonto\r\n          and   k2.inhname=k.inhname\r\n          and   k2.gebdat=k.gebdat\r\n          and   b2.buchngnr < b.buchngnr)
111	2	1	select k.kontonr, k.filiale\r\nfrom   konto k\r\nwhere  k.kontonr not in\r\n         (select b.vonkonto\r\n          from   buchung b, konto k2\r\n          where  b.aufkonto=k2.kontonr\r\n          and    k2.inhname='Hofreiter Martin'\r\n          and    k2.gebdat='12-FEB-80')
13648	2	1	select k.kontoNr from konto k\r\nwhere k.kontoNr not in (select distinct b.aufKonto from buchung b)
13649	2	1	SELECT segid, (toKM - fromKM) AS Length FROM segment WHERE   (toKM - fromKM) > (SELECT avg(toKM - fromKM) FROM segment)
13650	2	1	SELECT code, AVG(toKM - fromKM) AS AVGSegmentLength FROM segment GROUP BY code
13651	2	1	SELECT s1.code, s1.segID, s2.code, s2.segID FROM segment s1, segment s2 WHERE (s1.segID <> s2.segID OR s1.code <> s2.code) AND (s1.toKM - s1.fromKM) = (s2.toKM - s2.fromKM)
13652	2	1	SELECT segID, s1.toKM - s1.fromKM AS SegmentLength, s2.HighwayLength FROM segment s1, ( SELECT code, SUM(toKM-fromKM) AS HighwayLength FROM segment GROUP BY code) s2\r\n\tWHERE   s2.code = s1.code;
13653	2	1	SELECT DISTINCT longitude, latitude FROM node ORDER BY  longitude ASC, latitude DESC;\r\n
13654	2	1	(SELECT\tn1.nodeID, n1.longitude, n1.latitude FROM node n1, exit e, highwayExit he WHERE\the.code = 'H10' AND he.nodeID = e.nodeID AND e.nodeID = n1.nodeID) UNION (SELECT n2.nodeID, n2.longitude, n2.latitude FROM node n2, intersection i, HighwayIntersection hi WHERE hi.code = 'H10' AND hi.nodeID = i.nodeID AND i.nodeID = n2.nodeID)
13777	6	5	select count(*) ENTLEHNUNGEN from entlehng e where (e.bis = '01-01-2999');
13778	6	5	select B.NAME, B.Adresse, COUNT(*) ANZAHL\r\nfrom BENUTZER B, ENTLEHNG E\r\nwhere (B.BENNR = E.BENUTZER)\r\ngroup by B.NAME, B.ADRESSE\r\norder by 3 desc;
13779	6	5	select b.bennr, b.name, b.gebdat, b.adresse, count(*) ANZAHL\r\nfrom benutzer b, entlehng e\r\nwhere (b.bennr = e.benutzer) and (e.bis = '01-01-2999')\r\ngroup by b.bennr, b.name, b.gebdat, b.adresse\r\nhaving count(*) > 2\r\norder by 2;
13783	6	5	select e.entlngnr, e.von, be.name, bu.titel\r\nfrom entlehng e, benutzer be, buch bu\r\nwhere (e.buch = bu.buchnr) and (e.benutzer = be.bennr) and\r\n      (e.bis = '01-01-2999') and ((e.von + 100) < SYSDATE)\r\norder by be.name;
13788	6	5	select b.titel, count(*) ANZAHL,\r\n  ROUND(AVG(e.bis - e.von)) DURCHSCHNITT\r\nfrom buch b, entlehng e\r\nwhere (b.buchnr = e.buch) and (e.bis <> '01-01-2999')\r\ngroup by b.buchnr, b.titel\r\norder by 2 desc;
13795	6	5	select b.buchnr, b.titel\r\nfrom buch b\r\nwhere b.buchnr not in\r\n  (select distinct e.buch\r\n   from entlehng e\r\n   where e.von <= '31-12-1994' and e.bis >= '01-01-1994');
13805	2	1	select f.filNr, f.inhName, p.ean, p.bezeichnung, (p.ekPreis * s.bestand) "Wert"\r\nfrom filiale f, produkt p, sortiment s\r\nwhere p.ean = s.ean and s.filNr = f.filNr and p.listPreis = s.vkPreis;
13811	2	1	select distinct k.kundeNr, k.name, k.bonStufe\r\nfrom kunde k, rechnung r\r\nwhere k.kundeNr = r.kundeNr\r\nand k.kundeNr NOT IN\r\n(select r1.kundeNr from rechnung r1 where bezahlt = 'N');
13815	2	1	select p.ean, p.bezeichnung, p.ekPreis\r\nfrom produkt p\r\nwhere p.ekPreis > \r\n(select avg(p1.ekPreis)\r\n from produkt p1\r\n where p1.kategorie = p.kategorie);
13823	2	1	SELECT p.ean, p.bezeichnung, COUNT(f.filNr)\r\nFROM produkt p\r\nJOIN sortiment s ON p.ean = s.ean\r\nJOIN filiale f ON s.filNr = f.filNr\r\nWHERE p.ean NOT IN (\r\n  SELECT DISTINCT rp.ean\r\n  FROM rechnungPos rp\r\n)\r\nGROUP BY p.ean, p.bezeichnung;
13824	2	1	select r.datum, r.rechnungNr, Sum(p.menge * p.einzelPreis) "Summe", k.name\r\nfrom rechnung r, kunde k, rechnungPos p\r\nwhere r.datum = p.datum AND r.rechnungNr = p.rechnungNr AND r.kundeNr = k.kundeNr\r\ngroup by r.datum, r.rechnungNr, k.name\r\nhaving not exists \r\n(select * from rechnungPos p1\r\n where p1.rechnungNr = r.rechnungNr AND p1.datum = r.datum\r\n and (p1.menge * p1.einzelPreis) <= 1000)\r\norder by Sum(p.menge * p.einzelPreis) DESC;
13825	2	1	select f.filNr, f.inhName, f.strasse, f.plz\r\nfrom filiale f\r\nwhere not exists(\r\nselect p.kategorie\r\nfrom produkt p\r\nwhere 2*(select count(*) \r\n         from produkt p1, sortiment s where p1.ean = s.ean \r\n         and p1.kategorie = p.kategorie \r\n         and f.filNr = s.filNr)\r\n      <=\r\n        (select count(*)\r\n         from produkt p2\r\n         where p2.kategorie = p.kategorie));
13826	2	1	select f.filNr, f.inhName, p.ean, p.bezeichnung, (s.vkPreis - s.preisRed) "Billigst"\r\nfrom filiale f, produkt p, sortiment s\r\nwhere f.filNr = s.filNr and s.ean = p.ean\r\nand (s.vkPreis - s.preisRed) = \r\n(select MIN(s1.vkPreis - s1.preisRed)\r\n from sortiment s1\r\n where s1.ean = p.ean);
13863	2	1	select f.filnr, f.inhName, count(*)\r\nfrom filiale f, sortiment s\r\nwhere f.filnr=s.filnr\r\ngroup by f.filnr, f.inhName\r\norder by count(*) desc;
13864	2	1	select s.ean, s.filnr, (s.vkpreis-s.preisred) tats_Preis, p.listpreis,\r\n       ROUND(100*((s.vkpreis-s.preisred)-p.listpreis)/p.listpreis) Abweichung\r\nfrom sortiment s, produkt p\r\nwhere s.ean=p.ean\r\nand ABS((s.vkpreis-s.preisred)-p.listpreis)>0.2*p.listpreis\r\norder by s.ean, s.filnr;
13865	2	1	select distinct k.kundenr, k.name, k.bonStufe\r\nfrom kunde k, rechnung r\r\nwhere k.kundenr = r.kundenr\r\nand   r.bezahlt='N'\r\nand   k.kundenr NOT IN (select r2.kundenr from rechnung r2 where r2.bezahlt='Y')\r\norder by k.kundenr;
13866	4	1	select s.filnr, p.kategorie, Sum(s.bestand*p.ekpreis) lagerwert\r\nfrom sortiment s, produkt p\r\nwhere s.ean=p.ean\r\nand p.kategorie NOT IN\r\n(select p2.kategorie\r\n from produkt p2, rechnungpos rp2, rechnung r2\r\n where p2.ean=rp2.ean\r\n and rp2.datum=r2.datum\r\n and rp2.rechnungnr=r2.rechnungnr\r\n and s.filnr=r2.filnr)\r\ngroup by s.filnr, p.kategorie\r\norder by s.filnr, p.kategorie
13867	2	1	select p.kategorie, p.ean, SUM(rp.menge) Stueck, Sum(rp.einzelpreis*rp.menge) Umsatz\r\nfrom produkt p, rechnungpos rp\r\nwhere p.ean=rp.ean\r\ngroup by p.kategorie, p.ean\r\nhaving SUM(rp.menge)>=ALL\r\n (select sum(rp2.menge)\r\n  from produkt p2, rechnungpos rp2\r\n  where p2.ean = rp2.ean\r\n  and p2.kategorie =p.kategorie\r\n  group by p2.ean)\r\norder by p.kategorie;
13868	2	1	Select rp.rechnungnr, rp.datum, sum((rp.einzelpreis-p.ekpreis)*rp.menge) RG\r\nfrom rechnungpos rp, produkt p\r\nwhere p.ean=rp.ean\r\ngroup by rp.rechnungnr, rp.datum\r\nhaving sum((rp.einzelpreis-p.ekpreis)*rp.menge)\r\n       > (select avg(sum((rp2.einzelpreis-p2.ekpreis)*rp2.menge))\r\n          from rechnungpos rp2, produkt p2\r\n          where rp2.ean=p2.ean\r\n          group by rp2.rechnungnr, rp2.datum);
13869	2	1	select distinct f.filnr, f.plz, r.datum\r\nfrom filiale f, rechnung r\r\nwhere f.filnr = r.filnr\r\nand (select count(distinct p.kategorie)\r\n     from produkt p, sortiment s\r\n     where p.ean=s.ean and s.filnr=r.filnr) > 2*\r\n    (select count(distinct p2.kategorie)\r\n     from produkt p2, rechnungpos rp2, rechnung r2\r\n     where p2.ean=rp2.ean\r\n     and rp2.rechnungnr=r2.rechnungnr and rp2.datum=r2.datum\r\n     and r2.datum=r.datum and r2.filnr=r.filnr)\r\norder by f.filnr, r.datum;
13870	2	1	select f.filnr, f.plz, sum(rp.menge*rp.einzelpreis) Umsatz\r\nfrom filiale f, rechnung r, rechnungpos rp\r\nwhere f.filnr=r.filnr\r\nand r.rechnungnr=rp.rechnungnr and r.datum = rp.datum\r\ngroup by f.filnr, f.plz\r\nhaving sum(rp.menge*rp.einzelpreis) >\r\n  (select avg(sum(rp2.menge*rp2.einzelpreis))\r\n   from filiale f2, rechnung r2, rechnungpos rp2\r\n   where f2.filnr=r2.filnr and r2.rechnungnr=rp2.rechnungnr and r2.datum=rp2.datum\r\n   group by f2.filnr)\r\norder by umsatz desc;
13871	2	1	select p.ean, p.kategorie, sum(rp.einzelpreis*rp.menge) Umsatz, a.ges_umsatz\r\nfrom produkt p, rechnungpos rp,\r\n  (select p2.kategorie, sum(rp2.menge*rp2.einzelpreis) as ges_umsatz\r\n   from produkt p2, rechnungpos rp2\r\n   where p2.ean=rp2.ean\r\n   group by p2.kategorie) a\r\nwhere p.kategorie=a.kategorie\r\nand rp.ean=p.ean\r\ngroup by p.ean, p.kategorie, a.ges_umsatz\r\nhaving sum(rp.einzelpreis*rp.menge) > a.ges_umsatz/3\r\norder by p.ean;
13872	14	13	select kategorie, sum(vkpreis * menge) as umsatz \r\nfrom artikel a, auftragszeile az\r\nwhere az.artikel=a.ean\r\ngroup by kategorie\r\norder by umsatz;
13873	14	13	select k.nr, k.name, sum(vkpreis * menge) as umsatz \r\nfrom kunde k, auftragskopf ak, auftragszeile az\r\nwhere k.nr=ak.kunde and\r\n  ak.nr = az.nr\r\ngroup by k.nr, k.name\r\nhaving sum(vkpreis*menge) > 5000\r\norder by umsatz;
13874	14	13	select a.ean, a.bezeichnung\r\nfrom artikel a\r\nwhere\r\n  not exists (\r\n     select * \r\n     from auftragszeile az\r\n     where az.artikel = a.ean);
13875	14	13	select distinct v.kurzzeichen, v.name\r\nfrom vertreter v, auftragskopf ak, kunde k\r\nwhere v.kurzzeichen = ak.vertreter AND\r\n      ak.kunde = k.nr AND\r\n      k.gebiet = 'OST';
13876	14	13	select a.ean, a.bezeichnung, ekpreis, \r\n  sum(az.vkpreis * az.menge)/sum(Menge) as "d_vkpreis",\r\n  ((sum(az.vkpreis * az.menge)/sum(Menge))-ekpreis) as "d_db"\r\nfrom artikel a, auftragszeile az\r\nwhere a.ean = az.artikel\r\ngroup by ean, bezeichnung, ekpreis\r\norder by ean;
13877	14	13	select distinct k.gebiet\r\nfrom kunde k, auftragskopf ak, auftragszeile az\r\nwhere k.nr = ak.kunde AND\r\n      ak.nr = az.nr\r\ngroup by k.gebiet\r\nhaving sum(az.vkpreis * az.menge) = (\r\n  select max (sum(az2.vkpreis * az2.menge))\r\n  from kunde k2, auftragskopf ak2, auftragszeile az2\r\n  where k2.nr = ak2.kunde AND\r\n        ak2.nr = az2.nr\r\ngroup by k2.gebiet);
13878	14	13	select v.kurzzeichen as kz, v.name, v.provision, k.gebiet, \r\nsum(az.vkpreis*az.menge) as UMSATZ,\r\nsum(az.vkpreis*az.menge)*v.provision/100 as PROVBETRAG\r\nfrom kunde k, vertreter v, auftragskopf ak, auftragszeile az\r\nwhere k.nr = ak.kunde AND\r\n     v.kurzzeichen = ak.vertreter AND\r\n     ak.nr = az.nr AND\r\n     ak.lieferdatum >= '01-SEP-99'\r\ngroup by v.kurzzeichen, v.name, v.provision, k.gebiet\r\norder by v.kurzzeichen;
13879	14	13	(select k.nr, a.kategorie, sum(az.vkpreis*az.menge) as UMSATZ\r\n from kunde k, artikel a, auftragskopf ak, auftragszeile az\r\n where k.nr=ak.kunde and\r\n       ak.nr=az.nr and\r\n       az.artikel=a.ean and\r\n       k.gebiet = 'OST'\r\n group by k.nr, a.kategorie\r\n)\r\nUNION\r\n(select distinct k2.nr, a2.kategorie, 0 as UMSATZ\r\n from kunde k2, artikel a2\r\n where k2.gebiet = 'OST' and\r\n       not exists (\r\n          select * \r\n          from auftragskopf ak3, auftragszeile az3, artikel a3\r\n          where ak3.nr=az3.nr and\r\n                ak3.kunde=k2.nr and\r\n                az3.artikel=a3.ean and\r\n                a3.kategorie=a2.kategorie)\r\n)\r\norder by nr, kategorie;
13880	14	13	SELECT a.ean, a.bezeichnung,\r\n       (SUM((az.vkpreis - a.ekpreis) * az.menge)/SUM(az.menge)) AS DB_ARTIKEL,\r\n       kategorie.DB_KATEG\r\nFROM   artikel a, auftragszeile az,\r\n       (SELECT a2.kategorie, (SUM((az2.vkpreis - a2.ekpreis)*az2.menge)/SUM(az2.menge)) AS DB_KATEG\r\n        FROM   artikel a2, auftragszeile az2\r\n        WHERE  az2.artikel=a2.ean\r\n        GROUP BY a2.kategorie) kategorie\r\nWHERE  a.ean=az.artikel AND\r\n       kategorie.kategorie=a.kategorie\r\nGROUP BY a.ean, a.bezeichnung, kategorie.DB_KATEG\r\nHAVING (SUM((az.vkpreis - a.ekpreis) * az.menge)/SUM(az.menge)) > kategorie.DB_KATEG\r\nORDER BY ean;\r\n\r\n/*select a.ean, a.bezeichnung,\r\n(sum (az.vkpreis * az.menge)/sum(menge))-avg(a.ekpreis) as "DB_ARTIKEL",\r\nkategorie."DB_KATEG"\r\nfrom artikel a, auftragszeile az,\r\n(select a2.kategorie, (sum(az2.vkpreis*az2.menge)/sum(az2.menge)) - avg(a2.ekpreis) as "DB_KATEG"\r\n from artikel a2, auftragszeile az2\r\n where az2.artikel=a2.ean\r\n group by a2.kategorie) kategorie\r\nwhere\r\n a.ean=az.artikel and\r\n kategorie.kategorie=a.kategorie\r\ngroup by a.ean, a.bezeichnung, kategorie."DB_KATEG"\r\nhaving (sum(az.vkpreis*az.menge)/sum(Menge))\r\n       - avg(a.ekpreis) > kategorie."DB_KATEG"\r\norder by ean;*/
13881	14	13	select v.kurzzeichen, v.name\r\nfrom vertreter v\r\nwhere not exists (\r\n  select * \r\n  from kunde k\r\n  where not exists (\r\n    select * \r\n    from auftragskopf ak, kunde k2\r\n    where ak.kunde = k2.nr and\r\n          k2.gebiet = k.gebiet and\r\n          ak.vertreter = v.kurzzeichen));
13882	14	13	(select k.nr, k.name\r\n from kunde k, auftragskopf ak, auftragszeile az\r\n where k.nr = ak.kunde and\r\n       ak.nr = az.nr and\r\n       az.menge > 0\r\n)\r\nMINUS\r\n(select k2.nr, k2.name\r\n from kunde k2, auftragskopf ak2\r\n where k2.nr = ak2.kunde and ak2.vertreter = 'GP');
96	2	1	SELECT DISTINCT p.persnr, p.name FROM person p, wohnung w, mietet m WHERE  p.persnr=w.eigentuemer AND w.wohnnr=m.wohnnr AND m.bis=TO_DATE('31-12-2099','dd-mm-yyyy') AND p.stand='ledig' AND m.preis/w.gross > 7 ORDER BY p.persnr
97	2	1	SELECT w.wohnnr, w.eigentuemer AS v_persnr, p1.name as v_name, m.mieternr AS m_persnr, p2.name as m_name FROM   wohnung w, person p1, person p2, mietet m WHERE  w.wohnnr=m.wohnnr AND w.eigentuemer=p1.persnr AND m.mieternr=p2.persnr AND w.bezirk=1 AND m.bis=TO_DATE('31-12-2099','dd-mm-yyyy') ORDER BY w.wohnnr
98	2	1	SELECT DISTINCT w.wohnnr, w.bezirk FROM wohnung w, mietet m WHERE w.wohnnr=m.wohnnr AND w.wohnnr NOT IN (SELECT m2.wohnnr FROM mietet m2 WHERE  m2.bis=TO_DATE('31-12-2099','dd-mm-yyyy')) ORDER BY w.bezirk, w.wohnnr
99	2	1	SELECT DISTINCT p.persnr, p.name FROM person p, wohnung w WHERE  p.persnr=w.eigentuemer AND NOT EXISTS (SELECT * FROM wohnung w2 WHERE w2.eigentuemer=p.persnr AND NOT EXISTS (SELECT * FROM mietet m WHERE  m.wohnnr=w2.wohnnr))
79	2	1	SELECT KursNr FROM kurs WHERE Lektor='Mueller'
80	2	1	SELECT * FROM student WHERE Land='Deutschland' OR Land='Schweiz' ORDER BY land, name
81	2	1	SELECT DISTINCT(s.Land) AS Land FROM student s, kurs k, belegung b WHERE b.MatrikelNr=s.MatrikelNr AND b.KursNr=k.KursNr AND k.Name='Datenbanken'
82	2	1	SELECT Land, count(*) AS AnzahlStudierende FROM student GROUP BY Land
83	2	1	SELECT Land, count(*) AS AnzahlStudierende FROM student WHERE Land <> 'Austria' GROUP BY Land
84	2	1	SELECT Land, count(*) AS AnzahlStudierende FROM student GROUP BY Land HAVING count(*)>10
85	2	1	SELECT * FROM student WHERE MatrikelNr NOT IN (SELECT MatrikelNr FROM belegung)
86	2	1	SELECT * FROM person ORDER BY Name, Beruf
87	2	1	SELECT DISTINCT beruf FROM person
88	2	1	SELECT name, beruf FROM person WHERE persnr = 1
89	2	1	SELECT eigentuemer AS persnr FROM wohnung WHERE bezirk = 2 OR bezirk = 3
90	2	1	SELECT persnr, name FROM person, wohnung WHERE (bezirk = 2 OR bezirk = 3) AND person.persnr = wohnung.eigentuemer
91	2	1	SELECT p.persnr, p.name, w.gross, m.preis FROM person p, wohnung w, mietet m WHERE w.bezirk = 2 AND p.persnr = w.eigentuemer AND w.wohnnr = m.wohnnr
92	2	1	SELECT AVG(gross) AS durchschnittsgroesse FROM wohnung
93	2	1	SELECT bezirk, AVG(gross) AS durchschnittsgroesse FROM wohnung GROUP BY bezirk
94	2	1	SELECT bezirk, AVG(gross) AS durchschnittsgroesse FROM wohnung GROUP BY bezirk HAVING count(*) > 1
95	2	1	SELECT bezirk, AVG (gross) as durchschnittsgroesse FROM wohnung WHERE wohnung.eigentuemer IN (SELECT persnr FROM person WHERE stand = 'ledig') GROUP BY bezirk
10003	2	1	select distinct inhname as name, gebdat\r\nfrom konto\r\nwhere saldo>10000
13883	10	1	select count(*) from cat
10004	2	1	select i.name, i.gebdat, b.buchngNr, k.kontoNr \r\nfrom inhaber i, konto k, buchung b\r\nwhere i.name=k.inhname and i.gebdat=k.gebdat and b.aufkonto=k.kontonr \r\nand b.datum <= ALL(select datum from buchung b1 where b1.aufkonto=k.kontonr);
13884	10	1	select count(*) from cat
13885	1	1	select * from cat
114	2	1	select coursecode from course where lecturer='Miller'
13886	1	1	select count(*) from course
13887	1	1	select * from cat
13888	1	1	select name from course
13889	1	1	select * from course
13890	2	1	select * from artist
13891	6	5	SELECT titel, autor FROM buch WHERE LOWER(titel) LIKE '%database%';
13893	15	15	SELECT\r\n  name,\r\n  cnt\r\nFROM (\r\n       SELECT\r\n         c.NAME,\r\n         COUNT(DATE_VALID) AS cnt,\r\n         sum(SHARE_CNT)    AS share_sum\r\n       FROM COMPANY c\r\n         JOIN LISTED_AT la ON c.ID = la.COMPANY_ID AND DATE_VALID = TO_DATE('04-08-2016', 'dd-mm-yyyy')\r\n       GROUP BY c.ID, c.NAME) a\r\nWHERE share_sum = (SELECT max(summed)\r\n                   FROM (\r\n                          SELECT sum(SHARE_CNT) AS summed\r\n                          FROM LISTED_AT\r\n                          WHERE DATE_VALID = TO_DATE('04-08-2016', 'dd-mm-yyyy')\r\n                          GROUP BY COMPANY_ID) s)\r\n
13894	15	15	SELECT\r\n  c.NAME  AS company_name,\r\n  se.NAME AS stock_exchange_name,\r\n  value\r\nFROM (SELECT\r\n        COMPANY_ID,\r\n        STOCK_EXCHANGE_CODE,\r\n        SHARE_CNT * SHARE_PRICE AS value\r\n      FROM LISTED_AT\r\n      WHERE DATE_VALID = TO_DATE('04-08-2016', 'dd-mm-yyyy')) la\r\n  JOIN COMPANY c ON la.COMPANY_ID = c.ID\r\n  JOIN STOCK_EXCHANGE se ON se.CODE = la.STOCK_EXCHANGE_CODE\r\nORDER BY value DESC\r\n
13895	15	15	SELECT *\r\nFROM COMPANY c\r\nWHERE NOT exists(\r\n    SELECT sa.COUNTRY_CODE\r\n    FROM LISTED_AT la\r\n      JOIN STOCK_EXCHANGE sa ON la.STOCK_EXCHANGE_CODE = sa.CODE\r\n    WHERE la.COMPANY_ID = c.ID AND NOT exists(SELECT *\r\n                                              FROM BASED_IN bi\r\n                                              WHERE\r\n                                                bi.COUNTRY_CODE = sa.COUNTRY_CODE AND la.COMPANY_ID = bi.COMPANY_ID))\r\n
13896	15	15	SELECT co.NAME\r\nFROM COUNTRY co\r\nWHERE NOT exists(SELECT *\r\n                 FROM BASED_IN bi\r\n                 WHERE bi.COUNTRY_CODE = co.CODE)\r\n
13897	15	15	SELECT co.NAME\r\nFROM COUNTRY co\r\nWHERE exists(SELECT COUNTRY_CODE\r\n             FROM STOCK_EXCHANGE se\r\n             WHERE se.COUNTRY_CODE = co.CODE\r\n             GROUP BY COUNTRY_CODE\r\n             HAVING count(*) >= 2)\r\n
13898	15	15	SELECT DISTINCT STOCK_EXCHANGE_CODE\r\nFROM STOCK_EXCHANGE se\r\n  JOIN LISTED_AT la ON se.CODE = la.STOCK_EXCHANGE_CODE\r\nWHERE la.COMPANY_ID =\r\n      (SELECT id\r\n       FROM COMPANY c\r\n       WHERE c.year_established =\r\n             (SELECT min(YEAR_ESTABLISHED)\r\n              FROM COMPANY c))\r\nORDER BY STOCK_EXCHANGE_CODE;\r\n
13899	15	15	select s.*\r\nfrom stock_exchange s\r\nwhere (select sum(share_price * share_cnt) \r\n              from listed_at \r\n              where stock_exchange_code = s.code \r\n                and date_valid = to_date('2015-12-04', 'yyyy-mm-dd')) >\r\n      (select avg(sum(share_price * share_cnt))\r\n              from listed_at \r\n              where date_valid = to_date('2015-12-04', 'yyyy-mm-dd')\r\n              group by stock_exchange_code);
13900	15	15	WITH main AS (SELECT *\r\n              FROM (\r\n                     SELECT\r\n                       c.name,\r\n                       sum(share_cnt * share_price) AS summed\r\n                     FROM listed_at la\r\n                       JOIN company c ON la.company_id = c.id\r\n                     WHERE date_valid = TO_DATE('04-08-2016', 'dd-mm-yyyy')\r\n                     GROUP BY c.id, c.name) a )\r\nSELECT NAME\r\nFROM main\r\nWHERE summed IN ((SELECT min(summed)\r\n                  FROM main), (SELECT max(summed)\r\n                               FROM main))\r\nORDER BY summed DESC;
13901	16	16	SELECT c."product_category", SUM(f."unit_sales")\r\nFROM   "sales_fact_1997" f\r\n       JOIN "product" p ON \r\n         f."product_id" = p."product_id"\r\n       JOIN "product_class" c ON \r\n         p."product_class_id" = c."product_class_id"\r\nGROUP BY c."product_category";
13902	16	16	SELECT s."store_state", c."product_category", SUM(f."unit_sales")\r\nFROM   "sales_fact_1997" f\r\n       JOIN "product" p ON \r\n         f."product_id" = p."product_id"\r\n       JOIN "product_class" c ON \r\n         p."product_class_id" = c."product_class_id"\r\n       JOIN "store" s ON\r\n         f."store_id" = s."store_id"\r\nWHERE s."store_type" = 'Supermarket'\r\nGROUP BY c."product_category", s."store_state"\r\nORDER BY s."store_state", c."product_category";
13903	16	16	SELECT t."month_of_year" as month_of_year, SUM(f."unit_sales") as sum_unit_sales\r\nFROM   "sales_fact_1997" f\r\n       JOIN "time_by_day" t ON \r\n         f."time_id" = t."time_id"\r\nGROUP BY t."month_of_year"\r\nORDER BY t."month_of_year";
13904	16	16	SELECT r."sales_state_province" AS province, SUM(f."unit_sales") AS unit_sales\r\nFROM   "sales_fact_1997" f\r\n       JOIN "customer" c ON \r\n         f."customer_id" = c."customer_id"\r\n       JOIN "region" r ON \r\n         c."customer_region_id" = r."region_id"\r\nWHERE r."sales_region" = 'North West'\r\nGROUP BY r."sales_state_province";
13905	16	16	SELECT s."store_state", c."product_category", SUM(f."unit_sales")\r\nFROM   "sales_fact_1997" f\r\n       JOIN "product" p ON \r\n         f."product_id" = p."product_id"\r\n       JOIN "product_class" c ON \r\n         p."product_class_id" = c."product_class_id"\r\n       JOIN "store" s ON\r\n         f."store_id" = s."store_id"\r\nGROUP BY ROLLUP(s."store_state", c."product_category");
13906	16	16	SELECT s."store_state", c."product_category", SUM(f."unit_sales")\r\nFROM   "sales_fact_1997" f\r\n       JOIN "product" p ON \r\n         f."product_id" = p."product_id"\r\n       JOIN "product_class" c ON \r\n         p."product_class_id" = c."product_class_id"\r\n       JOIN "store" s ON\r\n         f."store_id" = s."store_id"\r\nGROUP BY CUBE(s."store_state", c."product_category");
13907	16	16	SELECT s."store_state", c."product_category", SUM(f."unit_sales")\r\nFROM   "sales_fact_1997" f\r\n       JOIN "product" p ON \r\n         f."product_id" = p."product_id"\r\n       JOIN "product_class" c ON \r\n         p."product_class_id" = c."product_class_id"\r\n       JOIN "store" s ON\r\n         f."store_id" = s."store_id"\r\nGROUP BY GROUPING SETS((s."store_state", c."product_category"), ());
13908	16	16	SELECT r."sales_state_province", cl."product_family", SUM(f."unit_sales")\r\nFROM   "sales_fact_1997" f\r\n       JOIN "product" p ON\r\n         f."product_id" = p."product_id"\r\n       JOIN "product_class" cl ON \r\n         p."product_class_id" = cl."product_class_id"\r\n       JOIN "customer" c ON \r\n         f."customer_id" = c."customer_id"\r\n       JOIN "region" r ON \r\n         c."customer_region_id" = r."region_id"\r\nGROUP BY CUBE(r."sales_state_province", cl."product_family");
13892	1	1	SELECT   g.branche, SUM(umsatz) AS Umsatz\r\nFROM     gewinne g\r\nWHERE    g.bundesland = 'OÖ' AND g.jahr = 2011\r\nGROUP BY g.branche\r\nHAVING   COUNT(g.firma) > 10;
13909	16	16	SELECT r."sales_state_province" AS province, cl."product_family" AS family, s."store_type" AS type, SUM(f."unit_sales") AS unit_sales\r\nFROM   "sales_fact_1997" f\r\n       JOIN "product" p ON\r\n         f."product_id" = p."product_id"\r\n       JOIN "product_class" cl ON \r\n         p."product_class_id" = cl."product_class_id"\r\n       JOIN "customer" c ON \r\n         f."customer_id" = c."customer_id"\r\n       JOIN "region" r ON \r\n         c."customer_region_id" = r."region_id"\r\n       JOIN "store" s ON\r\n         f."store_id" = s."store_id"\r\nGROUP BY GROUPING SETS((r."sales_state_province", cl."product_family", s."store_type"), (r."sales_state_province", s."store_type"), ())\r\nORDER BY r."sales_state_province", cl."product_family", s."store_type";
13910	16	16	SELECT r."sales_state_province" province, SUM(f."unit_sales") AS unit_sales,\r\n       RANK() OVER( PARTITION BY r."sales_country" ORDER BY SUM(f."unit_sales") DESC) AS rank\r\nFROM   "sales_fact_1997" f\r\n       JOIN "customer" c ON \r\n         f."customer_id" = c."customer_id"\r\n       JOIN "region" r ON \r\n         c."customer_region_id" = r."region_id"\r\nGROUP BY r."sales_state_province", r."sales_country";
13911	16	16	SELECT t."month_of_year", SUM(f."unit_sales") AS total,\r\n       RANK() OVER( PARTITION BY t."the_year" ORDER BY SUM(f."unit_sales") DESC) AS rank\r\nFROM   "sales_fact_1997" f\r\n       JOIN "time_by_day" t ON \r\n         f."time_id" = t."time_id"\r\nGROUP BY t."month_of_year", t."the_year"\r\nORDER BY t."the_year", t."month_of_year";
13912	16	16	SELECT t."month_of_year", SUM(f."unit_sales"),\r\n       SUM(SUM(f."unit_sales")) OVER( PARTITION BY t."the_year" ORDER BY t."month_of_year")\r\nFROM   "sales_fact_1997" f\r\n       JOIN "time_by_day" t ON \r\n         f."time_id" = t."time_id"\r\n       JOIN "store" s ON\r\n         f."store_id" = s."store_id"\r\nGROUP BY t."month_of_year", t."the_year";
13913	16	16	SELECT t."month_of_year", SUM(f."unit_sales"),\r\n       AVG(SUM(f."unit_sales")) OVER( PARTITION BY t."the_year" ORDER BY t."month_of_year" ROWS BETWEEN 1 PRECEDING AND 1 FOLLOWING) AS moving_average\r\nFROM   "sales_fact_1997" f\r\n       JOIN "time_by_day" t ON \r\n         f."time_id" = t."time_id"\r\n       JOIN "store" s ON\r\n         f."store_id" = s."store_id"\r\nGROUP BY t."month_of_year", t."the_year";
13818	2	1	SELECT ean, bezeichnung, Spanne, kategorie FROM (select p1.ean, p1.bezeichnung,\n((p1.listPreis - p1.ekPreis)/p1.ekPreis)*100 Spanne, p1.kategorie\nfrom produkt p1\nwhere ((p1.listPreis - p1.ekPreis)/p1.ekPreis) =\n    (select MIN((p2.listPreis - p2.ekPreis)/p2.ekPreis)\n     from produkt p2\n     group by p2.kategorie\n     having p2.kategorie = p1.kategorie) ) AS correctQuery ORDER BY ean, bezeichnung, Spanne, kategorie
\.


--
-- Name: connections CONNECTIONS_PK; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.connections
    ADD CONSTRAINT "CONNECTIONS_PK" PRIMARY KEY (id);


--
-- Name: exercises EXERCISES_PK; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercises
    ADD CONSTRAINT "EXERCISES_PK" PRIMARY KEY (id);


--
-- Name: exercises EXERCISES_FK1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercises
    ADD CONSTRAINT "EXERCISES_FK1" FOREIGN KEY (practise_db) REFERENCES public.connections(id);


--
-- Name: exercises EXERCISES_FK2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercises
    ADD CONSTRAINT "EXERCISES_FK2" FOREIGN KEY (submission_db) REFERENCES public.connections(id);


--
-- Name: TABLE connections; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.connections TO sql;


--
-- Name: TABLE exercises; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.exercises TO sql;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public REVOKE ALL ON TABLES  FROM postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT SELECT ON TABLES  TO sql;


--
-- PostgreSQL database dump complete
--

