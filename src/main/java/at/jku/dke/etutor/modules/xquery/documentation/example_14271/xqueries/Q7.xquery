let $hk := doc('D:/XQuery/docs/1.xml')/handelskette
for $kat in distinct-values($hk/produkte/produkt/kategorie)
let $prod := $hk/produkte/produkt[kategorie = $kat]
let $price := $hk/filialen/filiale/sortiment/prodInSortiment[ean = $prod/@ean]/vkPreis
return <kategorie val="{$kat}">{avg($price)}</kategorie>