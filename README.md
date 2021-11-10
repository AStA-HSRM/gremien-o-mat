# Gremi-O-Mat
Der _Gremi-O-Mat_ ist eine Webanwendung primär zur Information der Studienschaft der Hochschule RheinMain über die jährliche Gremienwahl, inspiriert durch den Wahl-O-Maten.
Diese Anwendung ist ein von Grund auf gebautes, maßgeschneidertes CMS mit Nutzer- und Admin-Login zur Verwaltung von Nutzern, Gremien und Aussagen. Zusätzlich wurde ein serverseitiger Mail-Client zum Versenden von Passwortreset- und Begrüßungsmails über ein SMTP-Relay einprogrammiert. Implementiert mithilfe von Java, Spring Boot, Thymeleaf und gebaut mit Gradle.

## Compile und Ausführung mit Docker

Voraussetzungen:
- Docker
- docker-compose
- (optional) Einen Webserver als Reverse Proxy

Repo mit live-Branch ins aktuelle Verzeichnis klonen (wird nur zum Bauen gebraucht):
```bash
git clone -b live git@github.com:AStA-HSRM/gremien-o-mat.git
```
Build-Skript ausführen (generiert fertiges Docker-Image):
```bash
./build.sh
```
Ist der Build erfolgreich, wird unter Anderem folgendes ausgegeben:
```bash
...

Build successful!
Docker image was tagged as gremiomat-spring:latest
Complete build took 139 seconds
```

Nun kann man die `docker-compose.yml` an einen beliebigen Ort kopieren und dort alle erforderlichen Environment-Variablen setzen. Bitte dazu in die Datei schauen!

Achtung! Es wird empfohlen, die `docker-compose.yml` nicht im Repo-Ordner zu lassen, da bei einem erneuten Pull alle Änderungen und Einstellungen verloren gehen.

Schlussendlich kann man das Image starten:
```bash
docker-compose up -d
```