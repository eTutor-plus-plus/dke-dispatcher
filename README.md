# eTutor-Grading

# Installation

### PostgreSQL
Zur Speicherung der Submissions und Gradings muss auf dem Port 5433 des localhosts eine PostgreSQL-Datenbankserver laufen. 
Die benötigten Datenbanken für die Submissions, Gradings und den Daten für das SQL-Modul können als Docker Container erstellt werden.
Dazu muss im Ordner /src/main/java/at/jku/dke/etutor/modules/sql/PostgreS/docker/ folgender Befehl ausgeführt werden: 

$docker build . 

Danach kann das erstellte Image als Container auf Port 5433 gestartet weden:

$docker run -p 5433:5432 -d <<Image-Id>>
  
Anschließend kann die Rest-Schnittstelle gestartet werden.





## Funktionsbeschreibung package grading

Es stehen 2 Schnittstellen zur Verfügung:
* SubmissionController (/submission)
* GradingController (/grading)

Der **SubmissionController** nimmt Objekte vom Typ Submission (als JSON) im Body eines Http-Post-Requests entgegen. 
Daraufhin wird in Abhängigkeit der Submission eine UUID generiert, im Submission Objekt referenziert, dieses persistiert, und damit ein Objekt vom Typ SubmissionDispatcher extends Runnable initialisiert->
die Verarbeitung der Submission startet in einem seperatem Trhead.
Die Schnittstelle schickt eine ResponseEntity<EntityModel<SubmissionId>> als Response zurück (Http.Status = Accepted), darin finden sich die ID (gewrapped als SubmissionId Objekt) 
sowie ein Link zur zweiten Schnittstelle (spezifisch für die ID).
Falls bei der Generierung der ID oder dem Iinitialisieren des Threads Exceptions auftreten, wird eine ResponseEntity mit Status Code 500 und einer Standard ID von -1 zurückgesendet.

Der **SubmissionDispatcher** 
identifiziert zunächst den vom jeweiligen Modul implementierten Evaluator in Abhängigkeit von dem übermittelten Task-Type und ruft danach die
unterschiedlichen Methoden zur Auswertung der Submission auf. 
Das daraus entstehende GradingDTO-Objekt wird zusammen mit der Submission ID ebenfalls in einer PostgreSQL Tabelle gespeichert. 
Darin finden sich die maximal erreichbaren sowie die erreichten Punkte.
Für nähere Informationen zur Analyse der Submission wird der Typ des gefundenen Evaluators mittels isntanceof in der Methode processAnalysis(Analysis analysis)
des SubmissionDispatchers gesucht. Nach einem Typecast können dann nähere Informationen zur Analyse abgefragt werden 
(Verschiedene Beurteilungskriterien und ob diese erfüllt wurden zB bei SQL Kartesisches Produkt, korrekte Spalten, etc.).

Der **GradingController** nimmt unter /grading/{submissionId} get-Requests mit einer ID als Pfadvariable entgegen und fragt die Verfügbarkeit einer
Grading-Instanz für diese in der Datenbank ab. Wird diese gefunden wird als Response eine ResponseEntity<EntityModel<RestGrading>> (Http.Status = Ok)
mit dem GradingDTO Objekt sowie einem auf sich selbst verweisenden Link im response-body zurückgesendet.
Wenn kein Grading für die angefragte ID gefunden wird, wird mit dem Status Code Not_Found ein GradingDTO mit 0 erreichten von 0 erreichbaren Punkten 
sowie ebenfalls ein Link als Selbstreferenz zurückgeschickt.
  
Eine Swageger-API Doku kann im Testbetrieb unter localhost:8081/swagger-ui.html eingesehen werden.








