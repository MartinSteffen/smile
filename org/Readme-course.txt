Vorl�ufige Projektstruktur:

Ich habe ein Eclipse-Projekt "smile" angelegt mit getrennten Verzeichnissen
f�r Sources und Classfiles.

----
Vorl�ufiger Inhalt des Wurzelverzeichnisses:

README.txt
    Diese Datei soll vorl�ufig f�r Notizen dienen, die alle etwas angehen.
    Macht damit, was ihr wollt.

src
    Das Source-Verzeichnis

bin
    Das Verzeichnis f�r die Classfiles

javadoc
    Das Verzeichnis f�r die von javadoc generierte HTML-Dokumentation
    (alle Inhalte werden von svn ignoriert, da die Property svn:ignore
    auf '*' gesetzt wurde. F�r handgeschriebene Dokumentation wird man
    also ein anderes Verzeichnis verwenden m�ssen)

Dokumentation
    handgeschriebene Dokumentation

----
Vorl�ufige Package-Struktur

(default package)
Main.java
    das Hauptprgramm, das die GUI aufruft

csm
    Die Core-State-Machine
    (geh�rt allein der Data-Structure-Gruppe)

gui
    Die GUI
    (geh�rt allein der GUI-Gruppe)

nua
    Der \nu-Automat
    (geh�rt allein der Data-Structure-Gruppe)

testing
    Sammelstelle f�r alle Unit-Tests
    (geh�rt allein der Testing-Gruppe)


----
Das ist alles vorl�ufig und unvollst�ndig und soll nur einen
bescheidenen Vorschlag darstellen.


(Holger, 27.10.06)