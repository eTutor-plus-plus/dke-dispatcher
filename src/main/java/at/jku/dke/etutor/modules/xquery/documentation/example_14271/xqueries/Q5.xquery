let $hk := doc('D:/XQuery/docs/1.xml')/handelskette,
    $knd := $hk/kunden/kunde,
    $rech := $hk/rechnungen/rechnung 

let $b := (for $bon in distinct-values($knd/bonStufe)
order by $bon
return $bon)
for $bstufe in $b
let $kums := (
	for $k in $knd[bonStufe=$bstufe]
	let $pums := (
		for $p in $rech[kundeNr=$k/@kundeNr]/rposition
		return $p/einzelPreis*$p/menge)
	return sum($pums))
return <bStufe val="{$bstufe}">
		<umsGesamt>{sum($kums)}</umsGesamt>
		<umsProKunde>{avg($kums)}</umsProKunde>
	</bStufe>