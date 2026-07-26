---

kanban-plugin: board

---

## Backlog (Referenzprojekt-Prio)

- [ ] CI/CD Pipeline (GitHub Actions) 🔴 Muss (Weiterbildung)
- [ ] Unit-Tests schreiben (Controller, Service, Security) ✅ Referenzqualität


## In Progress (aktuelle Woche 🔴)

- [ ] Router und API Routes anlegen (Frontend-Seite)
- [ ] Navbar anpassen (Links funktionsfähig machen)
- [ ] Spring Security: JWT Backend fertig (SecurityConfig + login-Token)
- [ ] Frontend: Login-Token in localStorage + Auth-Header + geschützte Routen
- [ ] POST /supplier + PUT /supplier
- [ ] POST /article + PUT /article
- [ ] PUT /invoice (payed status setzen)


## Future Backlog 🚀

- [ ] PDF2XML-Reader: Digitale Rechnungen parsen + Felder automatisch befüllen
- [ ] Objektspeicher: Rechnungen als PDF/XML ablegen (S3/MinIO)
- [ ] PDF-Viewer: Rechnung in der WebApp als PDF anzeigen
- [ ] Hosting + Caddy Reverse Proxy (HTTPS)
- [ ] Such-Popup mit Sub-Filtern (Lieferant, Artikel, Rechnung)
- [ ] User-CRUD (GET /users/{id}, POST, PUT, DELETE)
- [ ] Dashboard mit Auswertungen/Statistiken


## Done

- [x] DB setup und connection einrichten @date{26-07-26}
- [x] Frontend: Grundstruktur (JS/React) @date{26-07-26}
- [x] CommandLinerRunner DB Test @date{26-07-26} runs!
- [x] Bootstrap installieren @date{26-07-26}
- [x] react-router-dom installieren
- [x] Frontend: Homepage aufsetzen @date{26-07-26}
- [x] Dependencies einrichten @date{26-07-26}
- [x] Bcrypt in users implementieren @date{26-07-26}
- [x] Query-Klassen-Repos anlegen @date{26-07-26}
- [x] CREATE_Tables.sql aanlegen
- [x] React Vite Rohling aufsetzen @date{26-07-26}
- [x] README verfassen @date{26-07-26}
- [x] Hauptklassen angelegt
- [x] Repo aufsetzten @date{26-07-26}
- [x] DB aufsetzen
- [x] API-Routes skizziert @date{26-07-26}
- [x] MVPs schreiben
- [x] DB Skizze
- [x] Spring Initializr
- [x] No-arg-Konstruktoren für alle @Entity-Klassen @date{26-07-26}
- [x] EnumType.STRING für tax_type + SQL VARCHAR @date{26-07-26}
- [x] CommandLineRunner: DB-Test erfolgreich (save + findAll) @date{26-07-26}
- [x] GetController: REST-Endpoints mit @RequestParam-Filtern @date{26-07-26}
- [x] PostController: Login-Endpoint mit LoginRequest-DTO, Optional, ResponseEntity @date{26-07-26}
- [x] Repos aufgeräumt: findById/findBySupplierId entfernt, findByMail → Optional @date{26-07-26}
- [x] POST /invoice (+ ArticleRequest-DTO, createInvoice) @date{26-07-26}
- [x] SQL geupdated: article_number VARCHAR, invoices.supplier @date{26-07-26}
- [x] JwtService – generateToken + extractMail @date{26-07-26}
- [x] JwtAuthFilter – Token-Filter + SecurityContext setzen @date{26-07-26}
- [x] JWT-Theorie: JWS vs JWE, HMAC, Base64, Claim-Struktur @date{26-07-26}
- [x] Crypting + JwtService nach Security/ verschoben, alte JWT.java gelöscht @date{26-07-26}






%% kanban:settings
```
{"kanban-plugin":"board","list-collapse":[false,false,false],"show-checkboxes":true,"full-list-lane-width":true,"tag-colors":[],"move-dates":true,"date-trigger":"@date","time-trigger":"@time","date-format":"DD-MM-YY","date-display-format":"DD-MM-YY"}
```
%%
