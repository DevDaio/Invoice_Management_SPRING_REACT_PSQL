---

kanban-plugin: board

---

## Backlog (Referenzprojekt-Prio)

- [ ] CI/CD Pipeline (GitHub Actions)
- [ ] Unit-Tests schreiben (Controller, Service, Security)
- [ ] PUT /invoice (payed status setzen)


## In Progress

- [ ] Frontend: Login-Token in localStorage + Auth-Header + geschützte Routen
- [ ] Frontend: Router und API Routes anlegen
- [ ] Frontend: Navbar anpassen (Links funktionsfähig)


## Done

- [x] SecurityConfig fertig (CORS, CSRF, STATELESS, Routen, PasswordEncoder) @date{27-07-26}
- [x] PostController.login() gibt JWT-Token zurück (statt User-Objekt) @date{27-07-26}
- [x] Rollen-System ADMIN/USER (getRole(), SimpleGrantedAuthority, DB-Uppercase) @date{27-07-26}
- [x] Alle 6 Dateien zeilenweise kommentiert (inkl. Imports) @date{27-07-26}
- [x] DELETE /user (nur Admin, 204 No Content) @date{27-07-26}
- [x] PUT /update/user + /update/password (nur Admin) @date{27-07-26}
- [x] UserRepository aufgeräumt (save() statt updateUser/updatePassword) @date{27-07-26}
- [x] CreateTables.sql: role DEFAULT 'USER' @date{27-07-26}
- [x] Application.java: Admin-Seed auf "ADMIN" @date{27-07-26}
- [x] Crypting + JwtService nach Security/ verschoben, alte JWT.java gelöscht @date{26-07-26}
- [x] JWT-Theorie: JWS vs JWE, HMAC, Base64, Claim-Struktur @date{26-07-26}
- [x] JwtAuthFilter – Token-Filter + SecurityContext setzen @date{26-07-26}
- [x] JwtService – generateToken + extractMail @date{26-07-26}
- [x] SQL geupdated: article_number VARCHAR, invoices.supplier @date{26-07-26}
- [x] POST /invoice (+ ArticleRequest-DTO, createInvoice) @date{26-07-26}
- [x] Repos aufgeräumt: findById/findBySupplierId entfernt, findByMail → Optional @date{26-07-26}
- [x] PostController: Login-Endpoint mit LoginRequest-DTO, Optional, ResponseEntity @date{26-07-26}
- [x] GetController: REST-Endpoints mit @RequestParam-Filtern @date{26-07-26}
- [x] CommandLineRunner: DB-Test erfolgreich (save + findAll) @date{26-07-26}
- [x] EnumType.STRING für tax_type + SQL VARCHAR @date{26-07-26}
- [x] No-arg-Konstruktoren für alle @Entity-Klassen @date{26-07-26}
- [x] API-Routes skizziert @date{26-07-26}
- [x] Repo aufsetzten @date{26-07-26}
- [x] README verfassen @date{26-07-26}
- [x] React Vite Rohling aufsetzen @date{26-07-26}
- [x] Query-Klassen-Repos anlegen @date{26-07-26}
- [x] Bcrypt in users implementieren @date{26-07-26}
- [x] Dependencies einrichten @date{26-07-26}
- [x] Frontend: Homepage aufsetzen @date{26-07-26}
- [x] Bootstrap installieren @date{26-07-26}
- [x] CommandLinerRunner DB Test @date{26-07-26} runs!
- [x] Frontend: Grundstruktur (JS/React) @date{26-07-26}
- [x] DB setup und connection einrichten @date{26-07-26}
- [x] DB aufsetzen @date{22-07-26}
- [x] CREATE_Tables.sql aanlegen @date{22-07-26}
- [x] react-router-dom installieren @date{22-07-26}
- [x] Spring Initializr @date{21-07-26}
- [x] DB Skizze @date{21-07-26}
- [x] MVPs schreiben @date{21-07-26}
- [x] Hauptklassen angelegt @date{21-07-26}


## Future Backlog (Konzeptideen und kommende Features)

- [ ] Dashboard mit Auswertungen/Statistiken
- [ ] Hosting + Caddy Reverse Proxy (HTTPS)
- [ ] Objektspeicher: Rechnungen als PDF/XML ablegen (S3/MinIO)
- [ ] PDF-Viewer: Rechnung in der WebApp als PDF anzeigen
- [ ] PDF2XML-Reader: Digitale Rechnungen parsen + Felder automatisch befüllen
- [ ] Such-Popup mit Sub-Filtern (Lieferant, Artikel, Rechnung)



%% kanban:settings
```
{"kanban-plugin":"board","list-collapse":[false,false,false],"show-checkboxes":true,"full-list-lane-width":true,"tag-colors":[],"move-dates":true,"date-trigger":"@date","time-trigger":"@time","date-format":"DD-MM-YY","date-display-format":"DD-MM-YY"}
```
%%
