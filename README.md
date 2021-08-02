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





