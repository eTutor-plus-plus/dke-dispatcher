let $hk := doc('http://etutor.dke.uni-linz.ac.at/etutor/XML?id=1')/handelskette
let $bs := distinct-values($hk/kunden/kunde/bonStufe)
for $bStufe in $bs
let $kdNrBs := $hk/kunden//kunde[bonStufe eq $bStufe]/@kundeNr,
    $kdCnt := count(distinct-values($kdNrBs))
let $rPosten :=
    (for $r in $hk/rechnungen/rechnung[kundeNr = $kdNrBs]/rposition
    return ($r/einzelPreis * $r/menge))
order by $bStufe
return <bStufe val="{$bStufe}">
    <umsGesamt>{sum($rPosten)}</umsGesamt>
    <umsProKunde>{sum($rPosten) div $kdCnt}</umsProKunde>
</bStufe>
