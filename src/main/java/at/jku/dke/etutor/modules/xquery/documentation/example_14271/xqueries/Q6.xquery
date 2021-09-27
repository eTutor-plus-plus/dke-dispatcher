let $hk := doc('D:/XQuery/docs/1010.xml')/handelskette
let $bs := distinct-values($hk/kunden/kunde/bonStufe)
for $bStufe in $bs
return <res stufe="{$bStufe}">{
let $kdNrBs := $hk/kunden/kunde[bonStufe eq $bStufe]/@kundeNr
for $x in $kdNrBs
return <res2>{$x}</res2>
}</res>

