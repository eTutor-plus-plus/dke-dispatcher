-- Database: sql_trial_begin

-- DROP DATABASE sql_trial_begin;
--2

SELECT r1.recordId, r1.artistName, r1.title
FROM   record r1, track t1
WHERE  r1.type='Album' AND r1.recordId=t1.recordId
GROUP BY r1.recordId, r1.artistName, r1.title
HAVING SUM(length) <
  (SELECT 
   		AVG(sum)
	FROM
		(
		SELECT
			SUM(t2.length) 
		FROM 
			record r2, track t2
		WHERE 
			r2.type='Album' AND r2.recordId=t2.recordId
		GROUP BY 
			r2.recordId 
		) as subquery
  )
;

--23

(SELECT k.kundenr, k.name
FROM   kunde k, rechnung r, rechnungpos rp, produkt p
WHERE  k.kundenr=r.kundenr
AND    r.rechnungnr=rp.rechnungnr
AND    r.datum=rp.datum
AND    rp.ean=p.ean
AND    p.kategorie='Audio')
INTERSECT
(SELECT k.kundenr, k.name
FROM   kunde k, rechnung r, rechnungpos rp, produkt p
WHERE  k.kundenr=r.kundenr
AND    r.rechnungnr=rp.rechnungnr
AND    r.datum=rp.datum
AND    rp.ean=p.ean
AND    p.kategorie='Sonstiges')
EXCEPT
(SELECT k.kundenr, k.name
FROM   kunde k, rechnung r, rechnungpos rp, produkt p
WHERE  k.kundenr=r.kundenr
AND    r.rechnungnr=rp.rechnungnr
AND    r.datum=rp.datum
AND    rp.ean=p.ean
AND    p.kategorie='Pflege')


--25
SELECT MIN(rp.datum) as Tag
FROM   rechnungpos rp
WHERE  (SELECT SUM(rp2.menge*rp2.einzelpreis)
        FROM   rechnungpos rp2
        WHERE  rp2.datum<=rp.datum) > 0.5 *
       (SELECT SUM(rp3.menge*rp3.einzelpreis)
        FROM   rechnungpos rp3)


--26
SELECT f.filnr, f.plz, SUM(p.ekpreis * s.bestand) as Lagerwert
FROM   filiale f, produkt p, sortiment s
WHERE  f.filnr = s.filnr
AND    p.ean = s.ean
AND    p.kategorie = 'Ersatz'
GROUP BY f.filnr, f.plz
HAVING SUM(p.ekpreis * s.bestand) > 
                (
				SELECT 
				  	AVG(sum)
				FROM
				  	(
					SELECT
						SUM(p1.ekpreis * s1.bestand)
                  	FROM   
				  		produkt p1, sortiment s1
                 	WHERE  
				  		p1.ean = s1.ean
                  		AND    p1.kategorie = 'Ersatz'
                  	GROUP BY s1.filnr
				 	) as subquery
				);
				
--29
SELECT 
	AVG(ANZAHL)
	FROM
	(
		SELECT 
			COUNT (*) AS ANZAHL
		FROM 
			human p, parent par 
		WHERE 
			ParentName = name AND gender = 'f' 
		GROUP BY name
	) as subquery;
		
		
--
SELECT 
	p.ChildName As Name 
FROM 
	parent p, parent geschwister 
WHERE 
	p.ParentName = geschwister.ParentName 
GROUP BY 
	p.ChildName 
HAVING COUNT (distinct geschwister.ChildName) >
	(
		SELECT AVG(count)
		FROM
		(
			SELECT 
				COUNT(distinct g.ChildName)
			FROM parent p2, parent g 
			WHERE p2.ParentName = g.ParentName 
			GROUP BY p2.ChildName
		) as subquery
	);
	
	
--33
SELECT AVG(min) AS DURCHSCHNITTSALTER
FROM
	(
		SELECT MIN(p.age - enkel.age)
		FROM human p, human enkel, parent ist_kind, parent ist_enkel
		WHERE (p.name = ist_kind.ParentName)
		AND (ist_kind.ChildName = ist_enkel.ParentName)
		AND (ist_enkel.ChildName = enkel.name)
		AND (p.gender = 'm') GROUP BY p.name
	) as subquery

--39
select w.wohnnr, w.gross, count(*) Anzahlmietverh, avg(m.preis/w.gross) D_Miete, max(m.preis/w.gross) Hoechstmiete
from   wohnung w, mietet m
where  w.wohnnr=m.wohnnr
group by w.wohnnr, w.gross
having max(m.preis/w.gross) > 
          (
			  select avg(max)
			  from
			  (
			  	 	select (max(m1.preis/w1.gross))
				   from   wohnung w1, mietet m1
				   where  w1.wohnnr=m1.wohnnr
				   group by w1.wohnnr
			  ) as subquery
		   )
