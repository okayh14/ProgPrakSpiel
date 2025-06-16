# Farblogik-Spiel mit GUI 

## Beschreibung
Dieses Java-Projekt implementiert ein farbbasiertes Brettspiel mit grafischer Benutzeroberfläche (GUI). Ziel ist es, durch Auswahl von Farben auf einem Spielfeld Punkte zu sammeln. Die Spiellogik erlaubt Farbübernahme durch benachbarte Felder, ähnlich wie bei „Flood It“-Spielen.

## Hauptfunktionen
- **Grafische Oberfläche (Swing)**: Bedienung über Fenster, Buttons und Farbfelder.
- **Spiellogik**: Ein Spielfeld besteht aus farbigen Zellen. Durch Farbwechsel breitet sich der Einflussbereich des Spielers aus.
- **Start mit Ton**: Beim Start der Anwendung wird eine Audio-Datei (`HxH.wav`) abgespielt.
- **Testklasse enthalten**: Ein separates Testmodul (`Testing.java`) dient zur Überprüfung der Spiellogik.

## Projektstruktur
```
src/
├── gui/        → GUI-Komponenten (Panels, Fenster)
├── logic/      → Spiellogik (Board, Field)
├── start/      → Einstiegspunkt und Audio-Handling
├── testing/    → Manuelle Tests der Spiellogik
```

## Ausführung
Die Anwendung wird über die Klasse `Start.java` gestartet:
```bash
javac -d bin src/start/Start.java
java -cp bin start.Start
```

## Abhängigkeiten
- Java Standardbibliothek (Swing, Audio)
- Keine externen Bibliotheken notwendig

