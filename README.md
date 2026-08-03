# Invoice Management – Spring Boot & React

## Über das Projekt

Eine webbasierte Rechnungsverwaltung, gebaut mit **Java & Spring Boot** als Kern. Das Projekt ist mein praktischer Weg, Java nicht nur theoretisch zu lernen, sondern in einem realen Anwendungsfall mit typischen Enterprise-Komponenten umzusetzen: Datenbankanbindung, Sicherheit, REST-API, Web-Frontend und eine vollständige CI/CD-Deployment-Pipeline auf AWS.

## Backend im Fokus (Java & Spring Boot)

Der Schwerpunkt liegt auf dem Java-Backend:

- **JPA-Modellierung** – Entities (`Invoice`, `Article`, `Supplier`, `User`) mit `@Entity`, `@Table`, `@OneToMany`/`@ManyToOne`-Beziehungen
- **Spring Data JPA** – Repositories mit CRUD ohne Boilerplate
- **REST-API** – Controller für `GET`, `POST`, `PUT`, `DELETE` (User, Invoices, Articles, Suppliers)
- **Spring Security mit JWT** – Token-basierte Authentifizierung (JJWT), rollenbasierter Zugriff (Admin/User)
- **Passwort-Sicherheit** – BCrypt-Hashing, Passwort-Änderung mit Current-Password-Prüfung
- **CORS-Konfiguration** – konfigurierbar über Environment-Variablen für den Cloudfront-Deploy

## Deployment & Infrastruktur


- **CI/CD** – GitHub Actions: Terraform-Workflow + getrennte Deploy-Workflows für Backend und Frontend
- **Terraform** – Infrastruktur als Code: EC2, S3, Security Groups, CloudFront
- **Docker** – Multi-Stage-Dockerfile für das Backend, Docker Compose auf dem Server
- **HTTPS** – zwei CloudFront-Distributionen (Frontend statisch, Backend als API-Origin)
- **AWS** – EC2 (Backend + PostgreSQL), S3 (Frontend), CloudFront (CDN + Zertifikate)

## Frontend

React (Vite) als dünne Schicht über der REST-API: Login, Rechnungs-CRUD in Modals, Context-API für State, JWT-Handling im Frontend.

## Architektur & Infrastruktur

```
Browser
   │  https
   ▼
CloudFront Frontend  ──http──▶  S3-Website (Invoice-Management_FRONTEND/dist)
   ▼
CloudFront Backend   ──http──▶  EC2 :8080 (Spring Boot, Docker)
                                  │
                                  ▼  JDBC
                            PostgreSQL 16 (Docker, Port 5432)
```

Die Infrastruktur ist komplett als Code abgebildet und wird automatisch deployed:

| Komponente | Implementierung | Zweck |
|---|---|---|
| **Terraform-Workflow** | `.github/workflows/tf_aws.yml` | Baut die gesamte AWS-Infrastruktur bei `Terraform/**`-Änderungen |
| **Backend-Deploy** | `.github/workflows/deploy-backend.yml` | Gradle-Build → Docker-Image → per SCP auf EC2 → `docker compose up` |
| **Frontend-Deploy** | `.github/workflows/deploy-frontend.yml` | `npm ci` + Build → `aws s3 sync` auf den Frontend-Bucket |
| **Infrastruktur** | `Terraform/main.tf` | EC2, Security Group, S3, zwei CloudFront-Distributionen |
| **Backend-Image** | `Invoice_Management_BACKEND/Dockerfile` | Multi-Stage-Build (Gradle → JRE-Runtime) |
| **Server-Setup** | `docker-compose.ec2.yml` | Spring Boot + PostgreSQL als Services |
| **DB-Schema** | `Invoice_Management_DB/CreateTables.sql` | 4 Tabellen (`users`, `suppliers`, `invoices`, `articles`) mit FK |

### HTTPS

Zwei CloudFront-Distributionen liefern Frontend und Backend getrennt aus – je mit kostenlosem AWS-Zertifikat und http→https-Weiterleitung. Das Backend-CDN reicht `Authorization`- und `Origin`-Header durch (JWT + CORS) und cacht nicht (`ttl = 0`), damit API-Antworten immer aktuell sind.

### Herausforderungen, die ich dabei gelöst habe

- **S3-Website-Origin**: CloudFront braucht für Website-Endpoints einen expliziten Custom-Origin-Block, sonst lehnt AWS die Distribution ab
- **Kein API-Caching**: Ohne `default_ttl = 0` würden CloudFront-Edge-Knoten veraltete Rechnungsdaten ausliefern
- **CORS + JWT durch das CDN**: `Origin`- und `Authorization`-Header müssen explizit weitergeleitet werden
- **Docker auf EC2**: Ubuntu-24.04-Paket heißt `docker-compose-v2`, nicht `docker-compose-plugin`

## Datenbank

PostgreSQL 16 läuft aktuell als Docker-Container auf der EC2-Instanz. Ein Umzug auf **Amazon RDS** (gemanagte DB mit Backups, Snapshots und Failover) ist technisch vorbereitet, wird aber aus Kostengründen zurückgestellt – der aktuelle Setup erfüllt den Zweck für ein Lern-/Portfolio-Projekt vollständig.

## Tech-Stack

| Ebene | Technologie |
|-------|------------|
| Backend | Java, Spring Boot 3, Spring MVC, Spring Data JPA |
| Sicherheit | Spring Security, JWT (JJWT), BCrypt |
| Datenbank | PostgreSQL |
| Frontend | React (Vite), HTML, CSS |
| Build-Tool | Gradle |
| Container | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| Infrastruktur | Terraform, AWS (EC2, S3, CloudFront) |

## Projektmanagement

- [Kanban-Board](./Invoice-Management_Vault/Project-Kanban.md)
---

*Juli 2026*