order by w.wohnnr


--55
SELECT w.bezirk, SUM(w.gross) FLAECHE
FROM   wohnung w, mietet m
WHERE  w.wohnnr=m.wohnnr AND m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') GROUP BY w.bezirk
HAVING SUM(w.gross)> 
	(
		SELECT AVG(sum)
		FROM
		(
			 SELECT SUM(w1.gross)
             FROM   wohnung w1, mietet m1
            WHERE  w1.wohnnr=m1.wohnnr AND m1.bis=TO_DATE('31-12-2099','DD-MM-YYYY')                      
			GROUP BY w1.bezirk
		) as subquery			
);

--59
SELECT bezirk, SUM(erloes) as MIETEINNAHMEN 
FROM 
	(
		(
			SELECT w.bezirk, SUM(ROUND(extract(year from (age(m.bis, m.von)))*12 + extract(month from (age(m.bis, m.von))))*m.preis) erloes
			FROM   wohnung w, mietet m
			WHERE  w.wohnnr=m.wohnnr
			AND    m.bis <> TO_DATE('31-12-2099','DD-MM-YYYY')
			GROUP BY w.bezirk
		) 
		UNION
		(
			SELECT w.bezirk, SUM(ROUND( extract(year from (age(current_date, m.von)))*12+ extract(month from (age(current_date, m.von))))*m.preis) erloes
			FROM   wohnung w, mietet m
			WHERE  w.wohnnr=m.wohnnr
			AND    m.bis = TO_DATE('31-12-2099','DD-MM-YYYY')
			GROUP BY w.bezirk
		)
	)as subquery
GROUP BY bezirk
HAVING SUM(erloes)=
		(
			SELECT MAX(sum)
			FROM 
			(
			SELECT SUM(erloes) 
            FROM 
				(
					(
							SELECT w.bezirk, SUM(ROUND( extract(year from (age(m.bis, m.von)))*12+ extract(month from (age(m.bis, m.von))))*m.preis) erloes
                           FROM   wohnung w, mietet m
                           WHERE  w.wohnnr=m.wohnnr
                           AND    m.bis <> TO_DATE('31-12-2099','DD-MM-YYYY')
                           GROUP BY w.bezirk
					)
                          UNION
                          (
							  SELECT w.bezirk, SUM(ROUND(extract(year from (age(current_date, m.von)))*12+extract(month from (age(current_date, m.von))))*m.preis) erloes
							   FROM   wohnung w, mietet m
							   WHERE  w.wohnnr=m.wohnnr
							  AND    m.bis = TO_DATE('31-12-2099','DD-MM-YYYY')
							   GROUP BY w.bezirk
							)
					) as subquery
				GROUP BY bezirk
				) as subquery2
		) 


--68
select r.recordid, r.title, count(*) Tracks 
from   record r, track t 
where  r.recordid=t.recordid 
group by r.recordid, r.title 
having count(*) = 
	(
		select max(count)
		from
		(
			select count(*) 
            from   record r1, track t1 
            where  r1.recordid=t1.recordid 
            group by r1.recordid
			
		) as subquery
	)
order by r.title;


--71

select f.kurzbez, f.bezeichnung, count(*) Anzahl
from fakultaet f, studienrichtung s, koje k
where s.gehoert_zu = f.kurzbez AND
      k.gemietet_von = s.kennzahl
group by f.kurzbez, f.bezeichnung
having count (standnr) = 
(
 	select MAX(count)
	from
	(
		select count(*)
 		from studienrichtung s2, koje k2
 		where k2.gemietet_von = s2.kennzahl
		group by s2.gehoert_zu
	) as subquery
);


--104
SELECT AVG(count)AS AVG_Anzahl
FROM
(
	SELECT COUNT(*)  FROM   konto k GROUP BY k.inhname, k.gebdat
) as subquery

--108
select i1.name, i1.gebdat, i1.adresse, count(*) anzahl, sum(betrag) sum_betrag
from   inhaber i1, konto k1, buchung b1
where  i1.name = k1.inhname and i1.gebdat = k1.gebdat
and    k1.kontonr = b1.aufkonto
group by i1.name, i1.gebdat, i1.adresse
having count(*) >
       (select avg(count)
		from
			(
			Select count(*)
			from   konto k2, buchung b2
			where  k2.kontonr = b2.aufkonto
			group by k2.inhname, k2.gebdat
			) as subquery
		)
		
