# Gremi-O-Mat

Wahl-O-Mat-ähnliche Webapplikation, entwickelt vom IT-Referat des AStA der Hochschule RheinMain für die studentischen Gremienwahlen 2021/2022.

Ein Webanwendung primär zur Information der Studienschaft der Hochschule RheinMain über die jährliche Gremienwahl, inspiriert durch den Wahl-O-Maten.
Diese Anwendung ist ein von Grund auf gebautes, maßgeschneidertes CMS mit Nutzer- und Admin-Login zur Verwaltung von Nutzern, Gremien und Aussagen. Zusätzlich wurde ein serverseitiger Mail-Client zum Versenden von Passwortreset- und Begrüßungsmails über ein SMTP-Relay einprogrammiert. Implementiert mithilfe von Spring Boot, Thymeleaf und gebaut mit Gradle.

# Compile und Ausführung mit Docker

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