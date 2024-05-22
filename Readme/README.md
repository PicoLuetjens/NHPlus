# NHPlus
Software Weiterentwicklung von NHPlus

> Entwickler: Pico Lütjens, Kevin Schmidt

---

### Login:
**User Login:**
```text 
Username: test
Password: pass123
```

**Admin Login:**
```text
Username: admin
Password: admin123
```

### Rollen
**User:**
- kann Daten im AllCaregiverView, AllPatientView, AllTreatmentView und TreatmentView verändern, sowie neue Pfleger, Patienten und Behandlungen anlegen
- hat eine beschränkte Ansicht
- hat keine Rechte, Daten zu sperren
- Behandlungen die vom Admin gesperrt wurden, werden für normale user nicht mehr angezeigt
- Pfleger die vom Admin gesperrt wurden, werden für normale user nicht mehr angezeigt

**Admin:**
- kann alle Daten in allen Ansichten verändern
- hat eine erweiterte Ansicht
- kann Daten sperren
- Behandlungen die vom Admin gesperrt wurden, werden immer noch angezeigt und können wieder entsperrt werden(gesperrt true -> false)
- Pfleger die vom Admin gesperrt wurden, werden immer noch angezeigt und können wieder entsperrt werden(gesperrt true -> false)

**Generell:**
- Beim Aufrufen der entsprechenden Ansicht werden Daten wie Behandlungen und Pfleger aufgrund ihres letzten aktiven Datums aus der Datenbank gelöscht, sofern dieses über 10 Jahre zurückliegt
- Sollte ein Pfleger noch Behandlungen in einem Zeitraum unter 10 Jahren durchgeführt haben, wird der Pfleger in der Datenbank behalten und nur entsprechend zugewiesene Behandlungen mit einem Alter von über 10 Jahren dieses Pflegers gelöscht 
- Das Sperren von Daten läuft unabhängig vom automatisierten Lösch-Prozess, solange keine Daten durch diesen endgültig gelöscht wurden
- Ist ein Pfleger gesperrt worden, so werden in seinen Behandlungen die Daten des Pflegers als "gesperrt" angezeigt
- Alle Daten, die verändert werden können werden auf Input validiert - das passiert entweder durch ein error css-styling, das Ausgrauen eines Buttons oder das nicht-updaten eines Feldes auf einen eingegebenen Wert