--153
SELECT   AVG(count) AS avgsupervised
FROM
(
	select count(*)
	FROM     staff s1, staff s2
	WHERE    s1.snum = s2.supervisor 
	GROUP BY s1.snum
) as subquery


--10009
select pu.prodno, t.m as month, SUM(pu.qty*pu.price) as value
from   purchase pu, time t
where  pu.dayno=t.dayno
group by pu.prodno, t.m
		
--10010
select pu.prodno, t.m as month, SUM(pu.qty*pu.price) as value
from   purchase pu, time t
where  pu.dayno=t.dayno
group by grouping sets ((pu.prodno, t.m), (pu.prodno))

--10011
select pu.prodno, t.y as year, t.m as month, SUM(pu.qty*pu.price) as value
from   purchase pu, time t
where  pu.dayno=t.dayno
group by rollup (pu.prodno, t.y, t.m)
order by pu.prodno, t.y, t.m


--10012
select pu.prodno, t.m as month, SUM(pu.qty*pu.price) as value,
SUM(SUM(pu.qty*pu.price)) OVER 
(PARTITION BY pu.prodno ORDER BY t.m ROWS UNBOUNDED PRECEDING) cumvalue
from   purchase pu, time t
where  pu.dayno=t.dayno
group by pu.prodno, t.m;

--13171
select buchngnr, betrag
from buchung
where vonkonto in
  (select kontonr
   from konto
   where (current_date - interval '360 months')<gebdat)
or aufkonto in 
  (select kontonr
   from konto
   where (current_date - interval '360 months')<gebdat)
order by betrag desc;

--13175
select i.name, i.gebdat, i.adresse, count(*)anzahl
from inhaber i, konto k
where i.name=k.inhname and i.gebdat=k.gebdat
group by i.name, i.gebdat, i.adresse
having count(*) >
  (select avg(count)
   from
   (select count(*)
   from konto
   group by inhname, gebdat
   )as subquery
   );
   
--13783
select e.entlngnr, e.von, be.name, bu.titel
from entlehng e, benutzer be, buch bu
where (e.buch = bu.buchnr) and (e.benutzer = be.bennr) and
      (e.bis = '01-01-2999') and ((e.von + 100) < current_date)
order by be.name;


--13868
Select rp.rechnungnr, rp.datum, sum((rp.einzelpreis-p.ekpreis)*rp.menge) RG
from rechnungpos rp, produkt p
where p.ean=rp.ean
group by rp.rechnungnr, rp.datum
having sum((rp.einzelpreis-p.ekpreis)*rp.menge)
       > (select avg(sum)
		  from
		  	(
		  	  select sum((rp2.einzelpreis-p2.ekpreis)*rp2.menge)
			  from rechnungpos rp2, produkt p2
			  where rp2.ean=p2.ean
			  group by rp2.rechnungnr, rp2.datum
		  	)as subquery
		  )
		  
		  
--13870
select f.filnr, f.plz, sum(rp.menge*rp.einzelpreis) Umsatz
from filiale f, rechnung r, rechnungpos rp
where f.filnr=r.filnr
and r.rechnungnr=rp.rechnungnr and r.datum = rp.datum
group by f.filnr, f.plz
having sum(rp.menge*rp.einzelpreis) >
  (select avg(sum)
   from
	   (
		   select sum(rp2.menge*rp2.einzelpreis)
		   from filiale f2, rechnung r2, rechnungpos rp2
		   where f2.filnr=r2.filnr and r2.rechnungnr=rp2.rechnungnr and r2.datum=rp2.datum
		   group by f2.filnr
	   )as subquery
	)  
order by umsatz desc;
		  
		  
		  
--13877
select distinct k.gebiet
from kunde k, auftragskopf ak, auftragszeile az
where k.nr = ak.kunde AND
      ak.nr = az.nr
group by k.gebiet
having sum(az.vkpreis * az.menge) = 
	(
  	select max(sum)
	from
		(
			select sum(az2.vkpreis * az2.menge)
			  from kunde k2, auftragskopf ak2, auftragszeile az2
			  where k2.nr = ak2.kunde AND
        		ak2.nr = az2.nr
			group by k2.gebiet
		)as subquery
	);
	
--13882
(select k.nr, k.name
 from kunde k, auftragskopf ak, auftragszeile az
 where k.nr = ak.kunde and
       ak.nr = az.nr and
       az.menge > 0
)
EXCEPT
(select k2.nr, k2.name
 from kunde k2, auftragskopf ak2
 where k2.nr = ak2.kunde and ak2.vertreter = 'GP');
	
	
--13795
select b.buchnr, b.titel
from buch b
where b.buchnr not in
  (select distinct e.buch
   from entlehng e
   where e.von <= '1994-12-31' and e.bis >= '1994-01-01');