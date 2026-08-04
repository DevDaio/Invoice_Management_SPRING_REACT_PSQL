# Präsentationsskizze – Invoice Management (Spring Boot / React / PostgreSQL)

> Aufgebaut entlang des Code-Walkthrough-Fadens.
> "Für die Präsi reicht"-Formulierungen sind markiert mit **»Präsi-Satz«**.
> Stand: 04.08.2026 – wird während des Walkthroughs fortgeschrieben.

---

## 0. Überblick / Warum dieses Projekt

- Full-Stack: **React (Vite)** Frontend, **Spring Boot** Backend, **PostgreSQL** Datenbank
- Rechnungen verwalten: Artikel, Lieferanten, Steuersätze, Bezahlstatus
- Authentifizierung mit **JWT**, Deployment auf **AWS** (Terraform + GitHub Actions)

**»Präsi-Satz«:** _Ein System, in dem ich mehrere getrennte Technologien zu einer laufenden Anwendung verbinde – von der Datenbank über die API bis zur Webseite._

---

## 1. Datenmodell (Entities)

### Grundidee

Vier Klassen (`Invoice`, `Article`, `Supplier`, `User`) + zwei Enums (`TaxType`, `UnitType`).
Jede Entity = eine Tabelle, jedes Feld = eine Spalte.

**»Präsi-Satz«:** _Meine Entities sind die Blaufdrucke meiner Datenbank. `@Entity` + `@Table` sagen dem Framework "das ist eine Tabelle", jedes Feld wird zu einer Spalte. Eine Zeile (Row) ist eine gespeicherte Instanz – also ein konkretes Objekt._

### Beziehungen

- `Invoice` → `Supplier`: **@ManyToOne** (eine Rechnung gehört einem Lieferanten)
- `Invoice` → `Article`: **@OneToMany** (eine Rechnung hat viele Artikel)
- `Article` → `Invoice` + `Supplier`: **@ManyToOne** (die FK-Seite)

**»Präsi-Satz«:** _Bei `@OneToMany(mappedBy=...)` halte ich den Fremdschlüssel nicht selbst. Die FK-Spalte liegt auf der "vielen"-Seite – also beim Article. Das `mappedBy` sagt: "Die andere Seite besitzt die Spalte, ich zeige nur darauf."_

### Enums als Eingabe-Begrenzung

**»Präsi-Satz«:** _Mit Enums limitiere ich die Eingabe auf definierte Werte – ein Artikel kann nur Steuer 0/7/19 und Stück/Meter/Palette... haben, keine freien Strings, keine Tippfehler. `@Enumerated(EnumType.STRING)` speichert den Namen statt der Zahl in der DB – das bleibt lesbar und robust._

### @JsonIgnore – JSON-Kaskaden verhindern

**»Präsi-Satz«:** _Objekte, die sich gegenseitig referenzieren, würden bei der JSON-Ausgabe endlos ineinander verschachtelt werden (Invoice → Article → Invoice → ...). Mit `@JsonIgnore` auf den zurückschauenden Gettern (`Article.getInvoice()`, `Supplier.getArticles()`, `Supplier.getInvoices()`) breche ich diese Kette bewusst an den Rück-Pfaden._

### Getter/Setter – mehr als Verkapselung

**»Präsi-Satz«:** _Getter/Setter sind nicht nur OOP-Verkapselung – sie sind die Pipeline zwischen meinen Klassen und dem Frontend. Jackson nutzt die Getter, um aus Objekten JSON zu machen, und die Setter, um aus JSON wieder Objekte zu machen (bei `@RequestBody`). Ohne sie gäbe es keine API-Antworten._

### User-Entity – der Auth-Baustein

- Kein Bezug zu Invoice/Article/Supplier (Standalone)
- Felder: `id`, `mail`, `password`, `role`

**»Präsi-Satz« Passwort:** _Im Setter wird das Passwort direkt in eine BCrypt-Hash-Funktion geleitet – mit Salt. Gespeichert wird nie das Klartext-Passwort, nur der Hash. Gleichlautende Passwörter erzeugen trotzdem verschiedene Hashes (Salt). Beim Login wird der eingegebene Wert mit demselben Salt neu gerechnet und verglichen._

**»Präsi-Satz« @JsonIgnore:** _Auf dem Passwort-Getter liegt `@JsonIgnore` – der Hash darf zu keinem Zeitpunkt in einer JSON-Antwort auftauchen, weder über `GET /users` noch nach `POST /newUser`._

**»Präsi-Satz« Role:** _Jeder User hat eine Role (`ADMIN`/`USER`). Beim Login wird sie als Claim in den JWT geschrieben. Bei jeder Anfrage liest der Filter den User aber frisch aus der Datenbank und baut daraus die Authority `ROLE_ADMIN`/`ROLE_USER`. Das `ROLE_`-Präfix ist die Spring-Security-Konvention, auf der `hasRole("ADMIN")` in der SecurityConfig basiert._

---

## 2. Repositories

### Spring Data JPA

**»Präsi-Satz«:** _Die Interfaces erben von `JpaRepository` – damit bekommen sie fertiges CRUD (`findAll`, `save`, `delete...`). Eigene Suchmethoden wie `findByMail` sind "Derived Queries": Spring Data übersetzt den Methodennamen zur Laufzeit in SQL (`WHERE mail = ?`). Man schreibt keinen SQL-Code, nur sprechende Methodennamen._

