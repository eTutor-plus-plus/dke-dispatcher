let $hk := fn:doc('D:/XQuery/docs/1.xml')/handelskette
for $AKunde in $hk/kunden/kunde[bonStufe='A']
return <res>{$AKunde/@kundeNr}</res>
