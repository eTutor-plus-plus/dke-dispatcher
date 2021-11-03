let $ums := (
let $hk := fn:doc('D:/XQuery/docs/1009.xml')/handelskette
for $AKunde in $hk/kunden/kunde[bonStufe='A']
for $rechnung in $hk/rechnungen/rechnung[kundeNr = $AKunde/@kundeNr]
for $pos in $rechnung/rposition
return <umsatz preis="{$pos/einzelPreis}" menge="{$pos/menge}">{$pos/einzelPreis * $pos/menge}</umsatz>)/text()
return fn:sum($ums)