### Optional – "nicht gefunden" sauber behandeln

**»Präsi-Satz«:** _Statt `null` zurückzugeben, kapselt die Repository-Methode das Ergebnis in ein `Optional`. Der Aufrufer muss den Fall "nicht gefunden" damit explizit behandeln (`isEmpty`, `orElse`, `orElseGet`) – das verhindert unerwartete NullPointerExceptions an unvorhersehbaren Stellen. In der Review haben wir alle `.orElse(null)`-Stellen aus den Controllern entfernt._

---

## 3. Controller / API

### @RestController

**»Präsi-Satz«:** _`@RestController` sorgt dafür, dass Rückgabewerte direkt als JSON in den HTTP-Response gehen (nicht in eine View gerendert). Mappings wie `@GetMapping`/`@PostMapping` legen fest, welche Methode welche Route und HTTP-Methode bedient._

### ResponseEntity – Status-Codes kontrollieren

**»Präsi-Satz«:** _Mit `ResponseEntity` steuere ich pro Fall den HTTP-Status-Code (200 ok, 201 created, 404 not found, 401 unauthorized). Das Frontend nutzt diese Codes, um zu entscheiden, was angezeigt wird._

### @RequestBody – JSON → Java-Objekt

**»Präsi-Satz«:** _`@RequestBody` nimmt den JSON-Body eines Requests und macht daraus ein Java-Objekt (Deserialisierung). Bei der Antwort ist es umgekehrt: Das Java-Objekt wird zu JSON (Serialisierung). Beides macht Jackson._

### Einfüge-Reihenfolge & Fremdschlüssel

**»Präsi-Satz«:** _Bevor ich einen Datensatz mit Fremdschlüssel speichere, muss der referenzierte Datensatz schon eine ID haben: erst Supplier, dann Invoice, dann Artikel (mit `invoice_id`). Da die Beziehung ohne Cascade ist, speichere ich die Kinder einzeln nach dem Parent._

---

## 4. Security

### JWT-Lebenszyklus

**»Präsi-Satz«:** _Nach dem Login erhält der User einen signierten JWT mit Mail, Rolle und Ablaufzeit (24h). Bei jeder Anfrage prüft ein Filter die Signatur des Tokens kryptographisch; die Route entscheidet dann anhand der Rolle, ob sie freigegeben ist. Ungültige oder abgelaufene Token werden abgewiesen – der Nutzer muss sich neu einloggen._

### Passwort-Sicherheit (BCrypt)

**»Präsi-Satz«:** _Passwörter werden nie im Klartext gespeichert, sondern mit BCrypt und Salt gehasht. Beim Login wird der eingegebene Wert mit demselben Verfahren neu gehasht und verglichen. Die kritische Logik (Hashing, Token) ist durch Unittests abgesichert._

### Rollen & Routen-Schutz

**»Präsi-Satz«:** _Jeder User hat eine Rolle (ADMIN/USER). Der Filter baut daraus die Spring-Authority `ROLE_ADMIN`/`ROLE_USER`; die SecurityConfig gibt bestimmte Routen nur für Admins frei (User-Verwaltung) und schützt den Rest vor unangemeldeten Zugriffen. Die Rolle wird bei jedem Request frisch aus der Datenbank gelesen, nicht dem Token vertraut._

---

## 5. Setup / Konfiguration

### CommandLineRunner – Admin-Bootstrap

**»Präsi-Satz«:** _Nach dem Start legt die Anwendung automatisch einen ersten Admin an, wenn noch keiner existiert. Die Zugangsdaten kommen aus Umgebungsvariablen – so steht im Quellcode kein Passwort._

### application.properties – die Schaltzentrale

**»Präsi-Satz«:** _Die `application.properties` enthält die Verbindung zur Datenbank, das Verhalten von Hibernate (Tabellen werden automatisch erzeugt) und die JWT-Lebensdauer. Geheimnisse wie das JWT-Secret und Passwörter kommen als Umgebungsvariablen, nie als Datei ins Repository._

### Tests

**»Präsi-Satz«:** _Die kritische Sicherheitslogik ist mit Unittests abgesichert – Passwort-Hashing und Token-Lebenszyklus. Sie laufen ohne Datenbank und ohne Spring-Context: `ReflectionTestUtils` setzt die privaten Felder direkt, so testet man nur die reine Logik. Tests prüfen z.B., dass abgelaufene oder manipulierte Token zurückgewiesen werden._

---

## Anhang: Das 1-Minuten-Modell "Wie läuft ein Request"

```
Browser/React
   │  HTTP (POST /login, JSON-Body)
   ▼
Spring Security-Filterkette   ← JwtAuthFilter fängt Bearer-Token ab
   │
   ▼
Controller (@RestController)  ← Jackson wandelt JSON → Java-Objekt (@RequestBody)
   │
   ▼
Repository (JpaRepository)    ← Hibernate macht aus Methodennamen SQL, spricht mit Postgres
   │
   ▼
Controller → ResponseEntity   ← Jackson wandelt Java-Objekt → JSON
   │
   ▼
Browser
```

**»Präsi-Satz«:** _Spring verwaltet meine Bausteine (Beans), Hibernate übersetzt Entities und Methodennamen in SQL, Jackson übersetzt Objekte in JSON. Ich habe sie konfiguriert, aber die Mechanik darunter liefert das Framework._
